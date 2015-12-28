package web.page.provider;

import model.Provider;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import service.ProviderService;
import web.RequireAdmin;
import web.page.BasePage;

@RequireAdmin
public class NewProviderPage extends BasePage {
	@SpringBean
	private ProviderService providerService;

	public NewProviderPage() {
		add(new NewProviderForm("providerForm"));
	}

	private class NewProviderForm extends Form {
		private String name;

		private String phone;

		public NewProviderForm(String id) {
			super(id);
			add(new TextField("name", new PropertyModel(this, "name")).setRequired(true));
			add(new TextField("phone", new PropertyModel(this, "phone")));
		}

		@Override
		protected void onSubmit() {
			Provider provider = new Provider();
			provider.setName(name);
			provider.setPhone(phone);
			providerService.createProvider(provider);

			setResponsePage(EditProviderPage.class, new PageParameters("id=" + provider.getId()));
		}
	}
}
