package com.example.demo.dtos.request;

import java.time.LocalDate;

import com.example.demo.entites.enums.TargetType;

import lombok.Data;

@Data
public class BookingRequest {
    private Integer userId;
    private Integer timeSlotId;
    private TargetType targetType;
    private Integer targetId;
    private LocalDate bookingDate;
}
