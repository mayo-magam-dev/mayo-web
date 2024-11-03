package com.example.mayoweb.fcm.dto;

public record CreateFCMTokenRequest(
        String userId,
        String fcmToken
) {
}
