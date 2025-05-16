package com.example.cook.service;

import com.example.cook.model.*;
import com.example.cook.repository.KitchenStationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KitchenStationService {
    private KitchenStationRepository kitchenStationRepository;

    public KitchenStationService(KitchenStationRepository kitchenStationRepository) {
        this.kitchenStationRepository = kitchenStationRepository;
    }

    public ResponseModel<List<KitchenStationModel>> getAllStation(){
        ResponseModel<List<KitchenStationModel>> result = new ResponseModel<>();

        result.setStatus(200);
        result.setDescription("success");

        try {
            List<KitchenStationModel> menu = this.kitchenStationRepository.findAllStations();
            result.setData(menu);
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<List<OrderItemModel>> getStationByName(String stationName){
        ResponseModel<List<OrderItemModel>> result = new ResponseModel<>();

        result.setStatus(200);
        result.setDescription("success");

        try {
            List<OrderItemModel> menu = this.kitchenStationRepository.findOrdersByStation(stationName);
            result.setData(menu);
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<Integer> insertLog(KitchenLogModel kitchenLogModel){
        ResponseModel<Integer> result = new ResponseModel<>();

        result.setStatus(201);
        result.setDescription("ok");

        try{
            int insertedRows = this.kitchenStationRepository.insertLog(kitchenLogModel);
            result.setData(insertedRows);
        } catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<KitchenLogModel> updateMenu(KitchenLogModel kitchenLogModel){
        ResponseModel<KitchenLogModel> result = new ResponseModel<>();

        result.setStatus(201);
        result.setDescription("ok");

        try {
            KitchenLogModel updated = this.kitchenStationRepository.updateLogStatus(kitchenLogModel);

            if (updated != null) {
                result.setStatus(200);
                result.setDescription("Update table status success");
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
