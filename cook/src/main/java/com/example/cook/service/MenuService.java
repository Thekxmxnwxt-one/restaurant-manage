package com.example.cook.service;

import com.example.cook.model.MenuItemModel;
import com.example.cook.model.ResponseModel;
import com.example.cook.model.TablesModel;
import com.example.cook.repository.MenuNativeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {
    private MenuNativeRepository menuNativeRepository;

    public MenuService(MenuNativeRepository menuNativeRepository) {
        this.menuNativeRepository = menuNativeRepository;
    }

    public ResponseModel<List<MenuItemModel>> getAllMenu(){
        ResponseModel<List<MenuItemModel>> result = new ResponseModel<>();

        result.setStatus(200);
        result.setDescription("success");

        try {
            List<MenuItemModel> menu = this.menuNativeRepository.findAllMenu();
            result.setData(menu);
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<MenuItemModel> getMenuByMenuID(int menuID){
        ResponseModel<MenuItemModel> result = new ResponseModel<>();

        result.setStatus(200);
        result.setDescription("success");

        try {
            MenuItemModel menu = this.menuNativeRepository.findMenuById(menuID);
            result.setData(menu);
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<Integer> insertMenu(List<MenuItemModel> menuItemModels){
        ResponseModel<Integer> result = new ResponseModel<>();

        result.setStatus(201);
        result.setDescription("ok");

        try{
            int insertedRows = this.menuNativeRepository.insertMenu(menuItemModels);
            result.setData(insertedRows);
        } catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<MenuItemModel> updateMenu(MenuItemModel menuItemModel){
        ResponseModel<MenuItemModel> result = new ResponseModel<>();

        result.setStatus(201);
        result.setDescription("ok");

        try {
            MenuItemModel updated = this.menuNativeRepository.updateMenu(menuItemModel);

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

    public ResponseModel<String> deleteMenu(int id){
        ResponseModel<String> result = new ResponseModel<>();

        result.setStatus(201);
        result.setDescription("ok");

        try {
            String deleteRows = this.menuNativeRepository.deleteMenu(id);
            result.setData(deleteRows);
        } catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }
}
