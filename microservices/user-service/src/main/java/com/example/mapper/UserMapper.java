package com.example.mapper;

import com.example.payload.dto.KeycloakUserDto;
import com.example.payload.request.UserInfoRequest;

public class UserMapper {
    public static KeycloakUserDto mapToKeycloakUserDto(UserInfoRequest userInfoRequest) {
        KeycloakUserDto user = new KeycloakUserDto();
        user.setFirstName(userInfoRequest.getFirstName());
        user.setLastName(userInfoRequest.getLastName());
        user.setEmail(userInfoRequest.getEmail());
        user.setUsername(userInfoRequest.getUsername());
        return user;
    }
}
