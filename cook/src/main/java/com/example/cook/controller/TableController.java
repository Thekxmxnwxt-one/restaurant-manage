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

    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping("/tables")
    public ResponseModel<List<TablesModel>> getAllTable() {
        List<TablesModel> tables = tableService.getAllTable();
        ResponseModel<List<TablesModel>> response = new ResponseModel<>();
        response.setStatus(200);
        response.setDescription("success");
        response.setData(tables);
        return response;
    }

    @GetMapping("/tables/{tableNumber}")
    public ResponseModel<TablesModel> getTableByTableNumber(@PathVariable int tableNumber) {
        TablesModel table = tableService.getTableByTableNumber(tableNumber);
        ResponseModel<TablesModel> response = new ResponseModel<>();
        response.setStatus(200);
        response.setDescription("success");
        response.setData(table);
        return response;

    }

    @PostMapping("/manager/tables")
    public ResponseModel<Integer> insertTable(@RequestBody List<TablesModel> tablesModels) {
        int inserted = tableService.insertTable(tablesModels);
        ResponseModel<Integer> response = new ResponseModel<>();
        response.setStatus(201);
        response.setDescription("success");
        response.setData(inserted);
        return response;
    }

    @PutMapping("/tables/status")
    public ResponseModel<TablesModel> updateTable(@RequestBody TablesModel tablesModel) {
        TablesModel updated = tableService.updateTable(tablesModel);
        ResponseModel<TablesModel> response = new ResponseModel<>();
        response.setStatus(200);
        response.setDescription("success");
        response.setData(updated);
        return response;
    }

    @DeleteMapping("/manager/tables/{tableId}")
    public ResponseModel<String> deleteMenu(@PathVariable int tableId) {
        String result = tableService.deleteTable(tableId);
        ResponseModel<String> response = new ResponseModel<>();
        response.setStatus(200);
        response.setDescription("success");
        response.setData(result);
        return response;
    }
}
