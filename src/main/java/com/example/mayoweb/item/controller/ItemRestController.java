package com.example.mayoweb.item.controller;

import com.example.mayoweb.commons.annotation.Authenticated;
import com.example.mayoweb.commons.exception.ValidationFailedException;
import com.example.mayoweb.item.domain.request.UpdateItemRequest;
import com.example.mayoweb.item.domain.response.ReadFirstItemResponse;
import com.example.mayoweb.item.service.ItemService;
import com.example.mayoweb.item.domain.request.CreateItemRequest;
import com.example.mayoweb.item.domain.response.ReadItemResponse;
import com.example.mayoweb.reservation.domain.dto.response.ReadReservationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "아이템 API", description = "아이템 관리 API")
@RestController
@RequiredArgsConstructor
public class ItemRestController {

    private final ItemService itemService;

    @Operation(summary = "itemId 값으로 해당 item객체를 가져옵니다.", description = "item PK값으로 해당 item 객체를 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이템 조회 성공", content = @Content(schema = @Schema(implementation = ReadItemResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/item")
    public ResponseEntity<ReadItemResponse> getItemById(@RequestParam String itemId) {
        return ResponseEntity.ok(itemService.getItemById(itemId));
    }

    @Operation(summary = "해당 가게의 item객체들을 리스트로 가져옵니다.", description = "해당 가게의 item객체들을 리스트로 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "스토어로 아이템 조회 성공", content = @Content(schema = @Schema(implementation = ReadItemResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @Authenticated
    @GetMapping("/item-store")
    public ResponseEntity<List<ReadItemResponse>> getItemsByUserId(HttpServletRequest req) {
        return ResponseEntity.ok(itemService.getItemsByUserId(req.getAttribute("uid").toString()));
    }

    @Operation(summary = "어플리케이션용 해당 가게의 item객체들을 리스트로 가져옵니다.", description = "어플리케이션용 해당 가게의 item객체들을 리스트로 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "스토어로 아이템 조회 성공", content = @Content(schema = @Schema(implementation = ReadItemResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @Authenticated
    @GetMapping("/item-store-app")
    public ResponseEntity<List<ReadItemResponse>> getItemsByUserIdApp(HttpServletRequest req) {
        return ResponseEntity.ok(itemService.getItemsByUserIdApp(req.getAttribute("uid").toString()));
    }

    @Operation(summary = "storeId값과 아이템 생성 정보로 아이템을 만듭니다.", description = "storeId값과 아이템 생성 정보로 아이템을 만듭니다.")
    @Parameter(name = "request", description = "아이템 이름, 설명, 가격, 할인 가격을 포함합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이템 생성 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @Authenticated
    @PostMapping("/item")
    public ResponseEntity<Void> createItem(HttpServletRequest req, @RequestPart @Valid CreateItemRequest request, BindingResult bindingResult, @RequestParam(value = "itemImage", required = false) MultipartFile file) {

        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        if(file != null && !file.isEmpty()) {
            itemService.save(request, req.getAttribute("uid").toString(), file);
        } else {
            itemService.save(request, req.getAttribute("uid").toString());
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
    @Authenticated
    @PutMapping("/item")
    public ResponseEntity<Void> updateItem(@RequestPart @Valid UpdateItemRequest request, BindingResult bindingResult, @RequestParam(value = "itemImage", required = false) MultipartFile file) {

        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        if(file != null && !file.isEmpty()) {
            itemService.updateItem(request, file);
        } else {
            itemService.updateItem(request);
        }

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "itemID로 아이템을 삭제합니다.", description = "아이템 PK로 아이템을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이템 수정 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @Authenticated
    @DeleteMapping("/item")
    public ResponseEntity<Void> deleteItem(@RequestParam String itemId) {

        itemService.deleteItem(itemId);

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

        return ResponseEntity.ok(itemService.getFirstItemNamesFromReservations(reservations));
    }

    @Authenticated
    @PostMapping("/item-quantity-plus")
    public ResponseEntity<Void> itemQuantityPlus(@RequestParam String itemId){
        itemService.updateItemQuantityPlus(itemId);

        return ResponseEntity.noContent().build();
    }

    @Authenticated
    @PostMapping("/item-quantity-minus")
    public ResponseEntity<Void> itemQuantityMinus(@RequestParam String itemId) {
        itemService.updateItemQuantityMinus(itemId);

        return ResponseEntity.noContent().build();
    }

    @Authenticated
    @PostMapping("/item-on")
    public ResponseEntity<Void> itemOn(@RequestParam String itemId) {
        itemService.updateItemOnActive(itemId);
        return ResponseEntity.noContent().build();
    }

    @Authenticated
    @PostMapping("/item-off")
    public ResponseEntity<Void> itemOff(@RequestParam String itemId) {
        itemService.updateItemOffActive(itemId);
        return ResponseEntity.noContent().build();
    }

}
