package com.example.demo.services.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.request.UserRequest;
import com.example.demo.dtos.response.UserResponse;

public interface UserService {
    List<UserResponse> getAllUsers();
    Optional<UserResponse> getUserById(Integer id);
    UserResponse createUser(UserRequest request);
    UserResponse updateUser(Integer id, UserRequest request);
    void deleteUser(Integer id);
    UserResponse updateAvatar(Integer id, MultipartFile file);
}
