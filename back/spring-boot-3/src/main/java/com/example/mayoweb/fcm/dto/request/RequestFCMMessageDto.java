package com.example.mayoweb.fcm.dto.request;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestFCMMessageDto {

    private List<String> tokens;
    private String title;
    private String body;
    private String image;

    @Builder
    public RequestFCMMessageDto(List<String> tokens, String title, String body, String image) {
        this.tokens = tokens;
        this.title = title;
        this.body = body;
        this.image = image;
    }
}