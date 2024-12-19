package com.example.mayoweb.item.domain.request;

import com.example.mayoweb.item.domain.ItemEntity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateItemRequest(

        @NotNull
        String itemId,

        @Size(min = 1, max = 15, message = "itemName의 글자 수는 1~15 사이여야합니다.")
        String itemName,

        @Size(max = 200, message = "최대 200글자입니다.")
        String itemDescription,

        @NotNull
        Integer originalPrice,

        @NotNull
        @Min(0)
        @Max(100)
        Double salePercent,

        String itemImage,

        @NotNull
        Double salePrice,

        @NotNull
        Integer cookingTime,

        String additionalInformation
) {
    public ItemEntity updateEntity() {
        return ItemEntity.builder()
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
