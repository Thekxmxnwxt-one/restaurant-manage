package com.example.cook;

import com.example.cook.controller.MenuController;
import com.example.cook.enums.MenuCategory;
import com.example.cook.model.MenuItemModel;
import com.example.cook.model.ResponseModel;
import com.example.cook.repository.MenuNativeRepository;
import com.example.cook.repository.MenuNativeRepositoryTest;
import com.example.cook.service.MenuService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class CookApplicationTests {

	@Test
	public void test_insertMenuItem_expect_ResponseModel_eq_200(){
		MenuNativeRepository menuNativeRepository = new MenuNativeRepositoryTest();
		MenuService menuService = new MenuService(menuNativeRepository);

		List<MenuItemModel> menuItemModels = new ArrayList<>();
		for (int i=0; i<9; i++){
			MenuItemModel x = new MenuItemModel();
			x.setName("Menu" +i);
			x.setImageUrl("http://image"+i+".com");
			x.setDescription("food is delicious");
			x.setPrice(BigDecimal.valueOf(30.00));
			x.setCategory(MenuCategory.SET);
			x.setAvailable(true);
			menuItemModels.add(x);
		}
		int result = menuService.insertMenu(menuItemModels);

		Assertions.assertEquals(9, result); // เพราะคุณใส่ 9 รายการ
	}

	@Test
	void contextLoads() {
	}

}
