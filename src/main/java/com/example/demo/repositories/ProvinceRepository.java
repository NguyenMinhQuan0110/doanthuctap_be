package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entites.Province;

public interface ProvinceRepository extends JpaRepository<Province, Integer>{
	boolean existsByProvinceName(String provinceName);
}
