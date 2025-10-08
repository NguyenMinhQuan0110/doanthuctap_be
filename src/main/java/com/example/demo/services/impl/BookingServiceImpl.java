package com.example.demo.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.request.BookingRequest;
import com.example.demo.dtos.response.BookingResponse;
import com.example.demo.entites.Booking;
import com.example.demo.entites.TimeSlot;
import com.example.demo.entites.User;
import com.example.demo.entites.enums.BookingStatus;
import com.example.demo.repositories.BookingRepository;
import com.example.demo.repositories.TimeSlotRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.interfaces.BookingService;

import jakarta.transaction.Transactional;
@Service
@Transactional
public class BookingServiceImpl implements BookingService{
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Override
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponse getBookingById(Integer id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        return mapToResponse(booking);
    }

    @Override
    public List<BookingResponse> getBookingsByUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return bookingRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponse createBooking(BookingRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        TimeSlot slot = timeSlotRepository.findById(request.getTimeSlotId())
                .orElseThrow(() -> new RuntimeException("TimeSlot not found"));

        // ✅ Kiểm tra trùng lịch
        if (bookingRepository.existsByTargetTypeAndTargetIdAndTimeSlotAndBookingDate(
                request.getTargetType(), request.getTargetId(), slot, request.getBookingDate())) {
            throw new RuntimeException("This target already has a booking in this time slot on that date");
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setTimeSlot(slot);
        booking.setTargetType(request.getTargetType());
        booking.setTargetId(request.getTargetId());
        booking.setBookingDate(request.getBookingDate());
        booking.setStatus(BookingStatus.pending);

        return mapToResponse(bookingRepository.save(booking));
    }

    @Override
    public BookingResponse updateBooking(Integer id, BookingRequest request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        TimeSlot slot = timeSlotRepository.findById(request.getTimeSlotId())
                .orElseThrow(() -> new RuntimeException("TimeSlot not found"));

        booking.setTargetType(request.getTargetType());
        booking.setTargetId(request.getTargetId());
        booking.setBookingDate(request.getBookingDate());
        booking.setTimeSlot(slot);

        return mapToResponse(bookingRepository.save(booking));
    }

    @Override
    public void deleteBooking(Integer id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        bookingRepository.delete(booking);
    }

    private BookingResponse mapToResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getBookingId());
        response.setTargetType(booking.getTargetType());
        response.setTargetId(booking.getTargetId());
        response.setBookingDate(booking.getBookingDate());
        response.setStatus(booking.getStatus());
        response.setCreatedAt(booking.getCreatedAt());
        response.setUpdatedAt(booking.getUpdatedAt());

        response.setUserId(booking.getUser().getUserId());
        response.setUserName(booking.getUser().getFullName());
        response.setTimeSlotId(booking.getTimeSlot().getTimeSlotId());
        response.setTimeSlotRange(booking.getTimeSlot().getStartTime() + " - " + booking.getTimeSlot().getEndTime());

        return response;
    }
    @Override
    public List<BookingResponse> getBookingsByComplex(Integer complexId) {
        return bookingRepository.findByComplexId(complexId).stream().map(this::mapToResponse).collect(Collectors.toList());
    }
}
