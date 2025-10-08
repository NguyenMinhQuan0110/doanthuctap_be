package com.example.demo.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.request.PaymentRequest;
import com.example.demo.dtos.response.PaymentResponse;
import com.example.demo.entites.Booking;
import com.example.demo.entites.Payment;
import com.example.demo.repositories.BookingRepository;
import com.example.demo.repositories.PaymentRepository;
import com.example.demo.services.interfaces.PaymentService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService{
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentResponse getPaymentById(Integer id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return mapToResponse(payment);
    }

    @Override
    public PaymentResponse createPayment(PaymentRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(request.getAmount());
        payment.setMethod(request.getMethod());
        payment.setStatus(request.getStatus());
        if (request.getStatus() != null && request.getStatus().toString().equalsIgnoreCase("paid")) {
            payment.setPaidAt(LocalDateTime.now());
        }

        return mapToResponse(paymentRepository.save(payment));
    }

    @Override
    public PaymentResponse updatePayment(Integer id, PaymentRequest request) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setAmount(request.getAmount());
        payment.setMethod(request.getMethod());
        payment.setStatus(request.getStatus());

        if (request.getStatus() != null && request.getStatus().toString().equalsIgnoreCase("paid")) {
            payment.setPaidAt(LocalDateTime.now());
        }

        return mapToResponse(paymentRepository.save(payment));
    }

    @Override
    public void deletePayment(Integer id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        paymentRepository.delete(payment);
    }

    private PaymentResponse mapToResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getPaymentId());
        response.setAmount(payment.getAmount());
        response.setMethod(payment.getMethod());
        response.setStatus(payment.getStatus());
        response.setPaidAt(payment.getPaidAt());
        response.setCreatedAt(payment.getCreatedAt());

        if (payment.getBooking() != null) {
            response.setBookingId(payment.getBooking().getBookingId());
            if (payment.getBooking().getUser() != null) {
                response.setUserId(payment.getBooking().getUser().getUserId());
                response.setUserName(payment.getBooking().getUser().getFullName());
            }
        }

        return response;
    }
}
