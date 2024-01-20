package com.example.mayoweb.Dto;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.PropertyName;
import com.google.type.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class CartsDto {

    private String cartId;

    private Integer ItemCount;

    private Boolean cartActive;

    private Date created_at;

    private Date pickup_time;

    private Double subtotal;

    private DocumentReference userRef;

    private DocumentReference item;

    private DocumentReference store_ref;
}
