package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entites.Complex;
import com.example.demo.entites.PitchGroup;

public interface PitchGroupRepository extends JpaRepository<PitchGroup, Integer>{
    boolean existsByNameAndComplex(String name, Complex complex);
    List<PitchGroup> findByComplex_ComplexId(Integer complexId);
}
