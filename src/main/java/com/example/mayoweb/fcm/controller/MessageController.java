package com.example.mayoweb.fcm.controller;

import com.example.mayoweb.fcm.dto.SendMessageRequest;
import com.example.mayoweb.fcm.service.FCMService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final FCMService fcmService;

    @PostMapping("/message")
    public ResponseEntity<Boolean> sendMessage(@RequestBody SendMessageRequest request) {
        return ResponseEntity.ok(fcmService.sendMarketingMessage(request));
    }
}
