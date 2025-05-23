package com.example.cook.service;

import com.example.cook.exception.NotFoundException;
import com.example.cook.model.*;
import com.example.cook.repository.OrderNativeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderNativeRepository orderNativeRepository;

    public OrderService(OrderNativeRepository orderNativeRepository) {
        this.orderNativeRepository = orderNativeRepository;
    }

    public List<OrderModel> getAllOrder() {
        return orderNativeRepository.findAllOrder();
    }

    public OrderModel getOrderByOrderID(int orderId) {
        OrderModel order = orderNativeRepository.findOrderById(orderId);
        if (order == null) {
            throw new NotFoundException("Order id " + orderId + " not found.");
        }
        return order;
    }

    public int insertOrder(OrderModel orderModel) {
        return orderNativeRepository.insertOrder(orderModel);
    }

    public OrderModel updateOrder(OrderModel orderModel) {
        OrderModel updated = orderNativeRepository.updateOrder(orderModel);
        if (updated == null) {
            throw new NotFoundException("Order not found or update failed.");
        }
        return updated;
    }

    public String deleteOrder(int id) {
        return orderNativeRepository.deleteOrder(id);
    }

    public int insertOrderItem(OrderItemModel orderItemModel) {
        return orderNativeRepository.insertOrderItem(orderItemModel);
    }

    public OrderItemModel updateOrderItem(OrderItemModel orderItemModel) {
        OrderItemModel updated = orderNativeRepository.updateOrderItems(orderItemModel);
        if (updated == null) {
            throw new NotFoundException("Order item not found or update failed.");
        }
        return updated;
    }

    public String deleteOrderItem(int id) {
        return orderNativeRepository.deleteOrderItem(id);
    }

    public OrderItemModel updateOrderItemStatus(OrderItemModel orderItemModel) {
        OrderItemModel updated = orderNativeRepository.updateOrderItemsStatus(orderItemModel);
        if (updated == null) {
            throw new NotFoundException("Order item status not found or update failed.");
        }
        return updated;
    }
}
