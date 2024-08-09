package com.example.mayoweb.store.domain.dto.response;

import lombok.Builder;

@Builder
public record UpdateStoreResponse(
        String storeId
) {
}
