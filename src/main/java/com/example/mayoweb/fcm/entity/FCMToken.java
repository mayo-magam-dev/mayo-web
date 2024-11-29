package com.example.mayoweb.fcm.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.firestore.annotation.PropertyName;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

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
    @Jacksonized
    public FCMToken(
            @JsonProperty("fcm_token")
            String token,
            @JsonProperty("created_at")
            String createdAt,
            @JsonProperty("device_type")
            String deviceType) {
        this.token = token;
        this.createdAt = createdAt;
        this.deviceType = deviceType;
    }
}
