package com.example.demo.services.interfaces;

import java.util.List;

import com.example.demo.dtos.request.DistrictRequest;
import com.example.demo.dtos.response.DistrictResponse;

public interface DistrictService {
    List<DistrictResponse> getAllDistricts();
    List<DistrictResponse> getDistrictsByProvince(Integer provinceId);
    DistrictResponse getDistrictById(Integer id);
    DistrictResponse createDistrict(DistrictRequest request);
    DistrictResponse updateDistrict(Integer id, DistrictRequest request);
    void deleteDistrict(Integer id);
}
