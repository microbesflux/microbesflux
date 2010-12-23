package edu.wustl.keggproject.client;

// Xueyang
public class LeftPanel {
	private RightPanel rp;
	public void setRightPanel(RightPanel r) {
		rp = r;
	}
	
	public void handle(){
		rp.changeToEric();
	}
}
