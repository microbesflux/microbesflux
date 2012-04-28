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

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.http.client.Request;


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
	private String tempvalue;
	private String tempemail;

	VerticalPanel newModelPanel;
	BateriaSuggestionBox suggestBox;
	// TextBox suggestBox;
	
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

	public void changeToCreateANewCollection() {

		sf.clearForm();
		sf.clearStatus();
		sf.setStatus("Creating a new model");
		final Configuration conf = ConfigurationFactory.getConfiguration();

		final FormPanel createForm = new FormPanel();
		createForm.setAction(conf.getBaseURL() + "collection/create/");
		createForm.setMethod(FormPanel.METHOD_GET);

		final TextBox collectionbox = new TextBox();
		collectionbox.setName("collection_name");
		
		final TextBox emailbox = new TextBox();
		emailbox.setName("email");
		
		

		// HorizontalPanel strapanel = new HorizontalPanel();
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
//
//		suggestBox = new TextBox();
//		suggestBox.getElement().setId("suggest-box");
//		suggestBox.setName("suggest-box");
//		suggestBox.setText("");
//		suggestBox.setSize("200px", "20px");
		
		
		Button buttonRun = new Button();
		buttonRun.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				tempvalue = new String(collectionbox.getText());
				if (!tempvalue.matches("^[a-zA-Z0-9]$")) {
					Window.alert("Model name should only contains lettes and numbers.");
					return;
				}
				tempemail = new String(emailbox.getText());
				if (tempemail.length()==0) {
					Window.alert("Please Enter a valid email address");
					return;
				}
				
				if (!tempemail.contains("@")) {
					Window.alert("Invalid email address");
					return;
				}
				
				createForm.submit();
			}
		});
		buttonRun.setText("Run");
		buttonRun.setSize("60px", "30px");
		
		Grid newModelGrid = new Grid(4, 2);
		
		newModelGrid.setWidget(0, 0, new Label("Name of the model: "));
		newModelGrid.setWidget(0, 1, collectionbox);
		
		newModelGrid.setWidget(1, 0, new Label("Your Email Address: "));
		newModelGrid.setWidget(1, 1, emailbox);
		
		newModelGrid.setWidget(2, 0, layoutpaneLink);
		newModelGrid.setWidget(2, 1, suggestBox);
		
		newModelGrid.setWidget(3, 0, buttonRun);
		
		
		createForm.addSubmitHandler(new FormPanel.SubmitHandler() {
			@Override
			public void onSubmit(SubmitEvent event) {
				
			}
		});

		// TODO: debug can not get the current model name
		createForm
				.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
					public void onSubmitComplete(SubmitCompleteEvent event) {
						if (event.getResults().contains("already in use"))
						{
							Window.alert("You have already used the name for an existing model, please " +
									"choose another name.");
						}
						
						else {
							conf.setCurrentCollection(tempvalue);
							conf.setCurrentEmail(tempemail);
							sf.setStatus(" Current Model: "
									+ conf.getCurrentCollection());
							changeToGenome();
						}
					}
				});

		createForm.setWidget(newModelGrid);

		sp.clear();
		sp.add(createForm);
	}

	public void changeToGenome() {
		sp.clear();
		ListGrid l = new ListGrid();
		XJSONDataSource stat = new XJSONDataSource();
		Configuration conf = ConfigurationFactory.getConfiguration();
		stat.setDataURL(conf.getBaseURL() + "pathway/stat/?collection_name=" + conf.getCurrentCollection());

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
		pathwayModule.setWidth(600);
		pathwayModule.setHeight(500);
		pathwayModule.setShowAllRecords(true);
		pathwayModule.setDataSource(PathwayDS.getInstance());

		ListGridField ko = new ListGridField("ko");
		ListGridField reac = new ListGridField("reactionid");
		ListGridField arrow = new ListGridField("arrow");
		ListGridField reactants = new ListGridField("reactants");
		ListGridField products = new ListGridField("products");
		ListGridField pathway = new ListGridField("pathway");
		// pathway.setHidden(true);

		pathwayModule.setFields(ko, reac, reactants, arrow, products, pathway);

		pathwayModule.setAutoFetchData(true);
		// pathwayModule.setSortField("pathway");
		// pathwayModule.setGroupByField("pathway"); // Don't do Groupby
		pathwayModule.setSortField("pathway");
		
		HorizontalPanel pathwayAndSavePanel = new HorizontalPanel();
		pathwayAndSavePanel.add(pathwayModule);

		final VerticalPanel pathwayPanel = new VerticalPanel();
		final DynamicForm form = new DynamicForm();

		// form.setIsGroup(true);
		form.setNumCols(4);
		form.setDataSource(PathwayDS.getInstance());
		
		final VerticalPanel formAndSaveCancelPanel = new VerticalPanel();
		final Button buttonSave = new Button();
		final Button buttonCancel = new Button();
		
		buttonSave.setText(" Save ");
		buttonCancel.setText("Cancel");
		
		formAndSaveCancelPanel.add(form);
		formAndSaveCancelPanel.add(buttonSave);
		formAndSaveCancelPanel.add(buttonCancel);
		formAndSaveCancelPanel.setVisible(false);
		pathwayAndSavePanel.add(formAndSaveCancelPanel);
		
		buttonCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				formAndSaveCancelPanel.setVisible(false);
				// form.setVisible(false);
				// buttonSave.setVisible(false);
				// buttonCancel.setVisible(false);
			}
		});
		
		buttonSave.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				form.saveData();
				formAndSaveCancelPanel.setVisible(false);		
				pathwayModule.markForRedraw();
			}
		});
				
		pathwayModule.addRecordClickHandler(new RecordClickHandler() {

			@Override
			public void onRecordClick(RecordClickEvent event) {
				formAndSaveCancelPanel.setVisible(true);
				
				// form.setVisible(true);
				// buttonSave.setVisible(true);
				// buttonCancel.setVisible(true);
				
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
					// System.out.println("Herehehrehrhehre");
					formitem[4].disable();
				}

			}
		});

		
		/* For adding pathways */
		final Grid gridPanel= new Grid(8, 3);
		final Label addReactionText = new Label("Add reactions");
		gridPanel.setWidget(0, 0, addReactionText);
		
		
		// HorizontalPanel addPanel = new HorizontalPanel();
		// final VerticalPanel textPanel = new VerticalPanel();
		// VerticalPanel operationPanel = new VerticalPanel();
		// addPanel.add(textPanel);
		// addPanel.add(operationPanel);

		final Label reactionText = new Label("Reaction Type");
		gridPanel.setWidget(1, 0, reactionText);
		
		final Label reactantsText = new Label("Reactants");
		gridPanel.setWidget(2, 0, reactantsText);
		
		final TextBox rbox = new TextBox();
		gridPanel.setWidget(2, 1, rbox);
		
		final Label ext1 = new Label(".ext");
		ext1.setVisible(false);
		gridPanel.setWidget(2, 2, ext1);
		
		final Label directionLabel = new Label("Direction");
		final ListBox arrow1 = new ListBox();
		arrow1.addItem("====>");
		arrow1.addItem("<===>");
		
		gridPanel.setWidget(3, 0, directionLabel);
		gridPanel.setWidget(3, 1, arrow1);
		
		// Row 4: products
		final Label productsText = new Label("Products");
		final TextBox prod = new TextBox();
		final Label ext2 = new Label(".ext");		
		ext2.setVisible(false);
		
		// Default values.
		prod.setValue("BIOMASS");
		prod.setEnabled(false);
		arrow1.setSelectedIndex(0);
		arrow1.setEnabled(false);
		

		gridPanel.setWidget(4, 0, productsText);
		gridPanel.setWidget(4, 1, prod);
		gridPanel.setWidget(4, 2, ext2);
		
		final ListBox reactionTypeList = new ListBox();
		final String pathway_values[] = { "BIOMASS", "Inflow", "Outflow","Heterologous Pathways" };
		final Button buttonAdd = new Button("Add");
		
		reactionTypeList.addItem("BIOMASS");
		reactionTypeList.addItem("Inflow");
		reactionTypeList.addItem("Outflow");
		reactionTypeList.addItem("Heterologous Pathways");
		
		reactionTypeList.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				int v = reactionTypeList.getSelectedIndex();
				buttonAdd.setEnabled(false);
				if (v == 0) {
					prod.setValue("BIOMASS");
					prod.setEnabled(false);
					ext1.setVisible(false);
					ext2.setVisible(false);
					arrow1.setSelectedIndex(0);	// single direction
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

		gridPanel.setWidget(1, 1, reactionTypeList);
		
		
		final FormPanel checkForm = new FormPanel();
		
		Configuration conf = ConfigurationFactory.getConfiguration();
		checkForm.setAction(conf.getBaseURL() + "pathway/check/");
		checkForm.setMethod(FormPanel.METHOD_GET);
		checkForm.setWidget(gridPanel);
		
		
		
		buttonAdd.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

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
				r.setAttribute("pathway", pathway_values[reactionTypeList.getSelectedIndex()]);
				r.setAttribute("arrow", arrow1.getSelectedIndex());
				
				pathwayModule.addData(r);
				buttonAdd.setEnabled(false);
			}
		});
		
		checkForm.addSubmitHandler(new FormPanel.SubmitHandler() {
			public void onSubmit(SubmitEvent event) {
			}
		});
		
		checkForm.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent event) {
				if (event.getResults().contains("Valid")) {
					buttonAdd.setEnabled(true);
				}
			}
		});
		
		// rbox.addFocusListener(new FocusListener() {});
		rbox.addFocusHandler(new FocusHandler() {
			public void onFocus(FocusEvent event) {
				buttonAdd.setEnabled(false); // When user is highlight something
				// add becomes invalid
			}});
		
		prod.addFocusHandler(new FocusHandler() {
			public void onFocus(FocusEvent event) {
				buttonAdd.setEnabled(false); // When user is highlight something
				// add becomes invalid
			}});
		
		final Button buttonCheck = new Button("Validate Pathway");
		
		buttonCheck.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {				
				StringBuffer url = new StringBuffer();
				url.append(ConfigurationFactory.getConfiguration().getBaseURL());
				url.append("pathway/check/");
				url.append("?reactants=");
				url.append(rbox.getText());
				url.append("&");
				url.append("products=");
				url.append(prod.getText());
				url.append("&pathway=");
				url.append(pathway_values[reactionTypeList.getSelectedIndex()]);
				
				RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url.toString()));
				try {
					builder.sendRequest(null, new RequestCallback() {
				    public void onError(Request request, Throwable exception) {
				       return;
				    }

				    public void onResponseReceived(Request request, Response response) {
				      if (200 == response.getStatusCode()) {
				          if (response.getText().contains("Valid")) {
				        	  buttonAdd.setEnabled(true);
				          }
				          else {
				        	  Window.alert("Invalid compound name(s). " +
				        	  		"MicrobesFlux only accepts a subset of KEGG " +
				        	  		"compound names. "+
				        			"For a list of valid compound names used by " +
				        	  		"MicrobesFlux, visit " +
				        	  		"http://tanglab.engineering.wustl.edu/media/valid_compounds.html");
				        	  buttonAdd.setEnabled(false);
				          }
				      } else {
				    	  return;
				      }
				    }
				  });      
				} catch (com.google.gwt.http.client.RequestException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		buttonAdd.setEnabled(false);
		
		gridPanel.setWidget(5, 1, buttonCheck);
		gridPanel.setWidget(5, 2, buttonAdd);
		
		
		// gridPanel.setWidget(5, 1, buttonAdd);
		// pathwayPanel.add(gridPanel);

		pathwayPanel.add(pathwayAndSavePanel);		
		pathwayPanel.add(checkForm);
		
		final VerticalPanel vp = new VerticalPanel();
		final Grid sbmlandsvgPanel = new Grid(2,2);
		
		
		final Label exportLabel = new Label("Export");
		
		sbmlandsvgPanel.setWidget(0, 0, exportLabel);
		
		final Button sbmlb = new Button("Get SBML");
		final Button svgb = new Button("Get Pathway Map");
		
		
		final FormPanel SBMLform = new FormPanel();
		SBMLform.setAction(ConfigurationFactory.getConfiguration().getBaseURL()+"model/sbml/");
		SBMLform.setMethod(FormPanel.METHOD_GET);
		
		
		SBMLform.addSubmitHandler(new FormPanel.SubmitHandler() {
			public void onSubmit(SubmitEvent event) {
			}
		});
		
		SBMLform.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			
			public void onSubmitComplete(SubmitCompleteEvent event) {
				Window.alert("Job submitted");
			}
		});

		sbmlb.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SBMLform.submit();
			}
		});
		
		final FormPanel SVGForm = new FormPanel();
		SVGForm.setAction(ConfigurationFactory.getConfiguration().getBaseURL()+"model/svg/");
		SVGForm.setMethod(FormPanel.METHOD_GET);
		
		
		SVGForm.addSubmitHandler(new FormPanel.SubmitHandler() {
			public void onSubmit(SubmitEvent event) {
			}
		});
		
		SVGForm.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			
			public void onSubmitComplete(SubmitCompleteEvent event) {
				Window.alert("Job submitted");
			}
		});

		svgb.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SVGForm.submit();
			}
		});
		vp.add(SBMLform);
		vp.add(SVGForm);
		
		vp.add(sbmlandsvgPanel);
		sbmlandsvgPanel.setWidget(1, 0, sbmlb);
		sbmlandsvgPanel.setWidget(1, 1, svgb);
		

		
		pathwayPanel.add(vp);
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
		objectiveList.setSize("600", "250");
		
		ListGridField reaction = new ListGridField("r");
		ListGridField weight = new ListGridField("w");
		
		objectiveList.setFields(reaction, weight);
		objectiveList.setAutoFetchData(true);
		objectiveList.setShowResizeBar(true);

		final DynamicForm form = new DynamicForm();
		form.setDataSource(ObjectiveDS.getInstance());
		

		final VerticalPanel objectiveUpdatePanel = new VerticalPanel();

		final Button buttonSave = new Button();
		final Button buttonCancel = new Button();
		buttonCancel.setText("Cancel");
		buttonSave.setText("Save");

		final HorizontalPanel hp = new HorizontalPanel();
		hp.add(buttonSave);
		hp.add(buttonCancel);
		objectiveUpdatePanel.add(hp);
		hp.setVisible(false);
		
		buttonSave.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				form.saveData();
				form.setVisible(false);
				hp.setVisible(false);
			}
		});
		
		buttonCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				form.setVisible(false);
				hp.setVisible(false);
			}
		});
		objectiveUpdatePanel.setVisible(false);
		objectiveUpdatePanel.add(form);
		
		
		
		objectiveList.addRecordClickHandler(new RecordClickHandler() {
			@Override
			public void onRecordClick(RecordClickEvent event) {
				objectiveUpdatePanel.setVisible(true);
				form.setVisible(true);
				hp.setVisible(true);
				form.reset();
				form.editSelectedData(objectiveList);
				final FormItem[] formitem = form.getFields();
				formitem[0].disable();
			}
		});

		// final ListBox maxmin = new ListBox();
		// maxmin.addItem("maximize");
		// maxmin.addItem("minimize");
		final Label maxmin = new Label("maximize  ");
		
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
		constraintList.setSize("600", "250");
		
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
		boundaryList.setSize("600", "250");
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
		final Button boundaryCancel = new Button();
		
		boundarySave.setText("Save");
		boundaryCancel.setText("Cancel");
		boundaryUpdatePanel.add(boundaryForm);
		final HorizontalPanel hp2 = new HorizontalPanel();
		hp2.add(boundarySave);
		hp2.add(boundaryCancel);
		boundaryUpdatePanel.add(hp2);
		
		boundaryCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				hp2.setVisible(false);
				boundaryForm.setVisible(false);
				
			}
		});
		
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
				hp2.setVisible(false);
				
				// boundarySave.setVisible(false);
				// boundaryCancel.setVisible(false);
			}
		});

		// boundarySave.setVisible(false);
		hp2.setVisible(false);
		
		boundaryList.addRecordClickHandler(new RecordClickHandler() {
			@Override
			public void onRecordClick(RecordClickEvent event) {
				
				boundaryForm.setVisible(true);
				hp2.setVisible(true);
				
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
		
		final HorizontalPanel runbuttonPanel = new HorizontalPanel();
		final Button fbaSubmitButton = new Button("Submit FBA Job");
		final Button switchToDFBAbutton = new Button("Set for Dynamic FBA");
		runbuttonPanel.add(fbaSubmitButton);	
		runbuttonPanel.add(switchToDFBAbutton);
		
		fbaSubmitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				StringBuffer url = new StringBuffer();
				url.append(ConfigurationFactory.getConfiguration().getBaseURL());
				url.append("model/optimization/");
				url.append("?obj_type=" + obj.getSelectedIndex());
				RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url.toString()));
					try {
						builder.sendRequest(null, new RequestCallback() {
						public void onError(Request request, Throwable exception) {
						   return;
						}
						
						public void onResponseReceived(Request request, Response response) {
						  if (200 == response.getStatusCode()) {
						      if (response.getText().contains("New Optimization problem submitted")) {
						    	  Window.alert("FBA job submitted.");
						      }
						  } else {
							  return;
						  }
						}      
						});
					} catch (com.google.gwt.http.client.RequestException e) {
						e.printStackTrace();
					}
			}
		});
		
		final VerticalPanel vPanel = new VerticalPanel();
		
		String instruction = "<b>Note:</b> <br/> 1) Please upload the data file for extracellular metabolites kinetics as shown in the <a href=\"http://tanglab.engineering.wustl.edu/media/sample.txt\">sample file</a>; <br/> 2) Make sure the names of metabolites are consistant with those in the genome-scale FBA; <br/> 3) The maximun number of time intervals allowed in dFBA is 10,000";
		
		final HTML instructiondFBA = new HTML(instruction);
		
		vPanel.add(instructiondFBA);
		
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
		fPanel.setAction(ConfigurationFactory.getConfiguration().getBaseURL() + "model/upload/");
		
		uploadButton.setText("Upload dFBA Data File");
		uploadButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				fPanel.submit();
			}
		});

		fPanel.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				final Configuration conf = ConfigurationFactory.getConfiguration();
				// content = """Successfully Uploaded. \n File key is """ + newkey
				String temp = event.getResults();
				if (temp.contains("Please check your file format")) {
					Window.alert("Your file format does not match the model. Please check your file or checkout the sample file for examples.");
				}
				else {
					String[] sarray = temp.split(" ");
					String fileKey = sarray[sarray.length-1];
					conf.setUploadFile(fileKey);
					Window.alert("File uploaded succesfully. The file key is " + fileKey + ".");
				}
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
		
		
		/* DFBA section */
		final Button dfbaSubmitButton = new Button("Submit DFBA Job");
		
		dfbaSubmitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				StringBuffer url = new StringBuffer();
				url.append(ConfigurationFactory.getConfiguration().getBaseURL());
				url.append("model/dfba/");	// Server handles everything
				url.append("?obj_type=" + obj.getSelectedIndex());
				RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url.toString()));
					try {
						builder.sendRequest(null, new RequestCallback() {
						public void onError(Request request, Throwable exception) {
						   return;
						}

						public void onResponseReceived(Request request, Response response) {
						  if (200 == response.getStatusCode()) {
						      if (response.getText().contains("New DFBA")) {
						    	  Window.alert("DFBA job submitted.");
						      }
						      else {
						    	  Window.alert("There was a problem with your uploaded data file, please check the format and upload again.");
						      }
						  } else {
							  Window.alert("There was a problem with your uploaded data file, please check the format and upload again.");
							  return;
						  }
						}      
						});
					} catch (com.google.gwt.http.client.RequestException e) {
						e.printStackTrace();
					}
			}
		});
		
		vPanel.add(dfbaSubmitButton);
		vPanel.setVisible(false);

		switchToDFBAbutton.addClickHandler(new ClickHandler() {
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
		topTabSet.setSize("55%", "75%");
		
		final Tab introductionTab = new Tab("Introduction");
		final Tab flowchartTab = new Tab("Architecture");
		final Tab demoTab = new Tab("Demo video");
		final Tab faqTab = new Tab("FAQ");
		final Tab futureTab = new Tab("Future Work");
		
		HTMLPane introHTML = new HTMLPane();
		introHTML.setIFrameURL("http://tanglab.engineering.wustl.edu/media/intro.html");
		introHTML.setScrollbarSize(0);
		
		
		HTMLPane archHTML = new HTMLPane();
		archHTML.setIFrameURL("http://tanglab.engineering.wustl.edu/media/arch.html");
		archHTML.setScrollbarSize(0);
		
		HTMLPane demoHTML = new HTMLPane();
		demoHTML.setIFrameURL("http://tanglab.engineering.wustl.edu/media/demo.html");
		demoHTML.setScrollbarSize(0);
		
		HTMLPane faqHTML = new HTMLPane();
		faqHTML.setIFrameURL("http://tanglab.engineering.wustl.edu/media/faq.html");
		faqHTML.setScrollbarSize(0);
		
		HTMLPane futureHTML = new HTMLPane();
		futureHTML.setIFrameURL("http://tanglab.engineering.wustl.edu/media/future.html");
		futureHTML.setScrollbarSize(0);
		
		introductionTab.setPane(introHTML);
		flowchartTab.setPane(archHTML);
		demoTab.setPane(demoHTML);
		faqTab.setPane(faqHTML);
		futureTab.setPane(futureHTML);
		

		topTabSet.addTab(introductionTab);
		topTabSet.addTab(flowchartTab);
		topTabSet.addTab(demoTab);
		topTabSet.addTab(faqTab);
		topTabSet.addTab(futureTab);
		
		sp.setWidget(topTabSet);
	}

	public void ChangeToLogin(final Anchor a) {
		final Configuration conf = ConfigurationFactory.getConfiguration();
		
		if (a.getText().equals("[Log Out]")) {
			sf.clearForm();
			sf.clearStatus();
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
			sf.clearForm();
			sf.clearStatus();
			
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
					+ "user/password/retrieve/");
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
								conf.setLogin(true);
								a.setText("[Log Out]");
								changeToWelcome();
							} else {
								Window.alert("Wrong Username/Email and password combination.");
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

		final TextBox firstName = new TextBox();
		firstName.setName("firstname");

		final TextBox lastName = new TextBox();
		lastName.setName("lastname");

		final TextBox department = new TextBox();
		department.setName("department");

		final TextBox organization = new TextBox();
		organization.setName("organization");

		final TextBox address1 = new TextBox();
		address1.setName("address1");

		final TextBox address2 = new TextBox();
		address2.setName("address2");

		final TextBox country = new TextBox();
		country.setName("country");

		userInformation.setWidget(0, 0, new Label("Title"));
		userInformation.setWidget(0, 1, Title);
		userInformation.setWidget(1, 0, new Label("First Name"));
		userInformation.setWidget(1, 1, firstName);
		userInformation.setWidget(2, 0, new Label("Last Name"));
		userInformation.setWidget(2, 1, lastName);
		userInformation.setWidget(3, 0, new Label("Department"));
		userInformation.setWidget(3, 1, department);
		userInformation.setWidget(4, 0, new Label("Company/Institution"));
		userInformation.setWidget(4, 1, organization);
		userInformation.setWidget(5, 0, new Label("Organization Type"));
		userInformation.setWidget(5, 1, organizationType);
		userInformation.setWidget(6, 0, new Label("Address 1"));
		userInformation.setWidget(6, 1, address1);
		userInformation.setWidget(7, 0, new Label("Address 2"));
		userInformation.setWidget(7, 1, address2);
		userInformation.setWidget(8, 0, new Label("Country"));
		userInformation.setWidget(8, 1, country);

		final CheckBox agreement = new CheckBox();
		// TOS
		agreement
				.setHTML("I have read and agree to the <a href=\"http://tanglab.engineering.wustl.edu/media/tos.html\" target=\"_blank\">Term of Use</a>");

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
						if (event.getResults().contains("Successfully")) {
							changeToWelcome();
						}
						if (event.getResults().contains("Email")) {
							Window.alert("Email address already exists in system. Please choose another.");
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
				// check data integrity
				if (!txtPassword.getText().equals(confirmPassword.getText())) {
					Window.alert("Please check your passwords.");
					return;
				}
				if (!emailID.getText().contains("@")) {
					Window.alert("Invalid email address");
					return;
				}
				
				// Check empty. 
				boolean empty = false;
				if (emailID.getText().length()==0) empty = true;
				if (txtPassword.getText().length()==0) empty = true;
				if (empty) {
					Window.alert("Username or Password fields cannot be empty.");
					return;
				}
				registerForm.submit();
			}

		});

		sp.add(registerForm);

	}

}
