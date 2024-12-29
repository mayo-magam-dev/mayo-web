package com.example.mayoweb.reservation.domain.type;

import lombok.Getter;

@Getter
public enum ReservationState {

    NEW(0),
    PROCEEDING(1),
    END(2),
    FAIL(3);

    private final int state;

    ReservationState(int state) {
        this.state = state;
    }
}
