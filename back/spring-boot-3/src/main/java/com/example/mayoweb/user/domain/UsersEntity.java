package com.example.mayoweb.user.domain;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.PropertyName;
import com.google.firebase.database.annotations.Nullable;
import lombok.*;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@ToString
public class UsersEntity {
    @DocumentId
    public String userid;

    @PropertyName("uid")
    public String uid;

    @PropertyName("email")
    public String email;

    @PropertyName("display_name")
    public String displayName;

    @PropertyName("photo_url")
    public String photoUrl;

    @PropertyName("created_time")
    public Date createdTime;

    @PropertyName("phone_number")
    public String phoneNumber;

    @PropertyName("is_manager")
    public Boolean isManager;

    @PropertyName("agree_terms1")
    public Boolean agreeTerms1;

    @PropertyName("agree_terms2")
    public Boolean agreeTerms2;

    @PropertyName("agree_marketing")
    public Boolean agreeMarketing;

    @PropertyName("currentLocation")
    public @Nullable String currentLocation;

    @PropertyName("gender")
    public String gender;

    @PropertyName("name")
    public String name;

    @PropertyName("birthday")
    public Date birthday;

    @PropertyName("store_ref")
    public @Nullable DocumentReference storeRef;

    @PropertyName("favorite_stores")
    public List<DocumentReference> favoriteStores;

    @PropertyName("noticeStores")
    public List<DocumentReference> noticeStores;
}
