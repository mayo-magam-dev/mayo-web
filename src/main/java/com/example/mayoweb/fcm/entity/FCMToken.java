package com.example.mayoweb.fcm.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.firestore.annotation.PropertyName;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class FCMToken {

    @PropertyName("fcm_token")
    @JsonProperty("fcm_token")
    private String token;

    @PropertyName("created_at")
    @JsonProperty("created_at")
    private String createdAt;

    @PropertyName("device_type")
    @JsonProperty("device_type")
    private String deviceType;

    @Builder
    public FCMToken(
            String token,
            String createdAt,
            String deviceType) {
        this.token = token;
        this.createdAt = createdAt;
        this.deviceType = deviceType;
    }
}
