package com.example.demo.services.interfaces;

import java.util.List;

import com.example.demo.dtos.request.UserRoleAssignRequest;
import com.example.demo.dtos.response.UserRoleResponse;

public interface UserRoleService {
    List<UserRoleResponse> getRolesByUserId(Integer userId);
    List<UserRoleResponse> assignRolesToUser(UserRoleAssignRequest request);
    void removeRoleFromUser(Integer userId, Integer roleId);
}
