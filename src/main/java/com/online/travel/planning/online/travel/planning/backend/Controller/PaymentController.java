package com.online.travel.planning.online.travel.planning.backend.Controller;

import com.online.travel.planning.online.travel.planning.backend.Model.Payment;
import com.online.travel.planning.online.travel.planning.backend.Service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Create a new payment
    @PostMapping("/create")
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        try {
            Payment newPayment = paymentService.createPayment(payment);
            return ResponseEntity.ok(newPayment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Get payment by ID
    @GetMapping("/get/{paymentId}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable String paymentId) {
        Payment payment = paymentService.getPaymentById(paymentId);
        if (payment != null) {
            return ResponseEntity.ok(payment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Update payment status
    @PutMapping("/update/{paymentId}")
    public ResponseEntity<Payment> updatePaymentStatus(
            @PathVariable String paymentId, @RequestParam String status) {
        Payment updatedPayment = paymentService.updatePaymentStatus(paymentId, status);
        if (updatedPayment != null) {
            return ResponseEntity.ok(updatedPayment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}