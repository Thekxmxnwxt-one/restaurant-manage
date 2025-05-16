package com.example.cook.service;

import com.example.cook.model.MenuItemModel;
import com.example.cook.model.OrderModel;
import com.example.cook.model.PaymentModel;
import com.example.cook.model.ResponseModel;
import com.example.cook.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {
    private PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public ResponseModel<List<PaymentModel>> getAllPayments() {
        ResponseModel<List<PaymentModel>> response = new ResponseModel<>();
        try {
            List<PaymentModel> payments = paymentRepository.findAllPayments();
            response.setStatus(200);
            response.setDescription("OK");
            response.setData(payments);
        } catch (Exception e) {
            response.setStatus(500);
            response.setDescription("Error: " + e.getMessage());
        }
        return response;
    }

    public ResponseModel<PaymentModel> getPaymentById(int id){
        ResponseModel<PaymentModel> result = new ResponseModel<>();

        result.setStatus(200);
        result.setDescription("success");

        try {
            PaymentModel order = this.paymentRepository.findPaymentById(id);
            result.setData(order);
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<PaymentModel> getPaymentByIdDetail(int id){
        ResponseModel<PaymentModel> result = new ResponseModel<>();

        result.setStatus(200);
        result.setDescription("success");

        try {
            PaymentModel order = this.paymentRepository.findPaymentByIdDetail(id);
            result.setData(order);
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<Integer> insertPayment(PaymentModel paymentModel){
        ResponseModel<Integer> result = new ResponseModel<>();

        result.setStatus(201);
        result.setDescription("ok");

        try{
            int insertedRows = this.paymentRepository.insertPayment(paymentModel);
            result.setData(insertedRows);
        } catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }
}
