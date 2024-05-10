package com.example.mayoweb.response;

import com.example.mayoweb.items.ItemsDto;
import com.example.mayoweb.store.StoresDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class EnrollResponse {
    private final List<ItemsDto> itemsList;
    private final String storeid;
    private final StoresDto store;
}
