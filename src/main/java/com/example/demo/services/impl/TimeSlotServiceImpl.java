package com.example.demo.services.impl;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.request.TimeSlotRequest;
import com.example.demo.dtos.response.TimeSlotResponse;
import com.example.demo.entites.Complex;
import com.example.demo.entites.TimeSlot;
import com.example.demo.entites.enums.TimeSlotStatus;
import com.example.demo.repositories.ComplexRepository;

import com.example.demo.repositories.TimeSlotRepository;
import com.example.demo.services.interfaces.TimeSlotService;

import jakarta.transaction.Transactional;
@Service
@Transactional
public class TimeSlotServiceImpl implements TimeSlotService{
    @Autowired
    private TimeSlotRepository timeSlotRepository;

    
    @Autowired
    private ComplexRepository complexRepository;

    @Override
    public List<TimeSlotResponse> getAllTimeSlots() {
        return timeSlotRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TimeSlotResponse> getTimeSlotsByComplex(Integer complexId) {
        Complex complex = complexRepository.findById(complexId)
                .orElseThrow(() -> new RuntimeException("Complex not found"));

        return timeSlotRepository.findByComplex(complex)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TimeSlotResponse getTimeSlotById(Integer id) {
        TimeSlot timeSlot = timeSlotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TimeSlot not found"));
        return mapToResponse(timeSlot);
    }

    @Override
    public TimeSlotResponse createTimeSlot(TimeSlotRequest request) {
        Complex complex = complexRepository.findById(request.getComplexId())
                .orElseThrow(() -> new RuntimeException("Complex not found"));

        // ✅ Kiểm tra trùng khung giờ trong cùng cụm sân
        if (timeSlotRepository.existsByComplexAndStartTimeAndEndTime(complex, request.getStartTime(), request.getEndTime())) {
            throw new RuntimeException("TimeSlot already exists for this complex and time range");
        }

        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setStartTime(request.getStartTime());
        timeSlot.setEndTime(request.getEndTime());
        timeSlot.setPrice(request.getPrice());
        timeSlot.setComplex(complex);

        return mapToResponse(timeSlotRepository.save(timeSlot));
    }

    @Override
    public TimeSlotResponse updateTimeSlot(Integer id, TimeSlotRequest request) {
        TimeSlot timeSlot = timeSlotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TimeSlot not found"));

        Complex complex = complexRepository.findById(request.getComplexId())
                .orElseThrow(() -> new RuntimeException("Complex not found"));

        timeSlot.setStartTime(request.getStartTime());
        timeSlot.setEndTime(request.getEndTime());
        timeSlot.setPrice(request.getPrice());
        timeSlot.setComplex(complex);

        return mapToResponse(timeSlotRepository.save(timeSlot));
    }

    @Override
    public void deleteTimeSlot(Integer id) {
        TimeSlot timeSlot = timeSlotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TimeSlot not found"));
        timeSlotRepository.delete(timeSlot);
    }
    
    @Override
    public TimeSlotResponse updateStatus(Integer id, String status) {
        TimeSlot timeSlot = timeSlotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pitch not found"));

        try {
            TimeSlotStatus newStatus = TimeSlotStatus.valueOf(status.toLowerCase());
            timeSlot.setStatus(newStatus);
            return mapToResponse(timeSlotRepository.save(timeSlot));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status value: " + status);
        }
    }
    
    private TimeSlotResponse mapToResponse(TimeSlot timeSlot) {
        TimeSlotResponse response = new TimeSlotResponse();
        response.setId(timeSlot.getTimeSlotId());
        response.setStartTime(timeSlot.getStartTime());
        response.setEndTime(timeSlot.getEndTime());
        response.setPrice(timeSlot.getPrice());
        response.setStatus(timeSlot.getStatus());
        response.setComplexId(timeSlot.getComplex().getComplexId());
        response.setComplexName(timeSlot.getComplex().getName());
        return response;
    }
}
