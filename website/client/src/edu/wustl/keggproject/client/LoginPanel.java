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
				rp.changeToWelcome();
			}
		});
		logIn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				rp.ChangeToLogin(logIn);
			}
		});
		register.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				rp.ChangeToRegister();
			}
		});
		
		tabPanel.add(welcome);
		tabPanel.add(logIn);
		tabPanel.add(register);
				
		return tabPanel;
	}

}
