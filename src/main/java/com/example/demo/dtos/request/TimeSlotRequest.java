package com.example.demo.dtos.request;

import java.math.BigDecimal;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class TimeSlotRequest {
    private LocalTime startTime;
    private LocalTime endTime;
    private BigDecimal price;
    private Integer complexId;
}
