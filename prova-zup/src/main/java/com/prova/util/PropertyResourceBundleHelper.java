package com.prova.util;

import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;

public class PropertyResourceBundleHelper {

	private PropertyResourceBundle bundle;

	public PropertyResourceBundleHelper(String bundleName) {
		InputStream is = null;
		try {
			is = PropertyResourceBundleHelper.class.getClassLoader().getResourceAsStream(bundleName);
			bundle = new PropertyResourceBundle(is);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			try {
				is.close();
			} catch (Exception e) {
			}
		}
	}

	public String getProperty(String chave) {
		return bundle.getString(chave);
	}

	public String getProperty(String chave, String defaultValue) {
		try {
			return getProperty(chave);
		} catch (MissingResourceException e) {
			return defaultValue;
		}
	}

}