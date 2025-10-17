package com.example.demo.services.interfaces;


import java.util.List;

import com.example.demo.dtos.request.TimeSlotRequest;
import com.example.demo.dtos.response.TimeSlotResponse;

public interface TimeSlotService {
    List<TimeSlotResponse> getAllTimeSlots();
    List<TimeSlotResponse> getTimeSlotsByComplex(Integer complexId);
    TimeSlotResponse getTimeSlotById(Integer id);
    TimeSlotResponse createTimeSlot(TimeSlotRequest request);
    TimeSlotResponse updateTimeSlot(Integer id, TimeSlotRequest request);
    void deleteTimeSlot(Integer id);
    TimeSlotResponse updateStatus(Integer id, String status);
}
