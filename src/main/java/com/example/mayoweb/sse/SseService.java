package com.example.mayoweb.sse;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class SseService {

    private final ConcurrentMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, String> lastUuidMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Long> lastPongMap = new ConcurrentHashMap<>();
    private final Long SSE_TIMEOUT = 180000L;

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
}
