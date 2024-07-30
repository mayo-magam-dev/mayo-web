//package com.example.mayoweb.fcm;
//
//import com.google.cloud.firestore.DocumentReference;
//import com.google.cloud.firestore.Firestore;
//import com.google.firebase.cloud.FirestoreClient;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Repository;
//
//import com.google.api.core.ApiFuture;
//import com.google.cloud.firestore.WriteResult;
//
//@Repository
//@Slf4j
//public class WebPushNotificationsAdapter {
//
//    private static final String COLLECTION_NAME = "web_push_notifications";
//
//    public boolean addWebPushNotifications(WebPushNotificationsDto entity) {
//        Firestore firestore = FirestoreClient.getFirestore();
//        DocumentReference addDocRef = firestore.collection(COLLECTION_NAME).document();
//
//        try {
//            ApiFuture<WriteResult> writeResult = addDocRef.set(entity);
//
//            writeResult.get();
//
//            return true;
//        } catch (Exception e) {
//            log.error("addWebPushNotifications error" + e.getMessage());
//            return false;
//        }
//    }
//}