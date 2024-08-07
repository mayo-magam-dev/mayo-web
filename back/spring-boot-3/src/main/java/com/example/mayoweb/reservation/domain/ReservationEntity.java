package com.example.mayoweb.reservation.domain;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.PropertyName;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@Getter
@ToString
public class ReservationEntity {

    @DocumentId
    public String id;

    @PropertyName("reservation_id")
    public String reservationId;

    @PropertyName("reservation_state")
    public Integer reservationState;

    @PropertyName("reservation_request")
    public String reservationRequest;

    @PropertyName("reservation_is_plastics")
    public Boolean reservationIsPlastics;

    @PropertyName("created_at")
    public Timestamp createdAt;

    @PropertyName("quantityList")
    public List<Integer> quantityList;

    @PropertyName("pickup_time")
    public Timestamp pickupTime;

    @PropertyName("total_price")
    public Double totalPrice;

    @PropertyName("store_ref")
    public DocumentReference storeRef;

    @PropertyName("user_ref")
    public DocumentReference userRef;

    @PropertyName("itemList_ref")
    public List<DocumentReference> itemListRef;

    @PropertyName("cart_ref")
    public List<DocumentReference> cartRef;

    @Builder
    public ReservationEntity(String id, String reservationId, Integer reservationState, String reservationRequest, Boolean reservationIsPlastics, Timestamp createdAt, List<Integer> quantityList, Timestamp pickupTime, Double totalPrice, DocumentReference storeRef, DocumentReference userRef, List<DocumentReference> itemListRef, List<DocumentReference> cartRef) {
        this.id = id;
        this.reservationId = reservationId;
        this.reservationState = reservationState;
        this.reservationRequest = reservationRequest;
        this.reservationIsPlastics = reservationIsPlastics;
        this.createdAt = createdAt;
        this.quantityList = quantityList;
        this.pickupTime = pickupTime;
        this.totalPrice = totalPrice;
        this.storeRef = storeRef;
        this.userRef = userRef;
        this.itemListRef = itemListRef;
        this.cartRef = cartRef;
    }
}
