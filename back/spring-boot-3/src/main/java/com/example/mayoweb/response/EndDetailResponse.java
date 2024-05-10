package com.example.mayoweb.response;

import com.example.mayoweb.carts.CartsDto;
import com.example.mayoweb.items.ItemsDto;
import com.example.mayoweb.reservation.ReservationsDto;
import com.example.mayoweb.store.StoresDto;
import com.example.mayoweb.user.UsersDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class EndDetailResponse {
    private final String storeid;
    private final int endSize;
    private final List<String> endItem;
    private final List<ReservationsDto> endReservation;
    private final StoresDto store;
    private final ReservationsDto reservation;
    private final int item_count;
    private final UsersDto user;
    private final List<ItemsDto> items;
    private final List<CartsDto> carts;
}