package com.example.mayoweb.items.domain;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.PropertyName;
import lombok.*;

@NoArgsConstructor
@Getter
@ToString
public class ItemsEntity {

    @DocumentId
    private String itemId;

    @PropertyName("item_name")
    private String itemName;

    @PropertyName("item_description")
    private String itemDescription;

    @PropertyName("original_price")
    private Integer originalPrice;

    @PropertyName("sale_percent")
    private Double salePercent;

    @PropertyName("item_created")
    private Timestamp itemCreated;

    @PropertyName("item_modified")
    private Timestamp itemModified;

    @PropertyName("item_quantity")
    private Integer itemQuantity;

    @PropertyName("item_on_sale")
    private Boolean itemOnSale;

    @PropertyName("item_image")
    private String itemImage;

    @PropertyName("store_name")
    private String storeName;

    @PropertyName("store_address")
    private String storeAddress;

    @PropertyName("user_item_quantity")
    private Integer userItemQuantity;

    @PropertyName("sale_price")
    private Double salePrice;

    @PropertyName("cooking_time")
    private Integer cookingTime;

    @PropertyName("additional_information")
    private String additionalInformation;

    @PropertyName("store_ref")
    private DocumentReference storeRef;

    @Builder
    public ItemsEntity(String itemId, String itemName, String itemDescription, Integer originalPrice, Double salePercent, Timestamp itemCreated, Timestamp itemModified, Integer itemQuantity, Boolean itemOnSale, String itemImage, String storeName, String storeAddress, Integer userItemQuantity, Double salePrice, Integer cookingTime, String additionalInformation, DocumentReference storeRef) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.originalPrice = originalPrice;
        this.salePercent = salePercent;
        this.itemCreated = itemCreated;
        this.itemModified = itemModified;
        this.itemQuantity = itemQuantity;
        this.itemOnSale = itemOnSale;
        this.itemImage = itemImage;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.userItemQuantity = userItemQuantity;
        this.salePrice = salePrice;
        this.cookingTime = cookingTime;
        this.additionalInformation = additionalInformation;
        this.storeRef = storeRef;
    }
}
