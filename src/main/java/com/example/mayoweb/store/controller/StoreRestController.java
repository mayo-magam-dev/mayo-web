package com.example.mayoweb.store.controller;

import com.example.mayoweb.commons.exception.ValidationFailedException;
import com.example.mayoweb.fcm.service.FCMService;
import com.example.mayoweb.items.domain.request.OpenItemRequest;
import com.example.mayoweb.items.service.ItemsService;
import com.example.mayoweb.store.domain.dto.response.ReadStoreResponse;
import com.example.mayoweb.store.domain.dto.request.UpdateStoreRequest;
import com.example.mayoweb.store.domain.dto.response.UpdateStoreResponse;
import com.example.mayoweb.store.service.StoresService;
import com.example.mayoweb.user.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
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

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Tag(name = "가게 API", description = "가게 관리 API")
@RestController
@RequiredArgsConstructor
public class StoreRestController {

    private final StoresService storesService;
    private final ItemsService itemsService;
    private final FCMService fcmService;
    private final UsersService userService;

    @Operation(summary = "ID 값으로 store 객체를 가져옵니다.", description = "store PK 값으로 객체를 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "스토어 조회 성공", content = @Content(schema = @Schema(implementation = ReadStoreResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/stores")
    public ResponseEntity<ReadStoreResponse> getStoreById(@RequestParam String storeId) {
        return ResponseEntity.ok(storesService.getStoreById(storeId));
    }

    @Operation(summary = "가게 정보를 받아 업데이트 합니다.", description = "가게 정보를 받아 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "업데이트 성공", content = @Content(schema = @Schema(implementation = ReadStoreResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PutMapping("/stores")
    public ResponseEntity<UpdateStoreResponse> updateStore(@RequestBody @Valid UpdateStoreRequest request, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        return ResponseEntity.ok(storesService.updateStoreInformation(request));
    }

    @Operation(summary = "가게 상태 오픈으로 변경합니다.", description = "아이템 id 리스트와 수량 리스트를 받아 판매 상태로 변경 한 후 가게 상태를 오픈으로 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "오픈 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PutMapping("/store/open")
    public ResponseEntity<Void> openStore(@RequestBody(required = false)OpenItemRequest openItemRequest, @RequestParam String storeId) throws ExecutionException, InterruptedException, IOException {

        if(openItemRequest.itemIdList() != null && openItemRequest.quantityList() != null) {
            itemsService.openTask(openItemRequest.itemIdList(), openItemRequest.quantityList());
        }

        storesService.openStore(storeId);
        List<String> tokens = userService.getTokensByStoresRef(storeId);
        fcmService.sendOpenMessage(tokens, storeId);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "가게 상태를 마감으로 변경합니다.", description = "가게 상태를 마감으로 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "업데이트 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PutMapping("/store/close")
    public ResponseEntity<Void> closeStore(@RequestParam String storeId) {

        itemsService.closeTask(storeId);
        storesService.closeStore(storeId);

        return ResponseEntity.ok().build();
    }

}
