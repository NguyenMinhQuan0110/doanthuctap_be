package com.example.demo.controlers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.request.DistrictRequest;
import com.example.demo.dtos.response.DistrictResponse;
import com.example.demo.services.interfaces.DistrictService;

@RestController
@RequestMapping("/api/districts")
public class DistrictController {
    @Autowired
    private DistrictService districtService;

    @GetMapping
    public List<DistrictResponse> getAllDistricts() {
        return districtService.getAllDistricts();
    }

    @GetMapping("/{id}")
    public DistrictResponse getDistrictById(@PathVariable("id") Integer id) {
        return districtService.getDistrictById(id);
    }

    @GetMapping("/province/{provinceId}")
    public List<DistrictResponse> getDistrictsByProvince(@PathVariable("provinceId") Integer provinceId) {
        return districtService.getDistrictsByProvince(provinceId);
    }

    @PostMapping("/create")
    public DistrictResponse createDistrict(@RequestBody DistrictRequest request) {
        return districtService.createDistrict(request);
    }

    @PutMapping("/update/{id}")
    public DistrictResponse updateDistrict(@PathVariable("id") Integer id, @RequestBody DistrictRequest request) {
        return districtService.updateDistrict(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteDistrict(@PathVariable("id") Integer id) {
        districtService.deleteDistrict(id);
    }
}
