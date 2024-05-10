package com.example.mayoweb.fcm;

import com.example.mayoweb.reservation.ReservationsDto;
import com.google.cloud.Timestamp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
public class ReservationSchedule {

    private final FCMService fcmService;
    private static final String ACCEPT_TITLE = "예약을 성공했어요!";
    private static final String ACCEPT_TEXT = "픽업시간내로 픽업해주세요.";
    private static final String REJECT_TITLE = "예약을 실패되었어요.";
    private static final String REJECT_TEXT = "주문을 확인해주세요.";
    private static final String SERVER_NAME = "mayo-web-server";
    private static final String WEB_NOTIFICATION_FALSE = "push notification false = ";

    public boolean sendAcceptMessage(List<String> tokens, ReservationsDto dto) throws IOException {
        List<Boolean> results = new ArrayList<>();
        for (String token : tokens) {
            results.add(fcmService.sendMessageTo(token, ACCEPT_TITLE, ACCEPT_TEXT, "https://firebasestorage.googleapis.com/v0/b/mayo-app-280d4.appspot.com/o/mayo%2F%EB%A7%88%EC%9A%94-%EC%95%B1-%EB%A1%9C%EA%B3%A0-001%20(1).png?alt=media&token=2ada4e40-3cfb-4a16-9beb-b474a9a18a18"));
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
        if (!fcmService.addWebPushNotifications(webPushNotificationsDto)) {
            log.info(WEB_NOTIFICATION_FALSE + Timestamp.now());
        }
        return status;
    }

    public boolean sendRejectMessage(List<String> tokens, ReservationsDto dto) throws IOException {
        List<Boolean> results = new ArrayList<>();
        for (String token : tokens) {
            results.add(fcmService.sendMessageTo(token, REJECT_TITLE, REJECT_TEXT, "https://firebasestorage.googleapis.com/v0/b/mayo-app-280d4.appspot.com/o/mayo%2F%EB%A7%88%EC%9A%94-%EC%95%B1-%EB%A1%9C%EA%B3%A0-001%20(1).png?alt=media&token=2ada4e40-3cfb-4a16-9beb-b474a9a18a18"));
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
        if (!fcmService.addWebPushNotifications(webPushNotificationsDto)) {
            log.info(WEB_NOTIFICATION_FALSE + Timestamp.now());
        }
        return status;
    }
}
