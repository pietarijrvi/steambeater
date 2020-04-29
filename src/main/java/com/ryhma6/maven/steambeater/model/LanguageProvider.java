package com.ryhma6.maven.steambeater.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

public class LanguageProvider {
	private final String appConfigPath = "languageSettings.properties";
	private Locale curLocale;
	private ResourceBundle bundle;
	private static LanguageProvider self = null;

	private final Set<String> ISO_LANGUAGES = new HashSet<String>(Arrays.asList(Locale.getISOLanguages()));
	private final Set<String> ISO_COUNTRIES = new HashSet<String>(Arrays.asList(Locale.getISOCountries()));

	/**
	 * Creates a new instance of LanguageProvider and sets the language to the
	 * default language (English)
	 */
	private LanguageProvider() {
		curLocale = getSavedLocale();
		setBundle(curLocale);
	}

	/**
	 * Provides the instance of LanguageProvider
	 * 
	 * @return
	 */
	public static LanguageProvider getInstance() {
		if (self == null) {
			self = new LanguageProvider();
		}
		return self;
	}
	
	public static String getString(String key) {
		return getInstance().bundle.getString(key);
	}
	
	public Locale getCurrentLocale() {
		return curLocale;
	}

	public boolean setLanguage(String language, String country) {

		boolean success = false;
		if (isValidISOLanguage(language) && isValidISOCountry(country)) {
			curLocale = new Locale(language, country);
			setBundle(curLocale);
			Properties properties = loadLanguageProperties();
			properties.setProperty("language", language);
			properties.setProperty("country", country);

			FileOutputStream fos = null;
			try {
				File file = new File(getClass().getClassLoader().getResource(appConfigPath).getFile());
				fos = new FileOutputStream(file);
				properties.store(fos, "locale settings");
				success = true;
			} catch (Exception e) {
				System.out.println("Saving locale settings failed");
			} finally {
				try {
					fos.close();
				} catch (IOException e) {
				}
			}

			System.out.println("Language set to " + language);
		}else {
			System.out.println("Trying to save invalid locale settings, aborted.");
		}
		return success;
	}

	private Locale getSavedLocale() {
		Locale locale = Locale.getDefault();
		Properties properties = loadLanguageProperties();

		String language = properties.getProperty("language");
		String country = properties.getProperty("country");
		if (isValidISOLanguage(language) && isValidISOCountry(country)) {
			locale = new Locale(language, country);
		} else {
			System.out.println("Invalid locale settings loaded from file.");
		}

		return locale;
	}

	private Properties loadLanguageProperties() {
		Properties properties = new Properties();
		FileInputStream fis = null;
		try {
			File file = new File(getClass().getClassLoader().getResource(appConfigPath).getFile());
			fis = new FileInputStream(file);
			properties.load(fis);
		} catch (FileNotFoundException e) {
			System.out.println("Language properties not found");
		} catch (IOException e) {
			System.out.println("Failed to load the language properties");
		} catch (Exception e) {
			System.out.println("Loading locale settings failed");
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
			}
		}
		return properties;
	}
	
	private void setBundle(Locale locale) {
		bundle = ResourceBundle.getBundle("TextResources", locale);
	}

	private boolean isValidISOLanguage(String s) {
		return ISO_LANGUAGES.contains(s);
	}

	private boolean isValidISOCountry(String s) {
		return ISO_COUNTRIES.contains(s);
	}
}
