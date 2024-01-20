package com.example.mayoweb.Entity;

import com.example.mayoweb.Dto.StoresDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.PropertyName;
import com.google.firebase.database.annotations.Nullable;
import lombok.*;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Data
@ToString
public class UsersEntity {
    @DocumentId
    public String userid;

    @PropertyName("uid")
    public String uid;

    @PropertyName("email")
    public String email;

    @PropertyName("display_name")
    public String display_name;

    @PropertyName("photo_url")
    public String photo_url;

    @PropertyName("created_time")
    public Date created_time;

    @PropertyName("phone_number")
    public String phone_number;

    @PropertyName("is_manager")
    public Boolean is_manager;

    @PropertyName("agree_terms1")
    public Boolean agree_terms1;

    @PropertyName("agree_terms2")
    public Boolean agree_terms2;

    @PropertyName("agree_marketing")
    public Boolean agree_marketing;

    @PropertyName("currentLocation")
    public @Nullable String currentLocation;

    @PropertyName("gender")
    public String gender;

    @PropertyName("name")
    public String name;

    @PropertyName("birthday")
    public Date birthday;

    @PropertyName("store_ref")
    public @Nullable DocumentReference store_ref;

    @PropertyName("favorite_stores")
    public List<DocumentReference> favorite_stores;

    @PropertyName("noticeStores")
    public List<DocumentReference> noticeStores;
}
