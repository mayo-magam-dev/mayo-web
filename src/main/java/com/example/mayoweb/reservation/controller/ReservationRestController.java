package com.example.mayoweb.reservation.controller;

import com.example.mayoweb.reservation.domain.dto.response.ReadReservationDetailResponse;
import com.example.mayoweb.reservation.domain.dto.response.ReadReservationListResponse;
import com.example.mayoweb.reservation.domain.dto.response.ReadReservationResponse;
import com.example.mayoweb.reservation.service.ReservationService;
import com.google.cloud.Timestamp;
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

import java.util.List;
import java.util.concurrent.ExecutionException;

@Tag(name = "예약 API", description = "예약 관리 API")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ReservationRestController {

    private final ReservationService reservationService;

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
    public ResponseEntity<List<ReadReservationListResponse>> getNewReservationsByStoreId(@RequestParam String storeId) {
        return ResponseEntity.ok(reservationService.getNewByStoreId(storeId));
    }

    @Operation(summary = "storeId 값으로 해당 가게의 진행 예약들을 가져옵니다.", description = "storeId 값으로 해당 가게의 진행 예약들을 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "진행 예약 조회 성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/reservation-proceed")
    public ResponseEntity<List<ReadReservationListResponse>> getProceedingReservationsByStoreId(@RequestParam String storeId){
        return ResponseEntity.ok(reservationService.getProcessingByStoreId(storeId));
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

        return ResponseEntity.ok(reservationService.getEndByStoreIdAndTimestamp(storeId, ts));
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
        return ResponseEntity.ok(reservationService.getReservationDetailById(reservationId));
    }


    @PutMapping("/reservation/all-fail")
    public ResponseEntity<Void> reservationFailByStoreId(@RequestParam String storeId) {

        reservationService.reservationFailByStoreId(storeId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "유저 Id와 가게 Id값으로 해당 유저의 알림을 받을 수 있는 리스너를 생성합니다.", description = "유저 Id와 가게 Id값으로 해당 유저의 알림을 받을 수 있는 리스너를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "알림 리스너 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PostMapping("/reservation-new/fcm")
    public ResponseEntity<Void> reservationNewFCM(@RequestParam String storeId, @RequestParam String userId) {
        reservationService.sendFCMNewReservation(storeId, userId);
        return ResponseEntity.noContent().build();
    }

//    @Operation(summary = "storeId 값으로 해당 가게의 신규 예약들을 비동기로 가져옵니다.", description = "storeId 값으로 해당 가게의 신규 예약들을 비동기로 가져옵니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "신규 예약 조회 성공", content = @Content(schema = @Schema(implementation = CompletableFuture.class))),
//            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
//            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
//    })
//    @GetMapping("/reservation-new-async")
//    public CompletableFuture<ResponseEntity<List<ReadReservationListResponse>>> getNewReservationsByStoreIdAsync(@RequestParam String storeId) {
//        return reservationService.getNewReservationsByStoreId(storeId)
//                .thenApply(reservationResponseList -> {
//                    List<ReadFirstItemResponse> firstItemResponseList = itemsService.getFirstItemNamesFromReservations(reservationResponseList);
//                    List<ReadReservationListResponse> responseList = new ArrayList<>();
//                    for (int i = 0; i < reservationResponseList.size(); i++) {
//                        ReadReservationListResponse response = ReadReservationListResponse.builder()
//                                .reservationId(reservationResponseList.get(i).id())
//                                .firstItemName(firstItemResponseList.get(i).itemName())
//                                .itemQuantity(firstItemResponseList.get(i).itemQuantity())
//                                .createdAt(reservationResponseList.get(i).createdAt())
//                                .pickupTime(reservationResponseList.get(i).pickupTime())
//                                .reservationState(reservationResponseList.get(i).reservationState())
//                                .build();
//                        responseList.add(response);
//                    }
//
//                    return ResponseEntity.ok(responseList);
//                });
//    }

//    @Operation(summary = "storeId 값으로 해당 가게의 진행 예약들을 비동기로 가져옵니다.", description = "storeId 값으로 해당 가게의 진행 예약들을 비동기로 가져옵니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "신규 예약 조회 성공", content = @Content(schema = @Schema(implementation = CompletableFuture.class))),
//            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
//            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
//    })
//    @GetMapping("/reservation-proceed-async")
//    public CompletableFuture<ResponseEntity<List<ReadReservationListResponse>>> getProceedingReservationsByStoreIdAsync(@RequestParam String storeId) {
//        return reservationService.getProceedingReservationsByStoreId(storeId)
//                .thenApply(reservationResponseList -> {
//                    List<ReadFirstItemResponse> firstItemResponseList = itemsService.getFirstItemNamesFromReservations(reservationResponseList);
//                    List<ReadReservationListResponse> responseList = new ArrayList<>();
//                    for (int i = 0; i < reservationResponseList.size(); i++) {
//                        ReadReservationListResponse response = ReadReservationListResponse.builder()
//                                .reservationId(reservationResponseList.get(i).id())
//                                .firstItemName(firstItemResponseList.get(i).itemName())
//                                .itemQuantity(firstItemResponseList.get(i).itemQuantity())
//                                .createdAt(reservationResponseList.get(i).createdAt())
//                                .pickupTime(reservationResponseList.get(i).pickupTime())
//                                .reservationState(reservationResponseList.get(i).reservationState())
//                                .build();
//                        responseList.add(response);
//                    }
//                    return ResponseEntity.ok(responseList);
//                });
//    }

//    @Operation(summary = "storeId 값으로 해당 가게의 완료 예약들을 가져옵니다.", description = "storeId 값으로 해당 가게의 완료 예약들을 가져옵니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "완료 예약 조회 성공", content = @Content(schema = @Schema(implementation = List.class))),
//            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
//            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
//    })
//    @GetMapping("/reservation-done")
//    public ResponseEntity<List<ReadReservationListResponse>> getDoneReservationsByStoreId(@RequestParam String storeId) {
//        List<ReadReservationResponse> reservationResponseList = reservationService.getEndByStoreId(storeId);
//        List<ReadFirstItemResponse> firstItemResponse = itemsService.getFirstItemNamesFromReservations(reservationResponseList);
//        List<ReadReservationListResponse> responseList = new ArrayList<>();
//
//        for(int i=0; i<reservationResponseList.size(); i++) {
//            ReadReservationListResponse response = ReadReservationListResponse.builder()
//                    .reservationId(reservationResponseList.get(i).id())
//                    .firstItemName(firstItemResponse.get(i).itemName())
//                    .itemQuantity(firstItemResponse.get(i).itemQuantity())
//                    .createdAt(reservationResponseList.get(i).createdAt())
//                    .pickupTime(reservationResponseList.get(i).pickupTime())
//                    .reservationState(reservationResponseList.get(i).reservationState())
//                    .build();
//
//            responseList.add(response);
//        }
//
//        return ResponseEntity.ok(responseList);
//    }

//    @Operation(summary = "storeId 값으로 해당 가게의 완료 예약들을 비동기로 가져옵니다.", description = "storeId 값으로 해당 가게의 완료 예약들을 비동기로 가져옵니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "신규 예약 조회 성공", content = @Content(schema = @Schema(implementation = CompletableFuture.class))),
//            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
//            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
//    })
//    @GetMapping("/reservation-done-async")
//    public CompletableFuture<ResponseEntity<List<ReadReservationListResponse>>> getEndReservationsByStoreIdAsync(@RequestParam String storeId) {
//        return reservationService.getEndReservationsByStoreId(storeId)
//                .thenApply(reservationResponseList -> {
//                    List<ReadFirstItemResponse> firstItemResponseList = itemsService.getFirstItemNamesFromReservations(reservationResponseList);
//                    List<ReadReservationListResponse> responseList = new ArrayList<>();
//                    for (int i = 0; i < reservationResponseList.size(); i++) {
//                        ReadReservationListResponse response = ReadReservationListResponse.builder()
//                                .reservationId(reservationResponseList.get(i).id())
//                                .firstItemName(firstItemResponseList.get(i).itemName())
//                                .itemQuantity(firstItemResponseList.get(i).itemQuantity())
//                                .createdAt(reservationResponseList.get(i).createdAt())
//                                .pickupTime(reservationResponseList.get(i).pickupTime())
//                                .reservationState(reservationResponseList.get(i).reservationState())
//                                .build();
//                        responseList.add(response);
//                    }
//                    return ResponseEntity.ok(responseList);
//                });
//    }


    //    @Operation(summary = "storeId 값으로 해당 가게의 신규 예약들을 SSE를 통해 가져옵니다.", description = "storeId 값으로 해당 가게의 신규 예약들을 SSE를 통해 가져옵니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "신규 예약 조회 성공", content = @Content(schema = @Schema(implementation = SseEmitter.class))),
//            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
//            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
//    })
//    @GetMapping("/sse/reservations-new")
//    public SseEmitter streamNewReservations(@RequestParam String storeId, @RequestParam String userId) throws ExecutionException, InterruptedException {
//
//        SseEmitter emitter = sseService.addEmitter(userId);
//
//        CompletableFuture<List<ReadReservationListResponse>> future = reservationService.getNewReservationsByStoreIdSse(userId, storeId)
//                .thenApply(reservationResponseList -> {
//                    List<ReadFirstItemResponse> firstItemResponseList = itemsService.getFirstItemNamesFromReservations(reservationResponseList);
//                    List<ReadReservationListResponse> responseList = new ArrayList<>();
//                    for (int i = 0; i < reservationResponseList.size(); i++) {
//                        ReadReservationListResponse response = ReadReservationListResponse.builder()
//                                .reservationId(reservationResponseList.get(i).id())
//                                .firstItemName(firstItemResponseList.get(i).itemName())
//                                .itemQuantity(firstItemResponseList.get(i).itemQuantity())
//                                .createdAt(reservationResponseList.get(i).createdAt())
//                                .pickupTime(reservationResponseList.get(i).pickupTime())
//                                .reservationState(reservationResponseList.get(i).reservationState())
//                                .build();
//                        responseList.add(response);
//                    }
//
//                    return responseList;
//                });
//
//        return emitter;
//    }

//    @Operation(summary = "storeId 값으로 해당 가게의 신규 예약들을 SSE를 통해 가져옵니다.", description = "storeId 값으로 해당 가게의 신규 예약들을 SSE를 통해 가져옵니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "신규 예약 조회 성공", content = @Content(schema = @Schema(implementation = SseEmitter.class))),
//            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
//            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
//    })
//    @GetMapping(value = "/sse/reservations-new", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public ResponseEntity<SseEmitter> streamNewReservations(@RequestParam String storeId, @RequestParam String userId){
//        return ResponseEntity.ok(reservationService.getNewReservationsByStoreIdSse(userId, storeId));
//    }

//    @Operation(summary = "storeId 값으로 해당 가게의 완료 예약들을 slice 형태로 가져옵니다.", description = "storeId 값으로 해당 가게의 완료 예약들을 slice 형태로 가져옵니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "완료 예약 조회 성공", content = @Content(schema = @Schema(implementation = Slice.class))),
//            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
//            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
//    })
//    @GetMapping("/reservations-end-slice-time")
//    public Slice<ReadReservationListResponse> endReservationsCursor(@RequestParam String storeId, @RequestParam int page, @RequestParam int size, @RequestParam String timeStamp) throws ExecutionException, InterruptedException {
//        Timestamp ts = Timestamp.parseTimestamp(timeStamp);
//
//        Slice<ReadReservationResponse> reservationResponseSlice = reservationService.getReservationsByStoreIdAndTimeSlice(storeId, ts, page, size);
//
//        List<ReadReservationResponse> reservationResponseList = reservationResponseSlice.getContent();
//
//        List<ReadFirstItemResponse> firstItemResponseList = itemsService.getFirstItemNamesFromReservations(reservationResponseList);
//        List<ReadReservationListResponse> responseList = new ArrayList<>();
//
//        for (int i = 0; i < reservationResponseList.size(); i++) {
//            ReadReservationListResponse response = ReadReservationListResponse.builder()
//                    .reservationId(reservationResponseList.get(i).id())
//                    .firstItemName(firstItemResponseList.get(i).itemName())
//                    .itemQuantity(firstItemResponseList.get(i).itemQuantity())
//                    .createdAt(reservationResponseList.get(i).createdAt())
//                    .pickupTime(reservationResponseList.get(i).pickupTime())
//                    .reservationState(reservationResponseList.get(i).reservationState())
//                    .build();
//            responseList.add(response);
//        }
//
//        Pageable pageable = PageRequest.of(page, size);
//
//        return new SliceImpl<>(responseList, pageable, reservationResponseSlice.hasNext());
//    }
}
