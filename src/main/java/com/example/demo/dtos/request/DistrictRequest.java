package com.example.demo.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DistrictRequest {
    private String districtName;
    private Integer provinceId;
}
