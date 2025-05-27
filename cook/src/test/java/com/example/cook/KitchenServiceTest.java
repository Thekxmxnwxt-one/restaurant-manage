package com.example.cook;

import com.example.cook.enums.ItemStatus;
import com.example.cook.enums.KitchenstationType;
import com.example.cook.enums.LogStatus;
import com.example.cook.exception.BadRequestException;
import com.example.cook.exception.NotFoundException;
import com.example.cook.model.KitchenLogModel;
import com.example.cook.model.KitchenStationModel;
import com.example.cook.model.OrderItemModel;
import com.example.cook.repository.KitchenStationRepository;
import com.example.cook.service.KitchenStationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KitchenServiceTest {

    @Mock
    private KitchenStationRepository kitchenStationRepository;

    @InjectMocks
    private KitchenStationService kitchenStationService;

    @Test
    public void test_get_all_station_except_success() {
        when(kitchenStationRepository.findAllStations()).thenReturn(List.of(new KitchenStationModel()));

        List<KitchenStationModel> result = kitchenStationService.getAllStation();

        Assertions.assertTrue(result.size()>0);
        verify(kitchenStationRepository).findAllStations();
    }

    @Test
    public void test_get_station_by_name_except_success() {
        String stationName = "SET";

        OrderItemModel orderItemModel = new OrderItemModel();
        orderItemModel.setOrderId(1);
        orderItemModel.setUnitPrice(BigDecimal.valueOf(30.00));
        orderItemModel.setMenuItemId(1);
        orderItemModel.setQuantity(1);
        orderItemModel.setStatus(ItemStatus.pending);
        orderItemModel.setKitchenStation(KitchenstationType.SET);

        List<OrderItemModel> orderItemModels = List.of(orderItemModel);

        when(kitchenStationRepository.findOrdersByStation(stationName)).thenReturn(orderItemModels);

        List<OrderItemModel> result = kitchenStationService.getStationByName(stationName);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        verify(kitchenStationRepository).findOrdersByStation(stationName);
    }

    @Test
    public void test_get_station_by_name_expect_not_found() {
        String stationName = "SET";

        when(kitchenStationRepository.findOrdersByStation(stationName)).thenReturn(Collections.emptyList());

        NotFoundException ex = Assertions.assertThrows(NotFoundException.class, () -> {
            kitchenStationService.getStationByName(stationName);
        });

        Assertions.assertTrue(ex.getMessage().contains("No orders found for station: " + stationName));
        verify(kitchenStationRepository).findOrdersByStation(stationName);
    }

    @Test
    public void test_insert_kitchen_log_except_success(){
        KitchenLogModel kitchenLogModel = new KitchenLogModel();
        kitchenLogModel.setOrderItemId(1);
        kitchenLogModel.setStationId(1);
        kitchenLogModel.setStatus(LogStatus.cooking);

        when(kitchenStationRepository.insertLog(kitchenLogModel)).thenReturn(1);

        int result = kitchenStationService.insertLog(kitchenLogModel);

        Assertions.assertEquals(1, result);
        verify(kitchenStationRepository).insertLog(kitchenLogModel);
    }

    @Test
    public void test_insert_kitchen_log_except_bad_request() {
        KitchenLogModel kitchenLogModel = new KitchenLogModel();
        kitchenLogModel.setOrderItemId(1);
        kitchenLogModel.setStationId(1);
        kitchenLogModel.setStatus(LogStatus.cooking);

        when(kitchenStationRepository.insertLog(kitchenLogModel)).thenReturn(0);

        BadRequestException ex = Assertions.assertThrows(BadRequestException.class, () -> {
            kitchenStationService.insertLog(kitchenLogModel);
        });

        Assertions.assertTrue(ex.getMessage().contains("Insert failed for log"));
        verify(kitchenStationRepository).insertLog(kitchenLogModel);
    }

    @Test
    public void test_update_status_log_expect_success(){

        KitchenLogModel oldKitchenLog = new KitchenLogModel();
        oldKitchenLog.setOrderItemId(1);
        oldKitchenLog.setStationId(1);
        oldKitchenLog.setStatus(LogStatus.cooking);

        KitchenLogModel updateKitchenLog = new KitchenLogModel();
        updateKitchenLog.setOrderItemId(1);
        updateKitchenLog.setStationId(1);
        updateKitchenLog.setStatus(LogStatus.done);

        when(kitchenStationRepository.updateLogStatus(oldKitchenLog)).thenReturn(updateKitchenLog);

        KitchenLogModel result = kitchenStationService.updateKitchen(oldKitchenLog);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(updateKitchenLog.getId(), result.getId());
        Assertions.assertEquals(LogStatus.done, result.getStatus());
        verify(kitchenStationRepository).updateLogStatus(oldKitchenLog);
    }

    @Test
    public void test_update_status_log_expect_not_found(){

        KitchenLogModel updateKitchenLog = new KitchenLogModel();
        updateKitchenLog.setOrderItemId(1);
        updateKitchenLog.setStationId(1);
        updateKitchenLog.setStatus(LogStatus.done);

        when(kitchenStationRepository.updateLogStatus(updateKitchenLog)).thenReturn(null);

        NotFoundException ex = Assertions.assertThrows(NotFoundException.class,
                () -> kitchenStationService.updateKitchen(updateKitchenLog));

        Assertions.assertTrue(ex.getMessage().contains("Log not found or update failed for:"));
        verify(kitchenStationRepository).updateLogStatus(updateKitchenLog);
    }

}
