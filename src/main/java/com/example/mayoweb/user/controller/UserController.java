package com.example.mayoweb.user.controller;

import com.example.mayoweb.user.domain.dto.response.ReadUserResponse;
import com.example.mayoweb.user.service.UsersService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Tag(name = "유저 API", description = "유저 정보 API")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UsersService userService;

    @Operation(summary = "firebase 토큰 값으로 user정보를 가져옵니다.", description = "firebase 토큰 값으로 user정보를 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 조회 성공", content = @Content(schema = @Schema(implementation = ReadUserResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/user")
    public ResponseEntity<ReadUserResponse> getUserByToken(@RequestHeader("Authorization") String authorizationHeader) throws FirebaseAuthException {

        String idToken = authorizationHeader.substring(7);
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
        String uid = decodedToken.getUid();

        return ResponseEntity.ok(userService.getUserById(uid));
    }

    @Operation(summary = "userId값으로 fcm 토큰을 가져옵니다.", description = "userId값으로 fcm 토큰을 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 조회 성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/user-fcmToken")
    public ResponseEntity<List<String>> getFcmTokenByUserId(@RequestParam String userId) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(userService.getTokensByUserRef(userId));
    }
}
