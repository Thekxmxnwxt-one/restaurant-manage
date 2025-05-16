package com.example.cook.controller;

import com.example.cook.model.MenuItemModel;
import com.example.cook.model.OrderModel;
import com.example.cook.model.PaymentModel;
import com.example.cook.model.ResponseModel;
import com.example.cook.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {
    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/payment")
    public ResponseModel<List<PaymentModel>> getAllPayment(){
        return this.paymentService.getAllPayments();
    }

    @GetMapping("/payment/{id}")
    public ResponseModel<PaymentModel> getPaymentById(@PathVariable int id){
        return this.paymentService.getPaymentById(id);
    }

    @GetMapping("/payment/detail/{id}")
    public ResponseModel<PaymentModel> getPaymentByIdDetail(@PathVariable int id){
        return this.paymentService.getPaymentByIdDetail(id);
    }

    @PostMapping("/payment")
    public ResponseModel<Integer> insertPayment(@RequestBody PaymentModel paymentModel){
        return this.paymentService.insertPayment(paymentModel);
    }
}
