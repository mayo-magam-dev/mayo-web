package com.example.mayoweb.items.domain.request;


import java.util.List;

public record OpenItemRequest(

        List<String> itemIdList,

        List<Integer> quantityList

) {
}
