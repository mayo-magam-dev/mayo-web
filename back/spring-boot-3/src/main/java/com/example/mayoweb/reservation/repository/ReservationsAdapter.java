package com.example.mayoweb.reservation.repository;

import com.example.mayoweb.commons.exception.ApplicationException;
import com.example.mayoweb.commons.exception.payload.ErrorStatus;
import com.example.mayoweb.reservation.domain.ReservationEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Repository
public class ReservationsAdapter {

    //가게 도큐먼트 id를 받아 해당 가게의 모든 예약들을 가져옵니다.
    public List<ReservationEntity> getReservationsByStoreRef(String storesRef) throws ExecutionException, InterruptedException {

        List<ReservationEntity> reservations = new ArrayList<>();
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference storeDocumentId = firestore.collection("stores").document(storesRef);
        CollectionReference reservationsRef = firestore.collection("reservations");
        Query query = reservationsRef.whereEqualTo("store_ref", storeDocumentId);
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();
        QuerySnapshot querySnapshot = querySnapshotApiFuture.get();

        for (QueryDocumentSnapshot reservationDocument : querySnapshot.getDocuments()) {
            ReservationEntity reservationEntity = reservationDocument.toObject(ReservationEntity.class);
            reservations.add(reservationEntity);
        }

        return reservations;
    }

    //가게 도큐먼트 id를 받아 해당 가게의 신규 예약들을 가져옵니다.
    public List<ReservationEntity> getNewByStoreRef(String storeId){

        List<ReservationEntity> newReservations = new ArrayList<>();

        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference storeDocumentId = firestore.collection("stores").document(storeId);
        CollectionReference reservationsRef = firestore.collection("reservation");
        Query query = reservationsRef.whereEqualTo("store_ref", storeDocumentId);
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

    public CompletableFuture<List<ReservationEntity>> getNewByStoreIdAsync(String storeId) {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference storeDocumentId = firestore.collection("stores").document(storeId);
        CollectionReference reservationsRef = firestore.collection("reservation");
        Query query = reservationsRef.whereEqualTo("store_ref", storeDocumentId);
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();

        return CompletableFuture.supplyAsync(() -> {
            List<ReservationEntity> newReservations = new ArrayList<>();
            try {
                QuerySnapshot querySnapshot = querySnapshotApiFuture.get();
                for (QueryDocumentSnapshot reservationDocument : querySnapshot.getDocuments()) {
                    ReservationEntity reservationEntity = reservationDocument.toObject(ReservationEntity.class);
                    if (reservationEntity.getReservationState() == State.NEW.ordinal()) {
                        newReservations.add(reservationEntity);
                    }
                }
                Comparator<ReservationEntity> createdAtComparator = Comparator
                        .comparing(entity -> entity.getCreatedAt().toSqlTimestamp(), Comparator.reverseOrder());
                newReservations.sort(createdAtComparator);
            } catch (InterruptedException | ExecutionException e) {
                throw new ApplicationException(ErrorStatus.toErrorStatus("새로운 예약들을 가져오는데 실패하였습니다.", 400, LocalDateTime.now()));
            }
            return newReservations;
        });
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
        Query query = reservationsRef.whereEqualTo("store_ref", storeDocumentId);
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();

        return CompletableFuture.supplyAsync(() -> {
            List<ReservationEntity> proceedingReservations = new ArrayList<>();
            try {
                QuerySnapshot querySnapshot = querySnapshotApiFuture.get();
                for (QueryDocumentSnapshot reservationDocument : querySnapshot.getDocuments()) {
                    ReservationEntity reservationEntity = reservationDocument.toObject(ReservationEntity.class);
                    if (reservationEntity.getReservationState() == State.PROCEEDING.ordinal()) {
                        proceedingReservations.add(reservationEntity);
                    }
                }
                Comparator<ReservationEntity> createdAtComparator = Comparator
                        .comparing(entity -> entity.getCreatedAt().toSqlTimestamp(), Comparator.reverseOrder());
                proceedingReservations.sort(createdAtComparator);
            } catch (InterruptedException | ExecutionException e) {
                throw new ApplicationException(ErrorStatus.toErrorStatus("새로운 예약들을 가져오는데 실패하였습니다.", 400, LocalDateTime.now()));
            }
            return proceedingReservations;
        });
    }

    //가게 document id를 받아 해당 가게의 완료된 예약들을 가져옵니다.
    public List<ReservationEntity> getEndByStoreRef(String storesRef){

        List<ReservationEntity> reservations = new ArrayList<>();

        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference storeDocumentId = firestore.collection("stores").document(storesRef);
        CollectionReference reservationsRef = firestore.collection("reservation");
        Query query = reservationsRef.whereEqualTo("store_ref", storeDocumentId);
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = querySnapshotApiFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("완료된 예약을 가져오는 도중 에러가 발생하였습니다." ,400, LocalDateTime.now()));
        }

        Comparator<ReservationEntity> createdAtComparator = Comparator
                .comparing(ReservationEntity::getCreatedAt, Comparator.reverseOrder());

        for (QueryDocumentSnapshot reservationDocument : querySnapshot.getDocuments()) {
            ReservationEntity reservationEntity = reservationDocument.toObject(ReservationEntity.class);
            if(reservationEntity.getReservationState() == State.END.ordinal() || reservationEntity.getReservationState() == State.FAIL.ordinal()) {
                reservations.add(reservationEntity);
            }
        }
        reservations.sort(createdAtComparator);

        return reservations;
    }

    public CompletableFuture<List<ReservationEntity>> getEndByStoreIdAsync(String storeId) {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference storeDocumentId = firestore.collection("stores").document(storeId);
        CollectionReference reservationsRef = firestore.collection("reservation");
        Query query = reservationsRef.whereEqualTo("store_ref", storeDocumentId);
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();

        return CompletableFuture.supplyAsync(() -> {
            List<ReservationEntity> endReservations = new ArrayList<>();
            try {
                QuerySnapshot querySnapshot = querySnapshotApiFuture.get();
                for (QueryDocumentSnapshot reservationDocument : querySnapshot.getDocuments()) {
                    ReservationEntity reservationEntity = reservationDocument.toObject(ReservationEntity.class);
                    if (reservationEntity.getReservationState() == State.END.ordinal()) {
                        endReservations.add(reservationEntity);
                    }
                }
                Comparator<ReservationEntity> createdAtComparator = Comparator
                        .comparing(entity -> entity.getCreatedAt().toSqlTimestamp(), Comparator.reverseOrder());
                endReservations.sort(createdAtComparator);
            } catch (InterruptedException | ExecutionException e) {
                throw new ApplicationException(ErrorStatus.toErrorStatus("새로운 예약들을 가져오는데 실패하였습니다.", 400, LocalDateTime.now()));
            }
            return endReservations;
        });
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

    enum State {
        NEW, PROCEEDING, END, FAIL
    }

}