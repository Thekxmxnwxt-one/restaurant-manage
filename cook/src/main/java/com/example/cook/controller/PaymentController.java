package com.example.cook.controller;

import com.example.cook.model.PaymentModel;
import com.example.cook.model.ResponseModel;
import com.example.cook.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/payment")
    public ResponseModel<List<PaymentModel>> getAllPayment() {
        List<PaymentModel> payments = paymentService.getAllPayments();
        ResponseModel<List<PaymentModel>> response = new ResponseModel<>();
        response.setStatus(200);
        response.setDescription("success");
        response.setData(payments);
        return response;
    }

    @GetMapping("/payment/{id}")
    public ResponseModel<PaymentModel> getPaymentById(@PathVariable int id) {
        PaymentModel payment = paymentService.getPaymentById(id);
        ResponseModel<PaymentModel> response = new ResponseModel<>();
        response.setStatus(200);
        response.setDescription("success");
        response.setData(payment);
        return response;
    }

    @GetMapping("/payment/detail/{id}")
    public ResponseModel<PaymentModel> getPaymentByIdDetail(@PathVariable int id) {
        PaymentModel payment = paymentService.getPaymentByIdDetail(id);
        ResponseModel<PaymentModel> response = new ResponseModel<>();
        response.setStatus(200);
        response.setDescription("success");
        response.setData(payment);
        return response;
    }

    @PostMapping("/payment")
    public ResponseModel<Integer> insertPayment(@RequestBody PaymentModel paymentModel) {
        int inserted = paymentService.insertPayment(paymentModel);
        ResponseModel<Integer> response = new ResponseModel<>();
        response.setStatus(201);
        response.setDescription("success");
        response.setData(inserted);
        return response;
    }
}
