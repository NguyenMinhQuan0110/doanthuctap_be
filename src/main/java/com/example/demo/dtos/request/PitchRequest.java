package com.example.demo.dtos.request;

import java.math.BigDecimal;

import com.example.demo.entites.enums.PitchStatus;
import com.example.demo.entites.enums.PitchType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PitchRequest {
    private String name;
    private PitchType type;
    private BigDecimal pricePerHour;
    private String description;
    private PitchStatus status;
    private Integer complexId;
}
