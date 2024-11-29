package com.example.mayoweb.items.controller;

import com.example.mayoweb.commons.exception.ValidationFailedException;
import com.example.mayoweb.items.domain.request.UpdateItemRequest;
import com.example.mayoweb.items.domain.response.ReadFirstItemResponse;
import com.example.mayoweb.items.service.ItemsService;
import com.example.mayoweb.items.domain.request.CreateItemRequest;
import com.example.mayoweb.items.domain.response.ReadItemResponse;
import com.example.mayoweb.reservation.domain.dto.response.ReadReservationResponse;
import com.example.mayoweb.storage.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "아이템 API", description = "아이템 관리 API")
@RestController
@RequiredArgsConstructor
public class ItemRestController {

    private final ItemsService itemsService;
    private final StorageService storageService;

    @Operation(summary = "itemId 값으로 해당 item객체를 가져옵니다.", description = "item PK값으로 해당 item 객체를 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이템 조회 성공", content = @Content(schema = @Schema(implementation = ReadItemResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/item")
    public ResponseEntity<ReadItemResponse> getItemById(@RequestParam String itemId) {
        return ResponseEntity.ok(itemsService.getItemById(itemId));
    }

    @Operation(summary = "storeId 값으로 해당 해당 가게의 item객체들을 리스트로 가져옵니다.", description = "storeId 값으로 해당 해당 가게의 item객체들을 리스트로 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "스토어로 아이템 조회 성공", content = @Content(schema = @Schema(implementation = ReadItemResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/item-store")
    public ResponseEntity<List<ReadItemResponse>> getItemsByStoreId(@RequestParam String storeId) {
        return ResponseEntity.ok(itemsService.getItemsByStoreId(storeId));
    }

    @Operation(summary = "storeId값과 아이템 생성 정보로 아이템을 만듭니다.", description = "storeId값과 아이템 생성 정보로 아이템을 만듭니다.")
    @Parameter(name = "request", description = "아이템 이름, 설명, 가격, 할인 가격을 포함합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이템 생성 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PostMapping("/item")
    public ResponseEntity<Void> createItem(@RequestPart @Valid CreateItemRequest request, BindingResult bindingResult, @RequestParam String storeId, @RequestParam(value = "itemImage", required = false) MultipartFile file) {

        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        if(file != null && !file.isEmpty()) {
            String imageUrl = storageService.uploadFirebaseBucket(file, request.itemName());
            CreateItemRequest createItemRequest = CreateItemRequest.updateItemURL(request, imageUrl);
            itemsService.save(createItemRequest, storeId);
        } else {
            itemsService.save(request, storeId);
        }

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "아이템 수정 정보로 아이템을 수정합니다.", description = "아이템 수정 정보로 아이템을 수정합니다.")
    @Parameter(name = "request", description = "아이템 아이디, 아이템 이름, 설명, 가격, 할인 가격을 포함합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이템 수정 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PutMapping("/item")
    public ResponseEntity<Void> updateItem(@RequestPart @Valid UpdateItemRequest request, BindingResult bindingResult, @RequestParam(value = "itemImage", required = false) MultipartFile file) {

        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        if(file != null && !file.isEmpty()) {
            String imageUrl = storageService.uploadFirebaseBucket(file, request.itemName());
            UpdateItemRequest updateItemRequest = UpdateItemRequest.updateItemURL(request, imageUrl);
            itemsService.updateItem(updateItemRequest);
        } else {
            itemsService.updateItem(request);
        }

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "itemID로 아이템을 삭제합니다.", description = "아이템 PK로 아이템을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이템 수정 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @DeleteMapping("/item")
    public ResponseEntity<Void> deleteItem(@RequestParam String itemId) {

        itemsService.deleteItem(itemId);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "예약들로 각 예약의 첫번째 아이템이름을 가져옵니다.", description = "예약들로 각 예약의 첫번째 아이템이름을 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이템 수정 성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/first-item")
    public ResponseEntity<List<ReadFirstItemResponse>> getFirstItemByReservations(@RequestBody List<ReadReservationResponse> reservations) {

        return ResponseEntity.ok(itemsService.getFirstItemNamesFromReservations(reservations));
    }

    @PostMapping("/item-quantity-plus")
    public ResponseEntity<Void> itemQuantityPlus(@RequestParam String itemId){
        itemsService.updateItemQuantityPlus(itemId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/item-quantity-minus")
    public ResponseEntity<Void> itemQuantityMinus(@RequestParam String itemId) {
        itemsService.updateItemQuantityMinus(itemId);

        return ResponseEntity.noContent().build();
    }

}
