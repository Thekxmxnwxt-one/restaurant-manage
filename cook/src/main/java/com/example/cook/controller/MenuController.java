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
        return this.menuService.getAllMenu();
    }

    @GetMapping("/menu/{menuID}")
    public ResponseModel<MenuItemModel> getMenuByMenuId(@PathVariable int menuID){
        return this.menuService.getMenuByMenuID(menuID);
    }

    @PostMapping("/menu")
    public ResponseModel<Integer> insertMenu(@RequestBody List<MenuItemModel> menuItemModels){
        return this.menuService.insertMenu(menuItemModels);
    }

    @PutMapping("/menu")
    public ResponseModel<MenuItemModel> updateMenu(@RequestBody MenuItemModel menuItemModels) {
        return this.menuService.updateMenu(menuItemModels);
    }

    @DeleteMapping("menu/{menuID}")
    public ResponseModel<String> deleteMenu(@PathVariable int menuID){
        return this.menuService.deleteMenu(menuID);
    }

}
