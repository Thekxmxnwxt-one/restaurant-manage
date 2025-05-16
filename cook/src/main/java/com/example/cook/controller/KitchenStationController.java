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
        return this.kitchenStationService.getAllStation();
    }

    @GetMapping("/kitchen/stations/{stationName}")
    public ResponseModel<List<OrderItemModel>> getTableByTableNumber(@PathVariable String stationName){
        return this.kitchenStationService.getStationByName(stationName);
    }

    @PostMapping("/kitchen/log")
    public ResponseModel<Integer> insertLog(@RequestBody KitchenLogModel kitchenLogModel){
        return this.kitchenStationService.insertLog(kitchenLogModel);
    }

    @PatchMapping("/kitchen/log/status")
    public ResponseModel<KitchenLogModel> updateLogStatus(@RequestBody KitchenLogModel kitchenLogModel) {
        return this.kitchenStationService.updateMenu(kitchenLogModel);
    }
}
