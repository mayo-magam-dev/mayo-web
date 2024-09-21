package com.example.mayoweb.sse;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class SseService {

    private final ConcurrentMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, String> lastUuidMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Long> lastPongMap = new ConcurrentHashMap<>();
    private final Long SSE_TIMEOUT = 300000L;
    private final Long PONG_TIMEOUT = 180000L;

    public SseEmitter addEmitter(String clientId) {

        SseEmitter existingEmitter = getEmitter(clientId);

        if (existingEmitter != null) {
            return existingEmitter;
        }

        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);
        emitters.put(clientId, emitter);
        emitter.onCompletion(() -> emitters.remove(clientId));
        emitter.onTimeout(() -> emitters.remove(clientId));
        return emitter;
    }

    public SseEmitter getEmitter(String clientId) {
        return emitters.get(clientId);
    }

    public void sendMessageToClient(String clientId, String message, String name) {

        SseEmitter emitter = getEmitter(clientId);

        if (emitter != null) {

            try {
                UUID uuid = UUID.randomUUID();
                String lastUuid = lastUuidMap.get(clientId);

                if(lastUuid == null || lastUuid.isEmpty()) {
                    lastUuid = "first";
                }

                if (!lastUuid.equals(uuid.toString())) {

                    emitter.send(SseEmitter.event()
                            .name(name)
                            .data(message.getBytes(StandardCharsets.UTF_8))
                            .id(uuid.toString()));

                    lastUuidMap.put(clientId, uuid.toString());
                }
            } catch (IOException e) {
                emitters.remove(clientId);
                emitter.complete();
                emitter.completeWithError(e);
                lastUuidMap.remove(clientId);
                lastPongMap.remove(clientId);
            }
        }
    }

    public void pingClient(String clientId) {

        SseEmitter emitter = getEmitter(clientId);

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("ping")
                        .data("ping"));

            } catch (IOException e) {
                emitters.remove(clientId);
                emitter.complete();
                emitter.completeWithError(e);
                lastUuidMap.remove(clientId);
                lastPongMap.remove(clientId);
            }
        }
    }

    public void receivePong(String clientId) {
        lastPongMap.put(clientId, System.currentTimeMillis());
    }

    @Scheduled(fixedRate = 60000)
    public void sendPings() {
        for (String clientId : emitters.keySet()) {
            pingClient(clientId);
        }
    }

    @Scheduled(fixedRate = 60000)
    public void checkPongTimeouts() {

        long currentTime = System.currentTimeMillis();

        for (Map.Entry<String, Long> entry : lastPongMap.entrySet()) {

            String clientId = entry.getKey();
            long lastPongTime = entry.getValue();

            if (currentTime - lastPongTime > PONG_TIMEOUT) {

                SseEmitter emitter = getEmitter(clientId);

                if (emitter != null) {
                    emitter.complete();
                }

                emitters.remove(clientId);
                lastPongMap.remove(clientId);
            }
        }
    }
}
