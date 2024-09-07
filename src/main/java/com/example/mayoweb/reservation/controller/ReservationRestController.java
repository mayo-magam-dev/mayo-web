package com.example.mayoweb.reservation.controller;

import com.example.mayoweb.carts.domain.dto.response.ReadCartsResponse;
import com.example.mayoweb.carts.service.CartService;
import com.example.mayoweb.fcm.service.FCMService;
import com.example.mayoweb.items.domain.response.ReadFirstItemResponse;
import com.example.mayoweb.items.domain.response.ReadItemResponse;
import com.example.mayoweb.items.service.ItemsService;
import com.example.mayoweb.reservation.domain.dto.response.ReadReservationDetailResponse;
import com.example.mayoweb.reservation.domain.dto.response.ReadReservationListResponse;
import com.example.mayoweb.reservation.domain.dto.response.ReadReservationResponse;
import com.example.mayoweb.reservation.service.ReservationService;
import com.example.mayoweb.sse.SseService;
import com.example.mayoweb.user.domain.dto.response.ReadUserResponse;
import com.example.mayoweb.user.service.UsersService;
import com.google.cloud.Timestamp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Tag(name = "예약 API", description = "예약 관리 API")
@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ReservationRestController {

    private final ReservationService reservationService;
    private final SseService sseService;
    private final ItemsService itemsService;
    private final CartService cartService;
    private final UsersService usersService;
    private final UsersService userService;
    private final FCMService fcmService;

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
                    .reservationId(reservationResponseList.get(i).reservationId())
                    .firstItemName(firstItemResponse.get(i).itemName())
                    .itemQuantity(firstItemResponse.get(i).itemQuantity())
                    .createdAt(reservationResponseList.get(i).createdAt())
                    .pickupTime(reservationResponseList.get(i).pickupTime())
                    .reservationState(reservationResponseList.get(i).reservationState())
                    .build();

            responseList.add(response);
        }

        return ResponseEntity.ok(responseList);
    }

    @Operation(summary = "storeId 값으로 해당 가게의 신규 예약들을 비동기로 가져옵니다.", description = "storeId 값으로 해당 가게의 신규 예약들을 비동기로 가져옵니다.")
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
                                .reservationId(reservationResponseList.get(i).id())
                                .firstItemName(firstItemResponseList.get(i).itemName())
                                .itemQuantity(firstItemResponseList.get(i).itemQuantity())
                                .createdAt(reservationResponseList.get(i).createdAt())
                                .pickupTime(reservationResponseList.get(i).pickupTime())
                                .reservationState(reservationResponseList.get(i).reservationState())
                                .build();
                        responseList.add(response);
                    }

                    log.info("{}", responseList);

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
    public ResponseEntity<List<ReadReservationListResponse>> getProceedingReservationsByStoreId(@RequestParam String storeId) {
        List<ReadReservationResponse> reservationResponseList = reservationService.getProcessingByStoreId(storeId);
        List<ReadFirstItemResponse> firstItemResponse = itemsService.getFirstItemNamesFromReservations(reservationResponseList);
        List<ReadReservationListResponse> responseList = new ArrayList<>();

        for(int i=0; i<reservationResponseList.size(); i++) {
            ReadReservationListResponse response = ReadReservationListResponse.builder()
                    .reservationId(reservationResponseList.get(i).id())
                    .firstItemName(firstItemResponse.get(i).itemName())
                    .itemQuantity(firstItemResponse.get(i).itemQuantity())
                    .createdAt(reservationResponseList.get(i).createdAt())
                    .pickupTime(reservationResponseList.get(i).pickupTime())
                    .reservationState(reservationResponseList.get(i).reservationState())
                    .build();

            responseList.add(response);
        }

        return ResponseEntity.ok(responseList);
    }

    @Operation(summary = "storeId 값으로 해당 가게의 진행 예약들을 비동기로 가져옵니다.", description = "storeId 값으로 해당 가게의 진행 예약들을 비동기로 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신규 예약 조회 성공", content = @Content(schema = @Schema(implementation = CompletableFuture.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/reservation-proceed-async")
    public CompletableFuture<ResponseEntity<List<ReadReservationListResponse>>> getProceedingReservationsByStoreIdAsync(@RequestParam String storeId) {
        return reservationService.getProceedingReservationsByStoreId(storeId)
                .thenApply(reservationResponseList -> {
                    List<ReadFirstItemResponse> firstItemResponseList = itemsService.getFirstItemNamesFromReservations(reservationResponseList);
                    List<ReadReservationListResponse> responseList = new ArrayList<>();
                    for (int i = 0; i < reservationResponseList.size(); i++) {
                        ReadReservationListResponse response = ReadReservationListResponse.builder()
                                .reservationId(reservationResponseList.get(i).id())
                                .firstItemName(firstItemResponseList.get(i).itemName())
                                .itemQuantity(firstItemResponseList.get(i).itemQuantity())
                                .createdAt(reservationResponseList.get(i).createdAt())
                                .pickupTime(reservationResponseList.get(i).pickupTime())
                                .reservationState(reservationResponseList.get(i).reservationState())
                                .build();
                        responseList.add(response);
                    }
                    return ResponseEntity.ok(responseList);
                });
    }

    @Operation(summary = "storeId 값으로 해당 가게의 완료 예약들을 가져옵니다.", description = "storeId 값으로 해당 가게의 완료 예약들을 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "완료 예약 조회 성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/reservation-done")
    public ResponseEntity<List<ReadReservationListResponse>> getDoneReservationsByStoreId(@RequestParam String storeId) {
        List<ReadReservationResponse> reservationResponseList = reservationService.getEndByStoreId(storeId);
        List<ReadFirstItemResponse> firstItemResponse = itemsService.getFirstItemNamesFromReservations(reservationResponseList);
        List<ReadReservationListResponse> responseList = new ArrayList<>();

        for(int i=0; i<reservationResponseList.size(); i++) {
            ReadReservationListResponse response = ReadReservationListResponse.builder()
                    .reservationId(reservationResponseList.get(i).id())
                    .firstItemName(firstItemResponse.get(i).itemName())
                    .itemQuantity(firstItemResponse.get(i).itemQuantity())
                    .createdAt(reservationResponseList.get(i).createdAt())
                    .pickupTime(reservationResponseList.get(i).pickupTime())
                    .reservationState(reservationResponseList.get(i).reservationState())
                    .build();

            responseList.add(response);
        }

        return ResponseEntity.ok(responseList);
    }

    @Operation(summary = "storeId 값, 시간 값으로 해당 가게의 완료 예약들을 가져옵니다.", description = "storeId 값, 시간 값으로 해당 가게의 완료 예약들을 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "완료 예약 조회 성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/reservation-done-time")
    public ResponseEntity<List<ReadReservationListResponse>> getDoneReservationsByStoreId(@RequestParam String storeId, @RequestParam String timestamp) {

        Timestamp ts = Timestamp.parseTimestamp(timestamp);

        List<ReadReservationResponse> reservationResponseList = reservationService.getEndByStoreIdAndTimestamp(storeId, ts);
        List<ReadFirstItemResponse> firstItemResponse = itemsService.getFirstItemNamesFromReservations(reservationResponseList);
        List<ReadReservationListResponse> responseList = new ArrayList<>();



        for(int i=0; i<reservationResponseList.size(); i++) {
            ReadReservationListResponse response = ReadReservationListResponse.builder()
                    .reservationId(reservationResponseList.get(i).id())
                    .firstItemName(firstItemResponse.get(i).itemName())
                    .itemQuantity(firstItemResponse.get(i).itemQuantity())
                    .createdAt(reservationResponseList.get(i).createdAt())
                    .pickupTime(reservationResponseList.get(i).pickupTime())
                    .reservationState(reservationResponseList.get(i).reservationState())
                    .build();

            responseList.add(response);
        }

        return ResponseEntity.ok(responseList);
    }

    @Operation(summary = "storeId 값으로 해당 가게의 완료 예약들을 비동기로 가져옵니다.", description = "storeId 값으로 해당 가게의 완료 예약들을 비동기로 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신규 예약 조회 성공", content = @Content(schema = @Schema(implementation = CompletableFuture.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/reservation-done-async")
    public CompletableFuture<ResponseEntity<List<ReadReservationListResponse>>> getEndReservationsByStoreIdAsync(@RequestParam String storeId) {
        return reservationService.getEndReservationsByStoreId(storeId)
                .thenApply(reservationResponseList -> {
                    List<ReadFirstItemResponse> firstItemResponseList = itemsService.getFirstItemNamesFromReservations(reservationResponseList);
                    List<ReadReservationListResponse> responseList = new ArrayList<>();
                    for (int i = 0; i < reservationResponseList.size(); i++) {
                        ReadReservationListResponse response = ReadReservationListResponse.builder()
                                .reservationId(reservationResponseList.get(i).id())
                                .firstItemName(firstItemResponseList.get(i).itemName())
                                .itemQuantity(firstItemResponseList.get(i).itemQuantity())
                                .createdAt(reservationResponseList.get(i).createdAt())
                                .pickupTime(reservationResponseList.get(i).pickupTime())
                                .reservationState(reservationResponseList.get(i).reservationState())
                                .build();
                        responseList.add(response);
                    }
                    return ResponseEntity.ok(responseList);
                });
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

        ReadReservationResponse dto = reservationService.getReservationById(reservationId);
        try {
            List<String> tokens = userService.getTokensByUserRef(dto.userRef());
            fcmService.sendAcceptMessage(tokens);

        } catch (ExecutionException | InterruptedException e) {
            log.info("user 토큰을 찾지 못했습니다.");
        } catch (IOException e) {
            log.info("수락 fcm 메세지 전송에 실패하였습니다.");
        }

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

        ReadReservationResponse dto = reservationService.getReservationById(reservationId);
        try {
            List<String> tokens = userService.getTokensByUserRef(dto.userRef());
            fcmService.sendRejectMessage(tokens);

        } catch (ExecutionException | InterruptedException e) {
            log.info("user 토큰을 찾지 못했습니다.");
        } catch (IOException e) {
            log.info("거절 fcm 메세지 전송에 실패하였습니다.");
        }

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
        ReadUserResponse user = usersService.getUserById(reservation.userRef());
        List<String> itemName = new ArrayList<>();
        List<Integer> itemCount = new ArrayList<>();
        List<Double> subTotal = new ArrayList<>();
        Integer totalQuantity = 0;

        for(ReadCartsResponse cart : carts) {
            ReadItemResponse item = itemsService.getItemByCartId(cart.cartId());
            itemName.add(item.itemName());
            itemCount.add(cart.itemCount());
            subTotal.add(cart.subtotal());
            totalQuantity += cart.itemCount();
        }

        return ResponseEntity.ok(ReadReservationDetailResponse.builder()
                    .itemName(itemName)
                    .itemCount(itemCount)
                    .subTotal(subTotal)
                    .totalQuantity(totalQuantity)
                    .reservationId(reservationId)
                    .request(reservation.reservationRequest())
                    .createdAt(reservation.createdAt())
                    .pickupTime(reservation.pickupTime())
                    .totalPrice(reservation.totalPrice())
                    .reservationIsPlastic(reservation.reservationIsPlastics())
                    .userNickName(user.displayName())
                    .reservationState(reservation.reservationState())
                    .menuTypeCount(carts.size())
                    .build());
    }

    @Operation(summary = "storeId 값으로 해당 가게의 신규 예약들을 SSE를 통해 가져옵니다.", description = "storeId 값으로 해당 가게의 신규 예약들을 SSE를 통해 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신규 예약 조회 성공", content = @Content(schema = @Schema(implementation = SseEmitter.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/sse/reservations-new")
    public SseEmitter streamNewReservations(@RequestParam String storeId) throws ExecutionException, InterruptedException {

        SseEmitter emitter = sseService.addEmitter();

        CompletableFuture<List<ReadReservationListResponse>> future = reservationService.getNewReservationsByStoreIdSse(storeId)
                .thenApply(reservationResponseList -> {
                    List<ReadFirstItemResponse> firstItemResponseList = itemsService.getFirstItemNamesFromReservations(reservationResponseList);
                    List<ReadReservationListResponse> responseList = new ArrayList<>();
                    for (int i = 0; i < reservationResponseList.size(); i++) {
                        ReadReservationListResponse response = ReadReservationListResponse.builder()
                                .reservationId(reservationResponseList.get(i).id())
                                .firstItemName(firstItemResponseList.get(i).itemName())
                                .itemQuantity(firstItemResponseList.get(i).itemQuantity())
                                .createdAt(reservationResponseList.get(i).createdAt())
                                .pickupTime(reservationResponseList.get(i).pickupTime())
                                .reservationState(reservationResponseList.get(i).reservationState())
                                .build();
                        responseList.add(response);
                    }

                    return responseList;
                });

        return emitter;
    }


    @Operation(summary = "storeId 값으로 해당 가게의 완료 예약들을 slice 형태로 가져옵니다.", description = "storeId 값으로 해당 가게의 완료 예약들을 slice 형태로 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "완료 예약 조회 성공", content = @Content(schema = @Schema(implementation = Slice.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/reservations-end-slice-time")
    public Slice<ReadReservationListResponse> endReservationsCursor(@RequestParam String storeId, @RequestParam int page, @RequestParam int size, @RequestParam String timeStamp) throws ExecutionException, InterruptedException {
        Timestamp ts = Timestamp.parseTimestamp(timeStamp);

        Slice<ReadReservationResponse> reservationResponseSlice = reservationService.getReservationsByStoreIdAndTimeSlice(storeId, ts, page, size);

        List<ReadReservationResponse> reservationResponseList = reservationResponseSlice.getContent();

        List<ReadFirstItemResponse> firstItemResponseList = itemsService.getFirstItemNamesFromReservations(reservationResponseList);
        List<ReadReservationListResponse> responseList = new ArrayList<>();

        for (int i = 0; i < reservationResponseList.size(); i++) {
            ReadReservationListResponse response = ReadReservationListResponse.builder()
                    .reservationId(reservationResponseList.get(i).id())
                    .firstItemName(firstItemResponseList.get(i).itemName())
                    .itemQuantity(firstItemResponseList.get(i).itemQuantity())
                    .createdAt(reservationResponseList.get(i).createdAt())
                    .pickupTime(reservationResponseList.get(i).pickupTime())
                    .reservationState(reservationResponseList.get(i).reservationState())
                    .build();
            responseList.add(response);
        }

        Pageable pageable = PageRequest.of(page, size);

        return new SliceImpl<>(responseList, pageable, reservationResponseSlice.hasNext());
    }

}
