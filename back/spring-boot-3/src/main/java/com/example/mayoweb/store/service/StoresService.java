package com.example.mayoweb.store.service;

import com.example.mayoweb.commons.exception.ApplicationException;
import com.example.mayoweb.commons.exception.payload.ErrorStatus;
import com.example.mayoweb.store.repository.StoresAdapter;
import com.example.mayoweb.store.domain.dto.response.ReadStoreResponse;
import com.example.mayoweb.store.domain.dto.request.UpdateStoreRequest;
import com.example.mayoweb.store.domain.dto.response.UpdateStoreResponse;
import com.google.cloud.firestore.DocumentReference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StoresService {

    private final StoresAdapter storesAdapter;

    public ReadStoreResponse getStoreById(String storeId){
        return ReadStoreResponse.fromEntity(storesAdapter.findByStoreId(storeId).orElseThrow(() -> new ApplicationException(
                ErrorStatus.toErrorStatus("가게를 찾지 못했습니다.", 404, LocalDateTime.now())
        )));
    }

    public void closeStore(String storeId){
        storesAdapter.closeStore(storeId);
    }

    public void openStore(String storeId) {
        storesAdapter.openStore(storeId);
    }

    public DocumentReference getDocsRef(String storeId) {
        return storesAdapter.getDocsRef(storeId);
    }

    public UpdateStoreResponse updateStoreInformation(UpdateStoreRequest updateStoreRequest) {
        return storesAdapter.updateStore(updateStoreRequest);
    }

}
