package com.example.mayoweb.sse;

import com.example.mayoweb.commons.exception.SseException;
import com.example.mayoweb.commons.exception.payload.ErrorStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@Slf4j
public class SseService {
    private final ConcurrentMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter addEmitter(String clientId) {

        SseEmitter existingEmitter = getEmitter(clientId);

        if (existingEmitter != null) {
            return existingEmitter;
        }

        SseEmitter emitter = new SseEmitter(0L);
        emitters.put(clientId, emitter);

        sendMessageToClient(clientId, "initialMessage", "new-reservation");

        emitter.onError((e) -> {
            log.error("SSE에서 에러가 발생하였습니다 {}", e.getMessage());
            removeEmitter(clientId);
        });
        emitter.onCompletion(() -> {
            log.info("SSE 연결이 정상적으로 종료되었습니다.");
            removeEmitter(clientId);
        });
        emitter.onTimeout(() -> {
            log.info("SSE 연결이 타임아웃되었습니다.");
            removeEmitter(clientId);
    });

        return emitter;

    }

    public SseEmitter getEmitter(String clientId) {
        return emitters.get(clientId);
    }

    public synchronized void sendMessageToClient(String clientId, String message, String name) {

        SseEmitter emitter = getEmitter(clientId);

        if (emitter != null) {

            try {

                log.info("send message : {}", message);

                    emitter.send(SseEmitter.event()
                            .name(name)
                            .data(message.getBytes(StandardCharsets.UTF_8)));

                } catch (IOException e) {
                removeEmitter(clientId);

                throw new SseException(ErrorStatus.toErrorStatus("sse 오류가 발생하였습니다." + e.getMessage(), 500, LocalDateTime.now()));
            }
        }
    }

    private synchronized void removeEmitter(String clientId) {

        SseEmitter emitter = emitters.get(clientId);

        if (emitter != null) {

            log.info("removeEmitter : {}", emitter);

            emitter.complete();
            emitters.remove(clientId);
        }
    }
}
