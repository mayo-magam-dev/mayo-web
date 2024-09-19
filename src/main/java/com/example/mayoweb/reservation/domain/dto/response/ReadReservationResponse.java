package com.example.mayoweb.reservation.domain.dto.response;

import com.example.mayoweb.reservation.domain.ReservationEntity;
import com.google.cloud.Timestamp;
import lombok.Builder;

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

         Double totalPrice,

         String userRef
) {
    public static ReadReservationResponse fromEntity(ReservationEntity entity) {
        return ReadReservationResponse.builder()
                .id(entity.getId())
                .reservationId(entity.getReservationId())
                .reservationState(entity.getReservationState())
                .reservationRequest(entity.getReservationRequest())
                .reservationIsPlastics(entity.getReservationIsPlastics())
                .createdAt(entity.getCreatedAt())
                .quantityList(entity.getQuantityList())
                .pickupTime(entity.getPickupTime())
                .totalPrice(entity.getTotalPrice())
                .userRef(entity.getUserRef().getId())
                .build();
    }
}
