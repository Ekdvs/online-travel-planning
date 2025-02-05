package com.online.travel.planning.online.travel.planning.backend.Repository;


import com.online.travel.planning.online.travel.planning.backend.Model.Payment;

import org.springframework.data.mongodb.repository.MongoRepository;



public interface PaymentRepository extends MongoRepository<Payment, String> {

}
