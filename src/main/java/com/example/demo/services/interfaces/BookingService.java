package com.example.demo.services.interfaces;

import java.time.LocalDate;
import java.util.List;

import com.example.demo.dtos.request.BookingRequest;
import com.example.demo.dtos.response.BookingResponse;
import com.example.demo.dtos.response.TimeSlotResponse;
import com.example.demo.entites.enums.TargetType;

public interface BookingService {
    List<BookingResponse> getAllBookings();
    BookingResponse getBookingById(Integer id);
    List<BookingResponse> getBookingsByUser(Integer userId);
    BookingResponse createBooking(BookingRequest request);
    BookingResponse updateBooking(Integer id, BookingRequest request);
    void deleteBooking(Integer id);
    List<BookingResponse> getBookingsByComplex(Integer complexId);
    List<TimeSlotResponse> getAvailableTimeSlots(Integer complexId, TargetType targetType, Integer targetId, LocalDate bookingDate);
}
