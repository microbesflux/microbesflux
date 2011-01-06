package edu.wustl.keggproject.client;

public class ResourceSingleton {
	private static ResourceSingleton _instance;
	
	// private String baseurl = "http://127.0.0.1:8888/flux/";
	
	private String baseurl = "http://128.252.160.238:8000/";
	
	
	private String currentCollection = "";

	public String getCurrentCollection() {
		return _instance.currentCollection;
	}

	public void setCurrentCollection(String current_collection) {
		_instance.currentCollection = new String(current_collection);
	}

	protected ResourceSingleton() {
	}

	public static ResourceSingleton getInstace() {
		if (_instance == null) {
			_instance = new ResourceSingleton();
			return _instance;
		} else {
			return _instance;
		}
	}

	public String getBaseURL() {
		return baseurl;
	}

}
