package com.example.mayoweb.items.domain.request;

import com.example.mayoweb.items.domain.ItemsEntity;
import com.google.cloud.firestore.DocumentReference;

public record CreateItemRequest(
        String itemName,

        String itemDescription,

        Integer originalPrice,

        Double salePercent,

        String itemImage,

        Double salePrice,

        Integer cookingTime,

        String additionalInformation
) {
    public ItemsEntity createEntity(DocumentReference storeDocument) {
        return ItemsEntity.builder()
                .itemId(null)
                .itemName(itemName)
                .itemDescription(itemDescription)
                .originalPrice(originalPrice)
                .salePercent(salePercent)
                .itemImage(itemImage)
                .salePrice(salePrice)
                .cookingTime(cookingTime)
                .additionalInformation(additionalInformation)
                .storeRef(storeDocument)
                .build();
    }
}
