package com.example.mayoweb.items.domain.request;

import com.example.mayoweb.items.domain.ItemsEntity;

public record UpdateItemRequest(
        String itemId,

        String itemName,

        String itemDescription,

        Integer originalPrice,

        Double salePercent,

        String itemImage,

        Double salePrice,

        Integer cookingTime,

        String additionalInformation
) {
    public ItemsEntity updateEntity() {
        return ItemsEntity.builder()
                .itemId(itemId)
                .itemName(itemName)
                .itemDescription(itemDescription)
                .originalPrice(originalPrice)
                .salePercent(salePercent)
                .itemImage(itemImage)
                .salePrice(salePrice)
                .cookingTime(cookingTime)
                .additionalInformation(additionalInformation)
                .build();
    }
}
