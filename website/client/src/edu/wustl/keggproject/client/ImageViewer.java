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
		// LoginFunctionPanel lfp = new LoginFunctionPanel();
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(loginpanel.getLoginPanel());

		// vp.add(lfp.getLoginFunctionPanel());
		
		HorizontalPanel lower = new HorizontalPanel();
		LeftPanel leftp = new LeftPanel();
		RightPanel rightp = new RightPanel();
		rightp.initialize();
		
		leftp.setRightPanel(rightp);
		loginpanel.setRightPanel(rightp);
		
		lower.add(leftp.getLeftPanel());
		
		lower.add(rightp.getRightPanel());
		vp.add(lower);
		rootPanel.add(vp, 0, 0);
		
		// WorkPanel workPanel = new WorkPanel();
		// workPanel.initialize();
		// TopPanel tp = new TopPanel(workPanel);
		// rootPanel.add(tp, 0, 0);
		// rootPanel.add(workPanel.getPanel(), 0, 150);
		// tp.setWorkPanel(workPanel);
	}
}