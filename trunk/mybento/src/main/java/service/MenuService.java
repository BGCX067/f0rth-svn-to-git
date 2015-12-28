package service;

import java.util.Date;
import java.util.List;

import model.Menu;
import model.MenuItem;
import model.Provider;

import org.springframework.transaction.annotation.Transactional;

import dao.MenuDao;
import dao.MenuItemDao;
import dao.ProviderDao;
import dto.MenuData;
import dto.MenuItemData;

@Transactional
public class MenuService {
	private MenuDao menuDao;

	private MenuItemDao menuItemDao;

	private ProviderDao providerDao;

	public void setMenuDao(MenuDao menuDao) {
		this.menuDao = menuDao;
	}

	public void setMenuItemDao(MenuItemDao menuItemDao) {
		this.menuItemDao = menuItemDao;
	}

	public void setProviderDao(ProviderDao providerDao) {
		this.providerDao = providerDao;
	}

	public Menu createMenu(Menu menu) {
		Date now = new Date();
		menu.setCreatedAt(now);
		menu.setUpdatedAt(now);
		menuDao.save(menu);
		return menu;
	}

	public void deleteMenu(Menu menu) {
		menuDao.delete(menu);
	}

	public MenuData getEditMenu(Long id) {
		Menu menu = menuDao.load(id);
		List<Provider> providers = providerDao.findAll();
		return new MenuData(menu, providers);
	}

	public void saveEditMenu(MenuData data) {
		// delete or update menu items
		for (MenuItemData a : data.getItems()) {
			if (a.isSelected()) {
				menuItemDao.delete(menuItemDao.load(a.getMenuItemId()));
			} else {
				MenuItem menuItem = menuItemDao.load(a.getMenuItemId());
				menuItem.setProductName(a.getProductName());
				menuItem.setProductPrice(a.getProductPrice());
			}
		}
		// update comment
		Menu menu = menuDao.load(data.getMenuId());
		menu.setComment(data.getComment());
		menu.setUpdatedAt(new Date());
		// add menu items
		if (data.getNewProvider() != null) {
			menu.addMenuItems(providerDao.findByName(data.getNewProvider()));
		}
	}
}
