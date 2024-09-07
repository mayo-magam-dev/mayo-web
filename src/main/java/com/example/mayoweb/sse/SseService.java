package com.example.mayoweb.sse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class SseService {

    private final ConcurrentMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter addEmitter(String clientId) {

        SseEmitter existingEmitter = getEmitter(clientId);

        if (existingEmitter != null) {
            return existingEmitter;
        }

        SseEmitter emitter = new SseEmitter(0L);
        emitters.put(clientId, emitter);
        emitter.onCompletion(() -> emitters.remove(clientId));  // 완료 시 제거
        emitter.onTimeout(() -> emitters.remove(clientId));     // 타임아웃 시 제거
        return emitter;
    }

    public SseEmitter getEmitter(String clientId) {
        return emitters.get(clientId);
    }

    public void sendMessageToClient(String clientId, String message, String name) {

        SseEmitter emitter = getEmitter(clientId);

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name(name).data(message, MediaType.APPLICATION_JSON));
            } catch (IOException e) {
                emitters.remove(clientId);
            }
        }
    }
}
