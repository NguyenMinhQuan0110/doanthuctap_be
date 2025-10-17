package com.example.demo.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.request.LoginRequest;
import com.example.demo.dtos.request.UserRequest;
import com.example.demo.dtos.response.LoginResponse;
import com.example.demo.dtos.response.UserResponse;
import com.example.demo.entites.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.JwtUtils;
import com.example.demo.services.interfaces.AuthService;
import com.example.demo.services.interfaces.UserService;

import jakarta.servlet.http.HttpServletRequest;
@Service
public class AuthServiceImpl implements AuthService{
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    
    @Value("${server.url}")
    private String serverUrl;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        if (authentication.isAuthenticated()) {
            String token = jwtUtils.generateToken(loginRequest.getEmail());
            return new LoginResponse(token, "Login successful!");
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    @Override
    public UserResponse register(UserRequest request) {
        return userService.createUser(request);
    }

    @Override
    public UserResponse getCurrentUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        String email = jwtUtils.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return convertToResponse(user);
    }
    private UserResponse convertToResponse(User user) {
        String avatarUrl = null;
        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            avatarUrl = serverUrl + user.getAvatar();
        }
     // Lấy danh sách tên vai trò
        List<String> roles = user.getRoles().stream()
                .map(role -> role.getRoleName())
                .collect(Collectors.toList());

        // Trả về DTO phản hồi
        return new UserResponse(
                user.getUserId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                avatarUrl,
                roles
        );
    }
}
