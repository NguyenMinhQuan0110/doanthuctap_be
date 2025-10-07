package com.example.demo.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DistrictResponse {
    private Integer districtId;
    private String districtName;
    private Integer provinceId;
    private String provinceName;
}
