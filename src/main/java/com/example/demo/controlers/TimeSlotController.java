package com.example.demo.controlers;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.request.TimeSlotRequest;
import com.example.demo.dtos.response.TimeSlotResponse;
import com.example.demo.services.interfaces.TimeSlotService;

@RestController
@RequestMapping("/api/timeslots")
public class TimeSlotController {
    @Autowired
    private TimeSlotService timeSlotService;

    @GetMapping
    public List<TimeSlotResponse> getAllTimeSlots() {
        return timeSlotService.getAllTimeSlots();
    }

    @GetMapping("/{id}")
    public TimeSlotResponse getTimeSlotById(@PathVariable("id") Integer id) {
        return timeSlotService.getTimeSlotById(id);
    }

    @GetMapping("/complex/{complexId}")
    public List<TimeSlotResponse> getTimeSlotsByComplex(@PathVariable("complexId") Integer complexId) {
        return timeSlotService.getTimeSlotsByComplex(complexId);
    }
    
    @PreAuthorize("hasAnyRole('admin','owner')")
    @PostMapping("/create")
    public TimeSlotResponse createTimeSlot(@RequestBody TimeSlotRequest request) {
        return timeSlotService.createTimeSlot(request);
    }

    @PreAuthorize("hasAnyRole('admin','owner')")
    @PutMapping("/update/{id}")
    public TimeSlotResponse updateTimeSlot(@PathVariable("id") Integer id, @RequestBody TimeSlotRequest request) {
        return timeSlotService.updateTimeSlot(id, request);
    }

    @PreAuthorize("hasAnyRole('admin')")
    @DeleteMapping("/delete/{id}")
    public void deleteTimeSlot(@PathVariable("id") Integer id) {
        timeSlotService.deleteTimeSlot(id);
    }
    
    @PreAuthorize("hasAnyRole('admin','owner')")
    @PutMapping("/{id}/status")
    public ResponseEntity<TimeSlotResponse> updateStatus(
            @PathVariable("id") Integer id,
            @RequestBody Map<String, String> requestBody) {

        String status = requestBody.get("status");
        return ResponseEntity.ok(timeSlotService.updateStatus(id, status));
    }
}
