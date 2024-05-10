package com.example.mayoweb.items;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.PropertyName;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class ItemsEntity {

    @DocumentId
    public String itemId;

    @PropertyName("item_name")
    public String item_name;

    @PropertyName("item_description")
    public String item_description;

    @PropertyName("original_price")
    public Integer original_price;

    @PropertyName("sale_percent")
    public Double sale_percent;

    @PropertyName("item_created")
    public Timestamp item_created;

    @PropertyName("item_modified")
    public Timestamp item_modified;

    @PropertyName("item_quantity")
    public Integer item_quantity;

    @PropertyName("item_on_sale")
    public Boolean item_on_sale;

    @PropertyName("item_image")
    public String item_image;

    @PropertyName("store_name")
    public String store_name;

    @PropertyName("store_address")
    public String store_address;

    @PropertyName("user_item_quantity")
    public Integer user_item_quantity;

    @PropertyName("sale_price")
    public Double sale_price;

    @PropertyName("cooking_time")
    public Integer cooking_time;

    @PropertyName("additional_information")
    public String additional_information;

    @PropertyName("cartRef")
    public DocumentReference cartRef;

    @PropertyName("store_ref")
    public DocumentReference store_ref;
}
