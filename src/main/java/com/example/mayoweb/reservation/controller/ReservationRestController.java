package com.example.mayoweb.reservation.controller;

import com.example.mayoweb.commons.annotation.Authenticated;
import com.example.mayoweb.commons.exception.ApplicationException;
import com.example.mayoweb.commons.exception.payload.ErrorStatus;
import com.example.mayoweb.reservation.domain.dto.response.*;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "예약 API", description = "예약 관리 API")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ReservationRestController {

    private final ReservationService reservationService;

    @Value("${mayo.secret}")
    private String globalSecret;

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
    public ResponseEntity<List<ReadReservationListResponse>> getNewReservations(@RequestAttribute("uid") String uid) {
        return ResponseEntity.ok(reservationService.getNewByUserId(uid));
    }

    @Operation(summary = "storeId 값으로 해당 가게의 진행 예약들을 가져옵니다.", description = "storeId 값으로 해당 가게의 진행 예약들을 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "진행 예약 조회 성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @Authenticated
    @GetMapping("/reservation-proceed")
    public ResponseEntity<List<ReadReservationListResponse>> getProceedingReservations(@RequestAttribute("uid") String uid){
        return ResponseEntity.ok(reservationService.getProcessingByStoreId(uid));
    }

    @Operation(summary = "storeId 값, 시간 값으로 해당 가게의 완료 예약들을 가져옵니다.", description = "storeId 값, 시간 값으로 해당 가게의 완료 예약들을 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "완료 예약 조회 성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @Authenticated
    @GetMapping("/reservation-done-time")
    public ResponseEntity<List<ReadReservationListResponse>> getDoneReservations(@RequestAttribute("uid") String uid, @RequestParam String timestamp) {

        Timestamp ts = Timestamp.parseTimestamp(timestamp);

        return ResponseEntity.ok(reservationService.getEndByStoreIdAndTimestamp(uid, ts));
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
    public ResponseEntity<Void> reservationFailByStoreId(@RequestAttribute("uid") String uid) {

        reservationService.reservationFailByStoreId(uid);

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
    public ResponseEntity<Void> reservationNewFCM(@RequestAttribute("uid") String uid) {
        reservationService.createListener(uid);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "생성된 리스너를 제거합니다.", description = "생성된 리스너를 제거합니다 (브라우저 닫을 때 실행).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리스너 제거 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @Authenticated
    @DeleteMapping("/reservation-new/fcm")
    public ResponseEntity<Void> deleteFCM(@RequestAttribute("uid") String uid) {
        reservationService.stopListener(uid);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "해당 유저의 가게 예약 정보들을 날짜를 통해 가져옵니다.", description = "해당 유저의 가게 예약 정보들을 날짜를 통해 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @Authenticated
    @GetMapping("/reservation-all")
    public ResponseEntity<TotalReservationResponse> getAllReservations(@RequestAttribute("uid") String uid, @RequestParam LocalDate date) {
        return ResponseEntity.ok(reservationService.getReservationsByDate(uid, date));
    }

    @GetMapping("/reservation/{secret}/{year}/{month}")
    public ResponseEntity<List<ReadAllReservationResponse>> getReservations(@PathVariable String secret, @PathVariable int year, @PathVariable int month) {

        if(!secret.equals(globalSecret)) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("secret이 일치하지 않습니다.", 404, LocalDateTime.now()));
        }

        return ResponseEntity.ok(reservationService.getReservationByYearAndMonth(year, month));
    }
}
