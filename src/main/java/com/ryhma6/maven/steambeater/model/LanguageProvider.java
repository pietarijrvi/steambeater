package com.ryhma6.maven.steambeater.model;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageProvider {
	private Locale curLocale;
	private static ResourceBundle bundle;
	private static LanguageProvider self = null;
	
	/**
	 * Creates a new instance of LanguageProvider and sets the language to the default language (English)
	 */
	private LanguageProvider() {
		bundle = ResourceBundle.getBundle("TextResources");
	}
	
	
	/**
	 * Provides the instance of LanguageProvider
	 * @return
	 */
	public static LanguageProvider getInstance() {
		if (self == null) {
			self = new LanguageProvider();
		}
		return self;
	}
	
	public void setLanguage(String language, String country) {
		curLocale = new Locale(language, country);
		bundle = ResourceBundle.getBundle("TextResources", curLocale);
		System.out.println("Language set to " + language);
	}
	
	public static String getString(String key) {			
		return getInstance().bundle.getString(key);
	}
}
