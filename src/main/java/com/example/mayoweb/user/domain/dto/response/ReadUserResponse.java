package com.example.mayoweb.user.domain.dto.response;

import com.example.mayoweb.user.domain.UserEntity;
import com.google.cloud.Timestamp;
import lombok.*;
import java.util.Date;

@Builder
public record ReadUserResponse (
      String userid,

      String uid,

      String email,

      String displayName,

      String photoUrl,

      Timestamp createdTime,

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
      public static ReadUserResponse fromEntity(UserEntity entity) {

            return ReadUserResponse.builder()
                    .userid(entity.getUserid())
                    .uid(entity.getUid())
                    .email(entity.getEmail())
                    .displayName(entity.getDisplayName())
                    .photoUrl(entity.getPhotoUrl())
                    .createdTime(entity.getCreatedTime())
                    .phoneNumber(entity.getPhoneNumber())
                    .isManager(entity.getIsManager())
                    .agreeTerms1(entity.getAgreeTerms1())
                    .agreeTerms2(entity.getAgreeTerms2())
                    .agreeMarketing(entity.getAgreeMarketing())
                    .currentLocation(entity.getCurrentLocation())
                    .gender(entity.getGender())
                    .name(entity.getName())
                    .birthday(entity.getBirthday())
                    .storeRef(entity.getStoreRef() != null ? entity.getStoreRef().getId() : null)
                    .build();
      }
}
