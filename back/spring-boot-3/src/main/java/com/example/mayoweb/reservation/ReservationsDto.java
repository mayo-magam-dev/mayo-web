package com.example.mayoweb.reservation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@ToString
public class ReservationsDto {

    private String id;

    private String reservation_id;

    private Integer reservation_state;

    private String reservation_request;

    private Boolean reservation_is_plastics;

    private Timestamp created_at;

    private List<Integer> quantityList;

    private Timestamp pickup_time;

    private Double total_price;

    @JsonIgnore
    private DocumentReference store_ref;

    @JsonIgnore
    private DocumentReference user_ref;

    @JsonIgnore
    private List<DocumentReference> itemList_ref;

    @JsonIgnore
    private List<DocumentReference> cart_ref;

    public ReservationEntity toEntity() {
        return new ReservationEntity(id, reservation_id, reservation_state, reservation_request, reservation_is_plastics, created_at, quantityList, pickup_time, total_price, store_ref, user_ref, itemList_ref, cart_ref);
    }
}
