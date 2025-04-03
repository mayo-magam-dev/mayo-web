package com.example.mayoweb.reservation.repository;

import com.example.mayoweb.cart.repository.CartAdapter;
import com.example.mayoweb.commons.annotation.FirestoreTransactional;
import com.example.mayoweb.commons.exception.ApplicationException;
import com.example.mayoweb.commons.exception.payload.ErrorStatus;
import com.example.mayoweb.fcm.service.FCMService;
import com.example.mayoweb.item.domain.response.ReadFirstItemResponse;
import com.example.mayoweb.item.repository.ItemAdapter;
import com.example.mayoweb.reservation.domain.ReservationEntity;
import com.example.mayoweb.reservation.domain.type.ReservationState;
import com.example.mayoweb.user.repository.UserAdapter;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Repository
@RequiredArgsConstructor
@Slf4j
@FirestoreTransactional
public class ReservationAdapter {

    private final CartAdapter cartsAdapter;
    private final ItemAdapter itemAdapter;
    private final FCMService fcmService;
    private final Firestore firestore;
    private final UserAdapter userAdapter;

    private final Map<String, ListenerRegistration> listenerRegistry = new HashMap<>();

    //가게 도큐먼트 id를 받아 해당 가게의 신규 예약들을 가져옵니다.
    public List<ReservationEntity> getNewByStoreId(String storeId){

        List<ReservationEntity> newReservations = new ArrayList<>();

        DocumentReference storeDocumentId = firestore.collection("stores").document(storeId);
        CollectionReference reservationsRef = firestore.collection("reservation");
        Query query = reservationsRef.whereEqualTo("store_ref", storeDocumentId)
                .whereEqualTo("reservation_state", ReservationState.NEW.getState());

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
            if(reservationEntity.getReservationState() == ReservationState.NEW.getState()) {
                newReservations.add(reservationEntity);
            }
        }

        newReservations.sort(createdAtComparator);

