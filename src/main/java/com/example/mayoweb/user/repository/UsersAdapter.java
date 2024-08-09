package com.example.mayoweb.user.repository;
import com.example.mayoweb.commons.exception.ApplicationException;
import com.example.mayoweb.commons.exception.payload.ErrorStatus;
import com.example.mayoweb.store.domain.StoresEntity;
import com.example.mayoweb.user.domain.UsersEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Repository
@Slf4j
public class UsersAdapter {

    private static final String COLLECTION_NAME_FCM_TOKENS = "fcm_tokens";
    private static final String FIELD_FCM_TOKEN = "fcm_token";

    public UsersEntity getUserByDocRef(DocumentReference doc){
        ApiFuture<DocumentSnapshot> userSnapshotFuture = doc.get();
        DocumentSnapshot userSnapshot = null;
        try {
            userSnapshot = userSnapshotFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("유저를 찾을 수 없습니다.", 404, LocalDateTime.now()));
        }
        if (userSnapshot.exists()) {
            UsersEntity userEntity = userSnapshot.toObject(UsersEntity.class);
            return userEntity;
        }
        return null;
    }

    public Optional<UsersEntity> findByUserId(String userId) {

        Firestore db = FirestoreClient.getFirestore();
        DocumentReference documentReference = db.collection("users").document(userId);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = null;

        try {
            document = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("해당 유저를 찾는데 오류가 발생하였습니다", 400, LocalDateTime.now()));
        }

        return Optional.ofNullable(fromDocument(document));
    }

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
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("fcm 토큰을 가져오는데 에러가 발생하였습니다.", 404, LocalDateTime.now()));
        }

        return fcmTokens;
    }

    private UsersEntity fromDocument(DocumentSnapshot document) {
        return UsersEntity.builder()
                .uid(document.getId())
                .userid(document.getId())
                .email(document.getString("email"))
                .displayName(document.getString("display_name"))
                .photoUrl(document.getString("photo_url"))
                .isManager(document.getBoolean("is_manager"))
                .name(document.getString("name"))
                .storeRef((DocumentReference) document.get("store_ref"))
                .build();
    }
}

