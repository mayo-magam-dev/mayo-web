package com.example.mayoweb.sse;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseService {

    private SseEmitter emitter;

    // 새로운 Emitter 추가
    public SseEmitter addEmitter() {
        emitter = new SseEmitter(0L);
        emitter.onCompletion(() -> emitter = null);  // 완료 시 null 처리
        emitter.onTimeout(() -> emitter = null);     // 타임아웃 시 null 처리
        return emitter;
    }

    // 단일 Emitter에게 메시지 전송
    public void sendMessage(String message, String name) {
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name(name).data(message));
            } catch (IOException e) {
                emitter = null;  // 전송 실패 시 Emitter 초기화
            }
        }
    }
}
