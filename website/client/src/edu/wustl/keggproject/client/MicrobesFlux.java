package edu.wustl.keggproject.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */

public class MicrobesFlux implements EntryPoint {

	public void onModuleLoad() {

		RootPanel rootPanel = RootPanel.get();
		rootPanel.setSize("2000", "1000");
		
		VerticalPanel fullpanel = new VerticalPanel(); // fullpanel==rootpanel; fullpanel=loginpanel+lower;
		LoginPanel loginpanel = new LoginPanel();
		
		HorizontalPanel lower = new HorizontalPanel(); // lower=leftp+rightVerticalPanel;
		LeftPanel leftp = new LeftPanel();
		VerticalPanel rightVerticalPanel = new VerticalPanel();// rightVerticalPanel=sfp+rightp+amp;
		
		RightPanel rightp = new RightPanel();
		StatusFormPanel sfp = new StatusFormPanel();
		
		rightp.initialize();
		sfp.initialize();

		leftp.setRightPanel(rightp);
		leftp.setStatusFormPanel(sfp);
		
		rightp.setStatusFormPanel(sfp);
		loginpanel.setRightPanel(rightp);
		
		rightVerticalPanel.add(sfp.getStatusFormPanel());
		rightVerticalPanel.add(rightp.getRightPanel());
		
		lower.add(leftp.getLeftPanel());
		lower.add(rightVerticalPanel);
		
		fullpanel.add(loginpanel.getLoginPanel());
		fullpanel.add(lower);
		rootPanel.add(fullpanel, 0, 0);

	}
}