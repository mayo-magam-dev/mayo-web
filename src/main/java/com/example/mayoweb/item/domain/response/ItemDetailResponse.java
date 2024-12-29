package com.example.mayoweb.item.domain.response;

import lombok.Builder;

@Builder
public record ItemDetailResponse(
        String itemName,
        Integer itemQuantity,
        Double subTotal
) {
}
