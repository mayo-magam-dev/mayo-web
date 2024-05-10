package com.example.mayoweb.fcm;

import com.example.mayoweb.User.UsersService;
import com.example.mayoweb.reservation.ReservationService;
import com.example.mayoweb.reservation.ReservationsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fcm")
@Slf4j
@Tag(name = "FCM 메시지 API", description = "FCM 메시지 API 입니다.")
public class FCMController {

    private final ReservationService reservationService;
    private final UsersService userService;
    private final ReservationSchedule reservationSchedule;

    private static final String SERVER_NAME = "mayo-web-server";
    private static final String MS_PUSH_NOTIFICATION_FALSE = "push notification false = ";

    @Operation(summary = "수락 fcm 메시지", description = "예약 ref를 받아 해당 가게 주문 상태를 변경했을 시 푸시 알림을 전송합니다.")
    @PostMapping("/accept/{reservationId}")
    public ResponseEntity<Boolean> sendAcceptMessage(@PathVariable(value = "reservationId") String reservationRef)
            throws Exception {
        ReservationsDto dto = reservationService.getReservationById(reservationRef);
        List<String> tokens = userService.getTokensByUserRef(dto.getId());
        return ResponseEntity.status(HttpStatus.OK).body(reservationSchedule.sendAcceptMessage(tokens, dto));
    }

    @Operation(summary = "거절 fcm 메시지", description = "예약 ref를 받아 해당 가게 주문 상태를 변경했을 시 푸시 알림을 전송합니다.")
    @PostMapping("/reject/{reservationId}")
    public ResponseEntity<Boolean> sendRejectMessage(@PathVariable(value = "reservationId") String reservationRef)
            throws Exception {
        ReservationsDto dto = reservationService.getReservationById(reservationRef);
        List<String> tokens = userService.getTokensByUserRef(dto.getId());
        return ResponseEntity.status(HttpStatus.OK).body(reservationSchedule.sendRejectMessage(tokens, dto));
    }
}