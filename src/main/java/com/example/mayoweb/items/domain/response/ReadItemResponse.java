package com.example.mayoweb.items.domain.response;

import com.example.mayoweb.items.domain.ItemsEntity;
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

         String additionalInformation
) {

    public static ReadItemResponse fromEntity(ItemsEntity entity) {
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
                .build();
    }
}
