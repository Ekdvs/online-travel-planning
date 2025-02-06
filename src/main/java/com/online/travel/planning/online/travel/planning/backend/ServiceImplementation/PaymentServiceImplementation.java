package com.online.travel.planning.online.travel.planning.backend.ServiceImplementation;

import com.online.travel.planning.online.travel.planning.backend.Model.Payment;
import com.online.travel.planning.online.travel.planning.backend.Repository.PaymentRepository;
import com.online.travel.planning.online.travel.planning.backend.Service.PaymentService;
import com.online.travel.planning.online.travel.planning.backend.Service.Email_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class PaymentServiceImplementation implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private Email_Service emailService;

    @Override
    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPaymentById(String paymentId) {
        return paymentRepository.findById(paymentId).orElse(null);
    }

    @Override
    public Payment updatePaymentStatus(String paymentId, String status) {
        Payment payment = paymentRepository.findById(paymentId).orElse(null);
        if (payment != null) {
            payment.setStatus(status);
            sendPaymentSuccessEmail(payment);
            return paymentRepository.save(payment);
        }
        return null;
    }

    public void sendPaymentSuccessEmail(Payment payment) {

           try {
               String userEmail = payment.getUserEmail();
               String subject = "Payment Confirmation";
                String message = "";
                        /*"<html>" +
                        "<head>" +
                        "<style>" +
                        "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f4f4f4; }" +
                       ".container { max-width: 800px; margin: auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15); }" +
                       ".header { background-color: #135bf2; color: white; padding: 15px; border-radius: 8px 8px 0 0; text-align: center; }" +
                        ".content { padding: 20px; font-size: 16px; color: #333; }" +
                       ".content p { line-height: 1.6; }" +
                        ".amount { font-weight: bold; color: #135bf2; }" +
                       ".table { width: 100%; border-collapse: collapse; margin-top: 20px; }" +
                        ".table td { padding: 8px; border: 1px solid #dddddd; text-align: left; }" +
                        ".footer { margin-top: 20px; padding-top: 15px; border-top: 1px solid #dddddd; text-align: center; font-size: 13px; color: #777; }" +
                       ".footer p { margin: 5px 0; }" +
                       "</style>" +
                       "</head>" +
                       "<body>" +
                        "<div class='container'>" +
                       "<div class='header'><h2>Payment Confirmation " + " for " + reservation.getPackgeId() + "</h2></div>" +
                        "<div class='content'>" +
                       "<p>Dear " + user.getFirstName() + " " + user.getLastName() + ",</p>" +
                      "<p>Thank you for choosing BkTicketing for your reservation. Below are your payment details:</p>" +

                       "<table class='table'>" +
                        "<tr><td><strong>Email Address:</strong></td><td>" + user.getUserEmail() + "</td></tr>" +
                       "<tr><td><strong>Event Name:</strong></td><td>" + packages.getPackageName() + "</td></tr>" +
                      "<tr><td><strong>Reservation Date:</strong></td><td>" + user.getDateRegistered() + "</td></tr>" +
                    "<tr><td><strong>Reservation Time:</strong></td><td>" + user.getTimeRegistered() + "</td></tr>" +
                        "<tr><td><strong>Event Date:</strong></td><td>" + packages.getDescription() + "</td></tr>" +
                       "<tr><td><strong>Event Time:</strong></td><td>" + packages.getPackageName() + "</td></tr>" +
                    "<tr><td><strong>Event Venue:</strong></td><td class='amount'>" + packages.getLocation() + "</td></tr>" +
                   "<tr><td><strong>Number of Tickets:</strong></td><td>" + reservation.getNumOfPerson() + "</td></tr>" +
                      "<tr><td><strong>Amount Paid:</strong></td><td class='amount'>Rs" + payment.getAmount() + ".0</td></tr>" +
                      "<tr><td><strong>Transaction Description:</strong></td><td>" + payment.getDescription() + "</td></tr>" +
                      "</table>" +

                   "<p>We are thrilled to have you on board. Enjoy the event!</p>" +
                        "<p>Warm regards, <strong class='amount'>BkTicketing<sup>LK</sup></strong></p>" +
                        "</div>" +

                      "<div class='footer'>" +
                      "<p>&copy; 2024 BkTicketing LK.  All rights reserved.</p>" +
                      "<p>If you have any questions, please contact us at support@bkticketing.lk</p>" +
                      "</div>" +
                       "</div>" +
                       "</body>" +
                       "</html>";*/

                emailService.sendEmail(userEmail, subject, message); // Ensure this method supports HTML content
             System.out.println("Email sent successfully to: " + userEmail);       
    } catch (Exception e) {
             System.out.println("Failed to send email: " + e.getMessage());
          }
       }
    }


