package com.example.demo.dtos.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.demo.entites.enums.BookingStatus;
import com.example.demo.entites.enums.TargetType;

import lombok.Data;

@Data
public class BookingResponse {
    private Integer id;
    private TargetType targetType;
    private Integer targetId;
    private String targetName;
    private String complexName;
    private LocalDate bookingDate;
    private BookingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer userId;
    private String userName;

    private Integer timeSlotId;
    private String timeSlotRange;
}
