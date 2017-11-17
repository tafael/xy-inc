package com.prova.util;

public class Config {
	
	private static final PropertyResourceBundleHelper config = new PropertyResourceBundleHelper("config.properties");
	public static final String HOST_URL = config.getProperty("host_url"); 
	public static final String WS_APPLICATION_PATH = "/ws";

}
