package com.example.cook.repository;

import com.example.cook.model.MenuItemModel;
import com.example.cook.model.OrderItemModel;
import com.example.cook.model.OrderModel;
import com.example.cook.model.TablesModel;

import java.util.List;

public interface OrderNativeRepository {
    public List<OrderModel> findAllOrder();
    public OrderModel findOrderById(int orderId);
    public int insertOrder(OrderModel orderModels);
    public OrderModel updateOrder(OrderModel orderModel);
    public String deleteOrder(int id);

    public int insertOrderItem(OrderItemModel orderItemModel);
    public OrderItemModel updateOrderItems(OrderItemModel orderItemModel);
    public String deleteOrderItem(int id);
    public OrderItemModel updateOrderItemsStatus(OrderItemModel orderItemModel);
}
