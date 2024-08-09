package com.example.mayoweb.carts.domain;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.PropertyName;
import lombok.*;

@NoArgsConstructor
@Getter
@ToString
public class CartsEntity {

    @DocumentId
    public String cartId;

    @PropertyName("ItemCount")
    public Integer itemCount;

    @PropertyName("cartActive")
    public Boolean cartActive;

    @PropertyName("created_at")
    public Timestamp createdAt;

    @PropertyName("pickup_time")
    public Timestamp pickupTime;

    @PropertyName("subtotal")
    public Double subtotal;

    @PropertyName("userRef")
    public DocumentReference userRef;

    @PropertyName("item")
    public DocumentReference item;

    @PropertyName("store_ref")
    public DocumentReference storeRef;

    @Builder
    public CartsEntity(String cartId, Integer itemCount, Boolean cartActive, Timestamp createdAt, Timestamp pickupTime, Double subtotal, DocumentReference userRef, DocumentReference item, DocumentReference storeRef) {
        this.cartId = cartId;
        this.itemCount = itemCount;
        this.cartActive = cartActive;
        this.createdAt = createdAt;
        this.pickupTime = pickupTime;
        this.subtotal = subtotal;
        this.userRef = userRef;
        this.item = item;
        this.storeRef = storeRef;
    }
}
