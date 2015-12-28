package bento.client;

import bento.client.service.BentoService;
import bento.client.service.BentoServiceAsync;
import bento.model.User;
import bento.service.AuthenticationException;
import bento.service.UserAlreadyExistException;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class BentoApp implements EntryPoint, HistoryListener {
	private BentoServiceAsync bentoService;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		bentoService = (BentoServiceAsync)GWT.create(BentoService.class);
		Registry.register("bentoService", bentoService);

		History.addHistoryListener(this);
		History.fireCurrentHistoryState();
	}

	public void onHistoryChanged(String historyToken) {
		if ("register".equals(historyToken)) {
			showRegisterView();
		} else if ("main".equals(historyToken)) {
			showMainView();
		} else {
			showLoginView();
		}
	}

	private void showLoginView() {
		final Label loginLabel = new Label("登入");
		final TextField<String> email = new TextField<String>();
		final TextField<String> password = new TextField<String>();
		final Grid grid = new Grid(3, 2);
		final Button loginButton = new Button("登入");
		final Hyperlink registerLink = new Hyperlink("註冊", "register");
		final HorizontalPanel panel = new HorizontalPanel();

		final AsyncCallback<User> callback = new AsyncCallback<User>() {
			public void onFailure(Throwable cause) {
				if (cause instanceof AuthenticationException) {
					email.focus();
					email.selectAll();
					Window.alert("登入失敗，請重試一次。");
				} else {
					Window.alert(cause.toString());
				}
			}

			public void onSuccess(User user) {
				Window.alert("登入成功: " + user.toString());
			}
		};

		final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (loginButton == ce.component) {
					bentoService.login(email.getValue(), password.getValue(), callback);
				}
			}
		};

		password.setPassword(true);

		loginButton.addSelectionListener(listener);

		grid.setWidget(0, 0, new Label("電子郵件"));
		grid.setWidget(0, 1, email);
		grid.setWidget(1, 0, new Label("密碼"));
		grid.setWidget(1, 1, password);
		panel.add(loginButton);
		panel.add(registerLink);
		panel.setSpacing(5);
		grid.setWidget(2, 1, panel);

		RootPanel.get().clear();
		RootPanel.get().add(loginLabel);
		RootPanel.get().add(grid);
	}

	private void showRegisterView() {
		final Label registerLabel = new Label("註冊");
		final TextField<String> email = new TextField<String>();
		final TextField<String> password = new TextField<String>();
		final TextField<String> passwordConfirmation = new TextField<String>();
		final Grid grid = new Grid(4, 2);
		final Button registerButton = new Button("註冊");
		final Hyperlink loginLink = new Hyperlink("登入", "login");
		final HorizontalPanel panel = new HorizontalPanel();

		final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable cause) {
				if (cause instanceof UserAlreadyExistException) {
					email.setValue("");
					email.focus();
					Window.alert("電子郵件已被註冊，請重新輸入。");
				} else {
					Window.alert(cause.toString());
				}
			}

			public void onSuccess(Void result) {
				Window.alert("註冊成功。");
				History.newItem("login");
			}
		};

		final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (!email.isValid()) {
					email.focus();
					Window.alert("請輸入電子郵件地址。");
				} else if (!password.getValue().equals(passwordConfirmation.getValue())) {
					passwordConfirmation.setValue("");
					passwordConfirmation.focus();
					Window.alert("密碼不符，請再輸入一次。");
				} else {
					bentoService.register(email.getValue(), password.getValue(), callback);
				}
			}
		};

		email.setAllowBlank(false);
		password.setPassword(true);
		password.setAllowBlank(false);
		passwordConfirmation.setPassword(true);
		passwordConfirmation.setAllowBlank(false);

		registerButton.addSelectionListener(listener);

		grid.setWidget(0, 0, new Label("電子郵件"));
		grid.setWidget(0, 1, email);
		grid.setWidget(1, 0, new Label("密碼"));
		grid.setWidget(1, 1, password);
		grid.setWidget(2, 0, new Label("確認密碼"));
		grid.setWidget(2, 1, passwordConfirmation);
		panel.add(registerButton);
		panel.add(loginLink);
		panel.setSpacing(5);
		grid.setWidget(3, 1, panel);

		RootPanel.get().clear();
		RootPanel.get().add(registerLabel);
		RootPanel.get().add(grid);

	}

	private void showMainView() {
		RootPanel.get().clear();
	}
}
