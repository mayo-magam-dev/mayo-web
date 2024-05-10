package com.example.mayoweb.reservation;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.PropertyName;
import lombok.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class ReservationEntity {

    @DocumentId
    public String id;

    @PropertyName("reservation_id")
    public String reservation_id;

    @PropertyName("reservation_state")
    public Integer reservation_state;

    @PropertyName("reservation_request")
    public String reservation_request;

    @PropertyName("reservation_is_plastics")
    public Boolean reservation_is_plastics;

    @PropertyName("created_at")
    public Timestamp created_at;

    @PropertyName("quantityList")
    public List<Integer> quantityList;

    @PropertyName("pickup_time")
    public Timestamp pickup_time;

    @PropertyName("total_price")
    public Double total_price;

    @PropertyName("store_ref")
    public DocumentReference store_ref;

    @PropertyName("user_ref")
    public DocumentReference user_ref;

    @PropertyName("itemList_ref")
    public List<DocumentReference> itemList_ref;

    @PropertyName("cart_ref")
    public List<DocumentReference> cart_ref;
}
