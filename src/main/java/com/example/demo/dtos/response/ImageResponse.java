package com.example.demo.dtos.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ImageResponse {
    private Integer imageId;
    private String imageUrl;
    private LocalDateTime uploadedAt;
}
