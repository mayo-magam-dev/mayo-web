package com.example.mayoweb.Dto;

import com.google.cloud.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
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

    private String cartRef;

    private String store_ref;
}
