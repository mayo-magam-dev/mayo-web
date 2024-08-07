package com.example.mayoweb.items.domain.response;

import lombok.Builder;

@Builder
public record ItemDetailResponse(
        String itemName,
        Integer itemQuantity,
        Double subTotal
) {
}
