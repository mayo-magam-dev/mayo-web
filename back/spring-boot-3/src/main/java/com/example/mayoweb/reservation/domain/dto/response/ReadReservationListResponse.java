package com.example.mayoweb.reservation.domain.dto.response;

import com.google.cloud.Timestamp;
import lombok.Builder;

@Builder
public record ReadReservationListResponse(
        String firstItemName,
        Integer itemQuantity,
        Timestamp createdAt,
        Timestamp pickupTime
) {
}
