package web.page.user;

import model.User;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import service.UserService;
import web.page.BasePage;

public class SignInPage extends BasePage {
	@SpringBean
	private UserService userService;

	public SignInPage() {
		add(new SignInForm("signInForm"));
	}

	class SignInForm extends Form {
		private String userName;

		private String password;

		public SignInForm(String id) {
			super(id);
			add(new TextField("userName", new PropertyModel(this, "userName")).setRequired(true));
			add(new PasswordTextField("password", new PropertyModel(this, "password")));
		}

		@Override
		protected void onSubmit() {
			User user = userService.authenticate(userName, password);
			if (user != null) {
				setCurrentUser(user);
				if (!continueToOriginalDestination()) {
					setResponsePage(getApplication().getHomePage());
				}
			} else {
				error(getLocalizer().getString(getId() + ".signInError", getWebPage(), "User name or password is incorrect."));
			}
		}
	}
}
