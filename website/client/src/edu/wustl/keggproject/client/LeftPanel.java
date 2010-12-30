package edu.wustl.keggproject.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

// Xueyang
public class LeftPanel {
	private RightPanel rp;
	public void setRightPanel(RightPanel r) {
		rp = r;
	}
	public Widget getLeftPanel(){

		VerticalPanel filePanel = new VerticalPanel();
		final Anchor newFile = new Anchor("New model");
		final Anchor loadFile = new Anchor("Load model");
		final Anchor saveFile = new Anchor("Save model");
		final Anchor saveFileAs = new Anchor("Save model As");
		newFile.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				rp.ChangeTonewFile();
			}
		});
		loadFile.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				rp.ChangeToloadFile();
			}
		});
		saveFile.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				rp.ChangeTosaveFile();
			}
		});
		saveFileAs.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				rp.ChangeTosaveFileAs();
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
				rp.ChangeToGenome();
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
		final Anchor summaryHistory = new Anchor("Summary");
		final Anchor passwordChange = new Anchor("Change Passwords");
		summaryHistory.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				rp.ChangeToSummary();
			}
		});
		passwordChange.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				rp.ChangeToPasswordChange();
			}
		});
		accountPanel.add(summaryHistory);
		accountPanel.add(passwordChange);
		
		StackPanel leftPanel = new StackPanel();
		leftPanel.add(filePanel, "Build/Load/Save a Model", false);
		leftPanel.add(functionPanel, "Pathways & FBA", false);
		leftPanel.add(accountPanel, "Account Management", false);
		
		return leftPanel;

	}


}