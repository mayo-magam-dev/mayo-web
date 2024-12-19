package com.example.mayoweb.cart.domain.dto.response;

import com.example.mayoweb.cart.domain.CartEntity;
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
    public static ReadCartsResponse fromEntity(CartEntity entity) {
        return ReadCartsResponse.builder()
                .cartId(entity.getCartId())
                .itemCount(entity.getItemCount())
                .createdAt(entity.getCreatedAt())
                .pickupTime(entity.getPickupTime())
                .subtotal(entity.getSubtotal())
                .build();
    }
}
