package com.example.mayoweb.item.domain.response;

import lombok.Builder;


@Builder
public record ReadFirstItemResponse(
        String itemName,
        Integer itemQuantity
) {
}
