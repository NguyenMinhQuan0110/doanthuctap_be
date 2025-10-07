package com.example.demo.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo.entites.enums.PitchStatus;
import com.example.demo.entites.enums.PitchType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PitchResponse {
    private Integer id;
    private String name;
    private PitchType type;
    private BigDecimal pricePerHour;
    private String description;
    private PitchStatus status;
    private LocalDateTime createdAt;

    private Integer complexId;
    private String complexName;
}
