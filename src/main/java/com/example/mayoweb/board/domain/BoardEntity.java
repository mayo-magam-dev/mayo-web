package com.example.mayoweb.board.domain;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.PropertyName;
import lombok.*;


@NoArgsConstructor
@Getter
public class BoardEntity {

    @DocumentId
    private String boardId;

    @PropertyName("title")
    private String title;

    @PropertyName("content")
    private String content;

    @PropertyName("category")
    private Integer category;

    @PropertyName("image")
    private String image;

    @PropertyName("writer")
    private String writer;

    @PropertyName("write_time")
    private Timestamp writeTime;

    @Builder
    public BoardEntity(String boardId, String title, String content, Integer category, String image, String writer, Timestamp writeTime) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.category = category;
        this.image = image;
        this.writer = writer;
        this.writeTime = writeTime;
    }
}
