package com.example.mayoweb.store.service;

import com.example.mayoweb.commons.exception.ApplicationException;
import com.example.mayoweb.commons.exception.payload.ErrorStatus;
import com.example.mayoweb.fcm.service.FCMService;
import com.example.mayoweb.items.domain.request.OpenItemRequest;
import com.example.mayoweb.items.repository.ItemsAdapter;
import com.example.mayoweb.store.repository.StoresAdapter;
import com.example.mayoweb.store.domain.dto.response.ReadStoreResponse;
import com.example.mayoweb.store.domain.dto.request.UpdateStoreRequest;
import com.example.mayoweb.store.domain.dto.response.UpdateStoreResponse;
import com.example.mayoweb.user.repository.UsersAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoresService {

    private final StoresAdapter storesAdapter;
    private final ItemsAdapter itemsAdapter;
    private final UsersAdapter userAdapter;
    private final FCMService fcmService;

    public ReadStoreResponse getStoreById(String storeId){
        return ReadStoreResponse.fromEntity(storesAdapter.findByStoreId(storeId).orElseThrow(() -> new ApplicationException(
                ErrorStatus.toErrorStatus("가게를 찾지 못했습니다.", 404, LocalDateTime.now())
        )));
    }

    public void closeStore(String storeId){
        storesAdapter.closeStore(storeId);
        itemsAdapter.updateItemsStateOutOfStock(storeId);
    }

    public void openStore(OpenItemRequest request, String storeId) {
        if(request.itemIdList() != null && request.quantityList() != null) {
            itemsAdapter.updateItemOnSale(request.itemIdList(), request.quantityList());
        }

        List<String> tokens = userAdapter.getFCMTokenByStoresRef(storeId);
        fcmService.sendOpenMessage(tokens, storeId);

        storesAdapter.openStore(storeId);
    }

    public UpdateStoreResponse updateStoreInformation(UpdateStoreRequest updateStoreRequest) {
        return storesAdapter.updateStore(updateStoreRequest);
    }

}
