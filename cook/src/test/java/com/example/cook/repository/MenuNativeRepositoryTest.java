package com.example.cook.repository;

import com.example.cook.model.MenuItemModel;

import java.util.List;

public class MenuNativeRepositoryTest implements MenuNativeRepository {
    @Override
    public List<MenuItemModel> findAllMenu() {
        return List.of();
    }

    @Override
    public MenuItemModel findMenuById(int menuID) {
        return null;
    }

    @Override
    public int insertMenu(List<MenuItemModel> menuItemModels) {
        return menuItemModels.size();
    }

    @Override
    public MenuItemModel updateMenu(MenuItemModel menuItemModel) {
        return null;
    }

    @Override
    public String deleteMenu(int id) {
        return "";
    }

    @Override
    public MenuItemModel findMenuByName(String name) {
        return null;
    }
}
