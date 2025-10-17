package com.example.demo.controlers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.request.UserRoleAssignRequest;
import com.example.demo.dtos.response.UserRoleResponse;
import com.example.demo.services.interfaces.UserRoleService;

@RestController
@RequestMapping("/api/user-roles")
public class UserRoleController {
    @Autowired
    private UserRoleService userRoleService;

    // Lấy danh sách role của 1 user
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/{userId}")
    public List<UserRoleResponse> getRolesByUser(@PathVariable("userId") Integer userId) {
        return userRoleService.getRolesByUserId(userId);
    }

    // Gán 1 hoặc nhiều role cho user
    @PreAuthorize("hasRole('admin')")
    @PostMapping("/assign")
    public List<UserRoleResponse> assignRoles(@RequestBody UserRoleAssignRequest request) {
        return userRoleService.assignRolesToUser(request);
    }

    // Xóa 1 role khỏi user
    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/{userId}/{roleId}")
    public void removeRole(@PathVariable("userId") Integer userId, @PathVariable("roleId") Integer roleId) {
        userRoleService.removeRoleFromUser(userId, roleId);
    }
}
