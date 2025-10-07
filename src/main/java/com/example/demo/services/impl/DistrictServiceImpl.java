package com.example.demo.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.request.DistrictRequest;
import com.example.demo.dtos.response.DistrictResponse;
import com.example.demo.entites.District;
import com.example.demo.entites.Province;
import com.example.demo.repositories.DistrictRepository;
import com.example.demo.repositories.ProvinceRepository;
import com.example.demo.services.interfaces.DistrictService;

import jakarta.transaction.Transactional;
@Service
@Transactional
public class DistrictServiceImpl implements DistrictService{
    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Override
    public List<DistrictResponse> getAllDistricts() {
        return districtRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DistrictResponse> getDistrictsByProvince(Integer provinceId) {
        Province province = provinceRepository.findById(provinceId)
                .orElseThrow(() -> new RuntimeException("Province not found"));

        return districtRepository.findByProvince(province)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DistrictResponse getDistrictById(Integer id) {
        District district = districtRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("District not found"));
        return mapToResponse(district);
    }

    @Override
    public DistrictResponse createDistrict(DistrictRequest request) {
        if (districtRepository.existsByDistrictName(request.getDistrictName())) {
            throw new RuntimeException("District name already exists");
        }

        Province province = provinceRepository.findById(request.getProvinceId())
                .orElseThrow(() -> new RuntimeException("Province not found"));

        District district = new District();
        district.setDistrictName(request.getDistrictName());
        district.setProvince(province);

        return mapToResponse(districtRepository.save(district));
    }

    @Override
    public DistrictResponse updateDistrict(Integer id, DistrictRequest request) {
        District district = districtRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("District not found"));

        Province province = provinceRepository.findById(request.getProvinceId())
                .orElseThrow(() -> new RuntimeException("Province not found"));

        district.setDistrictName(request.getDistrictName());
        district.setProvince(province);

        return mapToResponse(districtRepository.save(district));
    }

    @Override
    public void deleteDistrict(Integer id) {
        District district = districtRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("District not found"));
        districtRepository.delete(district);
    }

    private DistrictResponse mapToResponse(District district) {
        return new DistrictResponse(
                district.getDistrictId(),
                district.getDistrictName(),
                district.getProvince().getProvinceId(),
                district.getProvince().getProvinceName()
        );
    }
}
