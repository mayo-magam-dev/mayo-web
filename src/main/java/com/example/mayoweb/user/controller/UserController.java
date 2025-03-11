package com.example.mayoweb.user.controller;

import com.example.mayoweb.commons.annotation.Authenticated;
import com.example.mayoweb.commons.annotation.CreateUser;
import com.example.mayoweb.fcm.dto.CreateFCMTokenRequest;
import com.example.mayoweb.user.domain.dto.reqeust.CreateUserRequest;
import com.example.mayoweb.user.domain.dto.response.ReadUserResponse;
import com.example.mayoweb.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "유저 API", description = "유저 정보 API")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "firebase 토큰 값으로 user정보를 가져옵니다.", description = "firebase 토큰 값으로 user정보를 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 조회 성공", content = @Content(schema = @Schema(implementation = ReadUserResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Authenticated
    @GetMapping("/user")
    public ResponseEntity<ReadUserResponse> getUserByToken(@RequestAttribute("uid") String uid) {
        return ResponseEntity.ok(userService.getUserById(uid));
    }

    @Operation(summary = "userId값으로 fcm 토큰을 가져옵니다.", description = "userId값으로 fcm 토큰을 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 조회 성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Authenticated
    @GetMapping("/user-fcmToken")
    public ResponseEntity<List<String>> getFcmTokenByUserId(@RequestAttribute("uid") String uid) {
        return ResponseEntity.ok(userService.getTokensByUserRef(uid));
    }

    @Operation(summary = "userId값 및 fcmToken으로 해당 user의 fcm토큰을 생성합니다.", description = "userId값 및 fcmToken으로 해당 user의 fcm토큰을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "토큰 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Authenticated
    @PostMapping("/fcm")
    public ResponseEntity<Void> createFCMToken(@RequestAttribute("uid") String uid, @RequestBody CreateFCMTokenRequest request) {
        userService.createWebFCMToken(uid ,request.fcmToken());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "유저 회원 가입", description = "유저 회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @CreateUser
    @PostMapping
    public ResponseEntity<Void> createUser(@RequestAttribute("uid") String uid, @RequestBody CreateUserRequest request) {
        userService.createUser(request, uid);
        return ResponseEntity.noContent().build();
    }
}
