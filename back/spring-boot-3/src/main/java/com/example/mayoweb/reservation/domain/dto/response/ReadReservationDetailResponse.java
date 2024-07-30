package com.example.mayoweb.reservation.domain.dto.response;

import lombok.Builder;

import java.sql.Timestamp;
import java.util.List;

@Builder
public record ReadReservationDetailResponse(

        List<String> itemName,

        List<Integer> itemCount,

        List<Double> subTotal,

        Integer totalQuantity,

        String reservationId,

        String request,

        Timestamp createdAt,

        Timestamp pickupTime,

        Double totalPrice,

        Boolean reservationIsPlastic

) {
}
