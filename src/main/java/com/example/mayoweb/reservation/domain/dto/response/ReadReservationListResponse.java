package com.example.mayoweb.reservation.domain.dto.response;

import com.google.cloud.Timestamp;
import lombok.Builder;

@Builder
public record ReadReservationListResponse(
        String reservationId,
        String firstItemName,
        Integer itemQuantity,
        Timestamp createdAt,
        Timestamp pickupTime,
        Integer reservationState
) {
}
