package com.example.demo.controlers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.request.ComplexRequest;
import com.example.demo.dtos.response.ComplexResponse;
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

    @GetMapping("/{id}")
    public ComplexResponse getComplexById(@PathVariable("id") Integer id) {
        return complexService.getComplexById(id);
    }

    @PostMapping("/create")
    public ComplexResponse createComplex(@RequestBody ComplexRequest request) {
        return complexService.createComplex(request);
    }

    @PutMapping("/update/{id}")
    public ComplexResponse updateComplex(@PathVariable("id") Integer id, @RequestBody ComplexRequest request) {
        return complexService.updateComplex(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteComplex(@PathVariable("id") Integer id) {
        complexService.deleteComplex(id);
    }
    // ✅ Lấy cụm sân theo District ID
    @GetMapping("/district/{districtId}")
    public ResponseEntity<List<ComplexResponse>> getByDistrict(@PathVariable("districtId") Integer districtId) {
        return ResponseEntity.ok(complexService.getComplexesByDistrictId(districtId));
    }

    // ✅ Lấy cụm sân theo Province ID
    @GetMapping("/province/{provinceId}")
    public ResponseEntity<List<ComplexResponse>> getByProvince(@PathVariable("provinceId") Integer provinceId) {
        return ResponseEntity.ok(complexService.getComplexesByProvinceId(provinceId));
    }
}
