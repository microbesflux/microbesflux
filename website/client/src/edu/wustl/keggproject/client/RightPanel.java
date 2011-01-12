package edu.wustl.keggproject.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class RightPanel {

	private StatusFormPanel sf;

	private String tempvalue;

	VerticalPanel newModelPanel;
	BateriaSuggestionBox suggestBox;
	SimplePanel sp = new SimplePanel();

	public void initialize() {
		// vp.add(status);
		// vp.add(sp);
		// this.initializeSuggestionPanel();

	}

	public void setStatusFormPanel(StatusFormPanel f) {
		sf = f;
	}

	public Widget getRightPanel() {
		return sp;
	}

	public void changeToNewFile() {

		sf.clearForm();
		sf.clearStatus();
		sf.setStatus("Creating a new model");
		final Configuration conf = ConfigurationFactory.getConfiguration();

		final FormPanel createForm = new FormPanel();
		createForm.setAction(conf.getBaseURL() + "collection/create/");
		createForm.setMethod(FormPanel.METHOD_GET);

		newModelPanel = new VerticalPanel();

		// Part 1
		HorizontalPanel namepanel = new HorizontalPanel();
		namepanel.add(new Label("Name of the model: "));
		final TextBox collectionbox = new TextBox();

		collectionbox.setName("collection_name");
		namepanel.add(collectionbox);

		createForm.addSubmitHandler(new FormPanel.SubmitHandler() {
			@Override
			public void onSubmit(SubmitEvent event) {
				// tempvalue = new String(collectionbox.getValue());
			}
		});

		// TODO: debug can not get the current model name
		createForm
				.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
					public void onSubmitComplete(SubmitCompleteEvent event) {
						conf.setCurrentCollection(tempvalue);
						sf.setStatus(" Current Model: "
								+ conf.getCurrentCollection());
						changeToGenome();
					}
				});

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
				tempvalue = new String(collectionbox.getText());
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

	public void changeToGenome() {
		sp.clear();

		// TODO
	}

	public void ChangeToPathway() {
		// TODO
		sp.clear();
	}

	public void ChangeToOptimization() {
		// TODO
	}

	public void ChangeToSummary() {
		// Construct a JsonDS, and then put it here
		// TODO
	}

	public void ChangeToPasswordChange() {
		// TODO
	}

	public void changeToWelcome(String info) {
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
		String introductionContent = info
				+ " Here is what we will input in the Introduciton tab";
		String functionContent = "Here is what we will input in the Function tab";
		String flowchartContent = "Here is what we will input in the Flowchart tab";
		String demoContent = "Here is what we will input in the Demo tab";
		String helpContent = "Here is what we will input in the Help tab";

		com.smartgwt.client.widgets.Label introductionForm = new com.smartgwt.client.widgets.Label(
				introductionContent);
		com.smartgwt.client.widgets.Label functionForm = new com.smartgwt.client.widgets.Label(
				functionContent);
		com.smartgwt.client.widgets.Label flowchartForm = new com.smartgwt.client.widgets.Label(
				flowchartContent);
		com.smartgwt.client.widgets.Label demoForm = new com.smartgwt.client.widgets.Label(
				demoContent);
		com.smartgwt.client.widgets.Label helpForm = new com.smartgwt.client.widgets.Label(
				helpContent);

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

	public void ChangeToLogin(final Anchor a) {
		final Configuration conf = ConfigurationFactory.getConfiguration();
		if (a.getText().equals("[Log Out]")) {
			Window.alert("In Logout");
			final FormPanel logoutForm = new FormPanel();
			logoutForm.setAction(conf.getBaseURL()
					+ "user/logout/");
			logoutForm.setMethod(FormPanel.METHOD_GET);

			logoutForm.addSubmitHandler(new FormPanel.SubmitHandler() {

				@Override
				public void onSubmit(SubmitEvent event) {
					;

				}
			});

			logoutForm
					.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {

						@Override
						public void onSubmitComplete(SubmitCompleteEvent event) {
							changeToWelcome("Thank you, you have logged out. ");
							a.setText("[Log In]");
						}
					});
			sp.clear();
			sp.add(logoutForm);
			logoutForm.submit();
		}

		else {

			final VerticalPanel loginPanel = new VerticalPanel();

			final FormPanel loginForm = new FormPanel();
			loginForm.setAction(conf.getBaseURL()
					+ "user/login/");
			loginForm.setMethod(FormPanel.METHOD_POST);

			final Grid grid = new Grid(3, 2);
			loginForm.setWidget(grid);

			Label l = new Label("User name: ");
			Label p = new Label("Password: ");
			final TextBox ubox = new TextBox();
			final PasswordTextBox pbox = new PasswordTextBox();

			ubox.setName("username");
			pbox.setName("password");
			Button submit = new Button("Login");
			grid.setWidget(0, 0, l);
			grid.setWidget(0, 1, ubox);
			grid.setWidget(1, 0, p);
			grid.setWidget(1, 1, pbox);
			grid.setWidget(2, 0, submit);

			Button forgot = new Button("Forgot Password?");
			grid.setWidget(2, 1, forgot);

			TextBox forgotemail = new TextBox();
			forgotemail.setName("username_forgot");
			Label enter = new Label("Enter your email: ");
			Button retrieve = new Button("Retrieve Password");

			HorizontalPanel forgotpassword = new HorizontalPanel();
			forgotpassword.add(enter);
			forgotpassword.add(forgotemail);
			forgotpassword.add(retrieve);
			final FormPanel forgotform = new FormPanel();
			forgotform.add(forgotpassword);

			forgotform.setMethod(FormPanel.METHOD_GET);
			forgotform.setAction(conf.getBaseURL()
					+ "user/retrievepassword/");
			forgotform.setVisible(false);

			forgot.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					forgotform.setVisible(true);
				}

			});

			retrieve.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					forgotform.submit();
				}

			});

			forgotform.addSubmitHandler(new FormPanel.SubmitHandler() {

				@Override
				public void onSubmit(SubmitEvent event) {
					// TODO Auto-generated method stub

				}

			});

			forgotform
					.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {

						@Override
						public void onSubmitComplete(SubmitCompleteEvent event) {
							forgotform.setVisible(false);
							if (event.getResults().contains("sent")) {
								forgotform.setVisible(false);
								Window.alert("Email sent!");
							}

						}

					});

			submit.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					if (ubox.getText().isEmpty()) {
						Window.alert("Username can't be empty. ");
						ubox.setFocus(true);
						return;
					}

					if (pbox.getText().isEmpty()) {
						Window.alert("Password can't be empty. ");
						pbox.setFocus(true);
						return;
					}
					loginForm.submit();
				}

			});

			loginForm.addSubmitHandler(new FormPanel.SubmitHandler() {

				@Override
				public void onSubmit(SubmitEvent event) {
					;

				}
			});

			loginForm
					.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {

						@Override
						public void onSubmitComplete(SubmitCompleteEvent event) {
							if (event.getResults().contains("Successful")) {
								changeToWelcome("Welcome, " + ubox.getText());
								a.setText("[Log Out]");
							} else {
								changeToWelcome("You need correct username/password to use this website");
							}
						}
					});
			sp.clear();
			loginPanel.add(loginForm);
			loginPanel.add(forgotform);
			sp.add(loginPanel);

		}
	}

	protected void ChangeToRegister() {

		VerticalPanel registerPanel = new VerticalPanel();

		Grid accountGrid = new Grid(4, 2);
		Grid userInformation = new Grid(9, 2);
		Grid agreementGrid = new Grid(2, 1);

		final TextBox emailID = new TextBox();
		emailID.setName("username");

		final PasswordTextBox txtPassword = new PasswordTextBox();
		final PasswordTextBox confirmPassword = new PasswordTextBox();
		accountGrid.setWidget(0, 0, new Label("Required in *."));
		accountGrid.setWidget(1, 0, new Label("Email(ID)*"));
		accountGrid.setWidget(1, 1, emailID);
		accountGrid.setWidget(2, 0, new Label("Password*"));
		accountGrid.setWidget(2, 1, txtPassword);
		accountGrid.setWidget(3, 0, new Label("Confirm Password*"));
		accountGrid.setWidget(3, 1, confirmPassword);

		txtPassword.setName("password");

		ListBox Title = new ListBox();
		Title.setName("title");
		Title.addItem("--Please Select--");
		Title.addItem("Dr");
		Title.addItem("Mr");
		Title.addItem("Mrs");
		Title.addItem("Ms");

		ListBox organizationType = new ListBox();
		organizationType.addItem("--Please Select--");
		organizationType.addItem("Academic");
		organizationType.addItem("Corporate");
		organizationType.setName("orgtype");

		TextBox firstName = new TextBox();
		firstName.setName("firstname");

		TextBox lastName = new TextBox();
		lastName.setName("lastname");

		TextBox department = new TextBox();
		department.setName("department");

		TextBox organization = new TextBox();
		organization.setName("organization");

		TextBox address1 = new TextBox();
		address1.setName("address1");

		TextBox address2 = new TextBox();
		address2.setName("address2");

		TextBox country = new TextBox();
		country.setName("country");

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

		registerPopup.setEnabled(false);

		agreement.addClickHandler(new ClickHandler() {
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

		final FormPanel registerForm = new FormPanel();
		final Configuration conf = ConfigurationFactory.getConfiguration();
		registerForm.setAction( conf.getBaseURL()
				+ "user/add/");
		registerForm.setMethod(FormPanel.METHOD_POST);
		registerForm
				.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {

					@Override
					public void onSubmitComplete(SubmitCompleteEvent event) {
						if (event.getResults().equals("Successfully")) {
							changeToWelcome("Account registered");
						}
					}
				});

		registerForm.addSubmitHandler(new FormPanel.SubmitHandler() {

			@Override
			public void onSubmit(SubmitEvent event) {

			}

		});

		registerForm.setWidget(registerPanel);
		registerPopup.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO check data integrity
				if (!txtPassword.getText().equals(confirmPassword.getText())) {
					Window.alert("Please check your passwords.");
				}
				if (!emailID.getText().contains("@")) {
					Window.alert("Invalid email address");
				}
				registerForm.submit();
			}

		});

		sp.add(registerForm);

	}

}
