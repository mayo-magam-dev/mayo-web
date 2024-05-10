package com.example.mayoweb.response;

import com.example.mayoweb.items.ItemsDto;
import com.example.mayoweb.store.StoresDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class ItemInfoResponse {
    private final ItemsDto item;
    private final String storeid;
    private final StoresDto store;
}
