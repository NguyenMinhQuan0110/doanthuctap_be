package com.example.demo.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleResponse {
    private Integer userId;
    private String userName;
    private Integer roleId;
    private String roleName;
}
