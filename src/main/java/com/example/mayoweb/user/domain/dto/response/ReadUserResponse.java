package com.example.mayoweb.user.domain.dto.response;

import com.example.mayoweb.user.domain.UsersEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.cloud.firestore.DocumentReference;
import com.google.firebase.database.annotations.Nullable;
import lombok.*;

import java.util.Date;

@Builder
public record ReadUserResponse (
      String userid,

      String uid,

      String email,

      String displayName,

      String photoUrl,

      Date createdTime,

      String phoneNumber,

      Boolean isManager,

      Boolean agreeTerms1,

      Boolean agreeTerms2,

      Boolean agreeMarketing,

      String currentLocation,

      String gender,

      String name,

      Date birthday,

      String storeRef
)
{
      public static ReadUserResponse fromEntity(UsersEntity entity) {
            return ReadUserResponse.builder()
                    .userid(entity.userid)
                    .uid(entity.uid)
                    .email(entity.email)
                    .displayName(entity.displayName)
                    .photoUrl(entity.photoUrl)
                    .createdTime(entity.createdTime)
                    .phoneNumber(entity.phoneNumber)
                    .isManager(entity.isManager)
                    .agreeTerms1(entity.agreeTerms1)
                    .agreeTerms2(entity.agreeTerms2)
                    .agreeMarketing(entity.agreeMarketing)
                    .currentLocation(entity.currentLocation)
                    .gender(entity.gender)
                    .name(entity.name)
                    .birthday(entity.birthday)
                    .storeRef(entity.storeRef != null ? entity.storeRef.getId() : null)
                    .build();
      }
}
