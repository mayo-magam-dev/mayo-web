package com.example.mayoweb.items.domain.request;

import com.example.mayoweb.items.domain.ItemsEntity;
import com.google.cloud.firestore.DocumentReference;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.format.annotation.NumberFormat;

@Builder
public record CreateItemRequest(

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

    public static CreateItemRequest updateItemURL(CreateItemRequest createItemRequest, String imageUrl) {
        return CreateItemRequest.builder()
                .itemName(createItemRequest.itemName())
                .itemDescription(createItemRequest.itemDescription())
                .originalPrice(createItemRequest.originalPrice())
                .salePercent(createItemRequest.salePercent())
                .itemImage(imageUrl)
                .salePrice(createItemRequest.salePrice())
                .cookingTime(createItemRequest.cookingTime())
                .additionalInformation(createItemRequest.additionalInformation())
                .build();
    }
}
