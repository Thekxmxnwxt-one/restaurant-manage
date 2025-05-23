package com.example.cook.model;

import com.example.cook.enums.KitchenstationType;
import lombok.Data;

@Data
public class KitchenStationModel {
    private int id;
    private KitchenstationType name;
}
