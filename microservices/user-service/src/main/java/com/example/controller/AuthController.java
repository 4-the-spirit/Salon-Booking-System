package com.example.controller;

import com.example.payload.dto.LoginDto;
import com.example.payload.dto.SignupDto;
import com.example.payload.response.AuthResponse;
import com.example.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> handleSignup(
            @RequestBody SignupDto signupDto
            ) {
        AuthResponse response = authService.signup(signupDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> handleLogin(
            @RequestBody LoginDto loginDto
    ) {
        AuthResponse response = authService.login(loginDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/access-token/refresh-token/{refreshToken}")
    public ResponseEntity<AuthResponse> getAccessToken(
            @PathVariable("refreshToken") String refreshToken
    ) {
        AuthResponse response = authService.getAccessTokenFromRefreshToken(refreshToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
