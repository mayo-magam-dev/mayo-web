package com.example.mayoweb.user.service;

import com.example.mayoweb.commons.exception.ApplicationException;
import com.example.mayoweb.commons.exception.payload.ErrorStatus;
import com.example.mayoweb.user.domain.UsersEntity;
import com.example.mayoweb.user.domain.dto.response.ReadUserResponse;
import com.example.mayoweb.user.repository.UsersAdapter;
import com.google.cloud.firestore.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersAdapter usersAdapter;

    public ReadUserResponse getUserByDocRef(DocumentReference doc){
        if(doc != null) {
            return ReadUserResponse.fromEntity(usersAdapter.getUserByDocRef(doc));
        }
        return null;
    }

    public ReadUserResponse getUserById(String userId) {
        return ReadUserResponse.fromEntity(usersAdapter.findByUserId(userId)
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당 유저를 찾지 못했습니다.", 404, LocalDateTime.now())
                )));
    }

    public List<String> getTokensByUserRef(String userRef) throws ExecutionException, InterruptedException {
        return usersAdapter.getFCMTokenByUserRef(userRef);
    }
}
