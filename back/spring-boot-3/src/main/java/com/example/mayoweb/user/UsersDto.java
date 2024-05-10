package com.example.mayoweb.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.cloud.firestore.DocumentReference;
import com.google.firebase.database.annotations.Nullable;
import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsersDto {
    private String userid;

    private String uid;

    private String email;

    private String display_name;

    private String photo_url;

    private Date created_time;

    private String phone_number;

    private Boolean is_manager;

    private Boolean agree_terms1;

    private Boolean agree_terms2;

    private Boolean agree_marketing;

    private String currentLocation;

    private String gender;

    private String name;

    private Date birthday;

    @JsonIgnore
    private @Nullable DocumentReference store_ref;
}
