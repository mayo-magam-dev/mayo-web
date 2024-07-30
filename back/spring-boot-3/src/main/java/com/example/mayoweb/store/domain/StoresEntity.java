package com.example.mayoweb.store.domain;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.firebase.database.PropertyName;
import lombok.*;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class StoresEntity {

    @DocumentId
    public String id;

    @PropertyName("store_name")
    public String storeName;

    @PropertyName("open_state")
    public Boolean openState;

    @PropertyName("address")
    public String address;

    @PropertyName("store_image")
    public String storeImage;

    @PropertyName("open_time")
    public String openTime;

    @PropertyName("close_time")
    public String closeTime;

    @PropertyName("sale_start")
    public String saleStart;

    @PropertyName("sale_end")
    public String saleEnd;

    @PropertyName("store_description")
    public String storeDescription;

    @PropertyName("store_number")
    public String storeNumber;

    @PropertyName("store_mapUrl")
    public String storeMapUrl;

    @PropertyName("origin_info")
    public String originInfo;

    @PropertyName("additional_comment")
    public String additionalComment;

    @PropertyName("open_day_of_week")
    public List<Integer> openDayOfWeek;
}
