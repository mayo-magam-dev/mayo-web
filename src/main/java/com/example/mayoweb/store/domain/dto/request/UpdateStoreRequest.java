package com.example.mayoweb.store.domain.dto.request;

import jakarta.validation.constraints.*;

public record UpdateStoreRequest
        (
            @NotNull
            String storeId,

            @Size(min = 1, max = 15, message = "storeName의 글자 수는 1~15 사이여야합니다.")
            String storeName,

            @Size(min = 1, max = 35, message = "storeName의 글자 수는 1~35 사이여야합니다.")
            String address,

            @Pattern(regexp = "^([01]?\\d|2[0-3]):([0-5]\\d)$", message = "올바르지 않은 형식입니다.")
            String openTime,

            @Pattern(regexp = "^([01]?\\d|2[0-3]):([0-5]\\d)$", message = "올바르지 않은 형식입니다.")
            String closeTime,

            @Pattern(regexp = "^([01]?\\d|2[0-3]):([0-5]\\d)$", message = "올바르지 않은 형식입니다.")
            String saleStart,

            @Pattern(regexp = "^([01]?\\d|2[0-3]):([0-5]\\d)$", message = "올바르지 않은 형식입니다.")
            String saleEnd,

            @Max(value = 300, message = "최대 300글자입니다.")
            String additionalComment,

            @Pattern(regexp = "^(02|\\d{3,4})-\\d{3,4}-\\d{4}$", message = "올바르지 않은 형식입니다.")
            String storeNumber
        )
{
}
