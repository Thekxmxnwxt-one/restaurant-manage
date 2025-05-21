package com.example.cook.controller;

import com.example.cook.model.ResponseModel;
import com.example.cook.model.TablesModel;
import com.example.cook.service.TableService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class TableController {
    private TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping("/tables")
    public ResponseModel<List<TablesModel>> getAllTable(){
        return this.tableService.getAllTable();
    }

    @GetMapping("/tables/{tableNumber}")
    public ResponseModel<TablesModel> getTableByTableNumber(@PathVariable int tableNumber){
        return this.tableService.getTableByTableNumber(tableNumber);
    }

    @PostMapping("/manager/tables")
    public ResponseModel<Integer> insertTable(@RequestBody List<TablesModel> tablesModels){
        return this.tableService.insertTable(tablesModels);
    }

    @PutMapping("/tables/status")
    public ResponseModel<TablesModel> updateTable(@RequestBody TablesModel tablesModel) {
        return this.tableService.updateTable(tablesModel);
    }

    @DeleteMapping("/manager/tables/{tableId}")
    public ResponseModel<String> deleteMenu(@PathVariable int tableId){
        return this.tableService.deleteTable(tableId);
    }

}
