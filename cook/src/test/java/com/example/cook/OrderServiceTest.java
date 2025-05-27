package com.example.cook;

import com.example.cook.enums.*;
import com.example.cook.exception.NotFoundException;
import com.example.cook.model.OrderItemModel;
import com.example.cook.model.OrderModel;
import com.example.cook.repository.OrderNativeRepository;
import com.example.cook.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderNativeRepository orderNativeRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void test_get_all_order_expect_success() {
        when(orderNativeRepository.findAllOrder()).thenReturn(List.of(new OrderModel()));

        List<OrderModel> result = orderService.getAllOrder();

        Assertions.assertTrue(result.size()>0);
        verify(orderNativeRepository).findAllOrder();
    }

    @Test
    public void test_get_order_by_id_expect_success() {

        OrderModel orderModel = new OrderModel();
        orderModel.setId(1);

        when(orderNativeRepository.findOrderById(1)).thenReturn(orderModel);

        OrderModel result = orderService.getOrderByOrderID(1);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getId());
        verify(orderNativeRepository).findOrderById(1);
    }

    @Test
    public void test_get_order_by_id_expect_not_found() {

        when(orderNativeRepository.findOrderById(1)).thenReturn(null);

        NotFoundException ex = Assertions.assertThrows(NotFoundException.class,
                () -> orderService.getOrderByOrderID(1));

        Assertions.assertTrue(ex.getMessage().contains("Order id " + 1 + " not found"));
        verify(orderNativeRepository).findOrderById(1);
    }

    @Test
    public void test_insert_order_expect_success() {

        OrderModel orderModel = new OrderModel();
        orderModel.setCustomerId(1);
        orderModel.setTableId(1);
        orderModel.setEmployeeId(1);
        orderModel.setStatus(OrderStatus.processing);

        when(orderNativeRepository.insertOrder(orderModel)).thenReturn(1);

        int result = orderService.insertOrder(orderModel);

        Assertions.assertEquals(1, result);
        verify(orderNativeRepository).insertOrder(orderModel);
    }

    @Test
    public void test_update_status_order_expect_success(){

        OrderModel oldStatusOrder = new OrderModel();
        oldStatusOrder.setCustomerId(1);
        oldStatusOrder.setTableId(1);
        oldStatusOrder.setEmployeeId(1);
        oldStatusOrder.setStatus(OrderStatus.pending);

        OrderModel updateStatusOrder = new OrderModel();
        updateStatusOrder.setCustomerId(1);
        updateStatusOrder.setTableId(1);
        updateStatusOrder.setEmployeeId(1);
        updateStatusOrder.setStatus(OrderStatus.processing);

        when(orderNativeRepository.updateOrder(oldStatusOrder)).thenReturn(updateStatusOrder);

        OrderModel result = orderService.updateOrder(oldStatusOrder);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(updateStatusOrder.getId(), result.getId());
        Assertions.assertEquals(OrderStatus.processing, result.getStatus());
        verify(orderNativeRepository).updateOrder(oldStatusOrder);
    }

    @Test
    public void test_update_status_order_expect_not_found(){

        OrderModel updateStatusOrder = new OrderModel();
        updateStatusOrder.setCustomerId(1);
        updateStatusOrder.setTableId(1);
        updateStatusOrder.setEmployeeId(1);
        updateStatusOrder.setStatus(OrderStatus.processing);

        when(orderNativeRepository.updateOrder(updateStatusOrder)).thenReturn(null);

        NotFoundException ex = Assertions.assertThrows(NotFoundException.class,
                () -> orderService.updateOrder(updateStatusOrder));

        Assertions.assertTrue(ex.getMessage().contains("Order not found or update failed."));
        verify(orderNativeRepository).updateOrder(updateStatusOrder);
    }

    @Test
    public void test_delete_order_by_id_expect_success() {
        int orderId = 1;

        when(orderNativeRepository.deleteOrder(orderId)).thenReturn("Delete successful");

        String result = orderService.deleteOrder(orderId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Delete successful", result);
        verify(orderNativeRepository).deleteOrder(orderId);
    }

    @Test
    public void test_insert_order_item_expect_success() {

        OrderItemModel orderItemModel = new OrderItemModel();
        orderItemModel.setOrderId(1);
        orderItemModel.setUnitPrice(BigDecimal.valueOf(30.00));
        orderItemModel.setMenuItemId(1);
        orderItemModel.setQuantity(1);
        orderItemModel.setStatus(ItemStatus.pending);
        orderItemModel.setKitchenStation(KitchenstationType.SET);

        when(orderNativeRepository.insertOrderItem(orderItemModel)).thenReturn(1);

        int result = orderService.insertOrderItem(orderItemModel);

        Assertions.assertEquals(1, result);
        verify(orderNativeRepository).insertOrderItem(orderItemModel);
    }

    @Test
    public void test_update_order_item_expect_success(){

        OrderItemModel oldStatusOrderItem = new OrderItemModel();
        oldStatusOrderItem.setOrderId(1);
        oldStatusOrderItem.setUnitPrice(BigDecimal.valueOf(30.00));
        oldStatusOrderItem.setMenuItemId(1);
        oldStatusOrderItem.setQuantity(1);
        oldStatusOrderItem.setStatus(ItemStatus.pending);
        oldStatusOrderItem.setKitchenStation(KitchenstationType.SET);

        OrderItemModel updateStatusOrderItem = new OrderItemModel();
        updateStatusOrderItem.setOrderId(1);
        updateStatusOrderItem.setUnitPrice(BigDecimal.valueOf(30.00));
        updateStatusOrderItem.setMenuItemId(1);
        updateStatusOrderItem.setQuantity(3);
        updateStatusOrderItem.setStatus(ItemStatus.pending);
        updateStatusOrderItem.setKitchenStation(KitchenstationType.SET);

        when(orderNativeRepository.updateOrderItems(oldStatusOrderItem)).thenReturn(updateStatusOrderItem);

        OrderItemModel result = orderService.updateOrderItem(oldStatusOrderItem);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(updateStatusOrderItem.getId(), result.getId());
        verify(orderNativeRepository).updateOrderItems(oldStatusOrderItem);
    }

    @Test
    public void test_update_order_item_expect_not_found(){

        OrderItemModel updateStatusOrderItem = new OrderItemModel();
        updateStatusOrderItem.setOrderId(1);
        updateStatusOrderItem.setUnitPrice(BigDecimal.valueOf(30.00));
        updateStatusOrderItem.setMenuItemId(1);
        updateStatusOrderItem.setQuantity(3);
        updateStatusOrderItem.setStatus(ItemStatus.pending);
        updateStatusOrderItem.setKitchenStation(KitchenstationType.SET);

        when(orderNativeRepository.updateOrderItems(updateStatusOrderItem)).thenReturn(null);

        NotFoundException ex = Assertions.assertThrows(NotFoundException.class,
                () -> orderService.updateOrderItem(updateStatusOrderItem));

        Assertions.assertTrue(ex.getMessage().contains("Order item not found or update failed."));
        verify(orderNativeRepository).updateOrderItems(updateStatusOrderItem);
    }

    @Test
    public void test_delete_order_item_by_id_expect_success() {
        int orderItemId = 1;

        when(orderNativeRepository.deleteOrderItem(orderItemId)).thenReturn("Delete successful");

        String result = orderService.deleteOrderItem(orderItemId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Delete successful", result);
        verify(orderNativeRepository).deleteOrderItem(orderItemId);
    }

    @Test
    public void test_update_status_order_item_expect_success(){

        OrderItemModel oldStatusOrderItem = new OrderItemModel();
        oldStatusOrderItem.setOrderId(1);
        oldStatusOrderItem.setUnitPrice(BigDecimal.valueOf(30.00));
        oldStatusOrderItem.setMenuItemId(1);
        oldStatusOrderItem.setQuantity(1);
        oldStatusOrderItem.setStatus(ItemStatus.pending);
        oldStatusOrderItem.setKitchenStation(KitchenstationType.SET);

        OrderItemModel updateStatusOrderItem = new OrderItemModel();
        updateStatusOrderItem.setOrderId(1);
        updateStatusOrderItem.setUnitPrice(BigDecimal.valueOf(30.00));
        updateStatusOrderItem.setMenuItemId(1);
        updateStatusOrderItem.setQuantity(1);
        updateStatusOrderItem.setStatus(ItemStatus.cooking);
        updateStatusOrderItem.setKitchenStation(KitchenstationType.SET);

        when(orderNativeRepository.updateOrderItemsStatus(oldStatusOrderItem)).thenReturn(updateStatusOrderItem);

        OrderItemModel result = orderService.updateOrderItemStatus(oldStatusOrderItem);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(updateStatusOrderItem.getId(), result.getId());
        Assertions.assertEquals(ItemStatus.cooking, result.getStatus());
        verify(orderNativeRepository).updateOrderItemsStatus(oldStatusOrderItem);
    }

    @Test
    public void test_update_status_order_item_expect_not_found(){

        OrderItemModel updateStatusOrderItem = new OrderItemModel();
        updateStatusOrderItem.setOrderId(1);
        updateStatusOrderItem.setUnitPrice(BigDecimal.valueOf(30.00));
        updateStatusOrderItem.setMenuItemId(1);
        updateStatusOrderItem.setQuantity(1);
        updateStatusOrderItem.setStatus(ItemStatus.cooking);
        updateStatusOrderItem.setKitchenStation(KitchenstationType.SET);

        when(orderNativeRepository.updateOrderItemsStatus(updateStatusOrderItem)).thenReturn(null);

        NotFoundException ex = Assertions.assertThrows(NotFoundException.class,
                () -> orderService.updateOrderItemStatus(updateStatusOrderItem));

        Assertions.assertTrue(ex.getMessage().contains("Order item status not found or update failed."));
        verify(orderNativeRepository).updateOrderItemsStatus(updateStatusOrderItem);
    }
}
