package com.example.mayoweb.sse;

import java.util.Map;

public record PongDto(

        Map<String, String> body,

        String clientId
) {
}
