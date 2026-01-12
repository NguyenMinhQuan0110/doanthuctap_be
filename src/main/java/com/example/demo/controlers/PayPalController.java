package com.example.demo.controlers;

import com.example.demo.entites.Payment;
import com.example.demo.services.impl.PayPalService;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/paypal")
public class PayPalController {

    @Autowired
    private PayPalService payPalService;
    
    @Value("${allowed.origins}")
    private String frontendUrl;
    
    /**
     * Tạo PayPal payment và trả về approval URL
     */
    @PostMapping("/create/{paymentId}")
    public ResponseEntity<?> createPayment(@PathVariable("paymentId") Integer paymentId) {
        try {
            String approvalUrl = payPalService.createPayment(paymentId);
            Map<String, String> response = new HashMap<>();
            response.put("approvalUrl", approvalUrl);
            return ResponseEntity.ok(response);
        } catch (PayPalRESTException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create PayPal payment");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Xử lý khi user approved payment trên PayPal
     * Redirect về frontend page
     */
    @GetMapping("/success")
    public RedirectView paymentSuccess(HttpServletRequest request) {
        try {
            String paypalPaymentId = request.getParameter("paymentId");
            String payerId = request.getParameter("PayerID");
            String paymentIdStr = request.getParameter("payment_id");
            
            if (paypalPaymentId == null || payerId == null || paymentIdStr == null) {
                // Redirect về trang lỗi nếu thiếu param
                return new RedirectView(frontendUrl + "/payment/error?message=missing_params");
            }
            
            Integer paymentId = Integer.parseInt(paymentIdStr);
            Payment payment = payPalService.executePayment(paypalPaymentId, payerId);
            
            // Redirect về frontend với thông tin thành công
            String redirectUrl = frontendUrl + "/payment/success" +
                    "?payment_id=" + paymentId +
                    "&status=" + payment.getStatus() +
                    "&amount=" + payment.getAmount() +
                    "&booking_id=" + (payment.getBooking() != null ? payment.getBooking().getBookingId() : "");
            
            return new RedirectView(redirectUrl);
            
        } catch (NumberFormatException e) {
            return new RedirectView(frontendUrl + "/payment/error?message=invalid_format");
        } catch (PayPalRESTException e) {
            return new RedirectView(frontendUrl + "/payment/error?message=payment_failed&detail=" + e.getMessage());
        } catch (Exception e) {
            return new RedirectView(frontendUrl + "/payment/error?message=server_error");
        }
    }
    
    /**
     * Xử lý khi user cancelled payment trên PayPal
     */
    @GetMapping("/cancel")
    public RedirectView paymentCancel(HttpServletRequest request) {
        try {
            String paymentIdStr = request.getParameter("payment_id");
            if (paymentIdStr == null) {
                return new RedirectView(frontendUrl + "/payment/cancel");
            }
            
            Integer paymentId = Integer.parseInt(paymentIdStr);
            payPalService.cancelPayment(paymentId);
            
            // Redirect về frontend cancel page
            return new RedirectView(frontendUrl + "/payment/cancel?payment_id=" + paymentId);
            
        } catch (Exception e) {
            return new RedirectView(frontendUrl + "/payment/cancel");
        }
    }
}