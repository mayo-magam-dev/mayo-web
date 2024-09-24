package com.example.mayoweb.board.controller;

import com.example.mayoweb.board.service.BoardService;
import com.example.mayoweb.board.domain.dto.ReadBoardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "게시판 API", description = "게시판 관리 API")
public class BoardRestController {

    private final BoardService boardService;

    @Operation(summary = "약관 및 정책 게시판의 모든 글을 가져옵니다.", description = "약관 및 정책 게시판의 모든 글을 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "약관 및 정책 조회 성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/board-term")
    public ResponseEntity<List<ReadBoardResponse>> getTermBoard() {
        return ResponseEntity.ok(boardService.getTermsBoard());
    }

    @Operation(summary = "공지사항 게시판의 모든 글을 가져옵니다.", description = "공지사항 게시판의 모든 글을 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공지사항 조회 성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/board-notice")
    public ResponseEntity<List<ReadBoardResponse>> getNoticeBoard() {
        return ResponseEntity.ok(boardService.getNoticeBoard());
    }

    @Operation(summary = "boardId를 받아 해당 게시글을 가져옵니다.", description = "board PK값으로 게시판의 모든 글을 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 조회 성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/board")
    public ResponseEntity<ReadBoardResponse> getBoardById(@RequestParam String boardId) {
        return ResponseEntity.ok(boardService.getBoardById(boardId));
    }

}
