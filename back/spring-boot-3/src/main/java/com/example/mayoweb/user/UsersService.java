package com.example.mayoweb.user;

import com.google.cloud.firestore.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersAdapter usersAdapter;

    public UsersDto getUserByDocRef(DocumentReference doc) throws ExecutionException, InterruptedException {
        if(doc != null) {
            return toDto(usersAdapter.getUserByDocRef(doc));
        }
        return null;
    }

    public List<String> getTokensByUserRef(String userRef) throws ExecutionException, InterruptedException {
        return usersAdapter.getFCMTokenByUserRef(userRef);
    }

    private UsersDto toDto(UsersEntity entity) {
        return UsersDto.builder()
                .userid(entity.userid)
                .uid(entity.uid)
                .email(entity.email)
                .display_name(entity.display_name)
                .photo_url(entity.photo_url)
                .created_time(entity.created_time)
                .phone_number(entity.phone_number)
                .is_manager(entity.is_manager)
                .agree_terms1(entity.agree_terms1)
                .agree_terms2(entity.agree_terms2)
                .agree_marketing(entity.agree_marketing)
                .currentLocation(entity.currentLocation)
                .gender(entity.gender)
                .name(entity.name)
                .birthday(entity.birthday)
                .store_ref(entity.store_ref)
                .build();
    }
}
