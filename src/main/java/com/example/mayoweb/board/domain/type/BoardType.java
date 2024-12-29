package com.example.mayoweb.board.domain.type;

import lombok.Getter;

@Getter
public enum BoardType {
    TERMS(0),
    NOTICE(1),
    EVENT(2),
    TERMSDETAIL(3);

    private final int state;

    BoardType(int state) {
        this.state = state;
    }
}
