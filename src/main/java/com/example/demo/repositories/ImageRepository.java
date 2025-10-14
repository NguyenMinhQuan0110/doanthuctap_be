package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entites.Image;

public interface ImageRepository extends JpaRepository<Image, Integer>{
	List<Image> findByComplex_ComplexId(Integer complexId);
}
