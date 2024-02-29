package com.example.mayoweb.Items;

import com.example.mayoweb.Store.StoresEntity;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import lombok.*;

@Builder
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ItemsDto {
    private String itemId;

    private String item_name;

    private String item_description;

    private Integer original_price;

    private Double sale_percent;

    private Timestamp item_created;

    private Timestamp item_modified;

    private Integer item_quantity;

    private Boolean item_on_sale;

    private String item_image;

    private String store_name;

    private String store_address;

    private Integer user_item_quantity;

    private Double sale_price;

    private Integer cooking_time;

    private String additional_information;

    private DocumentReference cartRef;

    private DocumentReference store_ref;

    public ItemsEntity toEntity() {
        return new ItemsEntity(itemId, item_name, item_description, original_price, sale_percent, item_created, item_modified, item_quantity, item_on_sale, item_image, store_name, store_address, user_item_quantity, sale_price, cooking_time, additional_information, cartRef, store_ref);
    }
}
