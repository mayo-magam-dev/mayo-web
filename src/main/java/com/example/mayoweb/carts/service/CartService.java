package com.example.mayoweb.carts.service;

import com.example.mayoweb.carts.domain.dto.response.ReadCartsResponse;
import com.example.mayoweb.carts.repository.CartsAdapter;
import com.example.mayoweb.commons.exception.ApplicationException;
import com.example.mayoweb.commons.exception.payload.ErrorStatus;
import com.example.mayoweb.reservation.domain.ReservationEntity;
import com.example.mayoweb.reservation.repository.ReservationsAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CartService {

    private final CartsAdapter cartsAdapter;
    private final ReservationsAdapter reservationsAdapter;

    public List<ReadCartsResponse> getCartsByReservation(String reservationId) {

        ReservationEntity reservation = reservationsAdapter.findByReservationId(reservationId)
                .orElseThrow(() -> new ApplicationException(
                    ErrorStatus.toErrorStatus("예약을 찾을 수 없습니다.", 404, LocalDateTime.now())
                ));

        return cartsAdapter.getCartsByDocRef(reservation.getCartRef()).stream().map(ReadCartsResponse::fromEntity).toList();
    }

//    public ReadCartsResponse getCartsById(String cartId) {
//        return ReadCartsResponse.fromEntity(cartsAdapter.findCartById(cartId)
//                .orElseThrow(() -> new ApplicationException(ErrorStatus.toErrorStatus("알맞은 장바구니를 찾지 못했습니다.", 404, LocalDateTime.now())
//                )));
//    }

}
