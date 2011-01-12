package edu.wustl.keggproject.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Label;

public class LoginFunctionPanel {
	public SimplePanel loginFucntionPanel = new SimplePanel();

	public Widget getLoginFunctionPanel() {
		return loginFucntionPanel;
	}

	public void ChangeToLogin(final Anchor a) {
		final FormPanel loginForm = new FormPanel();
		loginForm.setAction("http://128.252.160.238:8000/user/login/");
		loginForm.setMethod(FormPanel.METHOD_POST);

		final Grid grid = new Grid(3, 2);
		loginForm.setWidget(grid);

		Label l = new Label("User name: ");
		Label p = new Label("Password: ");
		TextBox ubox = new TextBox();
		ubox.setName("username");
		PasswordTextBox pbox = new PasswordTextBox();
		pbox.setName("password");
		Button submit = new Button("Login");
		grid.setWidget(0, 0, l);
		grid.setWidget(0, 1, ubox);
		grid.setWidget(1, 0, p);
		grid.setWidget(1, 1, pbox);
		grid.setWidget(2, 1, submit);

		loginFucntionPanel.clear();
		loginFucntionPanel.setWidget(grid);

		submit.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loginForm.submit();
			}
		});

		loginForm.addSubmitHandler(new FormPanel.SubmitHandler() {
			@Override
			public void onSubmit(SubmitEvent event) {

			}

		});

		loginForm.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				String account = "";
				loginFucntionPanel.setWidget(new Label("Welcome, " + account));
				a.setText("Log Out"); // Window.alert(event.getResults());
			}

		});
	}
}
