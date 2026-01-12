package com.example.demo.services.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.entites.Booking;
import com.example.demo.entites.Payment;
import com.example.demo.entites.enums.BookingStatus;
import com.example.demo.entites.enums.PaymentMethod;
import com.example.demo.entites.enums.PaymentStatus;
import com.example.demo.repositories.BookingRepository;
import com.example.demo.repositories.PaymentRepository;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PayPalService {
	@Autowired
    private APIContext apiContext;
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Value("${paypal.return.url}")
    private String returnUrl;
    
    @Value("${paypal.cancel.url}")
    private String cancelUrl;

    /**
     * Tạo PayPal payment và trả về approval URL
     */
    public String createPayment(Integer paymentId) throws PayPalRESTException {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        
        // Kiểm tra xem payment đã được thanh toán chưa
        if (payment.getStatus() == PaymentStatus.paid) {
            throw new RuntimeException("Payment already completed");
        }
        
        // Tạo PayPal Payment object
        com.paypal.api.payments.Payment paypalPayment = new com.paypal.api.payments.Payment();
        paypalPayment.setIntent("sale");
        
        // Payer
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        paypalPayment.setPayer(payer);
        
        // Amount (chuyển từ VND sang USD, tỷ giá giả định 1 USD = 23,000 VND)
        Amount amount = new Amount();
        amount.setCurrency("USD");
        BigDecimal amountInUSD = payment.getAmount()
                .divide(new BigDecimal("23000"), 2, BigDecimal.ROUND_HALF_UP);
        amount.setTotal(String.format(Locale.US, "%.2f", amountInUSD));
        
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription("Payment for Booking #" + payment.getBooking().getBookingId());
        
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        paypalPayment.setTransactions(transactions);
        
        // Redirect URLs
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl + "?payment_id=" + paymentId);
        redirectUrls.setReturnUrl(returnUrl + "?payment_id=" + paymentId);
        paypalPayment.setRedirectUrls(redirectUrls);
        
        // Create payment in PayPal
        com.paypal.api.payments.Payment createdPayment = paypalPayment.create(apiContext);
        
        // Lưu PayPal transaction ID
        payment.setTransactionId(createdPayment.getId());
        paymentRepository.save(payment);
        
        // Tìm approval URL
        for (Links link : createdPayment.getLinks()) {
            if (link.getRel().equalsIgnoreCase("approval_url")) {
                return link.getHref();
            }
        }
        
        throw new RuntimeException("No approval URL found");
    }
    
    /**
     * Thực thi payment sau khi user approved trên PayPal
     */
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        // Lấy payment từ PayPal
        com.paypal.api.payments.Payment payment = new com.paypal.api.payments.Payment();
        payment.setId(paymentId);
        
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        
        // Execute payment
        com.paypal.api.payments.Payment executedPayment = payment.execute(apiContext, paymentExecution);
        
        // Tìm payment của chúng ta trong DB
        Payment myPayment = paymentRepository.findByTransactionId(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with transaction ID: " + paymentId));
        
        if (executedPayment.getState().equalsIgnoreCase("approved")) {
            // Cập nhật trạng thái payment
            myPayment.setStatus(PaymentStatus.paid);
            myPayment.setPaidAt(LocalDateTime.now());
            myPayment.setMethod(PaymentMethod.paypal);
            
            // Cập nhật trạng thái booking
            Booking booking = myPayment.getBooking();
            booking.setStatus(BookingStatus.confirmed);
            booking.setUpdatedAt(LocalDateTime.now());
            bookingRepository.save(booking);
        } else {
            myPayment.setStatus(PaymentStatus.failed);
        }
        
        return paymentRepository.save(myPayment);
    }
    
    /**
     * Hủy payment
     */
    public Payment cancelPayment(Integer paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        
        payment.setStatus(PaymentStatus.cancelled);
        return paymentRepository.save(payment);
    }
}
