package web.page.user;

import model.User;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import service.DuplicatedUserEmailException;
import service.DuplicatedUserNameException;
import service.UserService;
import web.page.BasePage;
import web.page.HomePage;

public class SignUpPage extends BasePage {
	@SpringBean
	private UserService userService;

	public SignUpPage() {
		add(new SignUpForm("signUpForm"));
	}

	class SignUpForm extends Form {
		private String name;

		private String email;

		private String password;

		@SuppressWarnings("unused")
		private String confirmPassword;

		public SignUpForm(String id) {
			super(id);
			PasswordTextField passwordField, confirmPasswordField;
			add(new TextField("name",  new PropertyModel(this, "name")).setRequired(true).add(new StringValidator.LengthBetweenValidator(2, 32)));
			add(new TextField("email", new PropertyModel(this, "email")).setRequired(true).add(EmailAddressValidator.getInstance()));
			add(passwordField        = new PasswordTextField("password",        new PropertyModel(this, "password")));
			add(confirmPasswordField = new PasswordTextField("confirmPassword", new PropertyModel(this, "confirmPassword")));
			add(new EqualPasswordInputValidator(passwordField, confirmPasswordField));
		}

		@Override
		protected void onSubmit() {
			User user = new User(name, email, password, User.ROLE_USER);
			try {
				userService.createUser(user);
				setResponsePage(HomePage.class);
			} catch (DuplicatedUserNameException e) {
				error(getLocalizer().getString(getId() + ".nameTaken", getWebPage(), "User's name is taken: " + name));
			} catch (DuplicatedUserEmailException e) {
				error(getLocalizer().getString(getId() + ".emailTaken", getWebPage(), "User's email is taken: " + email));
			}
		}
	}
}
