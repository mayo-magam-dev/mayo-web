package com.example.mayoweb.Reservation;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        for (QueryDocumentSnapshot reservationDocument : querySnapshot.getDocuments()) {
            ReservationEntity reservationEntity = reservationDocument.toObject(ReservationEntity.class);
            if(reservationEntity.getReservation_state() == 0) {
                newreservations.add(reservationEntity);
            }
        }
        return newreservations;
    }

    //가게 document id를 받아 해당 가게의 진행중인 예약들을 가져옵니다.
    public List<ReservationEntity> getProcessingByStoreRef(String storesRef) throws ExecutionException, InterruptedException {
        List<ReservationEntity> reservations = new ArrayList<>();
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference storeDocumentId = firestore.collection("stores").document(storesRef);
        CollectionReference reservationsRef = firestore.collection("reservation");
        Query query = reservationsRef.whereEqualTo("store_ref", storeDocumentId).whereEqualTo("reservation_state",1);
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();
        QuerySnapshot querySnapshot = querySnapshotApiFuture.get();
        for (QueryDocumentSnapshot reservationDocument : querySnapshot.getDocuments()) {
            ReservationEntity reservationEntity = reservationDocument.toObject(ReservationEntity.class);
                reservations.add(reservationEntity);
        }
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
        for (QueryDocumentSnapshot reservationDocument : querySnapshot.getDocuments()) {
            ReservationEntity reservationEntity = reservationDocument.toObject(ReservationEntity.class);
            if(reservationEntity.getReservation_state() == 2) {
                reservations.add(reservationEntity);
            }
        }
        return reservations;
    }

    //예약 도큐먼트 Id를 입력받아 reservation 객체를 가져옵니다.
    public Optional<ReservationEntity> findByReservationId(String reservationId) {
        Firestore db = FirestoreClient.getFirestore();
        ReservationEntity reservationEntity = null;

        try {
            DocumentReference docRef = db.collection("reservation").document(reservationId);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                reservationEntity = new ReservationEntity();
                reservationEntity.setId(document.getString("id"));
                reservationEntity.setReservation_id(document.getString("reservation_id"));
                reservationEntity.setReservation_is_plastics(document.getBoolean("reservation_is_plastics"));
                reservationEntity.setReservation_state((document.get("reservation_state", Integer.class)));
                reservationEntity.setReservation_request(document.getString("reservation_request"));
                reservationEntity.setCreated_at(document.getTimestamp("created_at"));
                reservationEntity.setPickup_time(document.getTimestamp("pickup_time"));
                reservationEntity.setTotal_price(document.getDouble("total_price"));

                DocumentReference storeRef = (DocumentReference) document.get("store_ref");
                if (storeRef != null) {
                    ApiFuture<DocumentSnapshot> store_ref = storeRef.get();
                    DocumentSnapshot storeDocument = store_ref.get();
                    if (storeDocument.exists()) {
                        reservationEntity.setStore_ref(storeDocument.getReference());
                    }
                }

                DocumentReference userRef = (DocumentReference) document.get("user_ref");
                if (userRef != null) {
                    ApiFuture<DocumentSnapshot> user_ref = userRef.get();
                    DocumentSnapshot userDocument = user_ref.get();
                    if (userDocument.exists()) {
                        reservationEntity.setUser_ref(userDocument.getReference());
                    }
                }

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
                    reservationEntity.setCart_ref(cart_ref);
                }
                catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("No such document!");
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(reservationEntity);
    }

    //예약 도큐먼트 Id를 입력받아 진행중 상태로 바꿉니다.
    public void ReservationProceeding(String reservationRef) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection("reservation").document(reservationRef);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            documentReference.update("reservation_state", 1);
        }
    }

    //예약 도큐먼트 Id를 입력받아 완료 상태로 바꿉니다.
    public void ReservationDone(String reservationRef) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection("reservation").document(reservationRef);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            documentReference.update("reservation_state", 2);
        }
    }

    //예약 도큐먼트 Id를 입력받아 실패 상태로 바꿉니다.
    public void ReservationFail(String reservationRef) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection("reservation").document(reservationRef);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            documentReference.update("reservation_state", 3);
        }
    }

    //예약 도큐먼트 Id를 입력받아 예약 객체를 받아옵니다.
    public ReservationEntity getReservationById(String reservationRef) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection("reservation").document(reservationRef);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        ReservationEntity reservation = null;
        if(document.exists()) {
            reservation = document.toObject(ReservationEntity.class);
        }
        return reservation;
    }
}
