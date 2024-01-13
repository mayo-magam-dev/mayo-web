package com.example.mayoweb.Dto;

import com.example.mayoweb.Entity.StoresEntity;
import com.google.cloud.firestore.DocumentReference;
import com.google.firebase.database.annotations.Nullable;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class StoresDto {
    private String id;

    private String store_name;

    private Boolean open_state;

    private String address;

    private String store_image;

    private String open_time;

    private String close_time;

    private String sale_start;

    private String sale_end;

    private String store_description;

    private String store_number;

    private String store_mapUrl;

    private String origin_info;

    private String additional_comment;

    public List<Integer> openDayOfWeek;

    public Boolean isAuto;

    public List<Integer> open_day_of_week;

    public StoresEntity toEntity() {
        return new StoresEntity(id, store_name, open_state, address, store_image, open_time, close_time, sale_start, sale_end, store_description, store_number, store_mapUrl, origin_info, additional_comment, open_day_of_week);
    }
}
