package com.example.mayoweb.reservation.controller;

import com.example.mayoweb.commons.annotation.Authenticated;

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
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @Authenticated
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
    @Authenticated
    @GetMapping("/reservation-new")
    public ResponseEntity<List<ReadReservationListResponse>> getNewReservations(HttpServletRequest req) {
        return ResponseEntity.ok(reservationService.getNewByUserId(req.getAttribute("uid").toString()));
    }

    @Operation(summary = "storeId 값으로 해당 가게의 진행 예약들을 가져옵니다.", description = "storeId 값으로 해당 가게의 진행 예약들을 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "진행 예약 조회 성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @Authenticated
    @GetMapping("/reservation-proceed")
    public ResponseEntity<List<ReadReservationListResponse>> getProceedingReservations(HttpServletRequest req){
        return ResponseEntity.ok(reservationService.getProcessingByStoreId(req.getAttribute("uid").toString()));
    }

    @Operation(summary = "storeId 값, 시간 값으로 해당 가게의 완료 예약들을 가져옵니다.", description = "storeId 값, 시간 값으로 해당 가게의 완료 예약들을 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "완료 예약 조회 성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @Authenticated
    @GetMapping("/reservation-done-time")
    public ResponseEntity<List<ReadReservationListResponse>> getDoneReservations(HttpServletRequest req, @RequestParam String timestamp) {

        Timestamp ts = Timestamp.parseTimestamp(timestamp);

        return ResponseEntity.ok(reservationService.getEndByStoreIdAndTimestamp(req.getAttribute("uid").toString(), ts));
    }

    @Operation(summary = "reservationId를 받아 해당 예약의 상태를 수락으로 변경합니다.", description = "reservationId를 받아 해당 예약의 상태를 수락으로 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "예약 수락 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @Authenticated
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
    @Authenticated
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
    @Authenticated
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
    @Authenticated
    @GetMapping("/reservation-detail")
    public ResponseEntity<ReadReservationDetailResponse> getItemByReservationId(@RequestParam String reservationId) {
        return ResponseEntity.ok(reservationService.getReservationDetailById(reservationId));
    }

    @Authenticated
    @PutMapping("/reservation/all-fail")
    public ResponseEntity<Void> reservationFailByStoreId(HttpServletRequest req) {

        reservationService.reservationFailByStoreId(req.getAttribute("uid").toString());

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "유저 Id와 가게 Id값으로 해당 유저의 알림을 받을 수 있는 리스너를 생성합니다.", description = "유저 Id와 가게 Id값으로 해당 유저의 알림을 받을 수 있는 리스너를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "알림 리스너 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @Authenticated
    @PostMapping("/reservation-new/fcm")
    public ResponseEntity<Void> reservationNewFCM(HttpServletRequest req) {
        reservationService.sendFCMNewReservation(req.getAttribute("uid").toString());
        return ResponseEntity.noContent().build();
    }
}
