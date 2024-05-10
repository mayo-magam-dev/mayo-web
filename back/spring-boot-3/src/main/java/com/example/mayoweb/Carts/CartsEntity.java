package com.example.mayoweb.carts;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.PropertyName;
import com.google.type.Date;
import lombok.*;

@NoArgsConstructor
@Getter
@Builder
@ToString
@AllArgsConstructor
public class CartsEntity {

    @DocumentId
    public String cartId;

    @PropertyName("ItemCount")
    public Integer ItemCount;

    @PropertyName("cartActive")
    public Boolean cartActive;

    @PropertyName("created_at")
    public Timestamp created_at;

    @PropertyName("pickup_time")
    public Timestamp pickup_time;

    @PropertyName("subtotal")
    public Double subtotal;

    @PropertyName("userRef")
    public DocumentReference userRef;

    @PropertyName("item")
    public DocumentReference item;

    @PropertyName("store_ref")
    public DocumentReference store_ref;
}
