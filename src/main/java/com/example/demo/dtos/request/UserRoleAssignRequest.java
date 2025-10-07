package com.example.demo.dtos.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class UserRoleAssignRequest {
    private Integer userId;
    private List<Integer> roleIds;
}
