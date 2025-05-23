package com.example.cook.controller;

import com.example.cook.model.*;
import com.example.cook.service.KitchenStationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class KitchenStationController {
    private KitchenStationService kitchenStationService;

    public KitchenStationController(KitchenStationService kitchenStationService) {
        this.kitchenStationService = kitchenStationService;
    }

    @GetMapping("/kitchen/stations")
    public ResponseModel<List<KitchenStationModel>> getAllStation(){
        List<KitchenStationModel> kitchen = kitchenStationService.getAllStation();
        ResponseModel<List<KitchenStationModel>> response = new ResponseModel<>();
        response.setStatus(200);
        response.setDescription("success");
        response.setData(kitchen);
        return response;
    }

    @GetMapping("/kitchen/stations/{stationName}")
    public ResponseModel<List<OrderItemModel>> getKitchenByStationName(@PathVariable String stationName){
        List<OrderItemModel> kitchen = kitchenStationService.getStationByName(stationName);
        ResponseModel<List<OrderItemModel>> response = new ResponseModel<>();
        response.setStatus(200);
        response.setDescription("success");
        response.setData(kitchen);
        return response;
    }

    @PostMapping("/kitchen/log")
    public ResponseModel<Integer> insertLog(@RequestBody KitchenLogModel kitchenLogModel){
        int kitchen = kitchenStationService.insertLog(kitchenLogModel);
        ResponseModel<Integer> response = new ResponseModel<>();
        response.setStatus(201);
        response.setDescription("success");
        response.setData(kitchen);
        return response;
    }

    @PatchMapping("/kitchen/log/status")
    public ResponseModel<KitchenLogModel> updateLogStatus(@RequestBody KitchenLogModel kitchenLogModel) {
        KitchenLogModel kitchen = kitchenStationService.updateKitchen(kitchenLogModel);
        ResponseModel<KitchenLogModel> response = new ResponseModel<>();
        response.setStatus(200);
        response.setDescription("success");
        response.setData(kitchen);
        return response;
    }
}
