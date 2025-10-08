package com.example.demo.controlers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.request.BookingRequest;
import com.example.demo.dtos.response.BookingResponse;
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
}
