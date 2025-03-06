package com.example.mayoweb.fcm.repository;

import com.example.mayoweb.fcm.dto.WebPushNotificationsDto;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.WriteResult;

@Repository
@Slf4j
@RequiredArgsConstructor
public class WebPushNotificationsAdapter {

    private static final String COLLECTION_NAME = "push_notifications";
    private final Firestore firestore;

    public boolean addWebPushNotifications(WebPushNotificationsDto entity) {

        DocumentReference addDocRef = firestore.collection(COLLECTION_NAME).document();

        try {
            ApiFuture<WriteResult> writeResult = addDocRef.set(entity);

            writeResult.get();

            return true;
        } catch (Exception e) {
            log.error("addWebPushNotifications error" + e.getMessage());
            return false;
        }
    }
}