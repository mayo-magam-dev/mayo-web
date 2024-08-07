package com.example.mayoweb.user.service;

import com.example.mayoweb.user.domain.UsersEntity;
import com.example.mayoweb.user.domain.dto.response.ReadUserResponse;
import com.example.mayoweb.user.repository.UsersAdapter;
import com.google.cloud.firestore.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersAdapter usersAdapter;

    public ReadUserResponse getUserByDocRef(DocumentReference doc){
        if(doc != null) {
            return toDto(usersAdapter.getUserByDocRef(doc));
        }
        return null;
    }

    public List<String> getTokensByUserRef(String userRef) throws ExecutionException, InterruptedException {
        return usersAdapter.getFCMTokenByUserRef(userRef);
    }

    private ReadUserResponse toDto(UsersEntity entity) {
        return ReadUserResponse.builder()
                .userid(entity.userid)
                .uid(entity.uid)
                .email(entity.email)
                .display_name(entity.displayName)
                .photo_url(entity.photoUrl)
                .created_time(entity.createdTime)
                .phone_number(entity.phoneNumber)
                .is_manager(entity.isManager)
                .agree_terms1(entity.agreeTerms1)
                .agree_terms2(entity.agreeTerms2)
                .agree_marketing(entity.agreeMarketing)
                .currentLocation(entity.currentLocation)
                .gender(entity.gender)
                .name(entity.name)
                .birthday(entity.birthday)
                .store_ref(entity.storeRef)
                .build();
    }
}
