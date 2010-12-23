package edu.wustl.keggproject.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.StackPanel;

public class FilePanel {
	
	private static HorizontalSplitPanel filePanel;
	
	public FilePanel(){
		if (filePanel == null){
			filePanel = new HorizontalSplitPanel();
		}
	}
	
	public HorizontalSplitPanel getPanel()
	{
		return filePanel;
	}
	
	public void initialize(){
		filePanel.setSize("100%", "100%");
		filePanel.setSplitPosition("20%");
		initializeLeft();
		
	}
   public void initializeLeft(){
	   StackPanel fileLeftpanel = new StackPanel();
	   final Anchor newFile = new Anchor("New File");
	   final Anchor loadFile = new Anchor("Load File");
	   final Anchor saveFile = new Anchor("Save File");
	   final Anchor saveFileAs = new Anchor("Save File As");
	   newFile.addClickHandler(new ClickHandler() {
	        public void onClick(ClickEvent event) {
	          //ChangeTonewFile;
	        }
	      });
	   loadFile.addClickHandler(new ClickHandler() {
	        public void onClick(ClickEvent event) {
	          //ChangeToloadFile;
	        }
	      });
	   saveFile.addClickHandler(new ClickHandler() {
	        public void onClick(ClickEvent event) {
	          //ChangeTosaveFile;
	        }
	      });
	   saveFileAs.addClickHandler(new ClickHandler() {
	        public void onClick(ClickEvent event) {
	          //ChangeTosaveFileAs;
	        }
	      });

   }
}
