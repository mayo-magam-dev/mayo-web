package com.example.mayoweb.carts.domain.dto.response;

import com.example.mayoweb.carts.domain.CartsEntity;
import com.google.cloud.Timestamp;
import lombok.Builder;

@Builder
public record ReadCartsResponse(
         String cartId,

        Integer itemCount,

        Timestamp createdAt,

        Timestamp pickupTime,

        Double subtotal
) {
    public static ReadCartsResponse fromEntity(CartsEntity entity) {
        return ReadCartsResponse.builder()
                .cartId(entity.cartId)
                .itemCount(entity.itemCount)
                .createdAt(entity.createdAt)
                .pickupTime(entity.pickupTime)
                .subtotal(entity.subtotal)
                .build();
    }
}
