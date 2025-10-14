package com.example.demo.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
    private Integer userId;
    private String fullName;
    private String email;
    private String phone;
    private String avatar;
}
