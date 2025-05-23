package com.example.cook.service;

import com.example.cook.exception.NotFoundException;
import com.example.cook.model.TablesModel;
import com.example.cook.repository.TableNativeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableService {

    private final TableNativeRepository tableNativeRepository;

    public TableService(TableNativeRepository tableNativeRepository) {
        this.tableNativeRepository = tableNativeRepository;
    }

    public List<TablesModel> getAllTable() {
        try {
            return tableNativeRepository.findAllTable();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all tables: " + e.getMessage());
        }
    }

    public TablesModel getTableByTableNumber(int tableNum) {
        try {
            TablesModel table = tableNativeRepository.findTableByTableNumber(tableNum);
            if (table == null) {
                throw new NotFoundException("Table number " + tableNum + " not found.");
            }
            return table;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get table by number: " + e.getMessage());
        }
    }

    public int insertTable(List<TablesModel> tablesModels) {
        try {
            return tableNativeRepository.insertTable(tablesModels);
        } catch (Exception e) {
            throw new RuntimeException("Failed to insert tables: " + e.getMessage());
        }
    }

    public TablesModel updateTable(TablesModel tablesModel) {
        try {
            TablesModel updated = tableNativeRepository.updateTable(tablesModel);
            if (updated == null) {
                throw new NotFoundException("Table not found or update failed.");
            }
            return updated;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update table: " + e.getMessage());
        }
    }

    public String deleteTable(int id) {
        try {
            return tableNativeRepository.deleteTable(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete table with id " + id + ": " + e.getMessage());
        }
    }
}
