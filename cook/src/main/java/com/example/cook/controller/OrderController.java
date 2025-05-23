package com.example.cook.controller;

import com.example.cook.model.*;
import com.example.cook.service.OrderService;
import org.hibernate.query.Order;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")//
    public ResponseModel<List<OrderModel>> getAllOrder(){
        List<OrderModel> order = orderService.getAllOrder();
        ResponseModel<List<OrderModel>> response = new ResponseModel<>();
        response.setStatus(200);
        response.setDescription("success");
        response.setData(order);
        return response;
    }

    @GetMapping("/orders/{orderId}")
    public ResponseModel<OrderModel> getOrderByOrderID(@PathVariable int orderId){
        OrderModel order = orderService.getOrderByOrderID(orderId);
        ResponseModel<OrderModel> response = new ResponseModel<>();
        response.setStatus(200);
        response.setDescription("success");
        response.setData(order);
        return response;
    }

    @PostMapping("/orders")
    public ResponseModel<Integer> insertOrder(@RequestBody OrderModel orderModel){
        int order = orderService.insertOrder(orderModel);
        ResponseModel<Integer> response = new ResponseModel<>();
        response.setStatus(201);
        response.setDescription("success");
        response.setData(order);
        return response;
    }

    @PutMapping("/orders/status")
    public ResponseModel<OrderModel> updateOrder(@RequestBody OrderModel orderModel) {
        OrderModel order = orderService.updateOrder(orderModel);
        ResponseModel<OrderModel> response = new ResponseModel<>();
        response.setStatus(200);
        response.setDescription("success");
        response.setData(order);
        return response;
    }

    @DeleteMapping("orders/{orderId}")
    public ResponseModel<String> deleteOrder(@PathVariable int orderId){
        String order = orderService.deleteOrder(orderId);
        ResponseModel<String> response = new ResponseModel<>();
        response.setStatus(200);
        response.setDescription("success");
        response.setData(order);
        return response;
    }

    @PostMapping("/orders/items")
    public ResponseModel<Integer> insertOrderItem(@RequestBody OrderItemModel orderModel){
        int order = orderService.insertOrderItem(orderModel);
        ResponseModel<Integer> response = new ResponseModel<>();
        response.setStatus(201);
        response.setDescription("success");
        response.setData(order);
        return response;
    }

    @PutMapping("/orders/items")
    public ResponseModel<OrderItemModel> updateOrderItem(@RequestBody OrderItemModel orderModel) {
        OrderItemModel order = orderService.updateOrderItem(orderModel);
        ResponseModel<OrderItemModel> response = new ResponseModel<>();
        response.setStatus(201);
        response.setDescription("success");
        response.setData(order);
        return response;
    }

    @DeleteMapping("orders/items/{orderId}")
    public ResponseModel<String> deleteOrderItem(@PathVariable int orderId){
        String order = orderService.deleteOrderItem(orderId);
        ResponseModel<String> response = new ResponseModel<>();
        response.setStatus(200);
        response.setDescription("success");
        response.setData(order);
        return response;
    }

    @PatchMapping("/orders/items")
    public ResponseModel<OrderItemModel> updateOrderItemStatus(@RequestBody OrderItemModel orderModel) {
        OrderItemModel order = orderService.updateOrderItemStatus(orderModel);
        ResponseModel<OrderItemModel> response = new ResponseModel<>();
        response.setStatus(200);
        response.setDescription("success");
        response.setData(order);
        return response;
    }
}
