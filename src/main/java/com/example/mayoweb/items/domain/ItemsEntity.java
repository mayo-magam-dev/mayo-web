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
    public String itemId;

    @PropertyName("item_name")
    public String itemName;

    @PropertyName("item_description")
    public String itemDescription;

    @PropertyName("original_price")
    public Integer originalPrice;

    @PropertyName("sale_percent")
    public Double salePercent;

    @PropertyName("item_created")
    public Timestamp itemCreated;

    @PropertyName("item_modified")
    public Timestamp itemModified;

    @PropertyName("item_quantity")
    public Integer itemQuantity;

    @PropertyName("item_on_sale")
    public Boolean itemOnSale;

    @PropertyName("item_image")
    public String itemImage;

    @PropertyName("store_name")
    public String storeName;

    @PropertyName("store_address")
    public String storeAddress;

    @PropertyName("user_item_quantity")
    public Integer userItemQuantity;

    @PropertyName("sale_price")
    public Double salePrice;

    @PropertyName("cooking_time")
    public Integer cookingTime;

    @PropertyName("additional_information")
    public String additionalInformation;

    @PropertyName("store_ref")
    public DocumentReference storeRef;

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
