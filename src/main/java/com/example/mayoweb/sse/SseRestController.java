package com.example.mayoweb.sse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SseRestController {

    private final SseService sseService;

    @PostMapping("/check-sse")
    public ResponseEntity<Void> checkSse(@RequestBody PongDto pongDto) {

        String pong = pongDto.body().get("pong");

        if("pong".equals(pong)) {
            sseService.receivePong(pongDto.clientId());
        }

        log.info("received PongDto: {}", pong);

        return ResponseEntity.ok().build();
    }
}
