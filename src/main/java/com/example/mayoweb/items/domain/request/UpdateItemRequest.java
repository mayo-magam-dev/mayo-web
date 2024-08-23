package com.example.mayoweb.items.domain.request;

import com.example.mayoweb.items.domain.ItemsEntity;
import lombok.Builder;

@Builder
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
                .itemImage(itemImage)
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

    public static UpdateItemRequest updateItemURL(UpdateItemRequest updateItemRequest, String imageUrl) {
        return UpdateItemRequest.builder()
                .itemId(updateItemRequest.itemId())
                .itemName(updateItemRequest.itemName())
                .itemDescription(updateItemRequest.itemDescription())
                .originalPrice(updateItemRequest.originalPrice())
                .salePercent(updateItemRequest.salePercent())
                .itemImage(imageUrl)
                .salePrice(updateItemRequest.salePrice())
                .cookingTime(updateItemRequest.cookingTime())
                .additionalInformation(updateItemRequest.additionalInformation())
                .build();
    }
}
