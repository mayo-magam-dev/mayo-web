package com.example.mayoweb.store;

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
    public String store_name;

    @PropertyName("open_state")
    public Boolean open_state;

    @PropertyName("address")
    public String address;

    @PropertyName("store_image")
    public String store_image;

    @PropertyName("open_time")
    public String open_time;

    @PropertyName("close_time")
    public String close_time;

    @PropertyName("sale_start")
    public String sale_start;

    @PropertyName("sale_end")
    public String sale_end;

    @PropertyName("store_description")
    public String store_description;

    @PropertyName("store_number")
    public String store_number;

    @PropertyName("store_mapUrl")
    public String store_mapUrl;

    @PropertyName("origin_info")
    public String origin_info;

    @PropertyName("additional_comment")
    public String additional_comment;

    @PropertyName("open_day_of_week")
    public List<Integer> open_day_of_week;
}
