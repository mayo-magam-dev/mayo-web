package com.example.mayoweb.store.domain.dto.response;

import com.example.mayoweb.store.domain.StoresEntity;
import lombok.*;

@Builder
public record ReadStoreResponse (
        String storeId,
        String storeName,
        Boolean openState,
        String address,
        String storeImage,
        String openTime,
        String closeTime,
        String saleStart,
        String saleEnd,
        String storeDescription,
        String storeNumber,
        String storeMapUrl,
        String originInfo,
        String additionalComment
    )
{
    public static ReadStoreResponse fromEntity(StoresEntity storesEntity) {
        return ReadStoreResponse.builder()
                .storeId(storesEntity.getId())
                .storeName(storesEntity.getStoreName())
                .openState(storesEntity.getOpenState())
                .address(storesEntity.getAddress())
                .storeImage(storesEntity.getStoreImage())
                .openTime(storesEntity.getOpenTime())
                .closeTime(storesEntity.getCloseTime())
                .saleStart(storesEntity.getSaleStart())
                .saleEnd(storesEntity.getSaleEnd())
                .storeDescription(storesEntity.getStoreDescription())
                .storeNumber(storesEntity.getStoreNumber())
                .storeMapUrl(storesEntity.getStoreMapUrl())
                .originInfo(storesEntity.getOriginInfo())
                .additionalComment(storesEntity.getAdditionalComment())
                .build();
    }
}
