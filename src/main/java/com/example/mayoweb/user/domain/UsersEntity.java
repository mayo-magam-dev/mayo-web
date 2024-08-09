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
    public String currentLocation;

    @PropertyName("gender")
    public String gender;

    @PropertyName("name")
    public String name;

    @PropertyName("birthday")
    public Date birthday;

    @PropertyName("store_ref")
    public DocumentReference storeRef;

    @PropertyName("favorite_stores")
    public List<DocumentReference> favoriteStores;

    @PropertyName("noticeStores")
    public List<DocumentReference> noticeStores;

    @Builder
    public UsersEntity(String userid, String uid, String email, String displayName, String photoUrl, Date createdTime, String phoneNumber, Boolean isManager, Boolean agreeTerms1, Boolean agreeTerms2, Boolean agreeMarketing, String currentLocation, String gender, String name, Date birthday, DocumentReference storeRef, List<DocumentReference> favoriteStores, List<DocumentReference> noticeStores) {
        this.userid = userid;
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
        this.photoUrl = photoUrl;
        this.createdTime = createdTime;
        this.phoneNumber = phoneNumber;
        this.isManager = isManager;
        this.agreeTerms1 = agreeTerms1;
        this.agreeTerms2 = agreeTerms2;
        this.agreeMarketing = agreeMarketing;
        this.currentLocation = currentLocation;
        this.gender = gender;
        this.name = name;
        this.birthday = birthday;
        this.storeRef = storeRef;
        this.favoriteStores = favoriteStores;
        this.noticeStores = noticeStores;
    }
}