        return newReservations;
    }


    //가게 document id를 받아 해당 가게의 진행중인 예약들을 가져옵니다.
    public List<ReservationEntity> getProcessingByStoreId(String storeId) {

        List<ReservationEntity> reservations = new ArrayList<>();

        DocumentReference storeDocumentId = firestore.collection("stores").document(storeId);
        CollectionReference reservationsRef = firestore.collection("reservation");
        Query query = reservationsRef.whereEqualTo("store_ref", storeDocumentId).whereEqualTo("reservation_state",ReservationState.PROCEEDING.getState());
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();
        QuerySnapshot querySnapshot = null;

        try {
            querySnapshot = querySnapshotApiFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("firebase 통신 중 오류가 발생하였습니다.", 500, LocalDateTime.now()));
        }


        Comparator<ReservationEntity> createdAtComparator = Comparator
                .comparing(entity -> entity.getCreatedAt().toSqlTimestamp(), Comparator.reverseOrder());

        for (QueryDocumentSnapshot reservationDocument : querySnapshot.getDocuments()) {
            ReservationEntity reservationEntity = reservationDocument.toObject(ReservationEntity.class);
            reservations.add(reservationEntity);
        }

        reservations.sort(createdAtComparator);

        return reservations;
    }

    public List<ReservationEntity> getEndByStoreRefAndTimestamp(String storesRef, Timestamp timestamp) {

        List<ReservationEntity> reservations = new ArrayList<>();

        LocalDateTime localDateTime = timestamp.toSqlTimestamp().toLocalDateTime();
        ZoneId kstZoneId = ZoneId.of("Asia/Seoul");

        LocalDateTime startOfDay = localDateTime.toLocalDate().atStartOfDay().atZone(kstZoneId).toLocalDateTime();
        LocalDateTime endOfDay = localDateTime.toLocalDate().atTime(23, 59, 59, 999999999).atZone(kstZoneId).toLocalDateTime();

        DocumentReference storeDocumentId = firestore.collection("stores").document(storesRef);
        CollectionReference reservationsRef = firestore.collection("reservation");

        Query query = reservationsRef.whereEqualTo("store_ref", storeDocumentId)
                .whereIn("reservation_state", Arrays.asList(ReservationState.END.getState(), ReservationState.FAIL.getState()));

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

            if ((!createdAtLocalDateTime.isBefore(startOfDay)) &&
                    (!createdAtLocalDateTime.isAfter(endOfDay)) &&
                    (reservationEntity.getReservationState() == ReservationState.END.getState() ||
                            reservationEntity.getReservationState() == ReservationState.FAIL.getState())) {
                reservations.add(reservationEntity);
            }
        }

        reservations.sort(createdAtComparator);

        return reservations;
    }

    public List<ReservationEntity> getReservationsByStoreRefAndDate(String storesRef, LocalDate date) {

        List<ReservationEntity> reservations = new ArrayList<>();

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

            if(createdAtLocalDateTime.getYear() == date.getYear() && createdAtLocalDateTime.getMonth() == date.getMonth()) {
                reservations.add(reservationEntity);
            }
        }

        reservations.sort(createdAtComparator);

        return reservations;
    }

    //예약 도큐먼트 Id를 입력받아 reservation 객체를 가져옵니다.
    public Optional<ReservationEntity> findByReservationId(String reservationId) {

        try {
            DocumentReference docRef = firestore.collection("reservation").document(reservationId);
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

                ReservationEntity reservationEntity = fromDocument(document, cart_ref);

                return Optional.ofNullable(reservationEntity);
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    //예약 도큐먼트 Id를 입력받아 진행중 상태로 바꿉니다.
    public void reservationProceeding(String reservationRef){

        DocumentReference documentReference = firestore.collection("reservation").document(reservationRef);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = null;

        try {
            document = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("진행중으로 업데이트 하는 도중 에러가 발생하였습니다.", 400, LocalDateTime.now()));
        }

        if (document.exists()) {
            documentReference.update("reservation_state", ReservationState.PROCEEDING.getState());
        }
    }

    //예약 도큐먼트 Id를 입력받아 완료 상태로 바꿉니다.
    public void reservationDone(String reservationRef){

        DocumentReference documentReference = firestore.collection("reservation").document(reservationRef);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = null;
        try {
            document = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("완료로 업데이트 하는 도중 에러가 발생하였습니다.", 400, LocalDateTime.now()));
        }

        if (document.exists()) {
            documentReference.update("reservation_state", ReservationState.END.getState());
        }

    }

    //예약 도큐먼트 Id를 입력받아 실패 상태로 바꿉니다.
    public void reservationFail(String reservationRef){

        DocumentReference documentReference = firestore.collection("reservation").document(reservationRef);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = null;
        try {
            document = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("거절로 업데이트하는 도중 에러가 발생하였습니다.", 400, LocalDateTime.now()));
        }

        if (document.exists()) {
            documentReference.update("reservation_state", ReservationState.FAIL.getState());
        }
    }

    public List<ReservationEntity> getReservationByYearAndMonth(int year, int month) {
        CollectionReference reservationRef = firestore.collection("reservation");

        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1);

        Timestamp startTimestamp = Timestamp.of(Date.from(startOfMonth.atZone(ZoneId.of("Asia/Seoul")).toInstant()));
        Timestamp endTimestamp = Timestamp.of(Date.from(endOfMonth.atZone(ZoneId.of("Asia/Seoul")).toInstant()));

        Query query = reservationRef
                .whereGreaterThanOrEqualTo("created_at", startTimestamp)
                .whereLessThan("created_at", endTimestamp);

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();
        List<ReservationEntity> reservations = new ArrayList<>();

        try {
            QuerySnapshot querySnapshot = querySnapshotApiFuture.get();

            for (QueryDocumentSnapshot reservationDocument : querySnapshot.getDocuments()) {
                ReservationEntity reservationEntity = reservationDocument.toObject(ReservationEntity.class);
                reservations.add(reservationEntity);
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus(
                    "완료된 예약을 가져오는 도중 에러가 발생하였습니다.", 400, LocalDateTime.now()
            ));
        }

        reservations.sort(Comparator.comparing(ReservationEntity::getCreatedAt, Comparator.reverseOrder()));

        return reservations;
    }

    private ReadFirstItemResponse getFirstItemNameFromReservation(ReservationEntity reservationEntity) throws ExecutionException, InterruptedException {

        DocumentReference carts = cartsAdapter.getFirstCartByReservation(reservationEntity);

        return itemAdapter.getFirstItemNameFromCart(carts);
    }

    //비동기적으로 fcmToken을 수신하는 Listener를 실행합니다.
    public CompletableFuture<Void> createListener(String storeId, String userId) {

        if (listenerRegistry.containsKey(userId)) {
            listenerRegistry.get(userId).remove();
            listenerRegistry.remove(userId);
        }

        DocumentReference storeDocumentId = firestore.collection("stores").document(storeId);
        CollectionReference reservationsRef = firestore.collection("reservation");

        Query query = reservationsRef.whereEqualTo("store_ref", storeDocumentId)
                .whereEqualTo("reservation_state", ReservationState.NEW.getState());

        CompletableFuture<Void> future = new CompletableFuture<>();

        ListenerRegistration registration = query.addSnapshotListener((querySnapshot, e) -> {
            if (e != null) {
                future.completeExceptionally(e);
                return;
            }

            if (querySnapshot != null) {

                querySnapshot.getDocumentChanges().forEach(change -> {
                    if (change.getType() == DocumentChange.Type.ADDED) {
                        Set<String> tokens = new HashSet<>(userAdapter.getFCMTokenByUserRef(userId));
                        log.info("tokens : {}", tokens);

                        CompletableFuture.runAsync(() -> {
                            fcmService.sendNewReservationMessage(tokens.stream().toList());
                            log.info("fcm send");
                        });
                    }
                });
            }
        });

        listenerRegistry.put(userId, registration);

        return future;
    }

    public void stopListener(String userId) {
        ListenerRegistration registration = listenerRegistry.get(userId);
        if (registration != null) {
            registration.remove();
            listenerRegistry.remove(userId);
            log.info("리스너 제거 성공 : {}", userId);
        }
    }

    private static ReservationEntity fromDocument(DocumentSnapshot document, List<DocumentReference> cartRef) {
        return ReservationEntity.builder()
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
                .cartRef(cartRef)
                .build();
    }

}