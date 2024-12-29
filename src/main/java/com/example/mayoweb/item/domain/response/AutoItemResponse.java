package com.example.mayoweb.item.domain.response;

import com.example.mayoweb.item.domain.ItemEntity;
import lombok.Builder;

@Builder
public record AutoItemResponse(
        String storeName,
        String itemName
) {

    public static AutoItemResponse from(ItemEntity item) {
        return AutoItemResponse.builder()
                .itemName(item.getItemName())
                .storeName(item.getStoreName())
                .build();
    }
}
