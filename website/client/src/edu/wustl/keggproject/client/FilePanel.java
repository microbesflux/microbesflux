package edu.wustl.keggproject.client;

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
	   Anchor newFile = new Anchor("New File");
	   Anchor loadFile = new Anchor("Load File");
	   Anchor saveFile = new Anchor("Save File");
	   Anchor saveFileAs = new Anchor("Save File As");
	   
	   
   }
}
