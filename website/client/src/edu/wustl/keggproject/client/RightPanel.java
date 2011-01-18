package edu.wustl.keggproject.client;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.data.XJSONDataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.HTMLPane;
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

public class RightPanel {

	private StatusFormPanel sf;
	private static final String baseurl = "http://128.252.160.238:8000/";
	private String id = "";
	private String tempvalue;

	VerticalPanel newModelPanel;
	BateriaSuggestionBox suggestBox;
	SimplePanel sp = new SimplePanel();

	public void initialize() {
		changeToWelcome();
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
		ListGrid l = new ListGrid();
		XJSONDataSource stat = new XJSONDataSource();
		stat.setDataURL(baseurl + "pathway/stat/?pathway_name=" + id);

		DataSourceTextField itemField = new DataSourceTextField("name", "Item");
		DataSourceTextField valueField = new DataSourceTextField("value", "Value");

		stat.setFields(itemField, valueField);

		l.setShowAllRecords(true);
		l.setDataSource(stat);
		l.fetchData();
		l.setSize("600", "500");
		sp.add(l);
		}

	public void ChangeToPathway() {
		sp.clear();
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

		final Button buttonAdd = new Button("Add");
		
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

		sp.add(pathwayPanel);

	}

	public void ChangeToOptimization() {
		sp.clear();
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
		form.setDataSource(ObjectiveDS.getInstance());
		

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
				// TODO, submit the file to server
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

		sp.add(modelPanel);

	}
	

	public void changeToWelcome() {
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
		String introductionContent = " Here is what we will input in the Introduciton tab";
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
							changeToWelcome();
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
								changeToWelcome();
								a.setText("[Log Out]");
							} else {
								changeToWelcome();
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
							changeToWelcome();
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
