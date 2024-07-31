package com.example.mayoweb.items.controller;

import com.example.mayoweb.items.domain.request.UpdateItemRequest;
import com.example.mayoweb.items.domain.response.ReadFirstItemResponse;
import com.example.mayoweb.items.service.ItemsService;
import com.example.mayoweb.items.domain.request.CreateItemRequest;
import com.example.mayoweb.items.domain.response.ReadItemResponse;
import com.example.mayoweb.reservation.domain.dto.response.ReadReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemRestController {

    private final ItemsService itemsService;

    @GetMapping("/item")
    public ResponseEntity<ReadItemResponse> getItemById(@RequestParam String itemId) {
        return ResponseEntity.ok(itemsService.getItemById(itemId));
    }

    @GetMapping("/item-store")
    public ResponseEntity<List<ReadItemResponse>> getItemsByStoreId(@RequestParam String storeId) {
        return ResponseEntity.ok(itemsService.getItemsByStoreId(storeId));
    }

    @PostMapping("/item")
    public ResponseEntity<Void> createItem(@RequestBody CreateItemRequest request, @RequestParam String storeId) {

        itemsService.save(request, storeId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/item")
    public ResponseEntity<Void> updateItem(@RequestBody UpdateItemRequest request) {

        itemsService.updateItem(request);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/item")
    public ResponseEntity<Void> deleteItem(@RequestParam String itemId) {

        itemsService.deleteItem(itemId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/first-item")
    public ResponseEntity<List<ReadFirstItemResponse>> getFirstItemByReservations(@RequestBody List<ReadReservationResponse> reservations) {

        return ResponseEntity.ok(itemsService.getFirstItemNamesFromReservations(reservations));
    }

}
