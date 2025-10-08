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

import com.example.demo.dtos.request.PaymentRequest;
import com.example.demo.dtos.response.PaymentResponse;
import com.example.demo.services.interfaces.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public List<PaymentResponse> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    public PaymentResponse getPaymentById(@PathVariable("id") Integer id) {
        return paymentService.getPaymentById(id);
    }

    @PostMapping("/create")
    public PaymentResponse createPayment(@RequestBody PaymentRequest request) {
        return paymentService.createPayment(request);
    }

    @PutMapping("/update/{id}")
    public PaymentResponse updatePayment(@PathVariable("id") Integer id, @RequestBody PaymentRequest request) {
        return paymentService.updatePayment(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePayment(@PathVariable("id") Integer id) {
        paymentService.deletePayment(id);
    }
}
