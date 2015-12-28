package web.page.menu;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import model.MealType;
import model.Menu;

import org.apache.wicket.PageParameters;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import service.MenuService;
import web.RequireAdmin;
import web.page.BasePage;

@RequireAdmin
public class NewMenuPage extends BasePage {
	@SpringBean
	MenuService menuService;

	private static final String LUNCH = "午餐";

	private static final String DINNER = "加班晚餐";

	private static final List<String> MENU_TYPES = Arrays.asList(new String[] {LUNCH, DINNER}); 

	public NewMenuPage() {
		add(new MenuForm("menuForm"));
	}

	private class MenuForm extends Form {
		private Date date = new Date();

		private String menuType = LUNCH;

		private String menuComment = "";

		public MenuForm(String id) {
			super(id);
			add(DateTextField.forDatePattern("date", new PropertyModel(this, "date"), "yyyy-MM-dd").add(new DatePicker()));
			add(new DropDownChoice("menuType", new PropertyModel(this, "menuType"), MENU_TYPES));
			add(new TextArea("menuComment", new PropertyModel(this, "menuComment")));
		}

		@Override
		protected void onSubmit() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Menu menu = new Menu();
			if (LUNCH.equals(menuType)) {
				menu.setName(sdf.format(date) + " " + LUNCH);
				menu.setMealType(MealType.LUNCH);
			} else {
				menu.setName(sdf.format(date) + " " + DINNER);
				menu.setMealType(MealType.DINNER);
			}
			menu.setComment(menuComment);
			menuService.createMenu(menu);

			setResponsePage(EditMenuPage.class, new PageParameters("id=" + menu.getId()));
		}
	}
}
