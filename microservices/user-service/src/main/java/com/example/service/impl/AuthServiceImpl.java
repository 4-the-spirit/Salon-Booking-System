package com.example.service.impl;

import com.example.model.User;
import com.example.payload.dto.LoginDto;
import com.example.payload.dto.SignupDto;
import com.example.payload.response.AuthResponse;
import com.example.payload.response.TokenResponse;
import com.example.repository.UserRepository;
import com.example.service.AuthService;
import com.example.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    private final KeycloakService keycloakService;

    @Override
    public AuthResponse login(LoginDto loginDto) {
        TokenResponse tokenResponse = keycloakService.getTokenResponse(
                loginDto.getUsername(),
                loginDto.getPassword(),
                "password",
                null);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setRefreshToken(tokenResponse.getRefreshToken());
        authResponse.setJwt(tokenResponse.getAccessToken());
        authResponse.setMessage("Login Succeed!");
        return authResponse;
    }

    @Override
    public AuthResponse signup(SignupDto signupDto) {
        keycloakService.createUser(signupDto);

        User user = new User();
        user.setUsername(signupDto.getUsername());
        user.setPassword(signupDto.getPassword());
        user.setEmail(signupDto.getEmail());
        user.setRole(signupDto.getRole());
        user.setFullName(signupDto.getFirstName() + " " + signupDto.getLastName());

        userRepository.save(user);

        TokenResponse tokenResponse = keycloakService.getTokenResponse(
                signupDto.getUsername(),
                signupDto.getPassword(),
                "password",
                null);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setRefreshToken(tokenResponse.getRefreshToken());
        authResponse.setJwt(tokenResponse.getAccessToken());
        authResponse.setRole(user.getRole());
        authResponse.setMessage("Registration Succeed!");

        return authResponse;
    }

    @Override
    public AuthResponse getAccessTokenFromRefreshToken(String refreshToken) {
        TokenResponse tokenResponse = keycloakService.getTokenResponse(
                null,
                null,
                "refresh_token",
                refreshToken);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setRefreshToken(tokenResponse.getRefreshToken());
        authResponse.setJwt(tokenResponse.getAccessToken());
        authResponse.setMessage("Access Token Received!");
        return authResponse;
    }
}
