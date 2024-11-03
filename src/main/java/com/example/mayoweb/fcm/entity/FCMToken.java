package com.example.mayoweb.fcm.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class FCMToken {

    @JsonProperty("fcm_token")
    private String token;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("device_type")
    private String deviceType;

    @Builder
    public FCMToken(String token, String createdAt, String deviceType) {
        this.token = token;
        this.createdAt = createdAt;
        this.deviceType = deviceType;
    }
}
