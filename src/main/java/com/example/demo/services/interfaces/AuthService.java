package com.example.demo.services.interfaces;

import com.example.demo.dtos.request.LoginRequest;
import com.example.demo.dtos.request.UserRequest;
import com.example.demo.dtos.response.LoginResponse;
import com.example.demo.dtos.response.UserResponse;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    UserResponse register(UserRequest request);
    UserResponse getCurrentUser(HttpServletRequest request);
}
