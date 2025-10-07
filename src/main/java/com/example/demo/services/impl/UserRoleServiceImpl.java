package com.example.demo.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.request.UserRoleAssignRequest;
import com.example.demo.dtos.response.UserRoleResponse;
import com.example.demo.entites.Role;
import com.example.demo.entites.User;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.interfaces.UserRoleService;

import jakarta.transaction.Transactional;
@Service
public class UserRoleServiceImpl implements UserRoleService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<UserRoleResponse> getRolesByUserId(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getRoles().stream().map(role -> {
            UserRoleResponse res = new UserRoleResponse();
            res.setUserId(user.getUserId());
            res.setUserName(user.getFullName());
            res.setRoleId(role.getRoleId());
            res.setRoleName(role.getRoleName());
            return res;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<UserRoleResponse> assignRolesToUser(UserRoleAssignRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Lấy danh sách role mới từ DB theo danh sách ID
        List<Role> newRoles = roleRepository.findAllById(request.getRoleIds());

        // Xóa toàn bộ quyền cũ
        user.getRoles().clear();

        // Gán quyền mới
        user.getRoles().addAll(newRoles);

        // Lưu lại user với quyền mới
        userRepository.save(user);

        return user.getRoles().stream().map(role -> {
            UserRoleResponse res = new UserRoleResponse();
            res.setUserId(user.getUserId());
            res.setUserName(user.getFullName());
            res.setRoleId(role.getRoleId());
            res.setRoleName(role.getRoleName());
            return res;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void removeRoleFromUser(Integer userId, Integer roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.getRoles().remove(role);
        userRepository.save(user);
    }
}
