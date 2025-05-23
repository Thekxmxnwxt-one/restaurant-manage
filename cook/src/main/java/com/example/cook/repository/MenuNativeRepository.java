package com.example.cook.repository;

import com.example.cook.model.CustomersModel;
import com.example.cook.model.MenuItemModel;
import com.example.cook.model.TablesModel;

import java.util.List;

public interface MenuNativeRepository {
    public List<MenuItemModel> findAllMenu();
    public MenuItemModel findMenuById(int menuID);
    public int insertMenu(List<MenuItemModel> menuItemModels);
    public MenuItemModel updateMenu(MenuItemModel menuItemModel);
    public String deleteMenu(int id);
    public MenuItemModel findMenuByName(String name);

}
