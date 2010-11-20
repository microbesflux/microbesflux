package edu.wustl.keggproject.client;

import com.smartgwt.client.data.XJSONDataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class WorkPanel{
	
	private static HorizontalSplitPanel instance;
	private static final String baseurl = "http://128.252.160.238:8000/";
	
	final Anchor titlePanel_welcome = new Anchor("Welcome", "#");
	final Anchor titlePanel_genome = new Anchor("Genomic Information", "#");
	final Anchor titlePanel_pathway = new Anchor("Metabolic Pathways", "#");
	final Anchor titlePanel_optimization = new Anchor("Optimization", "#");
	final Anchor titlePanel_result = new Anchor("Result", "#");
	VerticalPanel titlePanel; 
	private String id = "det";
	private int uid = 0;
	
	public WorkPanel(){
		if (instance == null){
			instance = new HorizontalSplitPanel();
		}
	}
	
	public HorizontalSplitPanel getPanel()
	{
		return instance;
	}
	
	public void initialize(){
		instance.setSize("100%", "100%");
		instance.setSplitPosition("20%");
		initializeLeft();
		changeToWelcome();
	}
	
	public void changeToPathway() {
		clearRight();
		
		final ListGrid pathwayModule = new ListGrid();
		pathwayModule.setWidth(400);
		pathwayModule.setHeight(500);
		pathwayModule.setShowAllRecords(true);
		pathwayModule.setDataSource(JSONDS.getInstance());
		
		ListGridField ko = new ListGridField("ko");
		ListGridField reac = new ListGridField("reaction");
		ListGridField arrow = new ListGridField("arrow");
		ListGridField reactants = new ListGridField("reactants");
		ListGridField products = new ListGridField("products");
		ListGridField pathway = new ListGridField("pathway");
		pathway.setHidden(true);
		pathwayModule.setFields(ko, reac, reactants, arrow, products, pathway);
		
		pathwayModule.setAutoFetchData(true);
		pathwayModule.setGroupByField("pathway");
		
		HorizontalPanel pathwayAndSavePanel = new HorizontalPanel();
		pathwayAndSavePanel.add(pathwayModule);
		
		
		
		final VerticalPanel pathwayPanel=new VerticalPanel();
		
		final DynamicForm form = new DynamicForm();  
		
		// form.setIsGroup(true);  
		form.setNumCols(4);
		form.setDataSource(JSONDS.getInstance());
		form.setVisible(false);
		
		pathwayAndSavePanel.add(form);
		
		final Button buttonSave = new Button();
		buttonSave.setText("Save");
	
		buttonSave.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				form.saveData();
				form.setVisible(false);
				buttonSave.setVisible(false);
			}
		});
		
        pathwayAndSavePanel.add(buttonSave);
        buttonSave.setVisible(false);
        
		pathwayModule.addRecordClickHandler(new RecordClickHandler() {  

			@Override
			public void onRecordClick(RecordClickEvent event) {
				 form.setVisible(true);
				 buttonSave.setVisible(true);
				 form.reset();
	             form.editSelectedData(pathwayModule);  
			}
		});
		
		
		
		
		HorizontalPanel addPanel = new HorizontalPanel();
		
		VerticalPanel textPanel = new VerticalPanel();
		VerticalPanel operationPanel = new VerticalPanel();
		
		addPanel.add(textPanel);
		addPanel.add(operationPanel);
		
		final TextBox rbox = new TextBox();
		final Label ext1 = new Label(".ext");
		final Label ext2 = new Label(".ext");
		final TextBox prod = new TextBox();
		
		ext1.setVisible(false);
		ext2.setVisible(false);
		
		Label arrowl = new Label("----->");
		
		textPanel.add(rbox);
		textPanel.add(ext1);
		
		textPanel.add(arrowl);
		textPanel.add(prod);
		textPanel.add(ext2);
		
		final ListBox lb = new ListBox();
		final String pathway_values[] = { "BIOMASS", "Inflow", "Outflow", "Heterogeneous Pathways"};
		
		lb.addItem("BIOMASS");
		lb.addItem("Inflow");
		lb.addItem("Outflow");
		lb.addItem("Heterogeneous Pathways");
		
		lb.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				int v = lb.getSelectedIndex();
				if ( v ==0 ) {
					prod.setValue("BIOMASS");
					prod.setEnabled(false);
					ext1.setVisible(false);
					ext2.setVisible(false);
				}
				else if (v == 1) {
					prod.setValue("");
					prod.setEnabled(true);
					ext1.setVisible(true);
					ext2.setVisible(false);
				}
				else if (v == 2) {
					prod.setValue("");
					prod.setEnabled(true);
					ext2.setVisible(true);
					ext1.setVisible(false);
				}
				else {
					prod.setValue("");
					prod.setEnabled(true);
					ext1.setVisible(false);
					ext2.setVisible(false);
				}
			}
		}
		);
		
		final Button buttonAdd = new Button();
		buttonAdd.setText("Add");
		
		buttonAdd.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				ListGridRecord r = new ListGridRecord();			
				r.setAttribute("ko", false);
				r.setAttribute("reactants", rbox.getText());
				r.setAttribute("products", prod.getText());
				r.setAttribute("pathway", 	pathway_values[ lb.getSelectedIndex()]);
				r.setAttribute("arrow", 0);
				pathwayModule.addData(r);
				// pathwayModule.startEditingNew();
			}
		});
		
		operationPanel.add(lb);
		operationPanel.add(buttonAdd);
		
		

		// final HorizontalPanel buttonPanel=new HorizontalPanel();
		// buttonPanel.add(buttonAdd);
		// buttonPanel.add(buttonSave);
		
		
		// pathwayPanel.add(pathwayModule);
		pathwayPanel.add(pathwayAndSavePanel);
		// pathwayPanel.add(buttonPanel);
		pathwayPanel.add(addPanel);
		
		instance.setRightWidget(pathwayPanel);

	}
	
	public void changeToWelcome(){
		clearRight();
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
		//Tab module		
		String introductionContent="Here is what we will input in the Introduciton tab";
		String functionContent="Here is what we will input in the Function tab";
		String flowchartContent="Here is what we will input in the Flowchart tab";
		String demoContent="Here is what we will input in the Demo tab";
		String helpContent="Here is what we will input in the Help tab";
		
		Label introductionForm=new Label(introductionContent);
		Label functionForm=new Label(functionContent);
		Label flowchartForm=new Label(flowchartContent);
		Label demoForm=new Label(demoContent);
		Label helpForm=new Label(helpContent);
				
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
		instance.setRightWidget(topTabSet);
		
	}
	
	public void sendGenomicInformation(String id){
		this.id = id;
	}
	
	public void changeToGenome()
	{
		clearRight();
		ListGrid l = new ListGrid();
		XJSONDataSource stat = new XJSONDataSource();
		stat.setDataURL(baseurl + "stat/?name="+id);

		DataSourceTextField itemField = new DataSourceTextField("name", "Item");  
		DataSourceTextField valueField = new DataSourceTextField("value", "Value");  

		stat.setFields(itemField, valueField);  

		l.setShowAllRecords(true);  
		l.setDataSource(stat);  
		l.fetchData();
		l.setSize("600", "500");	
		instance.setRightWidget(l);		
	}
	
	public void clearRight(){
		instance.setRightWidget(null);
	}
	
	public void clearStyle(){
		//default display
		titlePanel_welcome.removeStyleName("Link-selected");
		titlePanel_genome.removeStyleName("Link-selected");
		titlePanel_pathway.removeStyleName("Link-selected");
		titlePanel_optimization.removeStyleName("Link-selected");
		titlePanel_result.removeStyleName("Link-selected");
		titlePanel_welcome.addStyleName("Link-selected");	
	}
	
	
	public void initializeLeft(){
		
		titlePanel = new VerticalPanel();
		titlePanel.add(titlePanel_welcome);
		
		titlePanel_welcome.addClickHandler(new ClickHandler() {
			public void onClick(final ClickEvent event) {
				clearStyle();
				changeToWelcome();
			}
		});
		
		titlePanel.add(titlePanel_genome);	
		titlePanel_genome.addClickHandler(new ClickHandler() {
				public void onClick(final ClickEvent event) {
					changeToGenome();
				}
			}
		);
			
		
		titlePanel.add(titlePanel_pathway);
		
		titlePanel_pathway.addClickHandler(new ClickHandler() {
			public void onClick(final ClickEvent event) {
				changeToPathway();
			}
		}
		);
		
		titlePanel.add(titlePanel_optimization);
		
		titlePanel_optimization.addClickHandler(new ClickHandler() {
			public void onClick(final ClickEvent event) {
				changeToOptimization();
			}
		}
		);
		
		titlePanel.add(titlePanel_result);
		titlePanel_result.addClickHandler(new ClickHandler() {
			public void onClick(final ClickEvent event) {
				changeToResult();
			}
		}
		);
		
		instance.setLeftWidget(titlePanel);
		clearStyle();
	}
	
	protected void changeToResult() {
		// TODO Auto-generated method stub
		
	}
	protected void changeToOptimization() {
		clearRight();
		final ListGrid optimizationModule = new ListGrid();
		
		optimizationModule.setWidth(700);
		optimizationModule.setHeight(500);
		optimizationModule.setShowAllRecords(true);
		optimizationModule.setDataSource(OptimizationDS.getInstance());
		
		ListGridField right 		= new ListGridField("r");
		ListGridField symbol 	= new ListGridField("s");
		ListGridField left = new ListGridField("l");
		ListGridField type = new ListGridField("t");
		type.setHidden(true);
		
		optimizationModule.setFields(right, symbol, left, type);
		optimizationModule.setAutoFetchData(true);
		optimizationModule.setGroupByField("t");
		
		final VerticalPanel pathwayPanel=new VerticalPanel();  
		final HorizontalPanel buttonPanel=new HorizontalPanel();
		
		pathwayPanel.add(optimizationModule);
		pathwayPanel.add(buttonPanel);
		
		instance.setRightWidget(pathwayPanel);
		
	}
}



