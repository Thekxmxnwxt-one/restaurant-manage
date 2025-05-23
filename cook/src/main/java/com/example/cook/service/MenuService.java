package com.example.cook.service;

import com.example.cook.exception.AlreadyExistsException;
import com.example.cook.exception.NotFoundException;
import com.example.cook.model.MenuItemModel;
import com.example.cook.repository.MenuNativeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {
    private MenuNativeRepository menuNativeRepository;

    public MenuService(MenuNativeRepository menuNativeRepository) {
        this.menuNativeRepository = menuNativeRepository;
    }

    public List<MenuItemModel> getAllMenu() {
        return this.menuNativeRepository.findAllMenu();
    }

    public MenuItemModel getMenuByMenuID(int menuID) {
        MenuItemModel menu = this.menuNativeRepository.findMenuById(menuID);
        if (menu == null) {
            throw new NotFoundException("Menu item not found with id " + menuID);
        }
        return menu;
    }

    public int insertMenu(List<MenuItemModel> menuItemModels) {
        // ตัวอย่างเช็ค duplicate ก่อน insert
        for (MenuItemModel item : menuItemModels) {
            MenuItemModel existing = this.menuNativeRepository.findMenuByName(item.getName());
            if (existing != null) {
                throw new AlreadyExistsException("Menu item already exists with name: " + item.getName());
            }
        }
        return this.menuNativeRepository.insertMenu(menuItemModels);
    }

    public MenuItemModel updateMenu(MenuItemModel menuItemModel) {
        MenuItemModel updated = this.menuNativeRepository.updateMenu(menuItemModel);
        if (updated == null) {
            throw new NotFoundException("Menu item not found or update failed for id " + menuItemModel.getId());
        }
        return updated;
    }

    public String deleteMenu(int id) {
        String deleted = this.menuNativeRepository.deleteMenu(id);
        if (deleted == null || deleted.isEmpty()) {
            throw new NotFoundException("Menu item not found or delete failed for id " + id);
        }
        return deleted;
    }
}
