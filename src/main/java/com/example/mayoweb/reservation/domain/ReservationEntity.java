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
    private String id;

    @PropertyName("reservation_id")
    private String reservationId;

    @PropertyName("reservation_state")
    private Integer reservationState;

    @PropertyName("reservation_request")
    private String reservationRequest;

    @PropertyName("reservation_is_plastics")
    private Boolean reservationIsPlastics;

    @PropertyName("created_at")
    private Timestamp createdAt;

    @PropertyName("quantityList")
    private List<Integer> quantityList;

    @PropertyName("pickup_time")
    private Timestamp pickupTime;

    @PropertyName("total_price")
    private Double totalPrice;

    @PropertyName("store_ref")
    private DocumentReference storeRef;

    @PropertyName("user_ref")
    private DocumentReference userRef;

    @PropertyName("itemList_ref")
    private List<DocumentReference> itemListRef;

    @PropertyName("cart_ref")
    private List<DocumentReference> cartRef;

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
