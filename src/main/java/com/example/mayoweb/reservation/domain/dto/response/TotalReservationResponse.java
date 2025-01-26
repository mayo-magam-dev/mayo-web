package com.example.mayoweb.reservation.domain.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record TotalReservationResponse(
        List<ReadReservationListResponse> reservationList,
        int newCount,
        int processingCount,
        int endCount,
        int failCount,
        Double totalAmount
) {
}
