package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entites.TimeSlot;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Integer>{

}
