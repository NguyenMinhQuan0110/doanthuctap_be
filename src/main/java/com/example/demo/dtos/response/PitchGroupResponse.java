package com.example.demo.dtos.response;

import java.math.BigDecimal;
import java.util.List;

import com.example.demo.entites.enums.PitchStatus;
import com.example.demo.entites.enums.PitchType;

import lombok.Data;
@Data
public class PitchGroupResponse {
    private Integer id;
    private String name;
    private PitchType type;
    private BigDecimal pricePerHour;
    private PitchStatus status;

    private Integer complexId;
    private String complexName;

    private List<Integer> pitchIds;
    private List<String> pitchNames;
}
