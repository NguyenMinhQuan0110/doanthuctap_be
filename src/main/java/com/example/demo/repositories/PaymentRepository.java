package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entites.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer>{
	Optional<Payment> findByTransactionId(String transactionId);
}
