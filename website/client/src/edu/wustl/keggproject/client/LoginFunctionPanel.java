/* 
 * Copyright (c) 2011, Eric Xu, Xueyang Feng
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, 
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright notice, 
 *  this list of conditions and the following disclaimer in the documentation 
 *  and/or other materials provided with the distribution.
 *  
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 *  POSSIBILITY OF SUCH DAMAGE.
 */

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
