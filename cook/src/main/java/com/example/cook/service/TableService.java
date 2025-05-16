package com.example.cook.service;

import com.example.cook.model.EmployeesModel;
import com.example.cook.model.ResponseModel;
import com.example.cook.model.TablesModel;
import com.example.cook.model.UsersModel;
import com.example.cook.repository.TableNativeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableService {
    private TableNativeRepository tableNativeRepository;

    public TableService(TableNativeRepository tableNativeRepository) {
        this.tableNativeRepository = tableNativeRepository;
    }

    public ResponseModel<List<TablesModel>> getAllTable(){
        ResponseModel<List<TablesModel>> result = new ResponseModel<>();

        result.setStatus(200);
        result.setDescription("success");

        try {
            List<TablesModel> tables = this.tableNativeRepository.findAllTable();
            result.setData(tables);
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<TablesModel> getTableByTableNumber(int tableNum){
        ResponseModel<TablesModel> result = new ResponseModel<>();

        result.setStatus(200);
        result.setDescription("success");

        try {
            TablesModel tables = this.tableNativeRepository.findTableByTableNumber(tableNum);
            result.setData(tables);
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<Integer> insertTable(List<TablesModel> tablesModels){
        ResponseModel<Integer> result = new ResponseModel<>();

        result.setStatus(201);
        result.setDescription("ok");

        try{
            int insertedRows = this.tableNativeRepository.insertTable(tablesModels);
            result.setData(insertedRows);
        } catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<TablesModel> updateTable(TablesModel tablesModel){
        ResponseModel<TablesModel> result = new ResponseModel<>();

        result.setStatus(201);
        result.setDescription("ok");

        try {
            TablesModel updated = this.tableNativeRepository.updateTable(tablesModel);

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

    public ResponseModel<String> deleteTable(int id){
        ResponseModel<String> result = new ResponseModel<>();

        result.setStatus(201);
        result.setDescription("ok");

        try {
            String deleteRows = this.tableNativeRepository.deleteTable(id);
            result.setData(deleteRows);
        } catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }
}
