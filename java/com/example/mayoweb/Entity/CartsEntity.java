package com.example.mayoweb.Entity;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.PropertyName;
import com.google.type.Date;
import lombok.*;

@NoArgsConstructor
@Data
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
    public Date created_at;

    @PropertyName("pickup_time")
    public Date pickup_time;

    @PropertyName("subtotal")
    public Double subtotal;

    @PropertyName("userRef")
    public DocumentReference userRef;

    @PropertyName("item")
    public DocumentReference item;

    @PropertyName("store_ref")
    public DocumentReference store_ref;
}
