//package com.example.mayoweb.fcm;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.auth.oauth2.GoogleCredentials;
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.List;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//import org.apache.http.HttpHeaders;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class FCMService {
//
//    private static final String API_URL = "https://fcm.googleapis.com/v1/projects/mayo-app-280d4/messages:send";
//    private final ObjectMapper objectMapper;
//    private final WebPushNotificationsAdapter webPushNotificationsAdapter;
//
//    public Boolean sendMessageTo(String targetToken, String title, String body, String image)
//            throws IOException {
//        String message = makeMessage(targetToken, title, body, image);
//
//        OkHttpClient client = new OkHttpClient();
//        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=UTF-8"));
//
//        Request request = new Request.Builder()
//                .url(API_URL)
//                .post(requestBody)
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
//                .header(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
//                .build();
//
//        Response response;
//        boolean reslut = false;
//        try {
//            response = client.newCall(request).execute();
//            reslut = response.isSuccessful();
//        } catch (Exception e) {
//            log.error("FCM sendMessageTo error:" + LocalDateTime.now() + ", msg: " + e.getMessage());
//        }
//
//        return reslut;
//    }
//
//    private String makeMessage(String targetToken, String title, String body, String image)
//            throws JsonProcessingException {
//        FCMMessageDto fcmMessageDto = FCMMessageDto.builder()
//                .message(
//                        FCMMessageDto.Message.builder()
//                                .token(targetToken)
//                                .notification(
//                                        FCMMessageDto.Notification.builder()
//                                                .title(title)
//                                                .body(body)
//                                                .image(image)
//                                                .build()
//                                )
//                                .build()
//                )
//                .validateOnly(false)
//                .build();
//        return objectMapper.writeValueAsString(fcmMessageDto);
//    }
//
//    public boolean addWebPushNotifications(WebPushNotificationsDto webPushNotificationsDto) {
//        return webPushNotificationsAdapter.addWebPushNotifications(webPushNotificationsDto);
//    }
//
//    private String getAccessToken() throws IOException {
//        String firebaseConfigPath = "key/mayo-app-280d4-firebase-adminsdk-l6bfb-7770faf8c1.json";
//        String url = "https://www.googleapis.com/auth/cloud-platform";
//        GoogleCredentials googleCredentials = GoogleCredentials
//                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
//                .createScoped(List.of(url));
//        googleCredentials.refreshIfExpired();
//        return googleCredentials.getAccessToken().getTokenValue();
//    }
//}