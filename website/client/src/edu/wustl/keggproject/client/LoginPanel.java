package edu.wustl.keggproject.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LoginPanel {

	private RightPanel rp;

	public void setRightPanel(RightPanel r) {
		rp = r;
	}

	public Widget getLoginPanel() {
		HorizontalPanel tabPanel = new HorizontalPanel();
		final Anchor welcome = new Anchor("[Welcome]");
		final Anchor logIn = new Anchor("[Login]");
		final Anchor register = new Anchor("[Register]");
		welcome.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				rp.changeToWelcome("Welcome! ");
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
