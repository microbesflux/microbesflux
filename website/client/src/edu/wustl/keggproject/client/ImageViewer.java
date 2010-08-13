package edu.wustl.keggproject.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.NamedFrame;
import com.smartgwt.client.widgets.Img;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.DecoratedTabBar;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.smartgwt.client.widgets.viewer.DetailViewer;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.TabPanel;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.types.Side;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Grid;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Canvas;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.VerticalSplitPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ImageViewer implements EntryPoint {
	public void onModuleLoad() {
		RootPanel rootPanel = RootPanel.get();
		rootPanel.setSize("1500", "1000");
		
		SuggestBox suggestBox = new SuggestBox();
		suggestBox.setStyleName("gwt-SuggestBox-F1");
		suggestBox.setHeight("10");
		suggestBox.setText("Input KEGG Organisms");
		rootPanel.add(suggestBox, 303, 160);
		suggestBox.setSize("336px", "24px");
		
		Label lblKeggBasedFlux = new Label("KEGG Based Flux Analysis (KEB Flux)");
		rootPanel.add(lblKeggBasedFlux, 131, 49);
		lblKeggBasedFlux.setStyleName("gwt-Label-F1");
		lblKeggBasedFlux.setSize("352px", "62px");
		
		Hyperlink hyperlink = new Hyperlink("KEGG Organisms", false, "newHistoryToken");
		hyperlink.setStyleName("gwt-Hyperlink-F1");
		rootPanel.add(hyperlink, 150, 160);
		hyperlink.setSize("147px", "30px");
		
		Button btnRun = new Button("Run");
		rootPanel.add(btnRun, 651, 160);
		btnRun.setSize("129px", "30px");
		
		HorizontalSplitPanel horizontalSplitPanel = new HorizontalSplitPanel();
		horizontalSplitPanel.setSplitPosition("15%");
		rootPanel.add(horizontalSplitPanel, 6, 241);
		horizontalSplitPanel.setSize("788px", "491px");
		
		VerticalPanel verticalPanel = new VerticalPanel();
		horizontalSplitPanel.setLeftWidget(verticalPanel);
		verticalPanel.setSize("100%", "100%");
		
		Label lblKebFlux = new Label("KEB Flux");
		lblKebFlux.setStyleName("gwt-Label-F1");
		verticalPanel.add(lblKebFlux);
		
		Hyperlink hyperlink_1 = new Hyperlink("New hyperlink", false, "newHistoryToken");
		hyperlink_1.setStyleName("gwt-Hyperlink-F2");
		hyperlink_1.setHTML("Genome Information");
		verticalPanel.add(hyperlink_1);
		
		Hyperlink hyperlink_2 = new Hyperlink("New hyperlink", false, "newHistoryToken");
		hyperlink_2.setHTML("Metabolic Pathways");
		hyperlink_2.setStyleName("gwt-Hyperlink-F2");
		verticalPanel.add(hyperlink_2);
		
		Hyperlink hyperlink_3 = new Hyperlink("New hyperlink", false, "newHistoryToken");
		hyperlink_3.setHTML("Objetive Functions");
		hyperlink_3.setStyleName("gwt-Hyperlink-F2");
		verticalPanel.add(hyperlink_3);
		
		Hyperlink hyperlink_4 = new Hyperlink("New hyperlink", false, "newHistoryToken");
		hyperlink_4.setHTML("Optimization Results");
		hyperlink_4.setStyleName("gwt-Hyperlink-F2");
		verticalPanel.add(hyperlink_4);
		
		VerticalSplitPanel verticalSplitPanel = new VerticalSplitPanel();
		verticalSplitPanel.setSplitPosition("8%");
		horizontalSplitPanel.setRightWidget(verticalSplitPanel);
		verticalSplitPanel.setSize("648px", "477px");
		
		DecoratedTabBar decoratedTabBar = new DecoratedTabBar();
		verticalSplitPanel.setTopWidget(decoratedTabBar);
		decoratedTabBar.setSize("605px", "33px");
		decoratedTabBar.addTab("Welcome");
		decoratedTabBar.addTab("Function");
		decoratedTabBar.addTab("Flowchart");
		decoratedTabBar.addTab("Demo");
		decoratedTabBar.addTab("Help");
		
		VerticalPanel verticalPanel_1 = new VerticalPanel();
		verticalSplitPanel.setBottomWidget(verticalPanel_1);
		verticalPanel_1.setSize("100%", "100%");
		
		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		verticalPanel_1.add(horizontalPanel_1);
		horizontalPanel_1.setSize("100%", "21px");
		
		Label lblKo = new Label("KO");
		horizontalPanel_1.add(lblKo);
		
		Label lblReactionId = new Label("Reaction ID");
		horizontalPanel_1.add(lblReactionId);
		
		Label lblReaction_1 = new Label("Reaction");
		horizontalPanel_1.add(lblReaction_1);
		
		Label lblUb = new Label("UB");
		horizontalPanel_1.add(lblUb);
		
		Label lblLb = new Label("LB");
		horizontalPanel_1.add(lblLb);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		verticalPanel_1.add(horizontalPanel);
		horizontalPanel.setWidth("100%");
		
		CheckBox chckbxReactionId = new CheckBox("Reaction ID");
		horizontalPanel.add(chckbxReactionId);
		
		Label lblReaction = new Label("Reaction");
		horizontalPanel.add(lblReaction);
		
		TextBox textBox = new TextBox();
		horizontalPanel.add(textBox);
		
		TextBox textBox_1 = new TextBox();
		horizontalPanel.add(textBox_1);
		
		Button button_2 = new Button("New button");
		button_2.setText("Add Reactions");
		verticalPanel_1.add(button_2);
		
		HorizontalPanel horizontalPanel_5 = new HorizontalPanel();
		verticalPanel_1.add(horizontalPanel_5);
		
		SuggestBox suggestBox_1 = new SuggestBox();
		suggestBox_1.setText("Input compound name/ID");
		horizontalPanel_5.add(suggestBox_1);
		
		Label label_1 = new Label("+");
		horizontalPanel_5.add(label_1);
		
		SuggestBox suggestBox_2 = new SuggestBox();
		suggestBox_2.setText("Input compound name/ID");
		horizontalPanel_5.add(suggestBox_2);
		
		ListBox comboBox = new ListBox();
		comboBox.addItem("-->");
		horizontalPanel_5.add(comboBox);
		
		SuggestBox suggestBox_3 = new SuggestBox();
		suggestBox_3.setText("Input compound name/ID");
		horizontalPanel_5.add(suggestBox_3);
		
		Label label_4 = new Label("+");
		horizontalPanel_5.add(label_4);
		
		SuggestBox suggestBox_4 = new SuggestBox();
		suggestBox_4.setText("Input compound name/ID");
		horizontalPanel_5.add(suggestBox_4);
		
		Button button_5 = new Button("New button");
		button_5.setText("Add substrates");
		verticalPanel_1.add(button_5);
		
		Button button_6 = new Button("New button");
		button_6.setText("Add products");
		verticalPanel_1.add(button_6);
		
		Button button_4 = new Button("New button");
		button_4.setText("Download as SBML");
		verticalPanel_1.add(button_4);
		
		HorizontalPanel horizontalPanel_3 = new HorizontalPanel();
		verticalPanel_1.add(horizontalPanel_3);
		horizontalPanel_3.setSize("100%", "21px");
		
		Label lblObjReactions = new Label("Objetive Reactions");
		horizontalPanel_3.add(lblObjReactions);
		
		Label label_2 = new Label("Reaction ID");
		horizontalPanel_3.add(label_2);
		
		Label label_3 = new Label("Reaction");
		horizontalPanel_3.add(label_3);
		
		Label lblWeightingFactor = new Label("Weighting Factor");
		horizontalPanel_3.add(lblWeightingFactor);
		
		HorizontalPanel horizontalPanel_2 = new HorizontalPanel();
		verticalPanel_1.add(horizontalPanel_2);
		horizontalPanel_2.setWidth("100%");
		
		RadioButton rdbtnReactionId = new RadioButton("new name", "Reaction ID");
		horizontalPanel_2.add(rdbtnReactionId);
		
		Label label = new Label("Reaction");
		horizontalPanel_2.add(label);
		
		TextBox textBox_2 = new TextBox();
		horizontalPanel_2.add(textBox_2);
		
		Button button_1 = new Button("New button");
		button_1.setText("Add Objective Pathways");
		verticalPanel_1.add(button_1);
		
		Button button_3 = new Button("New button");
		button_3.setText("Reset");
		verticalPanel_1.add(button_3);
		
		Button button = new Button("New button");
		button.setText("Add Components");
		verticalPanel_1.add(button);
		
		Button button_7 = new Button("New button");
		button_7.setText("Run Optimization");
		verticalPanel_1.add(button_7);
		
		VerticalSplitPanel verticalSplitPanel_2 = new VerticalSplitPanel();
		verticalPanel_1.add(verticalSplitPanel_2);
		verticalSplitPanel_2.setSplitPosition("20%");
		verticalSplitPanel_2.setSize("434px", "155px");
		
		DecoratedTabBar decoratedTabBar_2 = new DecoratedTabBar();
		decoratedTabBar_2.addTab("Max Biomass");
		decoratedTabBar_2.addTab("Customer Defined");
		verticalSplitPanel_2.setTopWidget(decoratedTabBar_2);
		decoratedTabBar_2.setSize("100%", "100%");
		
		Grid grid_1 = new Grid(2, 2);
		verticalSplitPanel_2.setBottomWidget(grid_1);
		grid_1.setSize("100%", "100%");
		
		Label lblBiomassComponents_1 = new Label("Biomass Components");
		grid_1.setWidget(0, 0, lblBiomassComponents_1);
		
		Label lblWeightingFactor_2 = new Label("Weighting Factor");
		grid_1.setWidget(0, 1, lblWeightingFactor_2);
		
		Label label_6 = new Label("New label");
		grid_1.setWidget(1, 0, label_6);
		
		TextBox textBox_5 = new TextBox();
		grid_1.setWidget(1, 1, textBox_5);
		
		VerticalSplitPanel verticalSplitPanel_1 = new VerticalSplitPanel();
		rootPanel.add(verticalSplitPanel_1, 538, 18);
		verticalSplitPanel_1.setSplitPosition("15%");
		verticalSplitPanel_1.setSize("386px", "210px");
		
		DecoratedTabBar decoratedTabBar_1 = new DecoratedTabBar();
		decoratedTabBar_1.addTab("Statistics");
		decoratedTabBar_1.addTab("Visualization");
		verticalSplitPanel_1.setTopWidget(decoratedTabBar_1);
		decoratedTabBar_1.setSize("100%", "100%");
		
		Grid grid = new Grid(7, 2);
		verticalSplitPanel_1.setBottomWidget(grid);
		grid.setSize("100%", "100%");
		
		Label lblBacteriaNameshortName = new Label("Bacteria Name+short name");
		grid.setWidget(0, 0, lblBacteriaNameshortName);
		
		Label lblTotalNumbersOf = new Label("Total numbers of genes:");
		grid.setWidget(1, 0, lblTotalNumbersOf);
		
		Label label_7 = new Label("New label");
		grid.setWidget(1, 1, label_7);
		
		Label lblTotalNumbersOf_1 = new Label("Total numbers of annotated genes:");
		grid.setWidget(2, 0, lblTotalNumbersOf_1);
		
		Label label_8 = new Label("New label");
		grid.setWidget(2, 1, label_8);
		
		Label lblTotalNumbersOf_2 = new Label("Total numbers of orthologues:");
		grid.setWidget(3, 0, lblTotalNumbersOf_2);
		
		Label label_13 = new Label("New label");
		grid.setWidget(3, 1, label_13);
		
		Label lblTotalNumbersOf_3 = new Label("Total numbers of annotated orthologues:");
		grid.setWidget(4, 0, lblTotalNumbersOf_3);
		
		Label label_14 = new Label("New label");
		grid.setWidget(4, 1, label_14);
		
		Label lblTotalNumbersOf_4 = new Label("Total numbers of pathways:");
		grid.setWidget(5, 0, lblTotalNumbersOf_4);
		
		Label label_15 = new Label("New label");
		grid.setWidget(5, 1, label_15);
		
		Label lblTotalNumbersOf_5 = new Label("Total numbers of active pathways:");
		grid.setWidget(6, 0, lblTotalNumbersOf_5);
		
		Label label_16 = new Label("New label");
		grid.setWidget(6, 1, label_16);
		
		Grid grid_2 = new Grid(7, 2);
		rootPanel.add(grid_2, 24, 18);
		grid_2.setSize("342px", "256px");
		
		Label lblBacteriaNamexxx = new Label("Bacteria Name (XXX)");
		grid_2.setWidget(0, 0, lblBacteriaNamexxx);
		
		Label lblStatistics = new Label("Statistics");
		grid_2.setWidget(1, 0, lblStatistics);
		
		Label lblRows = new Label("6 rows");
		grid_2.setWidget(2, 0, lblRows);
		
		Label lblObjectiveFunctions = new Label("Objective Functions");
		grid_2.setWidget(3, 0, lblObjectiveFunctions);
		
		Label lblAllTheObj = new Label("All the Obj Rxn ");
		grid_2.setWidget(4, 0, lblAllTheObj);
		
		Label lblWeightingFactor_1 = new Label("Weighting factor");
		grid_2.setWidget(4, 1, lblWeightingFactor_1);
		
		Label lblFluxDistributions = new Label("Flux distributions");
		grid_2.setWidget(5, 0, lblFluxDistributions);
		
		Label lblFluxValues = new Label("Flux values");
		grid_2.setWidget(5, 1, lblFluxValues);
		
		Button button_8 = new Button("New button");
		button_8.setText("Download as pdf");
		grid_2.setWidget(6, 1, button_8);
	}
}

