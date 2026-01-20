package com.example.demo.services.interfaces;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.request.ComplexRequest;
import com.example.demo.dtos.response.ComplexResponse;
import com.example.demo.dtos.response.ComplexResponseDistance;
import com.example.demo.entites.enums.PitchType;

public interface ComplexService {
    List<ComplexResponse> getAllComplexes();
    ComplexResponse getComplexById(Integer id);
    ComplexResponse createComplex(ComplexRequest request);
    ComplexResponse updateComplex(Integer id, ComplexRequest request);
    void deleteComplex(Integer id);
    List<ComplexResponse> getComplexesByDistrictId(Integer districtId);
    List<ComplexResponse> getComplexesByProvinceId(Integer provinceId);
    List<ComplexResponse> searchComplexes(Integer provinceId, Integer districtId, PitchType pitchType); 
    ComplexResponse updateStatus(Integer id, String status);
    List<ComplexResponse> getComplexesByOwner(Authentication authentication);
    
    List<ComplexResponse> getActiveComplexes();
    List<ComplexResponseDistance> findNearbyComplexes(double latitude, double longitude, double radiusKm);
    ComplexResponse updateAvatarCom(Integer id, MultipartFile file);

}
