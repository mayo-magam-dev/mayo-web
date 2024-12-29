package com.example.mayoweb.store.domain.dto.response;

import com.example.mayoweb.store.domain.StoreEntity;
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
    public static ReadStoreResponse fromEntity(StoreEntity storeEntity) {
        return ReadStoreResponse.builder()
                .storeId(storeEntity.getId())
                .storeName(storeEntity.getStoreName())
                .openState(storeEntity.getOpenState())
                .address(storeEntity.getAddress())
                .storeImage(storeEntity.getStoreImage())
                .openTime(storeEntity.getOpenTime())
                .closeTime(storeEntity.getCloseTime())
                .saleStart(storeEntity.getSaleStart())
                .saleEnd(storeEntity.getSaleEnd())
                .storeDescription(storeEntity.getStoreDescription())
                .storeNumber(storeEntity.getStoreNumber())
                .storeMapUrl(storeEntity.getStoreMapUrl())
                .originInfo(storeEntity.getOriginInfo())
                .additionalComment(storeEntity.getAdditionalComment())
                .build();
    }
}
