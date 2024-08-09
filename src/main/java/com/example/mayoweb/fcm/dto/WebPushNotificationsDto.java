package com.example.mayoweb.fcm.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import com.google.cloud.Timestamp;
import lombok.Builder;

@Getter
@NoArgsConstructor
public class WebPushNotificationsDto {

    private String notificationImageUrl;
    private String notificationText;
    private String notificationTitle;
    private int numSent;
    private String sender;
    private Boolean status;
    private Timestamp timestamp;

    @Builder
    public WebPushNotificationsDto(String notificationImageUrl, String notificationText, String notificationTitle,
                                      int numSent, String sender, Boolean status, Timestamp timestamp) {
        this.notificationImageUrl = notificationImageUrl;
        this.notificationText = notificationText;
        this.notificationTitle = notificationTitle;
        this.numSent = numSent;
        this.sender = sender;
        this.status = status;
        this.timestamp = timestamp;
    }
}