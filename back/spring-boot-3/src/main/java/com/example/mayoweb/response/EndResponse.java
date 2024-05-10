package com.example.mayoweb.response;

import com.example.mayoweb.reservation.ReservationsDto;
import com.example.mayoweb.store.StoresDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class EndResponse {
    private final String storeid;
    private final int endSize;
    private final List<String> endItem;
    private final List<ReservationsDto> endReservation;
    private final StoresDto store;
}
