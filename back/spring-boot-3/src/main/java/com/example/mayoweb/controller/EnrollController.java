package com.example.mayoweb.controller;

import com.example.mayoweb.items.ItemsDto;
import com.example.mayoweb.items.ItemsService;
import com.example.mayoweb.store.StoresDto;
import com.example.mayoweb.store.StoresService;
import com.example.mayoweb.response.EnrollResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/mayo/enroll")
public class EnrollController {

    private final StoresService storesService;

    private final ItemsService itemsService;

    @GetMapping("/mayo/enroll/{storeid}")
    public ResponseEntity<EnrollResponse> enroll(@PathVariable String storeid) throws ExecutionException, InterruptedException {

        StoresDto storesDto = storesService.getStoreById(storeid);
        List<ItemsDto> itemsList = itemsService.getItemsByStoreRef(storeid);

        EnrollResponse response = new EnrollResponse(
                itemsList,
                storeid,
                storesDto
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{storeid}/open")
    public ResponseEntity<String> open(@PathVariable String storeid,@RequestParam("itemid") List<String> itemIdList,
                       @RequestParam("quantityList") List<Integer> quantityList) throws ExecutionException, InterruptedException {

        itemsService.updateItemOnSale(itemIdList, quantityList);
        storesService.openStore(storeid);

        return ResponseEntity.ok("Open Store");
    }


    @GetMapping("/{storeid}/close")
    public ResponseEntity<String> close(@PathVariable String storeid) throws ExecutionException, InterruptedException {
        itemsService.updateItemsStateOutOfStock(storeid);
        storesService.closeStore(storeid);

        return ResponseEntity.ok("Close Store");
    }
}
