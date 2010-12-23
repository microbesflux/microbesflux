package edu.wustl.keggproject.client;

public class ResourceSingleton {
	private static ResourceSingleton _instance;
	private String baseurl = "http://128.252.160.238:8080/";

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
