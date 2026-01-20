package com.example.demo.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo.entites.enums.ComplexStatus;

import lombok.Data;
;

@Data
public class ComplexResponse {
    private Integer id;
    private String name;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String phone;
    private ComplexStatus status;
    private LocalDateTime createdAt;
    private String avatarCom;
    private Integer ownerId;
    private String ownerName;
    private Integer districtId;
    private String districtName;
    private Integer provinceId;
    private String provinceName;
}
