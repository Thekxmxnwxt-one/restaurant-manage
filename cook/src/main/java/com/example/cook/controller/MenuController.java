package com.example.cook.controller;

import com.example.cook.model.MenuItemModel;
import com.example.cook.model.ResponseModel;
import com.example.cook.model.TablesModel;
import com.example.cook.service.MenuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class MenuController {
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    public MenuService menuService;

    @GetMapping("/menu")
    public ResponseModel<List<MenuItemModel>> getAllMenu(){
        List<MenuItemModel>  menu = menuService.getAllMenu();
        ResponseModel<List<MenuItemModel>>  response = new ResponseModel<>();
        response.setStatus(200);
        response.setDescription("success");
        response.setData(menu);
        return response;
    }

    @GetMapping("/menu/{menuID}")
    public ResponseModel<MenuItemModel> getMenuByMenuId(@PathVariable int menuID){
        MenuItemModel menu = menuService.getMenuByMenuID(menuID);
        ResponseModel<MenuItemModel> response = new ResponseModel<>();
        response.setStatus(200);
        response.setDescription("success");
        response.setData(menu);
        return response;
    }

    @PostMapping("/manager/menu")
    public ResponseModel<Integer> insertMenu(@RequestBody List<MenuItemModel> menuItemModels){
        int menu = menuService.insertMenu(menuItemModels);
        ResponseModel<Integer> response = new ResponseModel<>();
        response.setStatus(201);
        response.setDescription("success");
        response.setData(menu);
        return response;
    }

    @PutMapping("/manager/menu")
    public ResponseModel<MenuItemModel> updateMenu(@RequestBody MenuItemModel menuItemModels) {
        MenuItemModel menu = menuService.updateMenu(menuItemModels);
        ResponseModel<MenuItemModel> response = new ResponseModel<>();
        response.setStatus(200);
        response.setDescription("success");
        response.setData(menu);
        return response;
    }

    @DeleteMapping("/manager/menu/{menuID}")
    public ResponseModel<String> deleteMenu(@PathVariable int menuID){
        String menu = menuService.deleteMenu(menuID);
        ResponseModel<String> response = new ResponseModel<>();
        response.setStatus(200);
        response.setDescription("success");
        response.setData(menu);
        return response;
    }

}
