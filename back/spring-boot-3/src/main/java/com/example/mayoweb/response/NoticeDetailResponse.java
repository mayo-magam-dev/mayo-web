package com.example.mayoweb.response;

import com.example.mayoweb.board.BoardDto;
import com.example.mayoweb.store.StoresDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class NoticeDetailResponse {
    List<BoardDto> boardList;
    BoardDto board;
    private final String storeid;
    private final StoresDto store;
}
