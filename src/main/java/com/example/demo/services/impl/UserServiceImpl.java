package com.example.demo.services.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.request.UserRequest;
import com.example.demo.dtos.response.UserResponse;
import com.example.demo.entites.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.interfaces.UserService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService{
	
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${upload.avatar-dir}")
    private String avatarDir;

    @Value("${server.url}")
    private String serverUrl;

    private UserResponse convertToResponse(User user) {
        String avatarUrl = null;
        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            avatarUrl = serverUrl + user.getAvatar();
        }

        return new UserResponse(
                user.getUserId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                avatarUrl
        );
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
    public UserResponse createUser(UserRequest request) {
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return convertToResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateUser(Integer id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return convertToResponse(userRepository.save(user));
    }

    @Override
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Xoá avatar file nếu có
        if (user.getAvatar() != null) {
            File oldFile = new File("." + user.getAvatar());
            if (oldFile.exists()) oldFile.delete();
        }
        userRepository.delete(user);
    }

    @Override
    public UserResponse updateAvatar(Integer id, MultipartFile file) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        try {
            // Xoá file cũ
            if (user.getAvatar() != null) {
                File oldFile = new File("." + user.getAvatar());
                if (oldFile.exists()) oldFile.delete();
            }

            // Lưu file mới
            String newFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(avatarDir, newFileName);
            Files.createDirectories(filePath.getParent());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Cập nhật path tương đối trong DB
            user.setAvatar("/uploads/avatars/" + newFileName);
            userRepository.save(user);

            return convertToResponse(user);
        } catch (IOException e) {
            throw new RuntimeException("Error saving avatar", e);
        }
    }
}
