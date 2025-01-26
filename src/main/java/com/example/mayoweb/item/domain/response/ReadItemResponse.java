package com.example.mayoweb.item.domain.response;

import com.example.mayoweb.item.domain.ItemEntity;
import lombok.Builder;

@Builder
public record ReadItemResponse(
         String itemId,

         String itemName,

         String itemDescription,

         Integer originalPrice,

         Double salePercent,

         Integer itemQuantity,

         Boolean itemOnSale,

         String itemImage,

         Double salePrice,

         Integer cookingTime,

         String additionalInformation,

         Boolean isActive,

         Boolean isDisplay
) {

    public static ReadItemResponse fromEntity(ItemEntity entity) {
        return ReadItemResponse.builder()
                .itemId(entity.getItemId())
                .itemName(entity.getItemName())
                .itemDescription(entity.getItemDescription())
                .originalPrice(entity.getOriginalPrice())
                .salePercent(entity.getSalePercent())
                .itemQuantity(entity.getItemQuantity())
                .itemOnSale(entity.getItemOnSale())
                .itemImage(entity.getItemImage())
                .salePrice(entity.getSalePrice())
                .cookingTime(entity.getCookingTime())
                .additionalInformation(entity.getAdditionalInformation())
                .isActive(entity.getIsActive())
                .isDisplay(entity.getIsDisplay())
                .build();
    }
}
