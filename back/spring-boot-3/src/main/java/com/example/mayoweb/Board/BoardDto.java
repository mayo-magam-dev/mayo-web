package com.example.mayoweb.Board;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.PropertyName;
import lombok.*;

@Builder
@AllArgsConstructor
@Data
@ToString
public class BoardDto {

    private String boardId;

    private String title;

    private String content;

    private Integer category;

    private String image;

    private String writer;

    private Timestamp write_time;
}
