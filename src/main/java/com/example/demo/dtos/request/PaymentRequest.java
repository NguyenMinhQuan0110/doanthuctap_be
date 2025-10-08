package com.example.demo.dtos.request;

import java.math.BigDecimal;

import com.example.demo.entites.enums.PaymentMethod;
import com.example.demo.entites.enums.PaymentStatus;

import lombok.Data;

@Data
public class PaymentRequest {
    private Integer bookingId;
    private BigDecimal amount;
    private PaymentMethod method;
    private PaymentStatus status;
}
