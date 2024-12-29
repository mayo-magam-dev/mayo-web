package com.example.mayoweb.store.controller;

import com.example.mayoweb.commons.annotation.Authenticated;
import com.example.mayoweb.commons.exception.ValidationFailedException;
import com.example.mayoweb.item.domain.request.OpenItemRequest;
import com.example.mayoweb.store.domain.dto.response.ReadStoreResponse;
import com.example.mayoweb.store.domain.dto.request.UpdateStoreRequest;
import com.example.mayoweb.store.domain.dto.response.UpdateStoreResponse;
import com.example.mayoweb.store.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Tag(name = "가게 API", description = "가게 관리 API")
@RestController
@RequiredArgsConstructor
public class StoreRestController {

    private final StoreService storeService;

    @Operation(summary = "ID 값으로 store 객체를 가져옵니다.", description = "store PK 값으로 객체를 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "스토어 조회 성공", content = @Content(schema = @Schema(implementation = ReadStoreResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @Authenticated
    @GetMapping("/stores")
    public ResponseEntity<ReadStoreResponse> getStoreById(HttpServletRequest req) {
        return ResponseEntity.ok(storeService.getStoreById(req.getAttribute("uid").toString()));
    }

    @Operation(summary = "가게 정보를 받아 업데이트 합니다.", description = "가게 정보를 받아 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "업데이트 성공", content = @Content(schema = @Schema(implementation = UpdateStoreResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @Authenticated
    @PutMapping("/stores")
    public ResponseEntity<UpdateStoreResponse> updateStore(HttpServletRequest req, @RequestBody @Valid UpdateStoreRequest request, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        return ResponseEntity.ok(storeService.updateStoreInformation(req.getAttribute("uid").toString(), request));
    }

    @Operation(summary = "가게 상태 오픈으로 변경합니다.", description = "아이템 id 리스트와 수량 리스트를 받아 판매 상태로 변경 한 후 가게 상태를 오픈으로 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "오픈 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @Authenticated
    @PutMapping("/store/open")
    public ResponseEntity<Void> openStore(HttpServletRequest req, @RequestBody(required = false) OpenItemRequest openItemRequest){

        storeService.openStore(req.getAttribute("uid").toString(), openItemRequest);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "가게 상태를 마감으로 변경합니다.", description = "가게 상태를 마감으로 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "업데이트 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @Authenticated
    @PutMapping("/store/close")
    public ResponseEntity<Void> closeStore(HttpServletRequest req) {

        storeService.closeStore(req.getAttribute("uid").toString());

        return ResponseEntity.ok().build();
    }

}
