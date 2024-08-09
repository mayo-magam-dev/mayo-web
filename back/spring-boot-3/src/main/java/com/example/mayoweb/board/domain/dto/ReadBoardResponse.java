package com.example.mayoweb.board.domain.dto;

import com.example.mayoweb.board.domain.BoardEntity;
import com.google.cloud.Timestamp;
import lombok.Builder;

@Builder
public record ReadBoardResponse (
      String boardId,

      String title,

      String content,

      Integer category,

      String image,

      String writer,

      Timestamp writeTime
)
{
    public static ReadBoardResponse fromEntity(BoardEntity entity) {
        return ReadBoardResponse.builder()
                .boardId(entity.boardId)
                .title(entity.title)
                .content(entity.content)
                .category(entity.category)
                .image(entity.image)
                .writer(entity.writer)
                .writeTime(entity.writeTime)
                .build();
    }
}
