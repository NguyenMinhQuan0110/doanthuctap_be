package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entites.District;
import com.example.demo.entites.Province;

public interface DistrictRepository extends JpaRepository<District, Integer>{
    boolean existsByDistrictName(String districtName);
    List<District> findByProvince(Province province);
}
