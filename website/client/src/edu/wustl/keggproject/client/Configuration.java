package edu.wustl.keggproject.client;

public class Configuration {
	// private String baseurl = "http://128.252.160.238:8000/";

	// private String baseurl = "http://127.0.0.1:8000/";
	private String baseurl = "http://tanglab.engineering.wustl.edu/flux/";
	private String email = "";
	
	private boolean login = false;
	
	
	public void setLogin(boolean l) {
		login = l;
	}
	
	public boolean getLogin() {
		return login;
	}
	
	public String getBaseURL() {
		return baseurl;
	}

	public void setBaseURL(String baseurl) {
		this.baseurl = baseurl;
	}

	public String getCurrentCollection() {
		return currentCollection;
	}
	
	public String getCurrentEmail() {
		return email;
	}
	
	public void setCurrentEmail(String e) {
		email = e;
	}

	public void setCurrentCollection(String currentCollection) {
		this.currentCollection = currentCollection;
	}

	private String currentCollection = "";

	public Configuration(String secret) {
		if (!secret.equals("secret")) {
			throw new RuntimeException(
					"Not suppose to call the constructor that way");
		}
	}
	
	
}
