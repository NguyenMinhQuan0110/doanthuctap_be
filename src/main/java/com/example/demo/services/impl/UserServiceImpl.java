package com.example.demo.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.request.UserRequest;
import com.example.demo.dtos.response.UserResponse;
import com.example.demo.entites.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.interfaces.UserService;

import jakarta.transaction.Transactional;
@Service
public class UserServiceImpl implements UserService{
	
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserResponse convertToResponse(User user) {
        return new UserResponse(
                user.getUserId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone()
        );
    }

    private User convertToEntity(UserRequest request) {
        User user = new User();
        user.setFullName(request.getFullName()); // giả sử fullName = username
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // TODO: hash password
        user.setPhone(request.getPhone());
        return user;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserResponse> getUserById(Integer id) {
        return userRepository.findById(id).map(this::convertToResponse);
    }

    @Override
    @Transactional
    public UserResponse createUser(UserRequest request) {
        User newUser = convertToEntity(request);
        User savedUser = userRepository.save(newUser);
        return convertToResponse(savedUser);
    }

    @Override
    @Transactional
    public UserResponse updateUser(Integer id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword())); // TODO: hash password
        }

        User updatedUser = userRepository.save(user);
        return convertToResponse(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }
}
