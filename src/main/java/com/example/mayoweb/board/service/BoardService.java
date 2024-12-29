package com.example.mayoweb.board.service;

import com.example.mayoweb.board.domain.dto.ReadBoardResponse;
import com.example.mayoweb.board.repository.BoardAdapter;
import com.example.mayoweb.commons.annotation.FirestoreTransactional;

import com.example.mayoweb.commons.exception.ApplicationException;
import com.example.mayoweb.commons.exception.payload.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FirestoreTransactional

public class BoardService {

    private final BoardAdapter boardAdapter;

    public List<ReadBoardResponse> getTermsBoard() {
        return boardAdapter.getTermsBoard().stream().map(ReadBoardResponse::fromEntity).toList();
    }

    public List<ReadBoardResponse> getNoticeBoard(){
        return boardAdapter.getNoticeBoard().stream().map(ReadBoardResponse::fromEntity).toList();
    }

    public ReadBoardResponse getBoardById(String boardId) {
        return ReadBoardResponse.fromEntity(boardAdapter.getBoardById(boardId)
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 게시글이 없습니다.", 404, LocalDateTime.now())
                )));
    }

}
