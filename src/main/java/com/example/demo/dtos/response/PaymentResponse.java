package com.example.demo.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo.entites.enums.PaymentMethod;
import com.example.demo.entites.enums.PaymentStatus;

import lombok.Data;

@Data
public class PaymentResponse {
    private Integer id;
    private BigDecimal amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;

    private Integer bookingId;
    private Integer userId;
    private String userName;
}
