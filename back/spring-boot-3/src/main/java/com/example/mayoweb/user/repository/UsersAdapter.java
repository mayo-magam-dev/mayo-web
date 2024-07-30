package com.example.mayoweb.user.repository;
import com.example.mayoweb.commons.exception.ApplicationException;
import com.example.mayoweb.commons.exception.payload.ErrorStatus;
import com.example.mayoweb.user.domain.UsersEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
            new ApplicationException(ErrorStatus.toErrorStatus("fcm 토큰을 가져오는데 에러가 발생하였습니다.", 404, LocalDateTime.now()));
        }

        return fcmTokens;
    }
}

