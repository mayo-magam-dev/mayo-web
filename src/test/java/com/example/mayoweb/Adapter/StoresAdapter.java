package com.example.mayoweb.Adapter;

import com.example.mayoweb.Entity.StoresEntity;
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
public class StoresAdapter {

    private static final String COLLECTION_NAME = "stores";

    public DocumentReference getDocsRef(String storesRef) {
        Firestore firestore = FirestoreClient.getFirestore();
        return firestore.collection(COLLECTION_NAME).document(storesRef);
    }

    public Optional<StoresEntity> findByStoresRef(String storeRef) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference documentReference = db.collection(COLLECTION_NAME).document(storeRef);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        return Optional.ofNullable(document.toObject(StoresEntity.class));
    }

    public List<StoresEntity> getStores(Query query) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = query.get();
        QuerySnapshot querySnapshot = future.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        List<StoresEntity> stores = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            StoresEntity storesEntity = document.toObject(StoresEntity.class);
            stores.add(storesEntity);
        }
        return stores;
    }

    //모든 가게를 리스트로 가져옵니다.
//    public List<StoresEntity> getAllStores() throws ExecutionException, InterruptedException {
//
//        Firestore db = FirestoreClient.getFirestore();
//        Query query = db.collection(COLLECTION_NAME);
//
//        return getStores(query);
//    }


    //document id값을 받아 가게를 닫습니다.
    public void closeStore(String storesRef) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection(COLLECTION_NAME).document(storesRef);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            documentReference.update("open_state", false);
        }
    }

    //document id 값을 받아 가게를 오픈합니다.
    public void openStore(String storesRef) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection(COLLECTION_NAME).document(storesRef);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            documentReference.update("open_state", true);
        }
    }

    public void updateStore(StoresEntity storesEntity) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection(COLLECTION_NAME).document(storesEntity.getId());
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            documentReference.update("address", storesEntity.getAddress(),"store_name", storesEntity.getStore_name()
            ,"store_number", storesEntity.getStore_number(),"open_time", storesEntity.getOpen_time(), "close_time", storesEntity.getClose_time()
            , "sale_start", storesEntity.getSale_start(), "sale_end", storesEntity.getSale_end(), "additional_comment", storesEntity.getAdditional_comment());
        }
    }

}
