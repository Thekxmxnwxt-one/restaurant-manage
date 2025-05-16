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
        return this.orderService.getAllOrder();
    }

    @GetMapping("/orders/{orderId}")
    public ResponseModel<OrderModel> getOrderByOrderID(@PathVariable int orderId){
        return this.orderService.getOrderByOrderID(orderId);
    }

    @PostMapping("/orders")
    public ResponseModel<Integer> insertOrder(@RequestBody OrderModel orderModel){
        return this.orderService.insertOrder(orderModel);
    }

    @PutMapping("/orders/status")
    public ResponseModel<OrderModel> updateOrder(@RequestBody OrderModel orderModel) {
        return this.orderService.updateOrder(orderModel);
    }

    @DeleteMapping("orders/{orderId}")
    public ResponseModel<String> deleteOrder(@PathVariable int orderId){
        return this.orderService.deleteOrder(orderId);
    }

    @PostMapping("/orders/items")
    public ResponseModel<Integer> insertOrderItem(@RequestBody OrderItemModel orderModel){
        return this.orderService.insertOrderItem(orderModel);
    }

    @PutMapping("/orders/items")
    public ResponseModel<OrderItemModel> updateOrderItem(@RequestBody OrderItemModel orderModel) {
        return this.orderService.updateOrderItem(orderModel);
    }

    @DeleteMapping("orders/items/{orderId}")
    public ResponseModel<String> deleteOrderItem(@PathVariable int orderId){
        return this.orderService.deleteOrderItem(orderId);
    }

    @PatchMapping("/orders/items")
    public ResponseModel<OrderItemModel> updateOrderItemStatus(@RequestBody OrderItemModel orderModel) {
        return this.orderService.updateOrderItemStatus(orderModel);
    }
}
