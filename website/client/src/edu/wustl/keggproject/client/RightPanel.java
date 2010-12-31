package edu.wustl.keggproject.client;

import java.util.Iterator;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class RightPanel {

	private String current_collection = "";
	VerticalPanel newModelPanel;
	BateriaSuggestionBox suggestBox;
	SimplePanel sp = new SimplePanel();

	public void initialize() {
		// this.initializeSuggestionPanel();
	}

	public Widget getRightPanel() {
		return sp;
	}

	public void changeToNewFile() {

		// The Form

		final FormPanel createForm = new FormPanel();
		createForm.setAction(ResourceSingleton.getInstace().getBaseURL()
				+ "collection/create/");
		createForm.setMethod(FormPanel.METHOD_GET);
		createForm.addFormHandler(new FormHandler() {
			@Override
			public void onSubmit(FormSubmitEvent event) {
				;
			}

			@Override
			public void onSubmitComplete(FormSubmitCompleteEvent event) {
				// TODO
				Window.alert("You have created a new pathway!");
				changeToGenome();
			};
		});

		newModelPanel = new VerticalPanel();

		// Part 1
		HorizontalPanel namepanel = new HorizontalPanel();
		namepanel.add(new Label("Name of the model: "));
		final TextBox collectionbox = new TextBox();
		collectionbox.setName("collection_name");
		namepanel.add(collectionbox);

		HorizontalPanel strapanel = new HorizontalPanel();
		HTMLPane paneLink = new HTMLPane();
		paneLink.setContents("<a href=\"http://www.genome.jp/kegg/catalog/org_list.html\" target=\"_blank\">Input KEGG Organisms</a>");
		paneLink.setPosition("Center");
		HStack layoutpaneLink = new HStack();
		layoutpaneLink.addMember(paneLink);
		layoutpaneLink.setSize("100px", "40px");

		TextBox hiddenname = new TextBox();
		hiddenname.setName("bacteria");

		suggestBox = new BateriaSuggestionBox(hiddenname);

		suggestBox.setLimit(20);
		suggestBox.setStyleName("gwt-SuggestBox-F1");
		suggestBox.setText("");
		suggestBox.setSize("200px", "20px");

		strapanel.add(layoutpaneLink);
		strapanel.add(suggestBox);

		Button buttonRun = new Button();
		buttonRun.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				current_collection = collectionbox.getText();
				createForm.submit();
			}
		});
		buttonRun.setText("Run");
		buttonRun.setSize("60px", "30px");

		newModelPanel.add(namepanel);
		newModelPanel.add(strapanel);
		newModelPanel.add(buttonRun);

		createForm.setWidget(newModelPanel);

		sp.clear();
		sp.add(createForm);
	}

	public void changeToLoadFile() {

	}

	@SuppressWarnings("deprecation")
	public void changeToSaveFile() {
		final Label info = new Label("Saving model ....");
		final Widget p = sp.getWidget();
		final FormPanel saveForm = new FormPanel();
		saveForm.setAction(ResourceSingleton.getInstace().getBaseURL()
				+ "collection/save/");
		saveForm.setMethod(FormPanel.METHOD_GET);
		VerticalPanel hiddenPanel = new VerticalPanel();
		TextBox hiddenbox = new TextBox();
		hiddenbox.setVisible(false);
		hiddenbox.setName("collection_name");
		hiddenbox.setText(current_collection);
		hiddenPanel.add(hiddenbox);
		
		hiddenPanel.add(info);
		saveForm.add(hiddenPanel);
		saveForm.addFormHandler(new FormHandler() {
			@Override
			public void onSubmit(FormSubmitEvent event) {
				;
			}

			@Override
			public void onSubmitComplete(FormSubmitCompleteEvent event) {
				Window.alert("Model saved!");
				
				// changeToGenome();
			};
		});
		sp.clear();
		sp.setWidget(saveForm);
		saveForm.submit();
	}

	public void ChangeTosaveFileAs() {
		// TODO
	}

	public void changeToGenome() {
		// TODO
	}

	public void ChangeToPathway() {
		// TODO
	}

	public void ChangeToOptimization() {
		// TODO
	}

	public void ChangeToSummary() {
		// TODO
	}

	public void ChangeToPasswordChange() {
		// TODO
	}

	public void changeToWelcome() {
		sp.clear();

		final TabSet topTabSet = new TabSet();

		topTabSet.setTabBarPosition(Side.TOP);
		topTabSet.setTabBarAlign(Side.LEFT);
		topTabSet.setWidth(400);
		topTabSet.setHeight(200);

		topTabSet.setSize("550px", "400px");
		final Tab introductionTab = new Tab("Introduction");
		final Tab functionTab = new Tab("Functions");
		final Tab flowchartTab = new Tab("Flowchart");
		final Tab demoTab = new Tab("Demo");
		final Tab helpTab = new Tab("Help");
		// Tab module
		String introductionContent = "Here is what we will input in the Introduciton tab";
		String functionContent = "Here is what we will input in the Function tab";
		String flowchartContent = "Here is what we will input in the Flowchart tab";
		String demoContent = "Here is what we will input in the Demo tab";
		String helpContent = "Here is what we will input in the Help tab";

		Label introductionForm = new Label(introductionContent);
		Label functionForm = new Label(functionContent);
		Label flowchartForm = new Label(flowchartContent);
		Label demoForm = new Label(demoContent);
		Label helpForm = new Label(helpContent);

		introductionTab.setPane(introductionForm);
		functionTab.setPane(functionForm);
		flowchartTab.setPane(flowchartForm);
		demoTab.setPane(demoForm);
		helpTab.setPane(helpForm);

		topTabSet.addTab(introductionTab);
		topTabSet.addTab(functionTab);
		topTabSet.addTab(flowchartTab);
		topTabSet.addTab(demoTab);
		topTabSet.addTab(helpTab);
		sp.setWidget(topTabSet);
	}

	@SuppressWarnings("deprecation")
	public void ChangeToLogin(final Anchor a) {
		if (a.getText().equals("[Log Out]")) {
			Window.alert("In Logout");
			final FormPanel logoutForm = new FormPanel();
			logoutForm.setAction(ResourceSingleton.getInstace().getBaseURL()
					+ "user/logout/");
			logoutForm.setMethod(FormPanel.METHOD_GET);
			logoutForm.addFormHandler(new FormHandler() {
				public void onSubmitComplete(FormSubmitCompleteEvent event) {
					Window.alert("You have logged out successfully!");
					changeToWelcome();
					a.setText("[Log In]"); // Window.alert(event.getResults());
				}

				public void onSubmit(FormSubmitEvent event) {
					;
				}
			});
			sp.clear();
			sp.add(logoutForm);
			logoutForm.submit();
		}

		else {

			final FormPanel loginForm = new FormPanel();
			loginForm.setAction(ResourceSingleton.getInstace().getBaseURL()
					+ "user/login/");
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

			submit.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					loginForm.submit(); 
				}

			});

			loginForm.addFormHandler(new FormHandler() {

				@Override
				public void onSubmit(FormSubmitEvent event) {
					;
				}

				@Override
				public void onSubmitComplete(FormSubmitCompleteEvent event) {
					if (event.getResults().contains("Successful")) {
						Window.alert("Login Successfully");
						changeToWelcome();
					}
					a.setText("[Log Out]");
				}
			});
			sp.clear();
			sp.add(loginForm);

		}
	}

	protected void ChangeToRegister() {

		VerticalPanel registerPanel = new VerticalPanel();

		Grid accountGrid = new Grid(4, 2);
		Grid userInformation = new Grid(9, 2);
		Grid agreementGrid = new Grid(2, 1);

		TextBox emailID = new TextBox();
		PasswordTextBox txtPassword = new PasswordTextBox();
		PasswordTextBox confirmPassword = new PasswordTextBox();
		accountGrid.setWidget(0, 0, new Label("Required in *."));
		accountGrid.setWidget(1, 0, new Label("Email(ID)*"));
		accountGrid.setWidget(1, 1, emailID);
		accountGrid.setWidget(2, 0, new Label("Password*"));
		accountGrid.setWidget(1, 1, txtPassword);
		accountGrid.setWidget(3, 0, new Label("Confirm Password*"));
		accountGrid.setWidget(3, 1, confirmPassword);

		ListBox Title = new ListBox();
		Title.addItem("--Please Select--");
		Title.addItem("Dr");
		Title.addItem("Mr");
		Title.addItem("Mrs");
		Title.addItem("Ms");

		ListBox organizationType = new ListBox();
		organizationType.addItem("--Please Select--");
		organizationType.addItem("Academic");
		organizationType.addItem("Corporate");

		TextBox firstName = new TextBox();
		TextBox lastName = new TextBox();
		TextBox department = new TextBox();
		TextBox organization = new TextBox();
		TextBox address1 = new TextBox();
		TextBox address2 = new TextBox();
		TextBox country = new TextBox();

		userInformation.setWidget(0, 0, new Label("Title*"));
		userInformation.setWidget(0, 1, Title);
		userInformation.setWidget(1, 0, new Label("First Name*"));
		userInformation.setWidget(1, 1, firstName);
		userInformation.setWidget(2, 0, new Label("Last Name*"));
		userInformation.setWidget(2, 1, lastName);
		userInformation.setWidget(3, 0, new Label("Department*"));
		userInformation.setWidget(3, 1, department);
		userInformation.setWidget(4, 0, new Label("Company/Institution*"));
		userInformation.setWidget(4, 1, organization);
		userInformation.setWidget(5, 0, new Label("Organization Type*"));
		userInformation.setWidget(5, 1, organizationType);
		userInformation.setWidget(6, 0, new Label("Address 1*"));
		userInformation.setWidget(6, 1, address1);
		userInformation.setWidget(7, 0, new Label("Address 2*"));
		userInformation.setWidget(7, 1, address2);
		userInformation.setWidget(8, 0, new Label("Country*"));
		userInformation.setWidget(8, 1, country);

		final CheckBox agreement = new CheckBox();
		agreement
				.setHTML("I have read and agree to the <a href=\"http://www.genome.jp/kegg/catalog/org_list.html\" target=\"_blank\">Term of Use</a>");

		final Button registerPopup = new Button("Register");
		// TODO
		// Submit a form

		registerPopup.setEnabled(false);

		agreement.addClickHandler(new ClickHandler() {
			@SuppressWarnings("deprecation")
			public void onClick(ClickEvent event) {
				if (agreement.getValue()) {
					registerPopup.setEnabled(true);
				} else {
					registerPopup.setEnabled(false);
				}

			}
		});

		agreementGrid.setWidget(0, 0, agreement);
		agreementGrid.setWidget(1, 0, registerPopup);

		registerPanel.add(accountGrid);
		registerPanel.add(userInformation);
		registerPanel.add(agreementGrid);

		sp.clear();
		sp.add(registerPanel);
	}

}
