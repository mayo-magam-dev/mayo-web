package com.example.mayoweb.store.controller;

import com.example.mayoweb.items.service.ItemsService;
import com.example.mayoweb.store.domain.dto.response.ReadStoreResponse;
import com.example.mayoweb.store.domain.dto.request.UpdateStoreRequest;
import com.example.mayoweb.store.domain.dto.response.UpdateStoreResponse;
import com.example.mayoweb.store.service.StoresService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StoreRestController {

    private final StoresService storesService;
    private final ItemsService itemsService;

    @GetMapping("/stores")
    public ResponseEntity<ReadStoreResponse> getStoreById(@RequestParam String storeId) {
        return ResponseEntity.ok(storesService.getStoreById(storeId));
    }

    @PutMapping("/stores")
    public ResponseEntity<UpdateStoreResponse> updateStore(@RequestBody UpdateStoreRequest request) {
        return ResponseEntity.ok(storesService.updateStoreInformation(request));
    }

    @PutMapping("/store/open")
    public ResponseEntity<Void> openStore(@RequestParam(required = false) List<String> itemIdList, @RequestParam(required = false) List<Integer> quantityList, @RequestParam String storeId) {

        if(itemIdList != null && quantityList != null) {
            itemsService.openTask(itemIdList, quantityList);
        }
        storesService.openStore(storeId);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/store/close")
    public ResponseEntity<Void> closeStore(@RequestParam String storeId) {

        itemsService.closeTask(storeId);
        storesService.closeStore(storeId);

        return ResponseEntity.ok().build();
    }

}
