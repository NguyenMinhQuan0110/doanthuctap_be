package com.example.demo.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.request.ProvinceRequest;
import com.example.demo.dtos.response.ProvinceResponse;
import com.example.demo.entites.Province;
import com.example.demo.repositories.ProvinceRepository;
import com.example.demo.services.interfaces.ProvinceService;

import jakarta.transaction.Transactional;
@Service
public class ProvinceServiceImpl implements ProvinceService{
	@Autowired
    private ProvinceRepository provinceRepository;

    @Override
    public List<ProvinceResponse> getAllProvinces() {
        return provinceRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProvinceResponse getProvinceById(Integer id) {
        Province province = provinceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Province not found"));
        return mapToResponse(province);
    }

    @Override
    @Transactional
    public ProvinceResponse createProvince(ProvinceRequest request) {
        if (provinceRepository.existsByProvinceName(request.getProvinceName())) {
            throw new RuntimeException("Province name already exists");
        }

        Province province = new Province();
        province.setProvinceName(request.getProvinceName());

        return mapToResponse(provinceRepository.save(province));
    }

    @Override
    @Transactional
    public ProvinceResponse updateProvince(Integer id, ProvinceRequest request) {
        Province province = provinceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Province not found"));

        province.setProvinceName(request.getProvinceName());

        return mapToResponse(provinceRepository.save(province));
    }

    @Override
    @Transactional
    public void deleteProvince(Integer id) {
        Province province = provinceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Province not found"));
        provinceRepository.delete(province);
    }

    private ProvinceResponse mapToResponse(Province province) {
        return new ProvinceResponse(
                province.getProvinceId(),
                province.getProvinceName()
        );
    }
}
