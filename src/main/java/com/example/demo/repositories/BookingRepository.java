package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entites.Booking;

public interface BookingRepository extends JpaRepository<Booking, Integer>{

}
