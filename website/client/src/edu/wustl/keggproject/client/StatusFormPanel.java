package edu.wustl.keggproject.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;

public class StatusFormPanel {
	private HorizontalPanel hp;
	private Label status;
	private FormPanel f;

	public StatusFormPanel() {
		hp = new HorizontalPanel();
		status = new Label("");
		f = new FormPanel();
		hp.add(status);
		hp.add(f);
	}
	
	public void initialize() {
	}

	public Widget getStatusFormPanel() {
		return hp;
	}

	public void clearStatus() {
		status.setText("");
	}

	public void clearForm() {
		f.clear();
	}

	public void setStatus(String s) {
		status.setText(s);
	}

	public void show() {
		hp.setHeight("40px");
	}

	public void hide() {
		hp.setHeight("0px");
	}
	
	public void loadFile() {
		clearStatus();
		clearForm();
		
		RequestBuilder rb = new RequestBuilder(RequestBuilder.GET,
				ResourceSingleton.getInstace().getBaseURL()
						+ "collection/list/");

		final ListBox collectionListBox = new ListBox();
		collectionListBox.setName("collection_name");
		collectionListBox.setVisible(false);
		
		
		setStatus("Fetching a list of models from server ... ");


		HorizontalPanel loadPanel = new HorizontalPanel();
		loadPanel.add(collectionListBox);
		final Button loadButton = new Button("Load");
		loadButton.setVisible(false);
		
		
		class MyCallback implements RequestCallback {

			@Override
			public void onResponseReceived(Request request, Response response) {
				collectionListBox.clear();
				collectionListBox.setVisible(true);
				loadButton.setVisible(true);
				for (String i : response.getText().split(" ")) {
					collectionListBox.addItem(i);
				}
				setStatus("Load model: ");
			}

			@Override
			public void onError(Request request, Throwable exception) {
				// TODO Auto-generated method stub
			}
			
		}
			
		try {
			rb.sendRequest("", new MyCallback());
		} catch (RequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		loadPanel.add(loadButton);
		
		f.clear();
		f.setWidget(loadPanel);
		f.setAction(ResourceSingleton.getInstace().getBaseURL()
				+ "collection/select/");
		f.setMethod(FormPanel.METHOD_GET);

		f.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				if (event.getResults().contains("selected")) {
					setStatus("Model "
							+ collectionListBox
									.getValue(collectionListBox
											.getSelectedIndex())
							+ " loaded. ");
					ResourceSingleton
							.getInstace()
							.setCurrentCollection(
									collectionListBox.getValue(collectionListBox
											.getSelectedIndex()));

				} else {
					setStatus("Model "
							+ collectionListBox
									.getValue(collectionListBox
											.getSelectedIndex())
							+ " is not loaded. ");

				}
				clearForm();
			}
		});

		f.addSubmitHandler(new FormPanel.SubmitHandler() {
			@Override
			public void onSubmit(SubmitEvent event) {
				;
			}
		});

		loadButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setStatus("Loading  "
						+ collectionListBox.getValue(collectionListBox
								.getSelectedIndex()));
				collectionListBox.setVisible(false);
				loadButton.setVisible(false);
				f.submit();
			}
		});
		
	}

	public void saveFile(boolean s) {
		final boolean in_background = s;
		if (! in_background) {
			clearStatus();
			clearForm();
			
			setStatus("Saving model: "
				+ ResourceSingleton.getInstace().getCurrentCollection()
				+ " ... ");
		}
		
		RequestBuilder rb = new RequestBuilder(RequestBuilder.GET,
				ResourceSingleton.getInstace().getBaseURL()
						+ "collection/save/");

		class MyCallback2 implements RequestCallback {

			@Override
			public void onResponseReceived(Request request, Response response) {
				if (response.getText().contains("saved") && ! in_background)
				{
					setStatus("Model saved");
				}
			}

			@Override
			public void onError(Request request, Throwable exception) {
				// TODO Auto-generated method stub
			}
			
		}
		try {
			rb.sendRequest("", new MyCallback2());
		} catch (RequestException e) {
			e.printStackTrace();
		}
	}

	public void saveFileAs() {
		clearStatus();
		clearForm();
		setStatus("Saving current model as: ");
		f.setVisible(true);

		f.setAction(ResourceSingleton.getInstace().getBaseURL()
				+ "collection/saveas/");
		f.setMethod(FormPanel.METHOD_GET);

		final HorizontalPanel saveaspanel = new HorizontalPanel();

		final TextBox newname = new TextBox();
		newname.setName("new_name");

		final Button saveasButton = new Button("Save as");
		saveaspanel.add(newname);
		saveaspanel.add(saveasButton);

		saveasButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setStatus(" Saving model as " + newname.getText());
				newname.setVisible(false);
				saveasButton.setVisible(false);
				f.submit();
			}
		});

		f.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				if (event.getResults().contains("renamed")) {
					setStatus("Model " + newname.getText() + " saved. ");

				} else {
					setStatus("Model " + newname.getText() + " is not saved. ");
				}
				clearForm();
			}
		});

		f.addSubmitHandler(new FormPanel.SubmitHandler() {
			@Override
			public void onSubmit(SubmitEvent event) {
				// saveaspanel.setVisible(false);
			}

		});
		f.setWidget(saveaspanel);
	}
}
