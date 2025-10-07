package com.example.demo.dtos.response;

import java.math.BigDecimal;
import java.time.LocalTime;

import com.example.demo.entites.enums.TimeSlotStatus;

import lombok.Data;

@Data
public class TimeSlotResponse {
    private Integer id;
    private LocalTime startTime;
    private LocalTime endTime;
    private BigDecimal price;
    private TimeSlotStatus status;

    private Integer complexId;
    private String complexName;
}
