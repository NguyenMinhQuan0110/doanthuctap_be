package com.example.demo.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.request.RoleRequest;
import com.example.demo.dtos.response.RoleResponse;
import com.example.demo.entites.Role;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.services.interfaces.RoleService;

import jakarta.transaction.Transactional;
@Service
public class RoleServiceImpl implements RoleService{
    @Autowired
    private RoleRepository roleRepository;

    private RoleResponse mapToResponse(Role role) {
        RoleResponse res = new RoleResponse();
        res.setRoleId(role.getRoleId());
        res.setRoleName(role.getRoleName());
        res.setDescription(role.getDescription());
        return res;
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RoleResponse> getRoleById(Integer id) {
        return roleRepository.findById(id).map(this::mapToResponse);
    }

    @Override
    @Transactional
    public RoleResponse createRole(RoleRequest request) {
        Role role = new Role();
        role.setRoleName(request.getRoleName());
        role.setDescription(request.getDescription());
        Role saved = roleRepository.save(role);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public RoleResponse updateRole(Integer id, RoleRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        role.setRoleName(request.getRoleName());
        role.setDescription(request.getDescription());
        Role updated = roleRepository.save(role);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteRole(Integer id) {
        roleRepository.deleteById(id);
    }
}
