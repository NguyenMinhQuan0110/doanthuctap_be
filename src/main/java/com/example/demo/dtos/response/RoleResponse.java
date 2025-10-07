package com.example.demo.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleResponse {
    private Integer roleId;
    private String roleName;
    private String description;
}
