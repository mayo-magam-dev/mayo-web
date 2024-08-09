package com.example.mayoweb.user.domain.dto.response;

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

      String display_name,

      String photo_url,

      Date created_time,

      String phone_number,

      Boolean is_manager,

      Boolean agree_terms1,

      Boolean agree_terms2,

      Boolean agree_marketing,

      String currentLocation,

      String gender,

      String name,

      Date birthday,

      @JsonIgnore
      @Nullable
      DocumentReference store_ref
)
{
}
