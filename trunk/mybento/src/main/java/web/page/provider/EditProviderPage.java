package web.page.provider;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import service.ProviderService;
import web.RequireAdmin;
import web.page.BasePage;
import dto.ProductData;
import dto.ProviderData;

@RequireAdmin
public class EditProviderPage extends BasePage {
	@SpringBean
	private ProviderService providerService;

	private ProviderData data;

	public EditProviderPage(PageParameters parameters) {
		super(parameters);
		data = providerService.getProviderData(getPageParameters().getLong("id"));
		add(new EditProviderForm("providerForm"));
	}

	private class EditProviderForm extends Form {
		private String newProductName;

		private int newPrice;

		public EditProviderForm(String id) {
			super(id);
			add(new TextField("name", new PropertyModel(data, "name")));
			add(new TextField("phone", new PropertyModel(data, "phone")));
			add(new TextField("address", new PropertyModel(data, "address")));
			add(new ListView("products", data.getProducts()) {
				@Override
				protected void populateItem(ListItem item) {
					ProductData a = (ProductData)item.getModelObject();
					item.add(new CheckBox("selected", new PropertyModel(a, "selected")));
					item.add(new TextField("productName", new PropertyModel(a, "name")));
					item.add(new TextField("price", new PropertyModel(a, "price")));
				}
			});
			add(new TextField("newProductName", new PropertyModel(this, "newProductName")));
			add(new TextField("newPrice", new PropertyModel(this, "newPrice")));
		}

		@Override
		protected void onSubmit() {
			if (newProductName != null && newPrice > 0) {
				data.setNewProduct(new ProductData(newProductName, newPrice));
			}
			providerService.saveProviderData(data);
			setResponsePage(EditProviderPage.class, new PageParameters("id=" + data.getProviderId()));
		}
	}
}
