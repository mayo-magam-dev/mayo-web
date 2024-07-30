package com.example.mayoweb.board.controller;

import com.example.mayoweb.board.service.BoardService;
import com.example.mayoweb.board.domain.dto.ReadBoardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardRestController {

    private final BoardService boardService;

    @GetMapping("/board-term")
    public ResponseEntity<List<ReadBoardResponse>> getTermBoard() {
        return ResponseEntity.ok(boardService.getTermsBoard());
    }

    @GetMapping("/board-notice")
    public ResponseEntity<List<ReadBoardResponse>> getNoticeBoard() {
        return ResponseEntity.ok(boardService.getNoticeBoard());
    }

    @GetMapping("/board")
    public ResponseEntity<ReadBoardResponse> getBoardById(@RequestParam String boardId) {
        return ResponseEntity.ok(boardService.getBoardById(boardId));
    }

}
