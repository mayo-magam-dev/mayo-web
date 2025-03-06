package com.example.mayoweb.reservation.domain.dto.response;

import com.example.mayoweb.reservation.domain.ReservationEntity;
import com.example.mayoweb.reservation.domain.type.ReservationState;
import lombok.Builder;

import java.util.Arrays;
import java.util.Date;

@Builder
public record ReadAllReservationResponse(
        String reservationState,
        Date reservationDate,
        String storeName,
        Integer itemCount,
        Double totalPrice,
        String userName
) {
    public static ReadAllReservationResponse fromEntity(ReservationEntity entity, String storeName, Integer itemCount, String userName) {

        String state = Arrays.stream(ReservationState.values())
                .filter(s -> s.getState() == entity.getReservationState())
                .map(Enum::name)
                .findFirst()
                .orElse(null);

        return ReadAllReservationResponse.builder()
                .reservationState(state)
                .reservationDate(entity.getCreatedAt().toDate())
                .storeName(storeName)
                .itemCount(itemCount)
                .totalPrice(entity.getTotalPrice())
                .userName(userName)
                .build();
    }
}
