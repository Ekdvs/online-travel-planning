package com.online.travel.planning.online.travel.planning.backend.Service;

import com.online.travel.planning.online.travel.planning.backend.Model.Payment;



public interface PaymentService {
    Payment createPayment(Payment payment);
    Payment getPaymentById(String paymentId);
    Payment updatePaymentStatus(String paymentId, String status);
}
