package com.example.mayoweb.fcm.dto;

public record SendMessageRequest(
        String title,
        String content,
        String image
) {
}
