package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entites.Complex;

public interface ComplexRepository extends JpaRepository<Complex, Integer>{
	boolean existsByName(String name);
	boolean existsByNameIgnoreCase(String name);
    // ✅ Lấy cụm sân theo District
    List<Complex> findByDistrict_DistrictId(Integer districtId);

    // ✅ Lấy cụm sân theo Province
    List<Complex> findByDistrict_Province_ProvinceId(Integer provinceId);
}
