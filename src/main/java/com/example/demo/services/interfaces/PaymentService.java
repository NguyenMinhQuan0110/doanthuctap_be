package com.example.demo.services.interfaces;

import java.util.List;

import com.example.demo.dtos.request.PaymentRequest;
import com.example.demo.dtos.response.PaymentResponse;

public interface PaymentService  {
    List<PaymentResponse> getAllPayments();
    PaymentResponse getPaymentById(Integer id);
    PaymentResponse createPayment(PaymentRequest request);
    PaymentResponse updatePayment(Integer id, PaymentRequest request);
    void deletePayment(Integer id);
}
