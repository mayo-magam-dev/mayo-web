package com.example.mayoweb.store.domain.dto.request;

public record UpdateStoreRequest
        (
            String storeId,

            String storeName,

            String address,

            String openTime,

            String closeTime,

            String saleStart,

            String saleEnd,

            String additionalComment,

            String storeNumber
        )
{
}
