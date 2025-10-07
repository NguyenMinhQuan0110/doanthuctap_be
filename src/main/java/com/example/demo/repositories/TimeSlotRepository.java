package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entites.Complex;
import com.example.demo.entites.TimeSlot;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Integer>{
    List<TimeSlot> findByComplex(Complex complex);
    boolean existsByComplexAndStartTimeAndEndTime(Complex complex, java.time.LocalTime startTime, java.time.LocalTime endTime);
}
