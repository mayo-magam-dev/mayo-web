package com.example.mayoweb.sse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@Slf4j
public class SseService {
    private final ConcurrentMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter addEmitter(String clientId) {

        SseEmitter existingEmitter = getEmitter(clientId);

        if (existingEmitter != null) {
            log.error("SseEmitter already exists: {}", existingEmitter);
            removeEmitter(clientId);
        }

        SseEmitter emitter = new SseEmitter(0L);
        emitters.put(clientId, emitter);

        JSONObject json = new JSONObject();

        try {
            json.put("data", "연결시작");
        } catch (JSONException e) {
            log.error("json Error");
        }

        sendMessageToClient(clientId, String.valueOf(json), "new-reservation");

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

                log.error("sse 오류가 발생하였습니다. : {} / clientId : {}" ,e, clientId);

            }
        } else {
            log.info("해당하는 sse가 없습니다. {}", clientId);
            removeEmitter(clientId);
        }
    }

    private synchronized void removeEmitter(String clientId) {

        log.info("remove emitter: {}", clientId);

        SseEmitter emitter = emitters.get(clientId);

        if (emitter != null) {

            emitter.complete();
            emitters.remove(clientId);
            log.info("emitter removed: {}", clientId);

        }
    }
}
