package com.example.mayoweb.sse;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseService {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private String lastSentEvent = "";


    public void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
    }

    public void sendMessageToEmitters(String message, String name) {
        for (SseEmitter emitter : emitters) {
            try {
                if(!lastSentEvent.equals(message)) {
                    emitter.send(SseEmitter.event().name(name).data(message));
                    lastSentEvent = message;
                }
            } catch (IOException e) {
                emitters.remove(emitter);
            }
        }
    }
}
