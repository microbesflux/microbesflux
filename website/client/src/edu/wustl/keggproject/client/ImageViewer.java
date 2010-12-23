package edu.wustl.keggproject.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ImageViewer implements EntryPoint {
		
	public void onModuleLoad() {
		RootPanel rootPanel = RootPanel.get();
		rootPanel.setSize("2000", "1000");
		WorkPanel workPanel = new WorkPanel();
		
		workPanel.initialize();
		TopPanel tp = new TopPanel(workPanel);
		rootPanel.add(tp, 0, 0);
		rootPanel.add(workPanel.getPanel(),0,150);
		// tp.setWorkPanel(workPanel);
	}
}