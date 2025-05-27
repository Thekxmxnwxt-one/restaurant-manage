package com.example.cook;

import com.example.cook.enums.MenuCategory;
import com.example.cook.exception.AlreadyExistsException;
import com.example.cook.exception.NotFoundException;
import com.example.cook.model.MenuItemModel;
import com.example.cook.repository.MenuNativeRepository;
import com.example.cook.service.MenuService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private MenuNativeRepository menuNativeRepository;

    @InjectMocks
    private MenuService menuService;

    @Test
    public void test_get_all_menu_expect_success() {
        when(menuNativeRepository.findAllMenu()).thenReturn(List.of(new MenuItemModel()));

        List<MenuItemModel> result = menuService.getAllMenu();

        Assertions.assertTrue(result.size()>0);
        verify(menuNativeRepository).findAllMenu();
    }

    @Test
    public void test_get_menu_by_id_expect_success() {
        MenuItemModel menu = new MenuItemModel();
        menu.setId(1);

        when(menuNativeRepository.findMenuById(1)).thenReturn(menu);

        MenuItemModel result = menuService.getMenuByMenuID(1);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getId());
        verify(menuNativeRepository).findMenuById(1);
    }

    @Test
    public void test_get_menu_by_id_expect_not_found() {
        when(menuNativeRepository.findMenuById(1)).thenReturn(null);

        NotFoundException ex = Assertions.assertThrows(NotFoundException.class,
                () -> menuService.getMenuByMenuID(1));

        Assertions.assertTrue(ex.getMessage().contains("Menu item not found with id"));
        verify(menuNativeRepository).findMenuById(1);
    }

    @Test
    public void test_insert_menu_expect_success() {

        List<MenuItemModel> menuItems = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            MenuItemModel item = new MenuItemModel();
            item.setName("Menu" + i);
            item.setImageUrl("http://image" + i + ".com");
            item.setDescription("food is delicious");
            item.setPrice(BigDecimal.valueOf(30.00));
            item.setCategory(MenuCategory.SET);
            item.setAvailable(true);
            menuItems.add(item);
        }

        when(menuNativeRepository.insertMenu(menuItems)).thenReturn(2);

        int result = menuService.insertMenu(menuItems);

        Assertions.assertEquals(2, result);
        verify(menuNativeRepository).insertMenu(menuItems);
    }

    @Test
    public void test_insert_menu_expect_duplicate() {
        List<MenuItemModel> menuItems = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            MenuItemModel item = new MenuItemModel();
            item.setName("Pizza");
            item.setImageUrl("http://image" + i + ".com");
            item.setDescription("food is delicious");
            item.setPrice(BigDecimal.valueOf(30.00));
            item.setCategory(MenuCategory.SET);
            item.setAvailable(true);
            menuItems.add(item);
        }

        when(menuNativeRepository.findMenuByName("Pizza")).thenReturn(new MenuItemModel());

        AlreadyExistsException ex = Assertions.assertThrows(AlreadyExistsException.class,
                () -> menuService.insertMenu(menuItems));

        Assertions.assertTrue(ex.getMessage().contains("Menu item already exists with name: Pizza"));
        verify(menuNativeRepository).findMenuByName("Pizza");
        verify(menuNativeRepository, never()).insertMenu(any());
    }

    @Test
    public void test_update_menu_expect_success(){

        MenuItemModel item = new MenuItemModel();
        item.setName("Pizza");
        item.setImageUrl("http://image" + 1 + ".com");
        item.setDescription("food is delicious");
        item.setPrice(BigDecimal.valueOf(30.00));
        item.setCategory(MenuCategory.SET);
        item.setAvailable(true);

        MenuItemModel updateItem = new MenuItemModel();
        updateItem.setName("Pizza");
        updateItem.setImageUrl("http://image" + 1 + ".com");
        updateItem.setDescription("food is delicious");
        updateItem.setPrice(BigDecimal.valueOf(50.00));
        updateItem.setCategory(MenuCategory.SET);
        updateItem.setAvailable(true);

        when(menuNativeRepository.updateMenu(item)).thenReturn(updateItem);

        MenuItemModel result = menuService.updateMenu(item);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(updateItem.getId(), result.getId());
        verify(menuNativeRepository).updateMenu(item);
    }

    @Test
    public void test_update_menu_expect_not_found(){

        MenuItemModel updateItem = new MenuItemModel();
        updateItem.setName("Pizza");
        updateItem.setImageUrl("http://image" + 1 + ".com");
        updateItem.setDescription("food is delicious");
        updateItem.setPrice(BigDecimal.valueOf(50.00));
        updateItem.setCategory(MenuCategory.SET);
        updateItem.setAvailable(true);

        when(menuNativeRepository.updateMenu(updateItem)).thenReturn(null);

        NotFoundException ex = Assertions.assertThrows(NotFoundException.class,
                () -> menuService.updateMenu(updateItem));

        Assertions.assertTrue(ex.getMessage().contains("Menu item not found or update failed for id"));
        verify(menuNativeRepository).updateMenu(updateItem);
    }

    @Test
    public void test_delete_menu_by_id_expect_success() {
        int menuId = 1;

        when(menuNativeRepository.deleteMenu(menuId)).thenReturn("Delete successful");

        String result = menuService.deleteMenu(menuId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Delete successful", result);
        verify(menuNativeRepository).deleteMenu(menuId);
    }

}
