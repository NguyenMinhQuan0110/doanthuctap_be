package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entites.Complex;
import com.example.demo.entites.Pitch;

public interface PitchRepository extends JpaRepository<Pitch, Integer>{
    boolean existsByNameAndComplex(String name, Complex complex);
    List<Pitch> findByComplex_ComplexId(Integer complexId);
}
