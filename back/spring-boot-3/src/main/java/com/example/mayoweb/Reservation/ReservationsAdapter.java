package com.example.mayoweb.reservation;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Repository
@Slf4j
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
    public List<ReservationEntity> getNewByStoreRef(String storesRef) throws ExecutionException, InterruptedException {
        List<ReservationEntity> newreservations = new ArrayList<>();
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference storeDocumentId = firestore.collection("stores").document(storesRef);
        CollectionReference reservationsRef = firestore.collection("reservation");
        Query query = reservationsRef.whereEqualTo("store_ref", storeDocumentId);
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();
        QuerySnapshot querySnapshot = querySnapshotApiFuture.get();

        Comparator<ReservationEntity> createdAtComparator = Comparator
                .comparing(entity -> entity.getCreated_at().toSqlTimestamp(), Comparator.reverseOrder());

        for (QueryDocumentSnapshot reservationDocument : querySnapshot.getDocuments()) {
            ReservationEntity reservationEntity = reservationDocument.toObject(ReservationEntity.class);
            if(reservationEntity.getReservation_state() == State.NEW.ordinal()) {
                newreservations.add(reservationEntity);
            }
        }
        newreservations.sort(createdAtComparator);

        return newreservations;
    }

    //비동기식으로 가게 도큐먼트 id를 받아 해당 가게의 신규 예약들을 가져옵니다.
//    public List<ReservationEntity> getNewByStoreRef(String storesRef, Callback<List<ReservationEntity>> callback) throws ExecutionException, InterruptedException {
//        List<ReservationEntity> newreservations = new ArrayList<>();
//        Firestore firestore = FirestoreClient.getFirestore();
//        DocumentReference storeDocumentId = firestore.collection("stores").document(storesRef);
//        CollectionReference reservationsRef = firestore.collection("reservation");
//        Comparator<ReservationEntity> createdAtComparator = Comparator
//                .comparing(entity -> entity.getCreated_at().toSqlTimestamp(), Comparator.reverseOrder());
//        reservationsRef.whereEqualTo("store_ref", storeDocumentId).addSnapshotListener( new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirestoreException e) {
//                if(e != null) {
//                    log.info("Listen failed : " + e);
//                    return;
//                }
//                for(DocumentSnapshot doc : snapshots) {
//                    ReservationEntity reservationEntity = doc.toObject(ReservationEntity.class);
//                    if(reservationEntity.getReservation_state() == State.NEW.ordinal()) {
//                        newreservations.add(reservationEntity);
//                        log.info("newreservation : " + newreservations.toString());
//                    }
//                }
//                newreservations.sort(createdAtComparator);
//                callback.onSuccess(newreservations);
//            }
//        });
//        return newreservations;
//    }


    //가게 document id를 받아 해당 가게의 진행중인 예약들을 가져옵니다.
    public List<ReservationEntity> getProcessingByStoreRef(String storesRef) throws ExecutionException, InterruptedException {
        List<ReservationEntity> reservations = new ArrayList<>();
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference storeDocumentId = firestore.collection("stores").document(storesRef);
        CollectionReference reservationsRef = firestore.collection("reservation");
        Query query = reservationsRef.whereEqualTo("store_ref", storeDocumentId).whereEqualTo("reservation_state",State.PROCEEDING.ordinal());
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();
        QuerySnapshot querySnapshot = querySnapshotApiFuture.get();

        Comparator<ReservationEntity> createdAtComparator = Comparator
                .comparing(entity -> entity.getCreated_at().toSqlTimestamp(), Comparator.reverseOrder());

        for (QueryDocumentSnapshot reservationDocument : querySnapshot.getDocuments()) {
            ReservationEntity reservationEntity = reservationDocument.toObject(ReservationEntity.class);
            reservations.add(reservationEntity);
        }
        reservations.sort(createdAtComparator);

        return reservations;
    }

    //가게 document id를 받아 해당 가게의 완료된 예약들을 가져옵니다.
    public List<ReservationEntity> getEndByStoreRef(String storesRef) throws ExecutionException, InterruptedException {
        List<ReservationEntity> reservations = new ArrayList<>();
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference storeDocumentId = firestore.collection("stores").document(storesRef);
        CollectionReference reservationsRef = firestore.collection("reservation");
        Query query = reservationsRef.whereEqualTo("store_ref", storeDocumentId);
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();
        QuerySnapshot querySnapshot = querySnapshotApiFuture.get();

        Comparator<ReservationEntity> createdAtComparator = Comparator
                .comparing(entity -> entity.getCreated_at().toSqlTimestamp(), Comparator.reverseOrder());

        for (QueryDocumentSnapshot reservationDocument : querySnapshot.getDocuments()) {
            ReservationEntity reservationEntity = reservationDocument.toObject(ReservationEntity.class);
            if(reservationEntity.getReservation_state() == State.END.ordinal() || reservationEntity.getReservation_state() == State.FAIL.ordinal()) {
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
                catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                ReservationEntity reservationEntity = ReservationEntity.builder()
                        .id(document.getString("id"))
                        .reservation_id(document.getString("reservation_id"))
                        .reservation_is_plastics(document.getBoolean("reservation_is_plastics"))
                        .reservation_state((document.get("reservation_state", Integer.class)))
                        .reservation_request(document.getString("reservation_request"))
                        .created_at(document.getTimestamp("created_at"))
                        .pickup_time(document.getTimestamp("pickup_time"))
                        .total_price(document.getDouble("total_price"))
                        .store_ref((DocumentReference) document.get("store_ref"))
                        .user_ref((DocumentReference) document.get("user_ref"))
                        .cart_ref(cart_ref)
                        .build();

                return Optional.ofNullable(reservationEntity);
            }
            else {
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    //예약 도큐먼트 Id를 입력받아 진행중 상태로 바꿉니다.
    public void ReservationProceeding(String reservationRef) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection("reservation").document(reservationRef);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            documentReference.update("reservation_state", State.PROCEEDING.ordinal());
        }
    }

    //예약 도큐먼트 Id를 입력받아 완료 상태로 바꿉니다.
    public void ReservationDone(String reservationRef) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection("reservation").document(reservationRef);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            documentReference.update("reservation_state", State.END.ordinal());
        }
    }

    //예약 도큐먼트 Id를 입력받아 실패 상태로 바꿉니다.
    public void ReservationFail(String reservationRef) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection("reservation").document(reservationRef);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            documentReference.update("reservation_state", State.FAIL.ordinal());
        }
    }

    enum State {
        NEW, PROCEEDING, END, FAIL
    }

}