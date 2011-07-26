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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

import edu.wustl.keggproject.client.datasource.AccountSummaryDS;

public class LeftPanel {
	private RightPanel rp;
	private StatusFormPanel sf;

	private AccountDialogBox dialogBox;
	
	public void setRightPanel(RightPanel r) {
		rp = r;
	}

	public void setStatusFormPanel(StatusFormPanel sfp) {
		sf = sfp;
	}
	
	public Widget getLeftPanel() {

		dialogBox = new AccountDialogBox();	
		dialogBox.setGlassEnabled(true);
		
		final VerticalPanel filePanel = new VerticalPanel();
		final Anchor newFile = new Anchor("New model");
		final Anchor loadFile = new Anchor("Load model");
		final Anchor saveFile = new Anchor("Save model");
		final Anchor saveFileAs = new Anchor("Save model As");
		final Configuration conf = ConfigurationFactory.getConfiguration();

		newFile.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (conf.getCurrentCollection().length() > 0) {
					boolean proceed = Window
						.confirm("You are about to create a new model. Make sure to save all changes (if any) to your current model. Do you want to proceed? ");
					if (!proceed) {
							return;
					}
				}
				rp.changeToCreateANewCollection();
			}
		});

		loadFile.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				if (conf.getLogin() == false) {
					Window.alert("You have to login first to load existing models");
					return;
				}
				
				if (conf.getCurrentCollection().length() > 0) {
					boolean proceed = Window
							.confirm("You are about to load a new model. Changes to the current model will be discarded unless you click \"Save Model\" first. Do you want to proceed? ");
					if (!proceed) {
						return;
					}
				}
				
				sf.loadFile();
				rp.changeToWelcome();
			}
		});

		saveFile.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (conf.getLogin() == false) {
					Window.alert("You have to login first to save existing models");
					return;
				}
				
				sf.saveFile(false);
			}
		});

		saveFileAs.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (conf.getLogin() == false) {
					Window.alert("You have to login first to save existing models");
					return;
				}
				
				// rp.ChangeToPathway();
				sf.saveFileAs();
			}
		});

		filePanel.add(newFile);
		filePanel.add(loadFile);
		filePanel.add(saveFile);
		filePanel.add(saveFileAs);

		VerticalPanel functionPanel = new VerticalPanel();
		final Anchor genomeInfo = new Anchor("Genome Information");
		final Anchor pathwayInfo = new Anchor("Metabolic Pathways");
		final Anchor optimizationInfo = new Anchor("Optimization");
		genomeInfo.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				rp.changeToGenome();
			}
		});
		pathwayInfo.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				rp.ChangeToPathway();
			}
		});
		optimizationInfo.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				rp.ChangeToOptimization();
			}
		});
		functionPanel.add(genomeInfo);
		functionPanel.add(pathwayInfo);
		functionPanel.add(optimizationInfo);

		VerticalPanel accountPanel = new VerticalPanel();
		// final Anchor summaryHistory = new Anchor("Summary");
		final Anchor passwordChgLink = new Anchor("Change Passwords");

		
		final FormPanel passwordChangePanel = new FormPanel();
		passwordChangePanel.setAction(ConfigurationFactory.getConfiguration()
				.getBaseURL() + "user/password/change/");

		passwordChangePanel.setMethod(FormPanel.METHOD_POST);

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

		passwordChangePanel.add(changePasswordGrid);
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
				passwordChangePanel.submit();
				// accountManagementPanel.setVisible(false);
				//
			}

		});

		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				passwordChangePanel.setVisible(false);
			}
		});

		passwordChangePanel.addSubmitHandler(new FormPanel.SubmitHandler() {

			@Override
			public void onSubmit(SubmitEvent event) {
				;
			}
		});

		passwordChangePanel
				.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {

					@Override
					public void onSubmitComplete(SubmitCompleteEvent event) {
						if (event.getResults().contains("successfully")) {
							passwordChangePanel.setVisible(false);
							Window.alert("Password changed");
						}
					}
				});
		
		passwordChgLink.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (conf.getLogin() == false) {
					Window.alert("You have to login first to change your password.");
					return;
				}
				else {
					passwordChangePanel.setVisible(true);
				}
			}
		});
		
		accountPanel.add(passwordChgLink);
		accountPanel.add(passwordChangePanel);
		passwordChangePanel.setVisible(false);
		

		VerticalPanel biggerLeftPanel = new VerticalPanel();
		
		StackPanel leftPanel = new StackPanel();
		leftPanel.add(filePanel, "Build/Load/Save a Model", false);
		leftPanel.add(functionPanel, "Pathways & FBA", false);
		leftPanel.add(accountPanel, "Account Management", false);

		biggerLeftPanel.add(leftPanel);
		// String htmlcode = 
		// "<a href=\"http://tvshowpopularity.net\" target=\"blank\" ><img alt=\"tvshowpopularity.net\" hspace=\"0\" vspace=\"0\" border=\"0\" src=\"http://awesomemonitor.com/counter.php?id=953eba4341c2ae3b9d731130915c4874&theme=1c&digits=6&size=big&siteId=ctm_tp_c4\"/></a><noscript><br/><a href=\"http://tvshowpopularity.net\">Free Counter</a><br>The following text will not be seen after you upload your website, please keep it in order to retain your counter functionality <br> <a href=\"http://casino4fun.org\" target=\"_blank\">casino reviews</a></noscript>";
		// biggerLeftPanel.add(new HTMLPanel(htmlcode));
	    return biggerLeftPanel;
	}
}


class AccountDialogBox extends DialogBox {
	
	public void ChangeToPasswordChange() {
		this.center();

	}
	
	public void ChangeToSummary() {
		this.center();
		
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
				hide();
			}
		});

		summaryPanel.add(accountSummary);
		summaryPanel.add(buttonExit);
		setWidget(summaryPanel);		
	}
	
}
