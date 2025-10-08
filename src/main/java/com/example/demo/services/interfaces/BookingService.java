package com.example.demo.services.interfaces;

import java.util.List;

import com.example.demo.dtos.request.BookingRequest;
import com.example.demo.dtos.response.BookingResponse;

public interface BookingService {
    List<BookingResponse> getAllBookings();
    BookingResponse getBookingById(Integer id);
    List<BookingResponse> getBookingsByUser(Integer userId);
    BookingResponse createBooking(BookingRequest request);
    BookingResponse updateBooking(Integer id, BookingRequest request);
    void deleteBooking(Integer id);
    List<BookingResponse> getBookingsByComplex(Integer complexId);
}
