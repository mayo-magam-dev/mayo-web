package com.example.mayoweb.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final com.example.mayoweb.board.BoardAdapter boardAdapter;

    public List<BoardDto> getTermsBoard() throws ExecutionException, InterruptedException {
        return boardAdapter.getTermsBoard().stream().map(this::toDto).toList();
    }

    public List<BoardDto> getNoticeBoard() throws ExecutionException, InterruptedException {
        return boardAdapter.getNoticeBoard().stream().map(this::toDto).toList();
    }

    public BoardDto getBoardById(String boardId) throws ExecutionException, InterruptedException {
        return toDto(boardAdapter.getBoardById(boardId).orElseThrow());
    }

    private BoardDto toDto(BoardEntity entity) {
        return BoardDto.builder()
                .boardId(entity.boardId)
                .title(entity.title)
                .content(entity.content)
                .category(entity.category)
                .image(entity.image)
                .writer(entity.writer)
                .write_time(entity.write_time)
                .build();
    }
}
