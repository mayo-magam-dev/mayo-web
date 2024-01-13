package com.example.mayoweb.Entity;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.protobuf.util.Timestamps;
import lombok.*;

import javax.annotation.Nullable;
import java.text.ParseException;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BoardEntity {

    @DocumentId
    private String boardId;

    private String title;

    private String content;

    private Integer category;

    private @Nullable String image;

    private String writer;

    private Timestamp write_time;

    public void setWriteTime(String write_time) throws ParseException {
        this.write_time = Timestamp.fromProto(Timestamps.parse(write_time));
    }
}
