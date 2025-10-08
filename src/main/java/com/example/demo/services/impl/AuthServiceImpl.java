package com.example.demo.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.request.LoginRequest;
import com.example.demo.dtos.response.LoginResponse;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.JwtUtils;
import com.example.demo.services.interfaces.AuthService;
@Service
public class AuthServiceImpl implements AuthService{
    @Autowired
    private AuthenticationManager authenticationManager;

//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;
    
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

}
