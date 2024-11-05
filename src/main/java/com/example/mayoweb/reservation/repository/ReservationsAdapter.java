package com.example.mayoweb.reservation.repository;

import com.example.mayoweb.carts.repository.CartsAdapter;
import com.example.mayoweb.commons.exception.ApplicationException;
import com.example.mayoweb.commons.exception.payload.ErrorStatus;
import com.example.mayoweb.fcm.service.FCMService;
import com.example.mayoweb.items.domain.response.ReadFirstItemResponse;
import com.example.mayoweb.items.repository.ItemsAdapter;
import com.example.mayoweb.reservation.domain.ReservationEntity;
import com.example.mayoweb.reservation.domain.dto.response.ReadReservationListResponse;
import com.example.mayoweb.reservation.domain.dto.response.ReadReservationResponse;
import com.example.mayoweb.sse.SseService;
import com.example.mayoweb.user.service.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ReservationsAdapter {

//    private final SseService sseService;
    private final CartsAdapter cartsAdapter;
    private final ItemsAdapter itemsAdapter;
    private final FCMService fcmService;
    private final UsersService userService;

    //가게 도큐먼트 id를 받아 해당 가게의 신규 예약들을 가져옵니다.
    public List<ReservationEntity> getNewByStoreRef(String storeId){

        List<ReservationEntity> newReservations = new ArrayList<>();

        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference storeDocumentId = firestore.collection("stores").document(storeId);
        CollectionReference reservationsRef = firestore.collection("reservation");
        Query query = reservationsRef.whereEqualTo("store_ref", storeDocumentId)
                .whereEqualTo("reservation_state", State.NEW.ordinal());

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();
        QuerySnapshot querySnapshot = null;

        try {
            querySnapshot = querySnapshotApiFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("새로운 예약들을 가져오는데 실패하였습니다.", 400, LocalDateTime.now()));
        }

        Comparator<ReservationEntity> createdAtComparator = Comparator
                .comparing(entity -> entity.getCreatedAt().toSqlTimestamp(), Comparator.reverseOrder());

        for (QueryDocumentSnapshot reservationDocument : querySnapshot.getDocuments()) {
            ReservationEntity reservationEntity = reservationDocument.toObject(ReservationEntity.class);
            if(reservationEntity.getReservationState() == State.NEW.ordinal()) {
                newReservations.add(reservationEntity);
            }
        }

        newReservations.sort(createdAtComparator);

        return newReservations;
    }


    //가게 document id를 받아 해당 가게의 진행중인 예약들을 가져옵니다.
    public List<ReservationEntity> getProcessingByStoreRef(String storesRef) throws ExecutionException, InterruptedException {

        List<ReservationEntity> reservations = new ArrayList<>();

        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference storeDocumentId = firestore.collection("stores").document(storesRef);
        CollectionReference reservationsRef = firestore.collection("reservation");
        Query query = reservationsRef.whereEqualTo("store_ref", storeDocumentId).whereEqualTo("reservation_state",State.PROCEEDING.ordinal());
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();
        QuerySnapshot querySnapshot = null;
        querySnapshot = querySnapshotApiFuture.get();


        Comparator<ReservationEntity> createdAtComparator = Comparator
                .comparing(entity -> entity.getCreatedAt().toSqlTimestamp(), Comparator.reverseOrder());

        for (QueryDocumentSnapshot reservationDocument : querySnapshot.getDocuments()) {
            ReservationEntity reservationEntity = reservationDocument.toObject(ReservationEntity.class);
            reservations.add(reservationEntity);
        }

        reservations.sort(createdAtComparator);

        return reservations;
    }

    public CompletableFuture<List<ReservationEntity>> getProceedingByStoreIdAsync(String storeId) {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference storeDocumentId = firestore.collection("stores").document(storeId);
        CollectionReference reservationsRef = firestore.collection("reservation");
        Query query = reservationsRef.whereEqualTo("store_ref", storeDocumentId)
                .whereEqualTo("reservation_state", State.PROCEEDING.ordinal());

        CompletableFuture<List<ReservationEntity>> future = new CompletableFuture<>();

        query.addSnapshotListener((querySnapshot, e) -> {
            if (e != null) {
                future.completeExceptionally(e);
                return;
            }

            List<ReservationEntity> proceedingReservations = new ArrayList<>();
            if (querySnapshot != null) {
                for (QueryDocumentSnapshot reservationDocument : querySnapshot.getDocuments()) {
                    ReservationEntity reservationEntity = reservationDocument.toObject(ReservationEntity.class);
                    proceedingReservations.add(reservationEntity);
                }
                proceedingReservations.sort(Comparator.comparing(entity -> entity.getCreatedAt().toSqlTimestamp(), Comparator.reverseOrder()));
            }
            future.complete(proceedingReservations);
        });

        return future;
    }

    public List<ReservationEntity> getEndByStoreRefAndTimestamp(String storesRef, Timestamp timestamp) {

        List<ReservationEntity> reservations = new ArrayList<>();

        LocalDateTime localDateTime = timestamp.toSqlTimestamp().toLocalDateTime();
        ZoneId kstZoneId = ZoneId.of("Asia/Seoul");

        LocalDateTime startOfDay = localDateTime.toLocalDate().atStartOfDay().atZone(kstZoneId).toLocalDateTime();
        LocalDateTime endOfDay = localDateTime.toLocalDate().atTime(23, 59, 59, 999999999).atZone(kstZoneId).toLocalDateTime();

        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference storeDocumentId = firestore.collection("stores").document(storesRef);
        CollectionReference reservationsRef = firestore.collection("reservation");

        Query query = reservationsRef.whereEqualTo("store_ref", storeDocumentId);

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();
        QuerySnapshot querySnapshot = null;

        try {
            querySnapshot = querySnapshotApiFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("완료된 예약을 가져오는 도중 에러가 발생하였습니다.", 400, LocalDateTime.now()));
        }

        Comparator<ReservationEntity> createdAtComparator = Comparator
                .comparing(ReservationEntity::getCreatedAt, Comparator.reverseOrder());

        for (QueryDocumentSnapshot reservationDocument : querySnapshot.getDocuments()) {
            ReservationEntity reservationEntity = reservationDocument.toObject(ReservationEntity.class);
            Timestamp createdAt = reservationEntity.getCreatedAt();

            LocalDateTime createdAtLocalDateTime = createdAt.toSqlTimestamp().toLocalDateTime();

            if ((createdAtLocalDateTime.compareTo(startOfDay) >= 0) &&
                    (createdAtLocalDateTime.compareTo(endOfDay) <= 0) &&
                    (reservationEntity.getReservationState() == State.END.ordinal() ||
                            reservationEntity.getReservationState() == State.FAIL.ordinal())) {
                reservations.add(reservationEntity);
            }
        }

        reservations.sort(createdAtComparator);

        return reservations;
    }

    //예약 도큐먼트 Id를 입력받아 reservation 객체를 가져옵니다.
    public Optional<ReservationEntity> findByReservationId(String reservationId) {
        Firestore db = FirestoreClient.getFirestore();

        try {
            DocumentReference docRef = db.collection("reservation").document(reservationId);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {

                List<DocumentReference> cartRefs = (List<DocumentReference>) document.get("cart_ref");
                List<DocumentReference> cart_ref = new ArrayList<>();

                try {
                    if (cartRefs != null && !cartRefs.isEmpty()) {
                        for (DocumentReference cartRef : cartRefs) {
                            ApiFuture<DocumentSnapshot> cartFuture = cartRef.get();
                            DocumentSnapshot cartDocument = cartFuture.get();

                            if (cartDocument.exists()) {
                                cart_ref.add(cartDocument.getReference());
                            }
                        }
                    }
                }
                catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }

                ReservationEntity reservationEntity = ReservationEntity.builder()
                        .id(document.getString("id"))
                        .reservationId(document.getString("reservation_id"))
                        .reservationIsPlastics(document.getBoolean("reservation_is_plastics"))
                        .reservationState((document.get("reservation_state", Integer.class)))
                        .reservationRequest(document.getString("reservation_request"))
                        .createdAt(document.getTimestamp("created_at"))
                        .pickupTime(document.getTimestamp("pickup_time"))
                        .totalPrice(document.getDouble("total_price"))
                        .storeRef((DocumentReference) document.get("store_ref"))
                        .userRef((DocumentReference) document.get("user_ref"))
                        .cartRef(cart_ref)
                        .build();

                return Optional.ofNullable(reservationEntity);
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    //예약 도큐먼트 Id를 입력받아 진행중 상태로 바꿉니다.
    public void reservationProceeding(String reservationRef){

        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection("reservation").document(reservationRef);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = null;

        try {
            document = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("진행중으로 업데이트 하는 도중 에러가 발생하였습니다.", 400, LocalDateTime.now()));
        }

        if (document.exists()) {
            documentReference.update("reservation_state", State.PROCEEDING.ordinal());
        }
    }

    //예약 도큐먼트 Id를 입력받아 완료 상태로 바꿉니다.
    public void reservationDone(String reservationRef){

        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection("reservation").document(reservationRef);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = null;
        try {
            document = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("완료로 업데이트 하는 도중 에러가 발생하였습니다.", 400, LocalDateTime.now()));
        }

        if (document.exists()) {
            documentReference.update("reservation_state", State.END.ordinal());
        }

    }

    //예약 도큐먼트 Id를 입력받아 실패 상태로 바꿉니다.
    public void reservationFail(String reservationRef){

        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection("reservation").document(reservationRef);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = null;
        try {
            document = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("거절로 업데이트하는 도중 에러가 발생하였습니다.", 400, LocalDateTime.now()));
        }

        if (document.exists()) {
            documentReference.update("reservation_state", State.FAIL.ordinal());
        }
    }

    private ReadFirstItemResponse getFirstItemNameFromReservation(ReservationEntity reservationEntity) throws ExecutionException, InterruptedException {

        DocumentReference carts = cartsAdapter.getFirstCartByReservation(reservationEntity);

        return itemsAdapter.getFirstItemNameFromCart(carts);
    }

    //비동기적으로 fcmToken을 수신하는 Listener를 실행합니다.
    public CompletableFuture<Void> sendNewReservationFCM(String storeId, String userId) {

        Firestore firestore = FirestoreClient.getFirestore();

        DocumentReference storeDocumentId = firestore.collection("stores").document(storeId);
        CollectionReference reservationsRef = firestore.collection("reservation");

        Query query = reservationsRef.whereEqualTo("store_ref", storeDocumentId)
                .whereEqualTo("reservation_state", State.NEW.ordinal());

        CompletableFuture<Void> future = new CompletableFuture<>();

        query.addSnapshotListener((querySnapshot, e) -> {
            if (e != null) {
                future.completeExceptionally(e);
                return;
            }

            if (querySnapshot != null) {

                Set<String> tokens = new HashSet<>();
                try {
                    tokens.addAll(userService.getTokensByUserRef(userId));
                    log.info("tokens : {}", tokens);
                } catch (ExecutionException | InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                CompletableFuture.runAsync(() -> {
                    try {
                        fcmService.sendNewReservationMessage(tokens.stream().toList());
                        log.info("fcm send");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }
        });

        return future;
    }

    enum State {
        NEW, PROCEEDING, END, FAIL
    }


    //가게 도큐먼트 id를 받아 해당 가게의 모든 예약들을 가져옵니다.
//    public List<ReservationEntity> getReservationsByStoreRef(String storesRef) throws ExecutionException, InterruptedException {
//
//        List<ReservationEntity> reservations = new ArrayList<>();
//        Firestore firestore = FirestoreClient.getFirestore();
//        DocumentReference storeDocumentId = firestore.collection("stores").document(storesRef);
//        CollectionReference reservationsRef = firestore.collection("reservations");
//        Query query = reservationsRef.whereEqualTo("store_ref", storeDocumentId);
//        ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();
//        QuerySnapshot querySnapshot = querySnapshotApiFuture.get();
//
//        for (QueryDocumentSnapshot reservationDocument : querySnapshot.getDocuments()) {
//            ReservationEntity reservationEntity = reservationDocument.toObject(ReservationEntity.class);
//            reservations.add(reservationEntity);
//        }
//
//        return reservations;
//    }

    //    public CompletableFuture<List<ReservationEntity>> getNewByStoreIdAsync(String storeId) {
//        Firestore firestore = FirestoreClient.getFirestore();
//        DocumentReference storeDocumentId = firestore.collection("stores").document(storeId);
//        CollectionReference reservationsRef = firestore.collection("reservation");
//        Query query = reservationsRef.whereEqualTo("store_ref", storeDocumentId)
//                .whereEqualTo("reservation_state", State.NEW.ordinal());
//
//        CompletableFuture<List<ReservationEntity>> future = new CompletableFuture<>();
//
//        query.addSnapshotListener((querySnapshot, e) -> {
//            if (e != null) {
//                future.completeExceptionally(e);
//                return;
//            }
//
//            List<ReservationEntity> newReservations = new ArrayList<>();
//            if (querySnapshot != null) {
//                for (QueryDocumentSnapshot reservationDocument : querySnapshot.getDocuments()) {
//                    ReservationEntity reservationEntity = reservationDocument.toObject(ReservationEntity.class);
//                    newReservations.add(reservationEntity);
//
//                    List<String> tokens = null;
//                    try {
//                        tokens = userService.getTokensByStoresId(storeId);
//                        log.info("tokens : {}", tokens);
//                    } catch (ExecutionException | InterruptedException ex) {
//                        throw new RuntimeException(ex);
//                    }
//                    try {
//                        fcmService.sendNewReservationMessage(tokens);
//                        log.info("fcm send");
//                    } catch (IOException ex) {
//                        throw new RuntimeException(ex);
//                    }
//                }
//                newReservations.sort(Comparator.comparing(entity -> entity.getCreatedAt().toSqlTimestamp(), Comparator.reverseOrder()));
//            }
//            future.complete(newReservations);
//        });
//
//        return future;
//    }

//    public CompletableFuture<List<ReservationEntity>> getNewByStoreIdSse(String clientId, String storeId) throws ExecutionException, InterruptedException {
//
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        Firestore firestore = FirestoreClient.getFirestore();
//        DocumentReference storeDocumentId = firestore.collection("stores").document(storeId);
//        CollectionReference reservationsRef = firestore.collection("reservation");
//        Query query = reservationsRef.whereEqualTo("store_ref", storeDocumentId)
//                .whereEqualTo("reservation_state", State.NEW.ordinal());
//
//        ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();
//        QuerySnapshot querySnapshotReservation = querySnapshotApiFuture.get();
//
//        Set<String> existingReservationIds = new HashSet<>();
//
//        for (QueryDocumentSnapshot reservation : querySnapshotReservation.getDocuments()) {
//            ReservationEntity reservationEntity = reservation.toObject(ReservationEntity.class);
//            existingReservationIds.add(reservationEntity.getId());
//        }
//
//        CompletableFuture<List<ReservationEntity>> future = new CompletableFuture<>();
//
//        query.addSnapshotListener((querySnapshot, e) -> {
//
//            if (e != null) {
//                future.completeExceptionally(e);
//                return;
//            }
//
//            if (querySnapshot != null) {
//
//                for (DocumentChange change : querySnapshot.getDocumentChanges()) {
//
//                    if(change.getType() == DocumentChange.Type.ADDED) {
//                        String docId = change.getDocument().getId();
//                        if (!existingReservationIds.contains(docId)) {
//
//                            ReservationEntity reservationEntity = change.getDocument().toObject(ReservationEntity.class);
//                            ReadReservationResponse reservationResponse = ReadReservationResponse.fromEntity(reservationEntity);
//                            ReadFirstItemResponse firstItemResponse = null;
//
//                            try {
//                                firstItemResponse = getFirstItemNameFromReservation(reservationEntity);
//                            } catch (ExecutionException | InterruptedException ex) {
//                                throw new RuntimeException(ex);
//                            }
//
//
//                            ReadReservationListResponse response = ReadReservationListResponse.builder()
//                                    .reservationId(reservationResponse.id())
//                                    .firstItemName(firstItemResponse.itemName())
//                                    .itemQuantity(firstItemResponse.itemQuantity())
//                                    .createdAt(reservationResponse.createdAt())
//                                    .pickupTime(reservationResponse.pickupTime())
//                                    .reservationState(reservationEntity.getReservationState())
//                                    .build();
//
//                            String jsonResponse;
//
//                            try {
//                                jsonResponse = objectMapper.writeValueAsString(response);
//                            } catch (JsonProcessingException ex) {
//                                throw new RuntimeException(ex);
//                            }
//
//                            sseService.sendMessageToClient(clientId , jsonResponse, "new-reservation");
//                            existingReservationIds.add(docId);
//
//                        }
//                    }
//                }
//            }
//
//            future.complete(null);
//        });
//
//        return future;
//    }

//    public SseEmitter streamNewReservations(String clientId, String storeId) {
//
//        SseEmitter emitter = sseService.addEmitter(clientId);
//
//        Firestore firestore = FirestoreClient.getFirestore();
//
//        DocumentReference storeDocumentId = firestore.collection("stores").document(storeId);
//        CollectionReference reservationsRef = firestore.collection("reservation");
//        Query query = reservationsRef.whereEqualTo("store_ref", storeDocumentId)
//                .whereEqualTo("reservation_state", State.NEW.ordinal());
//
//        Set<String> existingReservationIds = new HashSet<>();
//
//        try {
//            ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();
//            QuerySnapshot querySnapshotReservation = querySnapshotApiFuture.get();
//
//            for (QueryDocumentSnapshot reservation : querySnapshotReservation.getDocuments()) {
//                ReservationEntity reservationEntity = reservation.toObject(ReservationEntity.class);
//                existingReservationIds.add(reservationEntity.getId());
//            }
//        } catch (ExecutionException | InterruptedException e) {
//            log.info("streamNewReservations api : {}", e.getMessage());
//        }
//
//        query.addSnapshotListener((querySnapshot, e) -> {
//
//            if (e != null) {
//                log.info("streamNewReservations snapshot : {}", e.getMessage());
//                return;
//            }
//
//            if (querySnapshot != null) {
//
//                ReadReservationListResponse response = null;
//
//                for (DocumentChange change : querySnapshot.getDocumentChanges()) {
//                    if (change.getType() == DocumentChange.Type.ADDED) {
//                        String docId = change.getDocument().getId();
//                        if (!existingReservationIds.contains(docId)) {
//                            ReservationEntity reservationEntity = change.getDocument().toObject(ReservationEntity.class);
//                            existingReservationIds.add(docId);
//
//                            ReadReservationResponse reservationResponse = ReadReservationResponse.fromEntity(reservationEntity);
//                            ReadFirstItemResponse firstItemResponse;
//                            try {
//                                firstItemResponse = getFirstItemNameFromReservation(reservationEntity);
//                            } catch (ExecutionException | InterruptedException ex) {
//                                log.error("sseError : {} ", ex.getMessage());
//                                return;
//                            }
//
//                            response = ReadReservationListResponse.builder()
//                                    .reservationId(reservationResponse.id())
//                                    .firstItemName(firstItemResponse.itemName())
//                                    .itemQuantity(firstItemResponse.itemQuantity())
//                                    .createdAt(reservationResponse.createdAt())
//                                    .pickupTime(reservationResponse.pickupTime())
//                                    .reservationState(reservationEntity.getReservationState())
//                                    .build();
//                        }
//                    }
//                }
//                if(response != null) {
//                    try {
//                        String jsonResponse = new ObjectMapper().writeValueAsString(response);
//                        sseService.sendMessageToClient(clientId, jsonResponse, "new-reservation");
//                        existingReservationIds.add(response.reservationId());
//                    } catch(IOException ioException) {
//                        log.error("streamNewReservations parse json : {}", ioException.getMessage());
//                    }
//                }
//            }
//        });
//
//        return emitter;
//    }

    //    public CompletableFuture<Slice<ReservationEntity>> findEndReservationsByStoreId(String storeId, Pageable pageable) {
//
//        Firestore firestore = FirestoreClient.getFirestore();
//
//        DocumentReference storeDocumentId = firestore.collection("stores").document(storeId);
//        CollectionReference reservationsRef = firestore.collection("reservation");
//
//        Query query = reservationsRef.whereEqualTo("store_ref", storeDocumentId)
//                .whereIn("reservation_state", Arrays.asList(State.END.ordinal(), State.FAIL.ordinal()));
//
//        Comparator<ReservationEntity> createdAtComparator = Comparator
//                .comparing(entity -> entity.getCreatedAt().toSqlTimestamp(), Comparator.reverseOrder());
//
//        CompletableFuture<Slice<ReservationEntity>> future = new CompletableFuture<>();
//
//        query.addSnapshotListener((querySnapshot, e) -> {
//            if (e != null) {
//                future.completeExceptionally(e);
//                return;
//            }
//
//            List<ReservationEntity> endReservations = new ArrayList<>();
//            if (querySnapshot != null) {
//                for (QueryDocumentSnapshot reservationDocument : querySnapshot.getDocuments()) {
//                    ReservationEntity reservationEntity = reservationDocument.toObject(ReservationEntity.class);
//                    endReservations.add(reservationEntity);
//                }
//
//                endReservations.sort(createdAtComparator);
//
//                int start = (int) pageable.getOffset();
//                int end = Math.min(start + pageable.getPageSize(), endReservations.size());
//                List<ReservationEntity> paginatedList = endReservations.subList(start, end);
//
//                boolean hasNext = endReservations.size() > end;
//                Slice<ReservationEntity> sliceResult = new SliceImpl<>(paginatedList, pageable, hasNext);
//
//                future.complete(sliceResult);
//            } else {
//                future.complete(new SliceImpl<>(new ArrayList<>(), pageable, false));
//            }
//        });
//
//        return future;
//    }

//    public CompletableFuture<Slice<ReservationEntity>> findEndReservationsByStoreIdAndTime(String storeId, Pageable pageable, Timestamp timestamp) {
//
//        Firestore firestore = FirestoreClient.getFirestore();
//
//        LocalDateTime localDateTime = timestamp.toSqlTimestamp().toLocalDateTime();
//        ZoneId kstZoneId = ZoneId.of("Asia/Seoul");
//
//        LocalDateTime startOfDay = localDateTime.toLocalDate().atStartOfDay().atZone(kstZoneId).toLocalDateTime();
//        LocalDateTime endOfDay = localDateTime.toLocalDate().atTime(23, 59, 59, 999999999).atZone(kstZoneId).toLocalDateTime();
//
//        DocumentReference storeDocumentId = firestore.collection("stores").document(storeId);
//        CollectionReference reservationsRef = firestore.collection("reservation");
//
//        Query query = reservationsRef.whereEqualTo("store_ref", storeDocumentId)
//                .whereIn("reservation_state", Arrays.asList(State.END.ordinal(), State.FAIL.ordinal()));
//
//        Comparator<ReservationEntity> createdAtComparator = Comparator
//                .comparing(entity -> entity.getCreatedAt().toSqlTimestamp(), Comparator.reverseOrder());
//
//        CompletableFuture<Slice<ReservationEntity>> future = new CompletableFuture<>();
//
//        query.addSnapshotListener((querySnapshot, e) -> {
//            if (e != null) {
//                future.completeExceptionally(e);
//                return;
//            }
//
//            List<ReservationEntity> endReservations = new ArrayList<>();
//            if (querySnapshot != null) {
//                for (QueryDocumentSnapshot reservationDocument : querySnapshot.getDocuments()) {
//                    ReservationEntity reservationEntity = reservationDocument.toObject(ReservationEntity.class);
//                    Timestamp createdAt = reservationEntity.getCreatedAt();
//
//                    LocalDateTime createdAtLocalDateTime = createdAt.toSqlTimestamp().toLocalDateTime();
//
//                    if ((createdAtLocalDateTime.compareTo(startOfDay) >= 0) &&
//                            (createdAtLocalDateTime.compareTo(endOfDay) <= 0) &&
//                            (reservationEntity.getReservationState() == State.END.ordinal() ||
//                                    reservationEntity.getReservationState() == State.FAIL.ordinal())) {
//                        endReservations.add(reservationEntity);
//                    }
//                }
//
//                endReservations.sort(createdAtComparator);
//
//                int start = (int) pageable.getOffset();
//                int end = Math.min(start + pageable.getPageSize(), endReservations.size());
//                List<ReservationEntity> paginatedList = endReservations.subList(start, end);
//
//                boolean hasNext = endReservations.size() > end;
//                Slice<ReservationEntity> sliceResult = new SliceImpl<>(paginatedList, pageable, hasNext);
//
//                future.complete(sliceResult);
//            } else {
//                future.complete(new SliceImpl<>(new ArrayList<>(), pageable, false));
//            }
//        });
//
//        return future;
//    }

//    public CompletableFuture<List<ReservationEntity>> getEndByStoreIdAsync(String storeId) {
//        Firestore firestore = FirestoreClient.getFirestore();
//        DocumentReference storeDocumentId = firestore.collection("stores").document(storeId);
//        CollectionReference reservationsRef = firestore.collection("reservation");
//        Query query = reservationsRef.whereEqualTo("store_ref", storeDocumentId)
//                .whereIn("reservation_state", Arrays.asList(State.END.ordinal(), State.FAIL.ordinal()));
//
//        CompletableFuture<List<ReservationEntity>> future = new CompletableFuture<>();
//
//        query.addSnapshotListener((querySnapshot, e) -> {
//            if (e != null) {
//                future.completeExceptionally(e);
//                return;
//            }
//
//            List<ReservationEntity> endReservations = new ArrayList<>();
//            if (querySnapshot != null) {
//                for (QueryDocumentSnapshot reservationDocument : querySnapshot.getDocuments()) {
//                    ReservationEntity reservationEntity = reservationDocument.toObject(ReservationEntity.class);
//                    endReservations.add(reservationEntity);
//                }
//                endReservations.sort(Comparator.comparing(entity -> entity.getCreatedAt().toSqlTimestamp(), Comparator.reverseOrder()));
//            }
//            future.complete(endReservations);
//        });
//
//        return future;
//    }

    //가게 document id를 받아 해당 가게의 완료된 예약들을 가져옵니다.
//    public List<ReservationEntity> getEndByStoreRef(String storesRef){
//
//        List<ReservationEntity> reservations = new ArrayList<>();
//
//        Firestore firestore = FirestoreClient.getFirestore();
//        DocumentReference storeDocumentId = firestore.collection("stores").document(storesRef);
//        CollectionReference reservationsRef = firestore.collection("reservation");
//        Query query = reservationsRef.whereEqualTo("store_ref", storeDocumentId);
//        ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();
//        QuerySnapshot querySnapshot = null;
//        try {
//            querySnapshot = querySnapshotApiFuture.get();
//        } catch (InterruptedException | ExecutionException e) {
//            throw new ApplicationException(ErrorStatus.toErrorStatus("완료된 예약을 가져오는 도중 에러가 발생하였습니다." ,400, LocalDateTime.now()));
//        }
//
//        Comparator<ReservationEntity> createdAtComparator = Comparator
//                .comparing(ReservationEntity::getCreatedAt, Comparator.reverseOrder());
//
//        for (QueryDocumentSnapshot reservationDocument : querySnapshot.getDocuments()) {
//            ReservationEntity reservationEntity = reservationDocument.toObject(ReservationEntity.class);
//            if(reservationEntity.getReservationState() == State.END.ordinal() || reservationEntity.getReservationState() == State.FAIL.ordinal()) {
//                reservations.add(reservationEntity);
//            }
//        }
//        reservations.sort(createdAtComparator);
//
//        return reservations;
//    }
}