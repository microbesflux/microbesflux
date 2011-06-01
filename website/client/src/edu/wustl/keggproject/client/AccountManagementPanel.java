package edu.wustl.keggproject.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

import edu.wustl.keggproject.client.datasource.AccountSummaryDS;




public class AccountManagementPanel {

	private DialogBox mydia;

	public SimplePanel accountManagementPanel = new SimplePanel();

	public Widget getAccountManagementPanel() {
		return accountManagementPanel;
	}

	public void setDiaglog(DialogBox d) {
		mydia = d;
	}
	
	public void initialize() {
		accountManagementPanel.setVisible(false);
	}

	public void ChangeToSummary() {
		accountManagementPanel.clear();
		accountManagementPanel.setVisible(true);

		VerticalPanel summaryPanel = new VerticalPanel();

		final ListGrid accountSummary = new ListGrid();
		accountSummary.setWidth(400);
		accountSummary.setHeight(500);
		accountSummary.setShowAllRecords(true);
		accountSummary.setDataSource(AccountSummaryDS.getInstance());

		ListGridField date = new ListGridField("date");
		ListGridField model = new ListGridField("model");
		ListGridField type = new ListGridField("type");
		ListGridField status = new ListGridField("status");
		// ListGridField url = new ListGridField("url");

		accountSummary.setFields(date, model, type, status);
		accountSummary.setAutoFetchData(true);

		Button buttonExit = new Button("Exit Summary");
		buttonExit.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// accountManagementPanel.setVisible(false);
				mydia.hide();
			}
		});

		summaryPanel.add(accountSummary);
		summaryPanel.add(buttonExit);
		accountManagementPanel.setWidget(summaryPanel);
	}

	public void ChangeToPasswordChange() {
		accountManagementPanel.clear();
		accountManagementPanel.setVisible(true);

		final FormPanel changePassword = new FormPanel();
		changePassword.setAction(ConfigurationFactory.getConfiguration()
				.getBaseURL() + "user/password/change/");

		changePassword.setMethod(FormPanel.METHOD_POST);

		Grid changePasswordGrid = new Grid(3, 2);
		Label newPassword = new Label("New Password");
		final PasswordTextBox newPasswordBox = new PasswordTextBox();
		Label confirmPassword = new Label("Confirm Password");
		final PasswordTextBox confirmPasswordBox = new PasswordTextBox();
		Button changeButton = new Button("Change Password");
		Button cancelButton = new Button("Cancel");

		newPasswordBox.setName("newpassword");
		confirmPasswordBox.setName("confirmpassword");

		changePasswordGrid.setWidget(0, 0, newPassword);
		changePasswordGrid.setWidget(0, 1, newPasswordBox);
		changePasswordGrid.setWidget(1, 0, confirmPassword);
		changePasswordGrid.setWidget(1, 1, confirmPasswordBox);
		changePasswordGrid.setWidget(2, 0, changeButton);
		changePasswordGrid.setWidget(2, 1, cancelButton);

		changePassword.add(changePasswordGrid);
		accountManagementPanel.setWidget(changePassword);

		changeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (newPasswordBox.getText().isEmpty()
						|| newPasswordBox.getText().isEmpty()) {
					Window.alert("Please check the new password");
					return;
				}

				if (!newPasswordBox.getText().equals(
						confirmPasswordBox.getText())) {
					Window.alert("Please confirm the new password");
					return;
				}
				changePassword.submit();
				// accountManagementPanel.setVisible(false);
				mydia.hide();
			}

		});

		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// accountManagementPanel.setVisible(false);
				mydia.hide();
			}
		});

		changePassword.addSubmitHandler(new FormPanel.SubmitHandler() {

			@Override
			public void onSubmit(SubmitEvent event) {
				;
			}
		});

		changePassword
				.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {

					@Override
					public void onSubmitComplete(SubmitCompleteEvent event) {
						if (event.getResults().contains("successfully")) {
							Window.alert("Password changed");
						}
					}
				});
	}
}
