package com.example.mayoweb.reservation.domain.dto.response;

import com.example.mayoweb.reservation.domain.ReservationEntity;
import lombok.Builder;

import java.sql.Timestamp;
import java.util.List;

@Builder
public record ReadReservationResponse(
         String id,

         String reservationId,

         Integer reservationState,

         String reservationRequest,

         Boolean reservationIsPlastics,

         Timestamp createdAt,

         List<Integer>quantityList,

         Timestamp pickupTime,

         Double totalPrice
) {
    public static ReadReservationResponse fromEntity(ReservationEntity entity) {
        return ReadReservationResponse.builder()
                .id(entity.id)
                .reservationId(entity.reservationId)
                .reservationState(entity.reservationState)
                .reservationRequest(entity.reservationRequest)
                .reservationIsPlastics(entity.reservationIsPlastics)
                .createdAt(entity.createdAt)
                .quantityList(entity.quantityList)
                .pickupTime(entity.pickupTime)
                .totalPrice(entity.totalPrice)
                .build();
    }
}
