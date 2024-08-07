package com.example.mayoweb.items.domain.response;

import lombok.Builder;


@Builder
public record ReadFirstItemResponse(
        String itemName,
        Integer itemQuantity
) {
}
