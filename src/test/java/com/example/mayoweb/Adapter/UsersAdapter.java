package com.example.mayoweb.Adapter;
import com.example.mayoweb.Entity.StoresEntity;
import com.example.mayoweb.Entity.UsersEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
@Slf4j
public class UsersAdapter {
    private static final String COLLECTION_NAME = "users";

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


    public List<UsersEntity> findAllUsers() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<UsersEntity> userList = new ArrayList<UsersEntity>();
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference users = db.collection("users");
        ApiFuture<QuerySnapshot> querySnapshot = users.get();
        try {
            for (DocumentSnapshot doc : querySnapshot.get().getDocuments()) {
                String uid = doc.getData().get("uid").toString();
                userList.add(getUserById(uid));
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    //uid로 유저 객체 받아오는 쿼리
    public UsersEntity getUserById(String userId) {
        Firestore db = FirestoreClient.getFirestore();
        UsersEntity usersEntity = null;

        try {
            DocumentReference docRef = db.collection("users").document(userId);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                usersEntity = new UsersEntity();
                usersEntity.setUserid(document.getString("userid"));
                usersEntity.setEmail(document.getString("email"));
                usersEntity.setUid(document.getString("uid"));
                usersEntity.setPhoto_url(document.getString("photo_url"));
                usersEntity.setDisplay_name(document.getString("display_name"));
                usersEntity.setPhone_number(document.getString("phone_number"));
                usersEntity.setCreated_time(document.getDate("created_time"));
                usersEntity.setIs_manager(document.getBoolean("is_manager"));
                usersEntity.setGender(document.getString("gender"));
                usersEntity.setName(document.getString("name"));
                usersEntity.setBirthday(document.getDate("birthday"));

                DocumentReference storeRef = (DocumentReference) document.get("store_ref");
                if (storeRef != null) {
                    ApiFuture<DocumentSnapshot> store_ref = storeRef.get();
                    DocumentSnapshot storeDocument = store_ref.get();
                    if (storeDocument.exists()) {
                        StoresEntity storeEntity = storeDocument.toObject(StoresEntity.class);
                        usersEntity.setStore_ref(storeDocument.getReference());
                        System.out.println(storeEntity.getId());
                    }
                }
            } else {
                System.out.println("No such document!");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return usersEntity;
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
}

