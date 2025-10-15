package com.example.demo.controlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.request.LoginRequest;
import com.example.demo.dtos.request.UserRequest;
import com.example.demo.dtos.response.LoginResponse;
import com.example.demo.dtos.response.UserResponse;
import com.example.demo.entites.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.JwtUtils;
import com.example.demo.services.interfaces.AuthService;
import com.example.demo.services.interfaces.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    
    @Autowired 
    private JwtUtils jwtUtils;

    @Autowired 
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;

    @Value("${server.url}") 
    private String serverUrl;
    
    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        return authService.login(request);
    }
    
    @PostMapping("/register")
    public UserResponse register(@RequestBody UserRequest request) {
        return userService.createUser(request);
    }
    
    @GetMapping("/me")
    public UserResponse getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtUtils.extractEmail(token);
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserResponse(user.getUserId(), user.getFullName(), user.getEmail(), user.getPhone(),serverUrl+user.getAvatar());
    }

}
