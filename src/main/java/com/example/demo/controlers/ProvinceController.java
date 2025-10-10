package com.example.demo.controlers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.request.ProvinceRequest;
import com.example.demo.dtos.response.ProvinceResponse;
import com.example.demo.services.interfaces.ProvinceService;

@RestController
@RequestMapping("/api/provinces")
public class ProvinceController {
    @Autowired
    private ProvinceService provinceService;
    
    @GetMapping
    public List<ProvinceResponse> getAllProvinces() {
        return provinceService.getAllProvinces();
    }
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/{id}")
    public ProvinceResponse getProvinceById(@PathVariable("id") Integer id) {
        return provinceService.getProvinceById(id);
    }
    @PreAuthorize("hasRole('admin')")
    @PostMapping("/create")
    public ProvinceResponse createProvince(@RequestBody ProvinceRequest request) {
        return provinceService.createProvince(request);
    }
    @PreAuthorize("hasRole('admin')")
    @PutMapping("update/{id}")
    public ProvinceResponse updateProvince(@PathVariable("id") Integer id, @RequestBody ProvinceRequest request) {
        return provinceService.updateProvince(id, request);
    }
    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("delete/{id}")
    public void deleteProvince(@PathVariable("id") Integer id) {
        provinceService.deleteProvince(id);
    }
}
