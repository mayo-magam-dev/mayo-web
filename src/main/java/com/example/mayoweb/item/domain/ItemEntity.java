package com.example.mayoweb.item.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.PropertyName;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Getter
@ToString
public class ItemEntity {

    @DocumentId
    @JsonProperty("item_id")
    public String itemId;

    @PropertyName("item_name")
    @JsonProperty("item_name")
    public String itemName;

    @PropertyName("item_description")
    @JsonProperty("item_description")
    public String itemDescription;

    @PropertyName("original_price")
    @JsonProperty("original_price")
    public Integer originalPrice;

    @PropertyName("sale_percent")
    @JsonProperty("sale_percent")
    public Double salePercent;

    @PropertyName("item_created")
    @JsonProperty("item_created")
    public Timestamp itemCreated;

    @PropertyName("item_modified")
    @JsonProperty("item_modified")
    public Timestamp itemModified;

    @PropertyName("item_quantity")
    @JsonProperty("item_quantity")
    public Integer itemQuantity;

    @PropertyName("item_on_sale")
    @JsonProperty("item_on_sale")
    public Boolean itemOnSale;

    @PropertyName("item_image")
    @JsonProperty("item_image")
    public String itemImage;

    @PropertyName("store_name")
    @JsonProperty("store_name")
    public String storeName;

    @PropertyName("store_address")
    @JsonProperty("store_address")
    public String storeAddress;

    @PropertyName("user_item_quantity")
    @JsonProperty("user_item_quantity")
    public Integer userItemQuantity;

    @PropertyName("sale_price")
    @JsonProperty("sale_price")
    public Double salePrice;

    @PropertyName("cooking_time")
    @JsonProperty("cooking_time")
    public Integer cookingTime;

    @PropertyName("additional_information")
    @JsonProperty("additional_information")
    public String additionalInformation;

    @PropertyName("store_ref")
    @JsonProperty("store_ref")
    public DocumentReference storeRef;

    @JsonProperty("is_active")
    @PropertyName("is_active")
    public Boolean isActive;

    @Builder
    public ItemEntity(String itemId, String itemName, String itemDescription, Integer originalPrice, Double salePercent, Timestamp itemCreated, Timestamp itemModified, Integer itemQuantity, Boolean itemOnSale, String itemImage, String storeName, String storeAddress, Integer userItemQuantity, Double salePrice, Integer cookingTime, String additionalInformation, DocumentReference storeRef, Boolean isActive) {
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
        this.isActive = isActive;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("item_id", itemId);
        map.put("item_name", itemName);
        map.put("item_description", itemDescription);
        map.put("original_price", originalPrice);
        map.put("sale_percent", salePercent);
        map.put("item_created", itemCreated);
        map.put("item_modified", itemModified);
        map.put("item_quantity", itemQuantity);
        map.put("item_on_sale", itemOnSale);
        map.put("item_image", itemImage);
        map.put("store_name", storeName);
        map.put("store_address", storeAddress);
        map.put("user_item_quantity", userItemQuantity);
        map.put("sale_price", salePrice);
        map.put("cooking_time", cookingTime);
        map.put("additional_information", additionalInformation);
        map.put("store_ref", storeRef);
        map.put("is_active", isActive);
        return map;
    }
}
