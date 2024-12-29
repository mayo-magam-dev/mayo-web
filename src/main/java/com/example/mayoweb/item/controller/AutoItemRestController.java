package com.example.mayoweb.item.controller;

import com.example.mayoweb.commons.exception.ApplicationException;
import com.example.mayoweb.commons.exception.payload.ErrorStatus;
import com.example.mayoweb.item.domain.response.AutoItemResponse;
import com.example.mayoweb.item.service.AutoItemService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auto")
public class AutoItemRestController {
    
    private final AutoItemService autoItemService;

    @Value("${mayo.secret}")
    private String globalSecret;

    @Operation(summary = "특정 가게의 자동화 메뉴 확인", description = "가게의 key를 통해 해당 가게의 자동화 설정 된 메뉴들을 반환 받습니다.")
    @GetMapping("/items/{secret}/{storeId}")
    public ResponseEntity<List<AutoItemResponse>> getAutoItemsByStoreId(@PathVariable String storeId, @PathVariable String secret) {

        if(!secret.equals(globalSecret)) {
            throw new ApplicationException(
                    ErrorStatus.toErrorStatus("권한이 없는 사용자입니다.", 401, LocalDateTime.now())
            );
        }

        return ResponseEntity.ok(autoItemService.getAutoItemsByStoreId(storeId));
    }

    @Operation(summary = "자동화 메뉴 삭제", description = "자동화 설정된 메뉴의 key를 받아 자동화 설정을 취소합니다.")
    @DeleteMapping("/items/{secret}/{itemId}")
    public ResponseEntity<Void> deleteByItemId(@PathVariable String itemId, @PathVariable String secret) {

        if(!secret.equals(globalSecret)) {
            throw new ApplicationException(
                    ErrorStatus.toErrorStatus("권한이 없는 사용자입니다.", 401, LocalDateTime.now())
            );
        }

        autoItemService.deleteByItemId(itemId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "자동화 메뉴 확인", description = "자동화 메뉴 전체를 확인합니다.")
    @GetMapping("/all/{secret}")
    public ResponseEntity<List<AutoItemResponse>> getAutoItems(@PathVariable String secret) {

        if(!secret.equals(globalSecret)) {
            throw new ApplicationException(
                    ErrorStatus.toErrorStatus("권한이 없는 사용자입니다.", 401, LocalDateTime.now())
            );
        }

        return ResponseEntity.ok(autoItemService.getAutoItems());
    }

    @Operation(summary = "전체 메뉴 자동화 설정", description = "해당 가게의 전체 메뉴를 자동화 설정 합니다.")
    @PostMapping("/add/{secret}/{storeId}")
    public ResponseEntity<Boolean> addAllAutoItems(@PathVariable String storeId, @PathVariable String secret) {

        if(!secret.equals(globalSecret)) {
            throw new ApplicationException(
                    ErrorStatus.toErrorStatus("권한이 없는 사용자입니다.", 401, LocalDateTime.now())
            );
        }

        return ResponseEntity.ok(autoItemService.addAllAutoItemsByStoreId(storeId));
    }
}
