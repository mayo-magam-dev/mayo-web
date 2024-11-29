package com.example.mayoweb.fcm.service;

import com.example.mayoweb.commons.exception.ApplicationException;
import com.example.mayoweb.commons.exception.payload.ErrorStatus;
import com.example.mayoweb.fcm.dto.FCMMessageDto;
import com.example.mayoweb.fcm.dto.WebPushNotificationsDto;
import com.example.mayoweb.fcm.repository.WebPushNotificationsAdapter;
import com.example.mayoweb.store.domain.dto.response.ReadStoreResponse;
import com.example.mayoweb.store.repository.StoresAdapter;
import com.example.mayoweb.store.service.StoresService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.google.cloud.Timestamp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.http.HttpHeaders;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMService {

    private final StoresAdapter storesAdapter;
    private static final String ACCEPT_TITLE = "예약을 성공했어요!";
    private static final String ACCEPT_TEXT = "픽업시간내로 픽업해주세요.";
    private static final String REJECT_TITLE = "예약을 실패되었어요.";
    private static final String REJECT_TEXT = "주문을 확인해주세요.";
    private static final String SERVER_NAME = "mayo-web-server";
    private static final String WEB_NOTIFICATION_FALSE = "push notification false = ";
    private static final String API_URL = "https://fcm.googleapis.com/v1/projects/mayo-app-280d4/messages:send";
    private final ObjectMapper objectMapper;
    private final WebPushNotificationsAdapter webPushNotificationsAdapter;

    public Boolean sendMessageTo(String targetToken, String title, String body, String image){

        String message = makeMessage(targetToken, title, body, image);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=UTF-8"));

        Request request = null;
        try {
            request = new Request.Builder()
                    .url(API_URL)
                    .post(requestBody)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                    .header(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                    .build();
        } catch (IOException e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("fcm 요청 생성 중 오류가 발생하였습니다.", 500, LocalDateTime.now()));
        }

        Response response;

        boolean result = false;

        try {
            response = client.newCall(request).execute();
            result = response.isSuccessful();
        } catch (Exception e) {
            log.error("FCM sendMessageTo error:" + LocalDateTime.now() + ", msg: " + e.getMessage());
        }

        return result;
    }

    private String makeMessage(String targetToken, String title, String body, String image)  {
        FCMMessageDto fcmMessageDto = FCMMessageDto.builder()
                .message(
                        FCMMessageDto.Message.builder()
                                .token(targetToken)
                                .notification(
                                        FCMMessageDto.Notification.builder()
                                                .title(title)
                                                .body(body)
                                                .image(image)
                                                .build()
                                )
                                .build()
                )
                .validateOnly(false)
                .build();

        try {
            return objectMapper.writeValueAsString(fcmMessageDto);
        } catch (JsonProcessingException e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("json 변경 중 오류가 발생하였습니다.", 500, LocalDateTime.now()));
        }
    }

    //web_push_notification에 추가
    public boolean addWebPushNotifications(WebPushNotificationsDto webPushNotificationsDto) {
        return webPushNotificationsAdapter.addWebPushNotifications(webPushNotificationsDto);
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "/key/mayo-app-280d4.json";
        String url = "https://www.googleapis.com/auth/cloud-platform";
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of(url));
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    public boolean sendAcceptMessage(List<String> tokens) {

        List<Boolean> results = new ArrayList<>();

        for (String token : tokens) {
            results.add(sendMessageTo(token, ACCEPT_TITLE, ACCEPT_TEXT, "https://firebasestorage.googleapis.com/v0/b/mayo-app-280d4.appspot.com/o/mayo%2F%EB%A7%88%EC%9A%94-%EC%95%B1-%EB%A1%9C%EA%B3%A0-001%20(1).png?alt=media&token=2ada4e40-3cfb-4a16-9beb-b474a9a18a18"));
        }

        boolean status = !results.contains(false);
        WebPushNotificationsDto webPushNotificationsDto = WebPushNotificationsDto.builder()
                .notificationText(ACCEPT_TEXT)
                .notificationTitle(ACCEPT_TITLE)
                .notificationImageUrl("https://firebasestorage.googleapis.com/v0/b/mayo-app-280d4.appspot.com/o/mayo%2F%EB%A7%88%EC%9A%94-%EC%95%B1-%EB%A1%9C%EA%B3%A0-001%20(1).png?alt=media&token=2ada4e40-3cfb-4a16-9beb-b474a9a18a18")
                .sender(SERVER_NAME)
                .numSent(results.size())
                .timestamp(Timestamp.now())
                .status(status)
                .build();

        if (!addWebPushNotifications(webPushNotificationsDto)) {
            log.info(WEB_NOTIFICATION_FALSE + Timestamp.now());
        }

        return status;
    }

    public boolean sendRejectMessage(List<String> tokens)  {

        List<Boolean> results = new ArrayList<>();

        for (String token : tokens) {
            results.add(sendMessageTo(token, REJECT_TITLE, REJECT_TEXT, "https://firebasestorage.googleapis.com/v0/b/mayo-app-280d4.appspot.com/o/mayo%2F%EB%A7%88%EC%9A%94-%EC%95%B1-%EB%A1%9C%EA%B3%A0-001%20(1).png?alt=media&token=2ada4e40-3cfb-4a16-9beb-b474a9a18a18"));
        }

        boolean status = !results.contains(false);
        WebPushNotificationsDto webPushNotificationsDto = WebPushNotificationsDto.builder()
                .notificationText(REJECT_TITLE)
                .notificationTitle(REJECT_TEXT)
                .notificationImageUrl("https://firebasestorage.googleapis.com/v0/b/mayo-app-280d4.appspot.com/o/mayo%2F%EB%A7%88%EC%9A%94-%EC%95%B1-%EB%A1%9C%EA%B3%A0-001%20(1).png?alt=media&token=2ada4e40-3cfb-4a16-9beb-b474a9a18a18")
                .sender(SERVER_NAME)
                .numSent(results.size())
                .timestamp(Timestamp.now())
                .status(status)
                .build();

        if (!addWebPushNotifications(webPushNotificationsDto)) {
            log.info(WEB_NOTIFICATION_FALSE + Timestamp.now());
        }

        return status;
    }

    public boolean sendOpenMessage(List<String> tokens, String storeId)  {

        ReadStoreResponse store = ReadStoreResponse.fromEntity(storesAdapter.findByStoreId(storeId).orElseThrow(() -> new ApplicationException(
                ErrorStatus.toErrorStatus("가게를 찾지 못했습니다.", 404, LocalDateTime.now())
        )));

        List<Boolean> results = new ArrayList<>();
        for (String token : tokens) {
            results.add(sendMessageTo(token, store.storeName(), "마감할인 오픈!", store.storeImage()));
        }

        boolean status = !results.contains(false);
        WebPushNotificationsDto webUserPushNotificationsDto = WebPushNotificationsDto.builder()
                .notificationText("마감할인 오픈!")
                .notificationTitle(store.storeName())
                .notificationImageUrl(store.storeImage())
                .sender(SERVER_NAME)
                .numSent(results.size())
                .timestamp(Timestamp.now())
                .status(status)
                .build();

        if (!addWebPushNotifications(webUserPushNotificationsDto)) {
            log.info("push notification false = " + Timestamp.now());
        }
        return status;
    }

    public boolean sendNewReservationMessage(List<String> tokens){

        List<Boolean> results = new ArrayList<>();

        for (String token : tokens) {
            results.add(sendMessageTo(token, "신규 주문이 있습니다!", "주문을 확인해주세요!", null));
        }

        boolean status = !results.contains(false);
        WebPushNotificationsDto webUserPushNotificationsDto = WebPushNotificationsDto.builder()
                .notificationText("주문을 확인해주세요!")
                .notificationTitle("신규 주문이 들어왔어요!")
                .sender(SERVER_NAME)
                .numSent(results.size())
                .timestamp(Timestamp.now())
                .status(status)
                .build();

        if (!addWebPushNotifications(webUserPushNotificationsDto)) {
            log.info("push notification false = " + Timestamp.now());
        }
        return status;
    }


}