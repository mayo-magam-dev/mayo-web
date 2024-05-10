package com.example.mayoweb.user;
import com.example.mayoweb.store.StoresEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
@Slf4j
public class UsersAdapter {

    private static final String COLLECTION_NAME_USER = "users";
    private static final String COLLECTION_NAME_FCM_TOKENS = "fcm_tokens";
    private static final String COLLECTION_NAME_STORES = "stores";
    private static final String FIELD_NOTICE_STORES = "noticeStores";
    private static final String FIELD_FCM_TOKEN = "fcm_token";
    private static final String COLLECTION_NAME = "users";

    //모든 유저 객체를 가져옵니다.
    private List<UsersEntity> getUsers(Query query) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = query.get();
        QuerySnapshot querySnapshot = future.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        List<UsersEntity> users = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            UsersEntity usersEntity = document.toObject(UsersEntity.class);
            users.add(usersEntity);
        }
        return users;
    }

    //userid를 받아서 store document의 id를 받아오는 쿼리
    public String getStoreIdByUserId(String uid) {
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
                        storeID = storeEntity.getId();
                    }
                }
            } else {
                System.out.println("No such document!");
            }
        }
        catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return storeID;
    }

    // store document의 store id를 받아 그 가게 userid를 가져옴
    public List<String> getUserIdByStoreRef(String storesRef) throws ExecutionException, InterruptedException {
        List<String> usersId = new ArrayList<>();
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference storeDocumentId = firestore.collection("stores").document(storesRef);
        CollectionReference usersRef = firestore.collection("users");
        Query query = usersRef.whereEqualTo("store_ref", storeDocumentId);
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();
        QuerySnapshot querySnapshot = querySnapshotApiFuture.get();
        for (QueryDocumentSnapshot itemDocument : querySnapshot.getDocuments()) {
            usersId.add(itemDocument.getId());
        }
        return usersId;
    }

    public UsersEntity getUserByDocRef(DocumentReference doc) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentSnapshot> userSnapshotFuture = doc.get();
        DocumentSnapshot userSnapshot = userSnapshotFuture.get();
        if (userSnapshot.exists()) {
            UsersEntity userEntity = userSnapshot.toObject(UsersEntity.class);
            return userEntity;
        } else {
            System.out.println("User document does not exist!");
        }
        return null;
    }

    //UserRef로 token을 가져오는 쿼리
    public List<String> getFCMTokenByUserRef(String user_ref) {
    Firestore firestore = FirestoreClient.getFirestore();
    List<String> fcmTokens = new ArrayList<>();
    DocumentReference userRef = firestore.document(user_ref);

        try {
            DocumentSnapshot userDocument = userRef.get().get();

            if (userDocument.exists()) {
                DocumentReference fcmTokensCollectionRef = userRef.collection(COLLECTION_NAME_FCM_TOKENS).document();

                DocumentSnapshot fcmTokenDocument = fcmTokensCollectionRef.get().get();

                if (fcmTokenDocument.exists()) {
                    String fcmToken = fcmTokenDocument.getString(FIELD_FCM_TOKEN);
                    fcmTokens.add(fcmToken);
                }
            } else {
                log.info("사용자 token 없음");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fcmTokens;
    }
}

