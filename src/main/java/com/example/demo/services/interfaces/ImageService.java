package com.example.demo.services.interfaces;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.response.ImageResponse;

public interface ImageService {
    List<ImageResponse> getImagesByComplexId(Integer complexId);
    ImageResponse uploadImage(Integer complexId, MultipartFile file) throws IOException;
    void deleteImage(Integer imageId) throws IOException;
}
