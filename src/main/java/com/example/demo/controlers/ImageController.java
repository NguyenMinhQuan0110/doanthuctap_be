package com.example.demo.controlers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.response.ImageResponse;
import com.example.demo.services.interfaces.ImageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/complex/{complexId}")
    public ResponseEntity<List<ImageResponse>> getImagesByComplex(@PathVariable("complexId") Integer complexId) {
        return ResponseEntity.ok(imageService.getImagesByComplexId(complexId));
    }
    @PreAuthorize("hasAnyRole('admin','owner')")
    @PostMapping("/upload/complex/{complexId}")
    public ResponseEntity<ImageResponse> uploadImage(
            @PathVariable("complexId") Integer complexId,
            @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(imageService.uploadImage(complexId, file));
    }
    @PreAuthorize("hasAnyRole('admin','owner')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteImage(@PathVariable("id") Integer id) throws IOException {
        imageService.deleteImage(id);
        return ResponseEntity.ok(Map.of("message", "Image deleted successfully"));
    }
}
