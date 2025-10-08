package com.example.demo.services.interfaces;

import com.example.demo.dtos.request.LoginRequest;
import com.example.demo.dtos.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
}
