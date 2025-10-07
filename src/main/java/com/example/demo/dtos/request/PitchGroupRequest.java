package com.example.demo.dtos.request;

import java.math.BigDecimal;
import java.util.Set;

import com.example.demo.entites.enums.PitchType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PitchGroupRequest {
    private String name;
    private PitchType type;
    private BigDecimal pricePerHour;
    private Integer complexId;
    private Set<Integer> pitchIds;
}
