package edu.wustl.keggproject.client;

public class ResourceSingleton {
	private static ResourceSingleton _instance;
	// private String baseurl = "http://128.252.160.238:8000/";
	
	private String baseurl = "http://127.0.0.1:8888/flux/";
	
	private String currentCollection = "";

	public String getCurrentCollection() {
		return currentCollection;
	}

	public void setCurrentCollection(String current_collection) {
		this.currentCollection = current_collection;
	}

	protected ResourceSingleton() {
	}

	public static ResourceSingleton getInstace() {
		if (_instance == null) {
			return new ResourceSingleton();
		} else {
			return _instance;
		}
	}

	public String getBaseURL() {
		return baseurl;
	}

}
