package dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import model.Menu;
import model.MenuItem;
import model.Provider;

public class MenuData implements Serializable {
	private Long menuId;

	private String name;

	private String comment;

	private List<MenuItemData> items;

	private String newProvider;

	private List<String> availableProviders;

	public MenuData(Menu menu, List<Provider> providers) {
		menuId = menu.getId();
		name = menu.getName();
		comment = menu.getComment();
		initMenuItems(menu);
		initAvailableProviders(providers);
	}

	private void initMenuItems(Menu menu) {
		items = new ArrayList<MenuItemData>();
		for (MenuItem a : menu.getMenuItems()) {
			items.add(new MenuItemData(a));
		}
	}

	private void initAvailableProviders(List<Provider> providers) {
		availableProviders = new ArrayList<String>();
		for (Provider a : providers) {
			availableProviders.add(a.getName());
		}
	}

	public Long getMenuId() {
		return menuId;
	}

	public String getName() {
		return name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getNewProvider() {
		return newProvider;
	}

	public void setNewProvider(String newProvider) {
		this.newProvider = newProvider;
	}

	public List<MenuItemData> getItems() {
		return items;
	}

	public List<String> getAvailableProviders() {
		return availableProviders;
	}
}
