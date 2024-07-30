package com.example.mayoweb.reservation.domain.dto.response;

import lombok.Builder;

import java.sql.Timestamp;

@Builder
public record ReadReservationListResponse(
        String firstItemName,
        Integer itemQuantity,
        Timestamp createdAt,
        Timestamp pickupTime
) {
}
