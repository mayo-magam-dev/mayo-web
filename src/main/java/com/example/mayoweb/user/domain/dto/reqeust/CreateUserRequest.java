package com.example.mayoweb.user.domain.dto.reqeust;

import com.example.mayoweb.user.domain.UserEntity;
import com.example.mayoweb.user.domain.type.GenderType;
import com.google.cloud.Timestamp;

public record CreateUserRequest(
        String email,
        GenderType gender,
        String name,
        String displayName,
        String phoneNumber,
        Boolean agreeMarketing,
        Boolean agreeTerms1,
        Boolean agreeTerms2
) {
    public UserEntity toEntity(String uid) {
        return UserEntity.builder()
                .uid(uid)
                .userid(uid)
                .email(email)
                .createdTime(Timestamp.now())
                .phoneNumber(phoneNumber)
                .agreeMarketing(agreeMarketing)
                .agreeTerms1(agreeTerms1)
                .agreeTerms2(agreeTerms2)
                .build();
    }
}
