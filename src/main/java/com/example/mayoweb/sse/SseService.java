package com.example.mayoweb.sse;

import com.example.mayoweb.commons.exception.SseException;
import com.example.mayoweb.commons.exception.payload.ErrorStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class SseService {

    private final ConcurrentMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();
//    private final ConcurrentMap<String, String> lastUuidMap = new ConcurrentHashMap<>();

    public void addEmitter(String clientId) {

        SseEmitter existingEmitter = getEmitter(clientId);

        if (existingEmitter != null) {
            existingEmitter.complete();
            removeEmitter(clientId);
        }

        SseEmitter emitter = new SseEmitter(864000L);
        emitters.put(clientId, emitter);
        emitter.onCompletion(() -> removeEmitter(clientId));
        emitter.onTimeout(() -> removeEmitter(clientId));
        emitter.onError(e -> removeEmitter(clientId));
    }

    public SseEmitter getEmitter(String clientId) {
        return emitters.get(clientId);
    }

    public void sendMessageToClient(String clientId, String message, String name) {

        SseEmitter emitter = getEmitter(clientId);

        if (emitter != null) {

            try {
//                UUID uuid = UUID.randomUUID();
//                String lastUuid = lastUuidMap.get(clientId);

//                if(lastUuid == null || lastUuid.isEmpty()) {
//                    lastUuid = "first";
//                }

//                if (!lastUuid.equals(uuid.toString())) {

                    emitter.send(SseEmitter.event()
                            .name(name)
                            .data(message.getBytes(StandardCharsets.UTF_8)));

//                    lastUuidMap.put(clientId, uuid.toString());
                } catch (IOException e) {
                removeEmitter(clientId);

                throw new SseException(ErrorStatus.toErrorStatus("sse 오류가 발생하였습니다." + e.getMessage(), 500, LocalDateTime.now()));
            }
        }
    }

    private void removeEmitter(String clientId) {

        SseEmitter emitter = emitters.get(clientId);

        if (emitter != null) {
            emitter.complete();
        }

        emitters.remove(clientId);
//        lastUuidMap.remove(clientId);
    }
}
