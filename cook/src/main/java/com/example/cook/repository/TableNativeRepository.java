package com.example.cook.repository;

import com.example.cook.model.TablesModel;

import java.util.List;

public interface TableNativeRepository {
    public List<TablesModel> findAllTable();
    public TablesModel findTableByTableNumber(int tableNum);
    public int insertTable(List<TablesModel> tablesModels);
    public TablesModel updateTable(TablesModel tablesModel);
    public String deleteTable(int id);
}
