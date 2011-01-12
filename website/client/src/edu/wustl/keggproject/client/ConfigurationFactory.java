package edu.wustl.keggproject.client;

public class ConfigurationFactory {

	private static Configuration _instance;

	public static Configuration getConfiguration() {
		if (_instance == null) {
			try {
				_instance = new Configuration("secret");
			} catch (Throwable t) {
				throw new RuntimeException("Can't construct Configuration.");
			}
		}
		return _instance;
	}
}
