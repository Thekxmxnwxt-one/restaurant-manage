package com.example.cook.service;

import com.example.cook.exception.NotFoundException;
import com.example.cook.model.PaymentModel;
import com.example.cook.model.ResponseModel;
import com.example.cook.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<PaymentModel> getAllPayments() {
        try {
            return paymentRepository.findAllPayments();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all payments: " + e.getMessage());
        }
    }

    public PaymentModel getPaymentById(int id){
        try {
            PaymentModel payment = paymentRepository.findPaymentById(id);
            if (payment == null) {
                throw new NotFoundException("Payment with id " + id + " not found.");
            }
            return payment;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get payment by id: " + e.getMessage());
        }
    }

    public PaymentModel getPaymentByIdDetail(int id){
        try {
            PaymentModel payment = paymentRepository.findPaymentByIdDetail(id);
            if (payment == null) {
                throw new NotFoundException("Payment detail with id " + id + " not found.");
            }
            return payment;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get payment detail by id: " + e.getMessage());
        }
    }

    public int insertPayment(PaymentModel paymentModel){
        try {
            return paymentRepository.insertPayment(paymentModel);
        } catch (Exception e) {
            throw new RuntimeException("Failed to insert payment: " + e.getMessage());
        }
    }
}
