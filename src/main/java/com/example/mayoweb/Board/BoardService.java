package com.example.mayoweb.Board;

import com.example.mayoweb.Store.StoresDto;
import com.example.mayoweb.Store.StoresEntity;
import com.google.cloud.Timestamp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardAdapter boardAdapter;

    public List<BoardDto> getBoard0() throws ExecutionException, InterruptedException {
        return boardAdapter.getBoard0().stream().map(this::toDto).toList();
    }

    public List<BoardDto> getBoard1() throws ExecutionException, InterruptedException {
        return boardAdapter.getBoard1().stream().map(this::toDto).toList();
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
