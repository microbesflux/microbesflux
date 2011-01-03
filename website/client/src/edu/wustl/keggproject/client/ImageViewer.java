package edu.wustl.keggproject.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ImageViewer implements EntryPoint {

	public void onModuleLoad() {

		RootPanel rootPanel = RootPanel.get();
		rootPanel.setSize("2000", "1000");

		LoginPanel loginpanel = new LoginPanel();

		VerticalPanel fullpanel = new VerticalPanel();
		fullpanel.add(loginpanel.getLoginPanel());

		HorizontalPanel lower = new HorizontalPanel();
		LeftPanel leftp = new LeftPanel();
		RightPanel rightp = new RightPanel();
		StatusFormPanel sfp = new StatusFormPanel();
		
		rightp.initialize();

		leftp.setRightPanel(rightp);
		leftp.setStatusFormPanel(sfp);
		rightp.setStatusFormPanel(sfp);
		
		loginpanel.setRightPanel(rightp);

		lower.add(leftp.getLeftPanel());
		VerticalPanel statusright = new VerticalPanel();
		
		statusright.add(sfp.getStatusFormPanel());
		statusright.add(rightp.getRightPanel());
		
		lower.add(statusright);
		
		fullpanel.add(lower);
		rootPanel.add(fullpanel, 0, 0);

		// WorkPanel workPanel = new WorkPanel();
		// workPanel.initialize();
		// TopPanel tp = new TopPanel(workPanel);c
		// rootPanel.add(tp, 0, 0);
		// rootPanel.add(workPanel.getPanel(), 0, 150);
		// tp.setWorkPanel(workPanel);
	}
}