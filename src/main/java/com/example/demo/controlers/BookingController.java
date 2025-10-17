package com.example.demo.controlers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.request.BookingRequest;
import com.example.demo.dtos.response.BookingResponse;
import com.example.demo.dtos.response.TimeSlotResponse;
import com.example.demo.entites.enums.TargetType;
import com.example.demo.services.interfaces.BookingService;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @GetMapping
    public List<BookingResponse> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/{id}")
    public BookingResponse getBookingById(@PathVariable("id") Integer id) {
        return bookingService.getBookingById(id);
    }

    @GetMapping("/user/{userId}")
    public List<BookingResponse> getBookingsByUser(@PathVariable("userId") Integer userId) {
        return bookingService.getBookingsByUser(userId);
    }

    @PostMapping("/create")
    public BookingResponse createBooking(@RequestBody BookingRequest request) {
        return bookingService.createBooking(request);
    }

    @PutMapping("/update/{id}")
    public BookingResponse updateBooking(@PathVariable("id") Integer id, @RequestBody BookingRequest request) {
        return bookingService.updateBooking(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBooking(@PathVariable("id") Integer id) {
        bookingService.deleteBooking(id);
    }
    @GetMapping("/complex/{complexId}")
    public List<BookingResponse> getBookingsByComplex(@PathVariable("complexId") Integer complexId) {
        return bookingService.getBookingsByComplex(complexId);
    }
    
    @PutMapping("/cancel/{id}")
    public BookingResponse cancelBooking(@PathVariable("id") Integer id) {
        return bookingService.cancelBooking(id);
    }

    
    @GetMapping("/available-timeslots")
    public List<TimeSlotResponse> getAvailableTimeSlots(
            @RequestParam("complexId") Integer complexId,
            @RequestParam("targetType") TargetType targetType,
            @RequestParam("targetId") Integer targetId,
            @RequestParam("bookingDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate bookingDate) {
        return bookingService.getAvailableTimeSlots(complexId, targetType, targetId, bookingDate);
    }
}
