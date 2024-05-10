package com.example.mayoweb.response;

import com.example.mayoweb.store.StoresDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class BasicResponse {
    private final String storeid;
    private final StoresDto store;

}
