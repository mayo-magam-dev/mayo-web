package com.example.mayoweb.Reservation;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.type.DateTime;
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

    private DocumentReference store_ref;

    private DocumentReference user_ref;

    private List<DocumentReference> itemList_ref;

    private List<DocumentReference> cart_ref;

    public ReservationEntity toEntity() {
        return new ReservationEntity(id, reservation_id, reservation_state, reservation_request, reservation_is_plastics, created_at, quantityList, pickup_time, total_price, store_ref, user_ref, itemList_ref, cart_ref);
    }
}
