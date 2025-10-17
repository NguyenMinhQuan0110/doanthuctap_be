package com.example.demo.controlers;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.request.ComplexRequest;
import com.example.demo.dtos.response.ComplexResponse;
import com.example.demo.dtos.response.ComplexResponseDistance;
import com.example.demo.entites.enums.PitchType;
import com.example.demo.services.interfaces.ComplexService;

@RestController
@RequestMapping("/api/complexes")
public class ComplexController {
    @Autowired
    private ComplexService complexService;
    @GetMapping
    public List<ComplexResponse> getAllComplexes() {
        return complexService.getAllComplexes();
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<ComplexResponse>> getActiveComplexes() {
        return ResponseEntity.ok(complexService.getActiveComplexes());
    }
    
    @GetMapping("/{id}")
    public ComplexResponse getComplexById(@PathVariable("id") Integer id) {
        return complexService.getComplexById(id);
    }
    
    @PreAuthorize("hasAnyRole('admin','owner')")
    @PostMapping("/create")
    public ComplexResponse createComplex(@RequestBody ComplexRequest request) {
        return complexService.createComplex(request);
    }
    
    @PreAuthorize("hasAnyRole('admin','owner')")
    @PutMapping("/update/{id}")
    public ComplexResponse updateComplex(@PathVariable("id") Integer id, @RequestBody ComplexRequest request) {
        return complexService.updateComplex(id, request);
    }

    @PreAuthorize("hasAnyRole('admin','owner')")
    @DeleteMapping("/delete/{id}")
    public void deleteComplex(@PathVariable("id") Integer id) {
        complexService.deleteComplex(id);
    }
    
    // ✅ Lấy cụm sân theo District ID
    @PreAuthorize("hasAnyRole('admin','owner')")
    @GetMapping("/district/{districtId}")
    public ResponseEntity<List<ComplexResponse>> getByDistrict(@PathVariable("districtId") Integer districtId) {
        return ResponseEntity.ok(complexService.getComplexesByDistrictId(districtId));
    }

    // ✅ Lấy cụm sân theo Province ID
    @PreAuthorize("hasAnyRole('admin','owner')")
    @GetMapping("/province/{provinceId}")
    public ResponseEntity<List<ComplexResponse>> getByProvince(@PathVariable("provinceId") Integer provinceId) {
        return ResponseEntity.ok(complexService.getComplexesByProvinceId(provinceId));
    }
    
    @GetMapping("/search")
    public List<ComplexResponse> searchComplexes(
            @RequestParam( name="provinceId",required = false) Integer provinceId,
            @RequestParam(name="districtId",required = false) Integer districtId,
            @RequestParam(name="pitchType",required = false) PitchType pitchType) {
        return complexService.searchComplexes(provinceId, districtId, pitchType);
    }
    
    @PreAuthorize("hasAnyRole('admin','owner')")
    @PutMapping("/{id}/status")
    public ResponseEntity<ComplexResponse> updateStatus(
            @PathVariable("id") Integer id,
            @RequestBody Map<String, String> requestBody) {

        String status = requestBody.get("status");
        return ResponseEntity.ok(complexService.updateStatus(id, status));
    }
    
    @PreAuthorize("hasAnyRole('admin','owner')")
    @GetMapping("/owner")
    public List<ComplexResponse> getComplexesByOwner(Authentication authentication) {
        return complexService.getComplexesByOwner(authentication);
    }

 // ✅ Lấy các cụm sân trong bán kính giới hạn quanh tọa độ nhập vào
    @GetMapping("/nearby")
    public ResponseEntity<List<ComplexResponseDistance>> getNearbyComplexes(
            @RequestParam("lat") double latitude,
            @RequestParam("lng") double longitude,
            @RequestParam("radius") double radiusKm) {
        return ResponseEntity.ok(complexService.findNearbyComplexes(latitude, longitude, radiusKm));
    }

}
