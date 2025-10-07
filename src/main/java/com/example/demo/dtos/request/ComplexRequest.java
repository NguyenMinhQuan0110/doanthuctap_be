package com.example.demo.dtos.request;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComplexRequest {
	
    private String name;
    private String address;
    private BigDecimal  latitude;
    private BigDecimal  longitude;
    private String phone;

    private Integer ownerId;

    private Integer districtId;
}
