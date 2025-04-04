package com.example.mayoweb.store.service;

import com.example.mayoweb.commons.annotation.FirestoreTransactional;
import com.example.mayoweb.commons.exception.ApplicationException;
import com.example.mayoweb.commons.exception.payload.ErrorStatus;
import com.example.mayoweb.fcm.service.FCMService;
import com.example.mayoweb.item.domain.request.OpenItemRequest;
import com.example.mayoweb.item.repository.ItemAdapter;
import com.example.mayoweb.store.repository.StoreAdapter;
import com.example.mayoweb.store.domain.dto.response.ReadStoreResponse;
import com.example.mayoweb.store.domain.dto.request.UpdateStoreRequest;
import com.example.mayoweb.store.domain.dto.response.UpdateStoreResponse;
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
public class StoreService {

    private final StoreAdapter storeAdapter;
    private final ItemAdapter itemAdapter;
    private final UserAdapter userAdapter;
    private final FCMService fcmService;

    public ReadStoreResponse getStoreById(String userId){

        DocumentReference storeRef = userAdapter.getStoreRefByUserId(userId).
                orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 가게가 없습니다.", 404, LocalDateTime.now())
                ));

        String storeId = storeRef.getId();

        return ReadStoreResponse.fromEntity(storeAdapter.findByStoreId(storeId)
                .orElseThrow(() -> new ApplicationException(
                    ErrorStatus.toErrorStatus("가게를 찾지 못했습니다.", 404, LocalDateTime.now())
                )));
    }

    public void closeStore(String userId){

        DocumentReference storeRef = userAdapter.getStoreRefByUserId(userId).
                orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 가게가 없습니다.", 404, LocalDateTime.now())
                ));

        String storeId = storeRef.getId();

        storeAdapter.closeStore(storeId);
        itemAdapter.updateItemsStateOutOfStock(storeId);
    }

    public void openStore(String userId, OpenItemRequest request) {

        DocumentReference storeRef = userAdapter.getStoreRefByUserId(userId).
                orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 가게가 없습니다.", 404, LocalDateTime.now())
                ));

        if(request.itemIdList() != null && request.quantityList() != null) {
            itemAdapter.updateItemOnSale(request.itemIdList(), request.quantityList());
        }

        String storeId = storeRef.getId();

        List<String> tokens = userAdapter.getNoticeUserFCMTokenByStoresRef(storeId);
        fcmService.sendOpenMessage(tokens, storeId);

        storeAdapter.openStore(storeId);
    }

    public UpdateStoreResponse updateStoreInformation(String userId, UpdateStoreRequest updateStoreRequest) {

        DocumentReference storeRef = userAdapter.getStoreRefByUserId(userId).
                orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 가게가 없습니다.", 404, LocalDateTime.now())
                ));

        if(!storeRef.getId().equals(updateStoreRequest.storeId())) {
            throw new ApplicationException(
                    ErrorStatus.toErrorStatus("본인 가게가 아닙니다.", 400, LocalDateTime.now())
            );
        }

        return storeAdapter.updateStore(updateStoreRequest);
    }

}
