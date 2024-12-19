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
    private String userid;

    @PropertyName("uid")
    private String uid;

    @PropertyName("email")
    private String email;

    @PropertyName("display_name")
    private String displayName;

    @PropertyName("photo_url")
    private String photoUrl;

    @PropertyName("created_time")
    private Date createdTime;

    @PropertyName("phone_number")
    private String phoneNumber;

    @PropertyName("is_manager")
    private Boolean isManager;

    @PropertyName("agree_terms1")
    private Boolean agreeTerms1;

    @PropertyName("agree_terms2")
    private Boolean agreeTerms2;

    @PropertyName("agree_marketing")
    private Boolean agreeMarketing;

    @PropertyName("currentLocation")
    private String currentLocation;

    @PropertyName("gender")
    private String gender;

    @PropertyName("name")
    private String name;

    @PropertyName("birthday")
    private Date birthday;

    @PropertyName("store_ref")
    private DocumentReference storeRef;

    @PropertyName("favorite_stores")
    private List<DocumentReference> favoriteStores;

    @PropertyName("noticeStores")
    private List<DocumentReference> noticeStores;

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
