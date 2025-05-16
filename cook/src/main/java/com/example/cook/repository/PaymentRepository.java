package com.example.cook.repository;

import com.example.cook.model.PaymentModel;

import java.util.List;

public interface PaymentRepository {
    public List<PaymentModel> findAllPayments();
    public PaymentModel findPaymentById(int id);
    public PaymentModel findPaymentByIdDetail(int id);
    public int insertPayment(PaymentModel payment);
}
