package com.example.mayoweb.store.repository;

import com.example.mayoweb.commons.exception.ApplicationException;
import com.example.mayoweb.commons.exception.payload.ErrorStatus;
import com.example.mayoweb.store.domain.StoresEntity;
import com.example.mayoweb.store.domain.dto.request.UpdateStoreRequest;
import com.example.mayoweb.store.domain.dto.response.UpdateStoreResponse;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Repository
public class StoresAdapter {

    private static final String COLLECTION_NAME = "stores";

    public Optional<String> getStoreIdByUserId(String uid) {
        Firestore db = FirestoreClient.getFirestore();
        String storeID = null;

        try {
            DocumentReference docRef = db.collection("users").document(uid);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                DocumentReference storeRef = (DocumentReference) document.get("store_ref");
                if (storeRef != null) {
                    ApiFuture<DocumentSnapshot> store_ref = storeRef.get();
                    DocumentSnapshot storeDocument = store_ref.get();
                    if (storeDocument.exists()) {
                        StoresEntity storeEntity = storeDocument.toObject(StoresEntity.class);
                        return Optional.ofNullable(storeEntity.getId());
                    }
                }
            }
        }
        catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("해당 가게를 찾는데 오류가 발생하였습니다.", 400, LocalDateTime.now()));
        }

        return Optional.empty();
    }

    public DocumentReference getDocsRef(String storeId) {
        Firestore firestore = FirestoreClient.getFirestore();
        return firestore.collection(COLLECTION_NAME).document(storeId);
    }

    public Optional<StoresEntity> findByStoreId(String storeId) {

        Firestore db = FirestoreClient.getFirestore();
        DocumentReference documentReference = db.collection(COLLECTION_NAME).document(storeId);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = null;

        try {
            document = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("해당 가게를 찾는데 오류가 발생하였습니다", 400, LocalDateTime.now()));
        }

        return Optional.ofNullable(document.toObject(StoresEntity.class));
    }

    //document id값을 받아 가게를 닫습니다.
    public void closeStore(String storeId){

        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection(COLLECTION_NAME).document(storeId);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = null;

        try {
            document = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("가게를 닫는 중 오류가 발생하였습니다.", 400, LocalDateTime.now()));
        }

        if (document.exists()) {
            documentReference.update("open_state", false);
        }
    }

    //document id 값을 받아 가게 상태를 오픈으로 변경합니다.
    public void openStore(String storeId){
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection(COLLECTION_NAME).document(storeId);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = null;
        try {
            document = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("가게를 오픈하는 도중 오류가 발생하였습니다.", 400, LocalDateTime.now()));
        }
        if (document.exists()) {
            documentReference.update("open_state", true);
        }
    }

    //Store의 주소, 이름, 번호, 시간, 공지사항을 업데이트 하는 쿼리
    public UpdateStoreResponse updateStore(UpdateStoreRequest storeRequest){

        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection(COLLECTION_NAME).document(storeRequest.storeId());
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = null;

        try {
            document = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("스토어를 업데이트 하는 도중 알 수 없는 에러가 발생하였습니다.", 400, LocalDateTime.now()));
        }

        if (document.exists()) {
            documentReference.update(
                    "address", storeRequest.address(),
                    "store_name", storeRequest.storeName(),
                    "store_number", storeRequest.storeNumber(),
                    "open_time", storeRequest.openTime(),
                    "close_time", storeRequest.closeTime(),
                    "sale_start", storeRequest.saleStart(),
                    "sale_end", storeRequest.saleEnd(),
                    "additional_comment", storeRequest.additionalComment());

            return UpdateStoreResponse.builder()
                    .storeId(storeRequest.storeId())
                    .build();
        }

        throw new ApplicationException(ErrorStatus.toErrorStatus("스토어가 존재하지 않습니다.", 404, LocalDateTime.now()));
    }

}
