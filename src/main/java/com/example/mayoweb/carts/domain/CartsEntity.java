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
    private String cartId;

    @PropertyName("ItemCount")
    private Integer itemCount;

    @PropertyName("cartActive")
    private Boolean cartActive;

    @PropertyName("created_at")
    private Timestamp createdAt;

    @PropertyName("pickup_time")
    private Timestamp pickupTime;

    @PropertyName("subtotal")
    private Double subtotal;

    @PropertyName("userRef")
    private DocumentReference userRef;

    @PropertyName("item")
    private DocumentReference item;

    @PropertyName("store_ref")
    private DocumentReference storeRef;

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
