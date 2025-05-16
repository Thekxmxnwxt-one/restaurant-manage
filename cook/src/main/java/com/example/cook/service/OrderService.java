package com.example.cook.service;

import com.example.cook.entity.OrdersEntity;
import com.example.cook.model.*;
import com.example.cook.repository.OrderNativeRepository;
import org.apache.coyote.Response;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private OrderNativeRepository orderNativeRepository;

    public OrderService(OrderNativeRepository orderNativeRepository) {
        this.orderNativeRepository = orderNativeRepository;
    }

    public ResponseModel<List<OrderModel>> getAllOrder(){
        ResponseModel<List<OrderModel>> result = new ResponseModel<>();

        result.setStatus(200);
        result.setDescription("success");

        try {
            List<OrderModel> order = this.orderNativeRepository.findAllOrder();
            result.setData(order);
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }
    public ResponseModel<OrderModel> getOrderByOrderID(int orderId){
        ResponseModel<OrderModel> result = new ResponseModel<>();

        result.setStatus(200);
        result.setDescription("success");

        try {
            OrderModel order = this.orderNativeRepository.findOrderById(orderId);
            result.setData(order);
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<Integer> insertOrder(OrderModel orderModel){
        ResponseModel<Integer> result = new ResponseModel<>();

        result.setStatus(201);
        result.setDescription("ok");

        try{
            int insertedRows = this.orderNativeRepository.insertOrder(orderModel);
            result.setData(insertedRows);
        } catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<OrderModel> updateOrder(OrderModel orderModel){
        ResponseModel<OrderModel> result = new ResponseModel<>();

        try {
            OrderModel updated = this.orderNativeRepository.updateOrder(orderModel);

            if (updated != null) {
                result.setStatus(200);
                result.setDescription("Update order status success");
                result.setData(updated);
            } else {
                result.setStatus(404);
                result.setDescription("Table not found or update failed");
                result.setData(null);
            }
        } catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<String> deleteOrder(int id){
        ResponseModel<String> result = new ResponseModel<>();

        result.setStatus(200);
        result.setDescription("ok");

        try {
            String deleteRows = this.orderNativeRepository.deleteOrder(id);
            result.setData(deleteRows);
        } catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<Integer> insertOrderItem(OrderItemModel orderItemModel){
        ResponseModel<Integer> result = new ResponseModel<>();

        result.setStatus(201);
        result.setDescription("ok");

        try{
            int insertedRows = this.orderNativeRepository.insertOrderItem(orderItemModel);
            result.setData(insertedRows);
        } catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<OrderItemModel> updateOrderItem(OrderItemModel orderItemModel){
        ResponseModel<OrderItemModel> result = new ResponseModel<>();

        result.setStatus(201);
        result.setDescription("ok");

        try {
            OrderItemModel updated = this.orderNativeRepository.updateOrderItems(orderItemModel);

            if (updated != null) {
                result.setStatus(200);
                result.setDescription("Update order item success");
                result.setData(updated);
            } else {
                result.setStatus(404);
                result.setDescription("Table not found or update failed");
                result.setData(null);
            }
        } catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<String> deleteOrderItem(int id){
        ResponseModel<String> result = new ResponseModel<>();

        result.setStatus(200);
        result.setDescription("ok");

        try {
            String deleteRows = this.orderNativeRepository.deleteOrderItem(id);
            result.setData(deleteRows);
        } catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<OrderItemModel> updateOrderItemStatus(OrderItemModel orderItemModel){
        ResponseModel<OrderItemModel> result = new ResponseModel<>();

        result.setStatus(201);
        result.setDescription("ok");

        try {
            OrderItemModel updated = this.orderNativeRepository.updateOrderItemsStatus(orderItemModel);

            if (updated != null) {
                result.setStatus(200);
                result.setDescription("Update order item status success");
                result.setData(updated);
            } else {
                result.setStatus(404);
                result.setDescription("Table not found or update failed");
                result.setData(null);
            }
        } catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }
}
