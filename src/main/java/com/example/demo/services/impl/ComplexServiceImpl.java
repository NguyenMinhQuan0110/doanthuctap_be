package com.example.demo.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.request.ComplexRequest;
import com.example.demo.dtos.response.ComplexResponse;
import com.example.demo.entites.Complex;
import com.example.demo.entites.District;
import com.example.demo.entites.Province;
import com.example.demo.entites.User;
import com.example.demo.repositories.ComplexRepository;
import com.example.demo.repositories.DistrictRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.interfaces.ComplexService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ComplexServiceImpl implements ComplexService{
    @Autowired
    private ComplexRepository complexRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Override
    public List<ComplexResponse> getAllComplexes() {
        return complexRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ComplexResponse getComplexById(Integer id) {
        Complex complex = complexRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complex not found"));
        return mapToResponse(complex);
    }

    @Override
    public ComplexResponse createComplex(ComplexRequest request) {
    	// ✅ Kiểm tra trùng tên cụm sân
        if (complexRepository.existsByNameIgnoreCase(request.getName())) {
            throw new RuntimeException("Complex name already exists");
        }
        
        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        District district = districtRepository.findById(request.getDistrictId())
                .orElseThrow(() -> new RuntimeException("District not found"));

        Complex complex = new Complex();
        complex.setName(request.getName());
        complex.setAddress(request.getAddress());
        complex.setLatitude(request.getLatitude());
        complex.setLongitude(request.getLongitude());
        complex.setPhone(request.getPhone());
        complex.setOwner(owner);
        complex.setDistrict(district);

        return mapToResponse(complexRepository.save(complex));
    }

    @Override
    public ComplexResponse updateComplex(Integer id, ComplexRequest request) {
        Complex complex = complexRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complex not found"));

        // ✅ Kiểm tra trùng tên (bỏ qua chính nó)
        boolean nameExists = complexRepository.existsByName(request.getName());
        if (!complex.getName().equals(request.getName()) && nameExists) {
            throw new RuntimeException("Complex name already exists");
        }

        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        District district = districtRepository.findById(request.getDistrictId())
                .orElseThrow(() -> new RuntimeException("District not found"));

        complex.setName(request.getName());
        complex.setAddress(request.getAddress());
        complex.setLatitude(request.getLatitude());
        complex.setLongitude(request.getLongitude());
        complex.setPhone(request.getPhone());
        complex.setOwner(owner);
        complex.setDistrict(district);

        return mapToResponse(complexRepository.save(complex));
    }

    @Override
    public void deleteComplex(Integer id) {
        Complex complex = complexRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complex not found"));
        complexRepository.delete(complex);
    }

    private ComplexResponse mapToResponse(Complex complex) {
        ComplexResponse response = new ComplexResponse();
        response.setId(complex.getComplexId());
        response.setName(complex.getName());
        response.setAddress(complex.getAddress());
        response.setLatitude(complex.getLatitude());
        response.setLongitude(complex.getLongitude());
        response.setPhone(complex.getPhone());
        response.setStatus(complex.getStatus());
        response.setCreatedAt(complex.getCreatedAt());

        if (complex.getOwner() != null) {
            response.setOwnerId(complex.getOwner().getUserId());
            response.setOwnerName(complex.getOwner().getFullName());
        }

        if (complex.getDistrict() != null) {
            response.setDistrictId(complex.getDistrict().getDistrictId());
            response.setDistrictName(complex.getDistrict().getDistrictName());
            if (complex.getDistrict().getProvince() != null) {
                Province province = complex.getDistrict().getProvince();
                response.setProvinceId(province.getProvinceId());
                response.setProvinceName(province.getProvinceName());
            }
        }

        return response;
    }
    @Override
    public List<ComplexResponse> getComplexesByDistrictId(Integer districtId) {
        List<Complex> complexes = complexRepository.findByDistrict_DistrictId(districtId);
        return complexes.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ComplexResponse> getComplexesByProvinceId(Integer provinceId) {
        List<Complex> complexes = complexRepository.findByDistrict_Province_ProvinceId(provinceId);
        return complexes.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}
