package com.example.mayoweb.user.service;

import com.example.mayoweb.commons.annotation.FirestoreTransactional;
import com.example.mayoweb.commons.exception.ApplicationException;
import com.example.mayoweb.commons.exception.payload.ErrorStatus;
import com.example.mayoweb.user.domain.dto.reqeust.CreateUserRequest;
import com.example.mayoweb.user.domain.dto.response.ReadUserResponse;
import com.example.mayoweb.user.repository.UserAdapter;
import com.google.cloud.firestore.DocumentReference;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FirestoreTransactional
public class UserService {

    private final UserAdapter userAdapter;

    @Cacheable(value = "user")
    public ReadUserResponse getUserById(String userId) {
        return ReadUserResponse.fromEntity(userAdapter.findByUserId(userId)
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당 유저를 찾지 못했습니다.", 404, LocalDateTime.now())
                )));
    }

    public List<String> getTokensByUserRef(String userRef){
        return userAdapter.getFCMTokenByUserRef(userRef);
    }

    public List<String> getTokensByStoresId(String storeId) {
        return userAdapter.getFCMTokenByStoresId(storeId);
    }

    public void createWebFCMToken(String userId, String fcmToken) {
        userAdapter.createFCMTokenById(userId, fcmToken);
    }

    public boolean isManager(String userId) {
        return userAdapter.isManager(userId);
    }

    public DocumentReference getStoreRefById(String userId) {
        return userAdapter.getStoreRefByUserId(userId)
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 가게가 없습니다.", 404, LocalDateTime.now())
                ));
    }

    public void createUser(CreateUserRequest request, String uid) {
        userAdapter.save(request.toEntity(uid));
    }
}
