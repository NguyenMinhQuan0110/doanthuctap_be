package com.example.demo.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.response.ImageResponse;
import com.example.demo.entites.Complex;
import com.example.demo.entites.Image;
import com.example.demo.repositories.ComplexRepository;
import com.example.demo.repositories.ImageRepository;
import com.example.demo.services.interfaces.ImageService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService{
    private final ImageRepository imageRepository;
    private final ComplexRepository complexRepository;

    @Value("${upload.path:uploads/images}") // Có thể config trong application.properties
    private String uploadDir;

    @Override
    public List<ImageResponse> getImagesByComplexId(Integer complexId) {
        return imageRepository.findByComplex_ComplexId(complexId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ImageResponse uploadImage(Integer complexId, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        Complex complex = complexRepository.findById(complexId)
                .orElseThrow(() -> new RuntimeException("Complex not found"));

        // Tạo thư mục upload nếu chưa có
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Tạo tên file duy nhất
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        // Lưu file lên server
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Lưu thông tin vào DB
        Image image = new Image();
        image.setComplex(complex);
        image.setImageUrl("/uploads/images/" + fileName);
        imageRepository.save(image);

        log.info("✅ Uploaded image: {}", fileName);
        return mapToResponse(image);
    }

    @Override
    public void deleteImage(Integer id) throws IOException {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        // Xóa file vật lý
        String fileName = Paths.get(image.getImageUrl()).getFileName().toString();
        Path filePath = Paths.get(uploadDir, fileName);

        if (Files.exists(filePath)) {
            Files.delete(filePath);
            log.info("🗑️ Deleted file: {}", filePath.toAbsolutePath());
        } else {
            log.warn("⚠️ File not found on disk: {}", filePath.toAbsolutePath());
        }

        imageRepository.delete(image);
        log.info("✅ Deleted image record with id: {}", id);
    }

    private ImageResponse mapToResponse(Image image) {
        ImageResponse res = new ImageResponse();
        res.setImageId(image.getImageId());
        res.setImageUrl(image.getImageUrl());
        res.setUploadedAt(image.getUploadedAt());
        return res;
    }
}
