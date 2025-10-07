package com.example.demo.services.interfaces;

import java.util.List;
import java.util.Optional;

import com.example.demo.dtos.request.RoleRequest;
import com.example.demo.dtos.response.RoleResponse;

public interface RoleService {
    List<RoleResponse> getAllRoles();
    Optional<RoleResponse> getRoleById(Integer id);
    RoleResponse createRole(RoleRequest request);
    RoleResponse updateRole(Integer id, RoleRequest request);
    void deleteRole(Integer id);
}
