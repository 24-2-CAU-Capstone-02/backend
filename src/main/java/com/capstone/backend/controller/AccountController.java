package com.capstone.backend.controller;

import com.capstone.backend.dto.request.GoogleLoginRequest;
import com.capstone.backend.dto.response.JwtResponse;
import com.capstone.backend.entity.Account;
import com.capstone.backend.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth API", description = "구글 로그인 관련 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @Operation(summary = "구글 로그인", description = "구글 로그인을 통해 JWT를 발급합니다.")
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> googleLogin(@Valid @RequestBody GoogleLoginRequest request) {
        Account account = accountService.googleLogin(request);
        JwtResponse response = accountService.generateJwt(account);
        return ResponseEntity.ok(response);
    }
}
