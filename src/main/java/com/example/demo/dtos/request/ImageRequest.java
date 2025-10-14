package com.example.demo.dtos.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ImageRequest {
    private Integer complexId;
    private MultipartFile file;
}
