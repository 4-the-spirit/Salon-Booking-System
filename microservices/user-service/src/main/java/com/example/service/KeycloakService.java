package com.example.service;

import com.example.domain.UserRole;
import com.example.mapper.UserMapper;
import com.example.payload.dto.KeycloakRoleDto;
import com.example.payload.dto.KeycloakUserDto;
import com.example.payload.dto.SignupDto;
import com.example.payload.request.CreateUserRequest;
import com.example.payload.request.CredentialRequest;
import com.example.payload.request.UserInfoRequest;
import com.example.payload.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakService {
    private static final String KEYCLOAK_BASE_URL = "http://localhost:8080";
    private static final String KEYCLOAK_ADMIN_API = KEYCLOAK_BASE_URL + "/admin/realms/master/users";
    private static final String TOKEN_URL = KEYCLOAK_BASE_URL + "/realms/master/protocol/openid-connect/token";
    private static final String CLIENT_ID = "salon-booking-client";
    private static final String CLIENT_SECRET = "tGmnkkHJrsrDgUJt1CwTUXJSHXz9zBj1";
    private static final String GRANT_TYPE = "password";
    private static final String SCOPE = "openid email profile";
    private static final String USERNAME = "newadmin";
    private static final String PASSWORD = "admin";
    private static final String CLIENT_DB_ID = "fed9b634-cb44-4f68-9f1b-d1f5f8b7a244";

    private final RestTemplate restTemplate;

    public void createUser(SignupDto signupDto) {
        final String ACCESS_TOKEN = getTokenResponse(
                USERNAME,
                PASSWORD,
                GRANT_TYPE,
                null
        ).getAccessToken();

        CredentialRequest credential = new CredentialRequest();
        credential.setTemporary(false);
        credential.setType("password");
        credential.setValue(signupDto.getPassword());

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(signupDto.getUsername());
        createUserRequest.setEmail(signupDto.getEmail());
        createUserRequest.setEnabled(true);
        createUserRequest.setFirstName(signupDto.getFirstName());
        createUserRequest.setLastName(signupDto.getLastName());
        createUserRequest.getCredentials().add(credential);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        RequestEntity<CreateUserRequest> requestHttpEntity = new RequestEntity<>(
                createUserRequest,
                headers,
                HttpMethod.POST,
                URI.create(KEYCLOAK_ADMIN_API));

        ResponseEntity<String> response = restTemplate.exchange(requestHttpEntity, String.class);

        if (response.getStatusCode().equals(HttpStatus.CREATED)) {
            System.out.println("User created successfully!");

            KeycloakUserDto user = fetchFirstUserByUsername(
                    signupDto.getUsername(),
                    ACCESS_TOKEN);

            KeycloakRoleDto role = getRoleByName(CLIENT_DB_ID, ACCESS_TOKEN, signupDto.getRole());

            List<KeycloakRoleDto> roles = new ArrayList<>();
            roles.add(role);

            assignRoleToUser(user.getId(), CLIENT_DB_ID, roles, ACCESS_TOKEN);
        } else {
            System.out.println("User creation failed!");
            throw new RuntimeException(response.getBody());
        }
    }

    public TokenResponse getTokenResponse(String username,
                                          String password,
                                          String grantType,
                                          String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", grantType);
        requestBody.add("username", username);
        requestBody.add("password", password);
        requestBody.add("refresh_token", refreshToken);
        requestBody.add("client_id", CLIENT_ID);
        requestBody.add("client_secret", CLIENT_SECRET);
        requestBody.add("scope", SCOPE);

        RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<>(
                requestBody,
                headers,
                HttpMethod.POST,
                URI.create(TOKEN_URL));

        ResponseEntity<TokenResponse> response = restTemplate.exchange(
                request,
                TokenResponse.class
        );

        if (response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
            return response.getBody();
        }
        throw new RuntimeException("Failed to obtain access token!");
    }

    public KeycloakRoleDto getRoleByName(String clientId,
                                         String token,
                                         UserRole role) {

        String url = KEYCLOAK_BASE_URL + "/admin/realms/master/clients/"
                + clientId + "/roles/" + role;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        RequestEntity<KeycloakRoleDto> request = new RequestEntity<>(
                headers,
                HttpMethod.GET,
                URI.create(url)
        );
        ResponseEntity<KeycloakRoleDto> response = restTemplate.exchange(
                request,
                KeycloakRoleDto.class
        );
        return response.getBody();
    }

    public KeycloakUserDto fetchFirstUserByUsername(String username,
                                                    String token) {
        String url = KEYCLOAK_BASE_URL + "/admin/realms/master/users?username=" + username;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        RequestEntity<Void> request = new RequestEntity<>(
                headers,
                HttpMethod.GET,
                URI.create(url)
        );
        ResponseEntity<KeycloakUserDto[]> response = restTemplate.exchange(
                request,
                KeycloakUserDto[].class
        );
        KeycloakUserDto[] users = response.getBody();

        if (users != null && users.length > 0) {
            return users[0];
        }
        throw new RuntimeException("User not found!");
    }

    public void assignRoleToUser(String userId,
                                 String clientId,
                                 List<KeycloakRoleDto> roles,
                                 String token) {
        String url = KEYCLOAK_BASE_URL + "/admin/realms/master/users/" + userId +
                "/role-mappings/clients/" + clientId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        RequestEntity<List<KeycloakRoleDto>> request = new RequestEntity<>(
                roles,
                headers,
                HttpMethod.POST,
                URI.create(url)
        );
        ResponseEntity<String> response = restTemplate.exchange(
                request,
                String.class
        );
    }

    public KeycloakUserDto fetchUserByJwtToken(String jwtToken) {
        String url = KEYCLOAK_BASE_URL + "/realms/master/protocol/openid-connect/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        RequestEntity<Void> request = new RequestEntity<>(
                headers,
                HttpMethod.GET,
                URI.create(url)
        );
        ResponseEntity<UserInfoRequest> response = restTemplate.exchange(
                request,
                UserInfoRequest.class
        );
        return UserMapper.mapToKeycloakUserDto(response.getBody());
    }
}
