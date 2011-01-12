package edu.wustl.keggproject.client;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.data.XJSONDataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

import edu.wustl.keggproject.client.datasource.BoundaryDS;
import edu.wustl.keggproject.client.datasource.ConstraintDS;
import edu.wustl.keggproject.client.datasource.ObjectiveDS;
import edu.wustl.keggproject.client.datasource.PathwayDS;

public class WorkPanel {

	private static HorizontalSplitPanel instance;
	private static final String baseurl = "http://128.252.160.238:8000/";

	final Anchor titlePanel_genome = new Anchor("Genomic Information", "#");
	final Anchor titlePanel_pathway = new Anchor("Metabolic Pathways", "#");
	final Anchor titlePanel_optimization = new Anchor("Optimization", "#");
	final Anchor titlePanel_result = new Anchor("Result", "#");
	VerticalPanel titlePanel;
	private String id = "det";
	private int uid = 0;

	VerticalPanel panel = new VerticalPanel();
	// SuggestBox suggestBox = new BateriaSuggestionBox();
	SuggestBox suggestBox = new SuggestBox();

	public WorkPanel() {
		if (instance == null) {
			instance = new HorizontalSplitPanel();
		}
	}

	public HorizontalSplitPanel getPanel() {
		return instance;
	}

	public void initialize() {
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
		pathwayModule.setDataSource(PathwayDS.getInstance());

		ListGridField ko = new ListGridField("ko");
		ListGridField reac = new ListGridField("reactionid");
		ListGridField arrow = new ListGridField("arrow");
		ListGridField reactants = new ListGridField("reactants");
		ListGridField products = new ListGridField("products");
		ListGridField pathway = new ListGridField("pathway");
		pathway.setHidden(true);

		pathwayModule.setFields(ko, reac, reactants, arrow, products, pathway);

		pathwayModule.setAutoFetchData(true);
		// pathwayModule.setSortField("pathway");
		pathwayModule.setGroupByField("pathway");
		HorizontalPanel pathwayAndSavePanel = new HorizontalPanel();
		pathwayAndSavePanel.add(pathwayModule);

		final VerticalPanel pathwayPanel = new VerticalPanel();

		final DynamicForm form = new DynamicForm();

		// form.setIsGroup(true);
		form.setNumCols(4);
		form.setDataSource(PathwayDS.getInstance());
		form.setVisible(false);

		pathwayAndSavePanel.add(form);

		final Button buttonSave = new Button();
		buttonSave.setText("Save");

		buttonSave.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				form.saveData();
				form.setVisible(false);
				buttonSave.setVisible(false);
				pathwayModule.markForRedraw();
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
				final FormItem[] formitem = form.getFields();
				formitem[1].disable();
				// formitem[4].disable();
				System.out.println(">>>"
						+ pathwayModule.getSelectedRecord().getAttribute(
								"pathway") + "<<<<");
				if (pathwayModule.getSelectedRecord().getAttribute("pathway")
						.equals("BIOMASS")) {
					System.out.println("Herehehrehrhehre");
					formitem[4].disable();
				}

			}
		});

		HorizontalPanel addPanel = new HorizontalPanel();

		final VerticalPanel textPanel = new VerticalPanel();
		VerticalPanel operationPanel = new VerticalPanel();

		addPanel.add(textPanel);
		addPanel.add(operationPanel);

		final TextBox rbox = new TextBox();
		final Label ext1 = new Label(".ext");
		final Label ext2 = new Label(".ext");
		final TextBox prod = new TextBox();

		ext1.setVisible(false);
		ext2.setVisible(false);

		// Label arrowl = new Label("----->");

		final ListBox arrow1 = new ListBox();
		arrow1.addItem("--->");
		arrow1.addItem("<-->");

		textPanel.add(rbox);
		textPanel.add(ext1);

		textPanel.add(arrow1);
		textPanel.add(prod);
		textPanel.add(ext2);

		final ListBox lb = new ListBox();
		final String pathway_values[] = { "BIOMASS", "Inflow", "Outflow",
				"Heterogeneous Pathways" };

		lb.addItem("BIOMASS");
		lb.addItem("Inflow");
		lb.addItem("Outflow");
		lb.addItem("Heterogeneous Pathways");
		prod.setValue("BIOMASS");
		prod.setEnabled(false);

		lb.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				int v = lb.getSelectedIndex();
				if (v == 0) {
					prod.setValue("BIOMASS");
					prod.setEnabled(false);
					ext1.setVisible(false);
					ext2.setVisible(false);
					arrow1.setSelectedIndex(0);
					arrow1.setEnabled(false);
					rbox.setValue("");
				} else if (v == 1) {
					prod.setValue("");
					prod.setEnabled(true);
					ext1.setVisible(true);
					ext2.setVisible(false);
					arrow1.setSelectedIndex(0);
					arrow1.setEnabled(false);
					rbox.setValue("");
				} else if (v == 2) {
					prod.setValue("");
					prod.setEnabled(true);
					ext2.setVisible(true);
					ext1.setVisible(false);
					arrow1.setSelectedIndex(0);
					arrow1.setEnabled(false);
					rbox.setValue("");
				} else {
					prod.setValue("");
					prod.setEnabled(true);
					ext1.setVisible(false);
					ext2.setVisible(false);
					arrow1.setSelectedIndex(1);
					arrow1.setEnabled(true);
					rbox.setValue("");
				}
			}
		});

		final Button buttonAdd = new Button();
		buttonAdd.setText("Add");

		buttonAdd.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				// for(int i=0;i<pathwayModule.getRecords().length;i++){
				// if(pathwayModule.getRecord(i).getAttribute("pathway").equals("BIOMASS")){
				// SC.say("Only one reaction is allowed in BIOMASS!");
				// return;
				// }
				// }

				if (rbox.getText().length() == 0) {
					SC.say("Reactants cannot be empty!");
					return;
				}
				if (prod.getText().length() == 0) {
					SC.say("Products cannot be empty!");
					return;
				}

				ListGridRecord r = new ListGridRecord();
				r.setAttribute("ko", false);
				r.setAttribute("reactants", rbox.getText());
				r.setAttribute("products", prod.getText());
				r.setAttribute("pathway", pathway_values[lb.getSelectedIndex()]);
				r.setAttribute("arrow", 0);
				// r.setAttribute(property, value)
				pathwayModule.addData(r);
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

	public void changeToWelcome() {
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
		instance.setRightWidget(topTabSet);

	}

	public void sendGenomicInformation(String id) {
		this.id = id;
	}

	public void changeToGenome() {
		clearRight();
		ListGrid l = new ListGrid();
		XJSONDataSource stat = new XJSONDataSource();
		stat.setDataURL(baseurl + "pathway/stat/?pathway_name=" + id);

		DataSourceTextField itemField = new DataSourceTextField("name", "Item");
		DataSourceTextField valueField = new DataSourceTextField("value",
				"Value");

		stat.setFields(itemField, valueField);

		l.setShowAllRecords(true);
		l.setDataSource(stat);
		l.fetchData();
		l.setSize("600", "500");
		instance.setRightWidget(l);
	}

	public void clearRight() {
		instance.setRightWidget(null);
	}

	public void clearStyle() {
		// default display
		// titlePanel_welcome.removeStyleName("Link-selected");
		titlePanel_genome.removeStyleName("Link-selected");
		titlePanel_pathway.removeStyleName("Link-selected");
		titlePanel_optimization.removeStyleName("Link-selected");
		titlePanel_result.removeStyleName("Link-selected");
		// titlePanel_welcome.addStyleName("Link-selected");
	}

	public void initializeLeft() {

		titlePanel = new VerticalPanel();

		suggestBox.setLimit(20);
		suggestBox.setStyleName("gwt-SuggestBox-F1");
		suggestBox.setText("");
		suggestBox.setSize("200px", "20px");

		// Label suggestBoxLabel=new Label("Enter KEGG Organisms");
		// panel.add(suggestBoxLabel);
		// suggestBoxLabel.setSize("200px", "20px");
		panel.add(suggestBox);

		// KEGG organism link module
		HTMLPane paneLink = new HTMLPane();
		paneLink.setContents("<a href=\"http://www.genome.jp/kegg/catalog/org_list.html\" target=\"_blank\">Input KEGG Organisms</a>");
		paneLink.setPosition("Center"); // not quite understand how setPosition
										// works
		HStack layoutpaneLink = new HStack();
		layoutpaneLink.addMember(paneLink);
		layoutpaneLink.setSize("100px", "40px");

		// "run" button module
		Button buttonRun = new Button();
		buttonRun.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				String nid = suggestBox.getText().split(" ")[0];
				// String pathway_name = "eric1";

				sendGenomicInformation(nid);
				changeToGenome();
			}
		});
		buttonRun.setText("Run");
		buttonRun.setSize("60px", "30px");

		titlePanel.add(layoutpaneLink);
		titlePanel.add(panel);
		titlePanel.add(buttonRun);
		// titlePanel.add(titlePanel_welcome);

		titlePanel.add(titlePanel_genome);
		titlePanel_genome.addClickHandler(new ClickHandler() {
			public void onClick(final ClickEvent event) {
				changeToGenome();
			}
		});

		titlePanel.add(titlePanel_pathway);

		titlePanel_pathway.addClickHandler(new ClickHandler() {
			public void onClick(final ClickEvent event) {
				changeToPathway();
			}
		});

		titlePanel.add(titlePanel_optimization);

		titlePanel_optimization.addClickHandler(new ClickHandler() {
			public void onClick(final ClickEvent event) {
				changeToOptimization();
			}
		});

		titlePanel.add(titlePanel_result);
		titlePanel_result.addClickHandler(new ClickHandler() {
			public void onClick(final ClickEvent event) {
				changeToResult();
			}
		});

		instance.setLeftWidget(titlePanel);
		clearStyle();
	}

	protected void changeToResult() {
		// TODO Auto-generated method stub

	}

	protected void changeToOptimization() {
		clearRight();
		final VerticalPanel modelPanel = new VerticalPanel();

		// objective function module
		final ListGrid objectiveList = new ListGrid();

		/*
		 * optimizationModule.setWidth(700); optimizationModule.setHeight(500);
		 * optimizationModule.setShowAllRecords(true);
		 * optimizationModule.setDataSource(OptimizationDS.getInstance());
		 * 
		 * ListGridField right = new ListGridField("r"); ListGridField symbol =
		 * new ListGridField("s"); ListGridField left = new ListGridField("l");
		 * ListGridField type = new ListGridField("t"); type.setHidden(true);
		 * 
		 * optimizationModule.setFields(right, symbol, left, type);
		 * optimizationModule.setAutoFetchData(true);
		 * optimizationModule.setGroupByField("t");
		 */
		objectiveList.setDataSource(ObjectiveDS.getInstance());
		ListGridField reaction = new ListGridField("r");
		ListGridField weight = new ListGridField("w");
		objectiveList.setFields(reaction, weight);
		objectiveList.setAutoFetchData(true);
		objectiveList.setShowResizeBar(true);

		final DynamicForm form = new DynamicForm();
		// form.setNumCols(4);
		form.setDataSource(ObjectiveDS.getInstance());
		// form.setVisible(false);

		final VerticalPanel objectiveUpdatePanel = new VerticalPanel();

		final Button buttonSave = new Button();
		buttonSave.setText("Save");

		buttonSave.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				form.saveData();
				form.setVisible(false);
				buttonSave.setVisible(false);
			}
		});

		objectiveUpdatePanel.setVisible(false);
		objectiveUpdatePanel.add(form);
		objectiveUpdatePanel.add(buttonSave);

		// objectiveList.disable();

		objectiveList.addRecordClickHandler(new RecordClickHandler() {
			@Override
			public void onRecordClick(RecordClickEvent event) {
				objectiveUpdatePanel.setVisible(true);
				form.setVisible(true);
				buttonSave.setVisible(true);
				form.reset();
				form.editSelectedData(objectiveList);
				final FormItem[] formitem = form.getFields();
				formitem[0].disable();
			}
		});

		final ListBox maxmin = new ListBox();
		maxmin.addItem("maximize");
		maxmin.addItem("minimize");

		final ListBox obj = new ListBox();
		obj.addItem("Customer-defined objective function");
		obj.addItem("Biomass");
		obj.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				int index = obj.getSelectedIndex();
				if (index == 1) {
					Window.alert("Please be sure the biomass equation has been input in pathway");
					objectiveList.disable();
				}
				if (index == 0) {
					objectiveList.enable();
				}
			}
		});

		final HorizontalPanel objectivelistModule = new HorizontalPanel();
		objectivelistModule.add(objectiveList);
		objectivelistModule.add(objectiveUpdatePanel);

		final HorizontalPanel objectiveModule = new HorizontalPanel();
		objectiveModule.add(maxmin);
		objectiveModule.add(obj);

		final VerticalPanel objectivePanel = new VerticalPanel();
		objectivePanel.add(objectiveModule);
		objectivePanel.add(objectivelistModule);

		// Linear constraint module
		final ListGrid constraintList = new ListGrid();
		constraintList.setDataSource(ConstraintDS.getInstance()); // data source
																	// for
																	// constrainList
		ListGridField compound = new ListGridField("c"); // term in data source
		ListGridField constraint = new ListGridField("r"); // term in data
															// source
		constraintList.setFields(compound, constraint);
		constraintList.setAutoFetchData(true);
		constraintList.setShowResizeBar(true);

		final VerticalPanel constraintPanel = new VerticalPanel();
		constraintPanel.setTitle("linear constraints (Sv=0):");
		constraintPanel.add(constraintList);

		// Boundary module

		final ListGrid boundaryList = new ListGrid();
		final DynamicForm boundaryForm = new DynamicForm();

		boundaryList.setDataSource(BoundaryDS.getInstance());
		ListGridField boundaryReaction = new ListGridField("r");
		ListGridField lb = new ListGridField("l");
		ListGridField ub = new ListGridField("u");
		boundaryList.setFields(boundaryReaction, lb, ub);
		boundaryList.setAutoFetchData(true);
		boundaryList.setShowResizeBar(true);

		boundaryForm.setDataSource(BoundaryDS.getInstance());
		boundaryForm.setVisible(false);

		final VerticalPanel boundaryUpdatePanel = new VerticalPanel();

		final Button boundarySave = new Button();
		boundarySave.setText("Save");

		boundarySave.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				float l, u;
				try {
					l = Float.valueOf(boundaryForm.getValueAsString("l"))
							.floatValue();
					u = Float.valueOf(boundaryForm.getValueAsString("u"))
							.floatValue();
				} catch (NumberFormatException e) {
					Window.alert("Boundary must a valid number."); // initialValue)
					return;
				}
				System.out.println(l);
				System.out.println(u);

				if (l > u) {
					Window.alert("Lower must be smaller than upper bound"); // initialValue)
					return;
				}
				boundaryForm.saveData();

				boundaryForm.setVisible(false);
				boundarySave.setVisible(false);
			}
		});

		boundaryUpdatePanel.add(boundaryForm);
		boundaryUpdatePanel.add(boundarySave);

		boundarySave.setVisible(false);

		boundaryList.addRecordClickHandler(new RecordClickHandler() {
			@Override
			public void onRecordClick(RecordClickEvent event) {
				boundaryForm.setVisible(true);
				boundarySave.setVisible(true);
				boundaryForm.reset();
				boundaryForm.editSelectedData(boundaryList);
				final FormItem[] boundaryformitem = boundaryForm.getFields();
				boundaryformitem[0].disable();
			}
		});
		final HorizontalPanel boundaryPanel = new HorizontalPanel();
		boundaryPanel.setTitle("Boundary for fluxes (lb<=v<=ub)");
		boundaryPanel.add(boundaryList);
		boundaryPanel.add(boundaryUpdatePanel);

		final Button submitButton = new Button();
		final Button rundfbaButton = new Button();

		submitButton.setText("Submit FBA Job");
		rundfbaButton.setText("Set for Dynamic FBA");

		final HorizontalPanel runbuttonPanel = new HorizontalPanel();
		runbuttonPanel.add(submitButton);
		runbuttonPanel.add(rundfbaButton);

		submitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// To do, submit the file to server
			}
		});

		final VerticalPanel vPanel = new VerticalPanel();
		String instruction = "Note:<br/>1) Please upload the data file for extracellular metabolites kinetics as shown in the sample file;<br/>2) Make sure the names of metabolites are consistant with those in the genome-scale FBA;<br/>3) The maximun number of time intervals allowed in dFBA is 10,000";
		final Label instructiondFBA = new Label(instruction);

		// Add a label
		// vPanel.add(new HTML());
		vPanel.add(instructiondFBA);

		// Add a file upload widget
		final FileUpload fileUpload = new FileUpload();
		fileUpload.setName("uploadFormElement");
		final FormPanel fPanel = new FormPanel();
		final VerticalPanel holder = new VerticalPanel();
		final Button uploadButton = new Button();

		holder.add(fileUpload);
		holder.add(uploadButton);
		fPanel.add(holder);

		fPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		fPanel.setMethod(FormPanel.METHOD_POST);
		fPanel.setAction("http://128.252.160.238:8000/model/upload");
		// fPanel.setAction(GWT.getModuleBaseURL() + "fileupload");

		// Add a button to upload the file

		uploadButton.setText("Upload dFBA Data File");
		/*
		 * uploadButton.addClickHandler(new ClickHandler() { public void
		 * onClick(ClickEvent event) { String filename =
		 * fileUpload.getFilename(); if (filename.length() == 0) {
		 * Window.alert("You must select a file to upload"); } else {
		 * fPanel.submit(); Window.alert("File uploaded"); } } });
		 */
		uploadButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				fPanel.submit();
			}
		});

		fPanel.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				// Window.alert(event.getResults());
				Window.alert("File Uploaded");
			}
		});

		fPanel.addSubmitHandler(new FormPanel.SubmitHandler() {

			@Override
			public void onSubmit(SubmitEvent event) {
				if (fileUpload.getFilename().length() == 0) {
					Window.alert("The text box must not be empty");
					event.cancel();
				}
			}
		});

		vPanel.add(fPanel);
		vPanel.setVisible(false);

		rundfbaButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				vPanel.setVisible(true);
			}
		});

		modelPanel.add(objectivePanel);
		modelPanel.add(constraintPanel);
		modelPanel.add(boundaryPanel);
		modelPanel.add(runbuttonPanel);
		modelPanel.add(vPanel);

		instance.setRightWidget(modelPanel);

	}
}
