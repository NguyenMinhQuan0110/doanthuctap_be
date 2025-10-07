package com.example.demo.services.interfaces;

import java.util.List;

import com.example.demo.dtos.request.ProvinceRequest;
import com.example.demo.dtos.response.ProvinceResponse;

public interface ProvinceService {
    List<ProvinceResponse> getAllProvinces();
    ProvinceResponse getProvinceById(Integer id);
    ProvinceResponse createProvince(ProvinceRequest request);
    ProvinceResponse updateProvince(Integer id, ProvinceRequest request);
    void deleteProvince(Integer id);
}
