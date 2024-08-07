package com.example.mayoweb.reservation.controller;

import com.example.mayoweb.carts.domain.dto.response.ReadCartsResponse;
import com.example.mayoweb.carts.service.CartService;
import com.example.mayoweb.items.domain.response.ReadFirstItemResponse;
import com.example.mayoweb.items.domain.response.ReadItemResponse;
import com.example.mayoweb.items.service.ItemsService;
import com.example.mayoweb.reservation.domain.dto.response.ReadReservationDetailResponse;
import com.example.mayoweb.reservation.domain.dto.response.ReadReservationListResponse;
import com.example.mayoweb.reservation.domain.dto.response.ReadReservationResponse;
import com.example.mayoweb.reservation.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Tag(name = "예약 API", description = "예약 관리 API")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ReservationRestController {

    private final ReservationService reservationService;
    private final ItemsService itemsService;
    private final CartService cartService;

    @Operation(summary = "ID 값으로 reservation 객체를 가져옵니다.", description = "reservation PK 값으로 객체를 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "예약 조회 성공", content = @Content(schema = @Schema(implementation = ReadReservationResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/reservation")
    public ResponseEntity<ReadReservationResponse> getReservationById(@RequestParam String reservationId) {
        return ResponseEntity.ok(reservationService.getReservationById(reservationId));
    }

    @Operation(summary = "storeId 값으로 해당 가게의 신규 예약들을 가져옵니다.", description = "storeId 값으로 해당 가게의 신규 예약들을 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신규 예약 조회 성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/reservation-new")
    public ResponseEntity<List<ReadReservationListResponse>> getNewReservationsByStoreId(String storeId) {

        List<ReadReservationResponse> reservationResponseList = reservationService.getNewByStoreId(storeId);
        List<ReadFirstItemResponse> firstItemResponse = itemsService.getFirstItemNamesFromReservations(reservationResponseList);
        List<ReadReservationListResponse> responseList = new ArrayList<>();

        for(int i=0; i<reservationResponseList.size(); i++) {
            ReadReservationListResponse response = ReadReservationListResponse.builder()
                    .firstItemName(firstItemResponse.get(i).itemName())
                    .itemQuantity(firstItemResponse.get(i).itemQuantity())
                    .createdAt(reservationResponseList.get(i).createdAt())
                    .pickupTime(reservationResponseList.get(i).pickupTime())
                    .build();

            responseList.add(response);
        }

        log.info("{}", responseList);

        return ResponseEntity.ok(responseList);
    }

    @Operation(summary = "storeId 값으로 해당 가게의 신규 예약들을 가져옵니다.", description = "storeId 값으로 해당 가게의 신규 예약들을 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신규 예약 조회 성공", content = @Content(schema = @Schema(implementation = CompletableFuture.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/reservation-new-async")
    public CompletableFuture<ResponseEntity<List<ReadReservationListResponse>>> getNewReservationsByStoreIdAsync(@RequestParam String storeId) {
        return reservationService.getNewReservationsByStoreId(storeId)
                .thenApply(reservationResponseList -> {
                    List<ReadFirstItemResponse> firstItemResponseList = itemsService.getFirstItemNamesFromReservations(reservationResponseList);
                    List<ReadReservationListResponse> responseList = new ArrayList<>();
                    for (int i = 0; i < reservationResponseList.size(); i++) {
                        ReadReservationListResponse response = ReadReservationListResponse.builder()
                                .firstItemName(firstItemResponseList.get(i).itemName())
                                .itemQuantity(firstItemResponseList.get(i).itemQuantity())
                                .createdAt(reservationResponseList.get(i).createdAt())
                                .pickupTime(reservationResponseList.get(i).pickupTime())
                                .build();
                        responseList.add(response);
                    }
                    return ResponseEntity.ok(responseList);
                });
    }

    @Operation(summary = "storeId 값으로 해당 가게의 진행 예약들을 가져옵니다.", description = "storeId 값으로 해당 가게의 진행 예약들을 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "진행 예약 조회 성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/reservation-proceed")
    public ResponseEntity<List<ReadReservationListResponse>> getProceedingReservationsByStoreId(String storeId) {
        List<ReadReservationResponse> reservationResponseList = reservationService.getProcessingByStoreId(storeId);
        List<ReadFirstItemResponse> firstItemResponse = itemsService.getFirstItemNamesFromReservations(reservationResponseList);
        List<ReadReservationListResponse> responseList = new ArrayList<>();

        for(int i=0; i<reservationResponseList.size(); i++) {
            ReadReservationListResponse response = ReadReservationListResponse.builder()
                    .firstItemName(firstItemResponse.get(i).itemName())
                    .itemQuantity(firstItemResponse.get(i).itemQuantity())
                    .createdAt(reservationResponseList.get(i).createdAt())
                    .pickupTime(reservationResponseList.get(i).pickupTime())
                    .build();

            responseList.add(response);
        }

        return ResponseEntity.ok(responseList);
    }

    @Operation(summary = "storeId 값으로 해당 가게의 완료 예약들을 가져옵니다.", description = "storeId 값으로 해당 가게의 완료 예약들을 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "완료 예약 조회 성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/reservation-done")
    public ResponseEntity<List<ReadReservationListResponse>> getDoneReservationsByStoreId(String storeId) {
        List<ReadReservationResponse> reservationResponseList = reservationService.getEndByStoreId(storeId);
        List<ReadFirstItemResponse> firstItemResponse = itemsService.getFirstItemNamesFromReservations(reservationResponseList);
        List<ReadReservationListResponse> responseList = new ArrayList<>();

        for(int i=0; i<reservationResponseList.size(); i++) {
            ReadReservationListResponse response = ReadReservationListResponse.builder()
                    .firstItemName(firstItemResponse.get(i).itemName())
                    .itemQuantity(firstItemResponse.get(i).itemQuantity())
                    .createdAt(reservationResponseList.get(i).createdAt())
                    .pickupTime(reservationResponseList.get(i).pickupTime())
                    .build();

            responseList.add(response);
        }

        return ResponseEntity.ok(responseList);
    }

    @Operation(summary = "reservationId를 받아 해당 예약의 상태를 수락으로 변경합니다.", description = "reservationId를 받아 해당 예약의 상태를 수락으로 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "예약 수락 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PutMapping("/reservation-accept")
    public ResponseEntity<Void> updateAccept(@RequestParam String reservationId) {

        reservationService.reservationAccept(reservationId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "reservationId를 받아 해당 예약의 상태를 실패로 변경합니다.", description = "reservationId를 받아 해당 예약의 상태를 거절으로 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "예약 거절 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PutMapping("/reservation-fail")
    public ResponseEntity<Void> updateFail(@RequestParam String reservationId) {

        reservationService.reservationFail(reservationId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "reservationId를 받아 해당 예약의 상태를 완료로 변경합니다.", description = "reservationId를 받아 해당 예약의 상태를 완료로 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "예약 완료 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PutMapping("/reservation-done")
    public ResponseEntity<Void> updateDone(@RequestParam String reservationId) {

        reservationService.reservationDone(reservationId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "reservationId를 받아 해당 예약의 자세한 정보를 가져옵니다.", description = "reservationId를 받아 해당 예약의 자세한 정보를 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "예약 상세 조회 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/reservation-detail")
    public ResponseEntity<ReadReservationDetailResponse> getItemByReservationId(@RequestParam String reservationId) {
        ReadReservationResponse reservation = reservationService.getReservationById(reservationId);
        List<ReadCartsResponse> carts = cartService.getCartsByReservation(reservationId);
        List<String> itemName = new ArrayList<>();
        List<Integer> itemCount = new ArrayList<>();
        List<Double> subTotal = new ArrayList<>();

        for(ReadCartsResponse cart : carts) {
            ReadItemResponse item = itemsService.getItemByCartId(cart.cartId());
            itemName.add(item.itemName());
            itemCount.add(cart.itemCount());
            subTotal.add(cart.subtotal());
        }

        return ResponseEntity.ok(ReadReservationDetailResponse.builder()
                .itemName(itemName)
                .itemCount(itemCount)
                .subTotal(subTotal)
                .totalQuantity(carts.size())
                .reservationId(reservation.reservationId())
                .request(reservation.reservationRequest())
                .createdAt(reservation.createdAt())
                .pickupTime(reservation.pickupTime())
                .totalPrice(reservation.totalPrice())
                .reservationIsPlastic(reservation.reservationIsPlastics())
                .build());
    }

}
