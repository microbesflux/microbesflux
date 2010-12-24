package edu.wustl.keggproject.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Label;

public class LoginPanel {

	private LoginFunctionPanel lfp;
	private RightPanel rp;
	public void setRightPanel(RightPanel r) {
		rp = r;
	}
	
	public Widget getLoginPanel(){
		
		HorizontalPanel tabPanel = new HorizontalPanel();
		final Anchor welcome = new Anchor("[Welcome]");
		final Anchor logIn = new Anchor("[Login]");
		final Anchor register = new Anchor("[Register]");
		welcome.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				rp.ChangeToWelcome();
			}
		});
		logIn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				lfp.ChangeToLogin(logIn);
			}
		});
		register.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ChangeToRegister();
			}
		});
		
		tabPanel.add(welcome);
		tabPanel.add(logIn);
		tabPanel.add(register);
				
		return tabPanel;
	}

	protected void ChangeToRegister() {
	    // Create a PopUpPanel with a button to close it
		final PopupPanel popup = new PopupPanel(false);
		VerticalPanel registerPanel = new VerticalPanel();
		
		Grid accountGrid=new Grid(4,2);
		Grid userInformation=new Grid(9,2);
		Grid agreementGrid=new Grid(2,1);
		
		TextBox emailID=new TextBox();
		PasswordTextBox txtPassword=new PasswordTextBox();
		PasswordTextBox confirmPassword=new PasswordTextBox();
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
		
		TextBox firstName=new TextBox();
		TextBox lastName=new TextBox();
		TextBox department=new TextBox();
		TextBox organization=new TextBox();
		TextBox address1=new TextBox();
		TextBox address2=new TextBox();
		TextBox country=new TextBox();

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
		agreement.setHTML("I have read and agree to the <a href=\"http://www.genome.jp/kegg/catalog/org_list.html\" target=\"_blank\">Term of Use</a>");
		
		ClickListener popupListener = new ClickListener()
	    {
	        public void onClick(Widget sender)
	        {
	            popup.hide();
	        }
	    };
		final Button registerPopup = new Button("Register", popupListener);	
		registerPopup.setEnabled(false);
		
		agreement.addClickHandler(new ClickHandler() {
		      @SuppressWarnings("deprecation")
			public void onClick(ClickEvent event) {
		    	 if(agreement.getValue()){
		    		 registerPopup.setEnabled(true);
		    	 }else{
		    		 registerPopup.setEnabled(false);
		    	 }
		       
		      }
		    });

		agreementGrid.setWidget(0, 0, agreement);
		agreementGrid.setWidget(1, 0, registerPopup);
	    
	    
	    
	    registerPanel.add(accountGrid);
		registerPanel.add(userInformation);
		registerPanel.add(agreementGrid);
	    popup.setWidget(registerPanel);
	    //
	    // Add a button to the demo to show the above PopUpPanel
	    ClickListener registerListener = new ClickListener()
	    {
	        public void onClick(Widget sender)
	        {
	            popup.center();
	        }
	    };
	    Button registerButton = new Button("Register", registerListener);
	    		
	}
}
