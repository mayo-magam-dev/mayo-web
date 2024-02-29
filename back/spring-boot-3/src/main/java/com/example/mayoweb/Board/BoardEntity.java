package com.example.mayoweb.Board;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.PropertyName;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
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
    public Timestamp write_time;
}
