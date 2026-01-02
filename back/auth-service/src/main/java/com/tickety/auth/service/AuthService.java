package com.tickety.auth.service;

import com.tickety.auth.dto.request.LoginRequest;
import com.tickety.auth.dto.request.RefreshTokenRequest;
import com.tickety.auth.dto.request.SignupRequest;
import com.tickety.auth.dto.response.TokenResponse;
import com.tickety.auth.dto.response.UserResponse;

import java.util.UUID;

public interface AuthService {

    UserResponse signup(SignupRequest request);

    TokenResponse login(LoginRequest request);

    TokenResponse refreshToken(RefreshTokenRequest request);

    void logout(UUID userId);

    UserResponse getCurrentUser(UUID userId);
}
