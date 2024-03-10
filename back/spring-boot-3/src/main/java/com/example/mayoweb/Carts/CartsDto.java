package com.example.mayoweb.carts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
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

    private Timestamp created_at;

    private Timestamp pickup_time;

    private Double subtotal;

    @JsonIgnore
    private DocumentReference userRef;

    @JsonIgnore
    private DocumentReference item;

    @JsonIgnore
    private DocumentReference store_ref;
}
