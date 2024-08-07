package com.example.mayoweb.board.domain;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.PropertyName;
import lombok.*;


@NoArgsConstructor
@Getter
public class BoardEntity {

    @DocumentId
    public String boardId;

    @PropertyName("title")
    public String title;

    @PropertyName("content")
    public String content;

    @PropertyName("category")
    public Integer category;

    @PropertyName("image")
    public String image;

    @PropertyName("writer")
    public String writer;

    @PropertyName("write_time")
    public Timestamp writeTime;

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
