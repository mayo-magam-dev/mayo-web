package com.example.mayoweb;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

@Data
@AllArgsConstructor
public class ResponseWrapper {
    private int statusCode;
    private String name;
    private Object payload;

    public ResponseEntity getResponse()
    {
        return ResponseEntity.status(statusCode).body(Collections.singletonMap(name, payload));
    }
}
