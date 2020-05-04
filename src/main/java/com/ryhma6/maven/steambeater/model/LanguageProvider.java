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
	/**
	 * Active locale, used to load correct resource bundle
	 */
	private Locale curLocale;
	/**
	 * Active resource bundle (locale-specific resources)
	 */
	private ResourceBundle bundle;
	private static LanguageProvider self = null;

	/**
	 * List that contains ISO language codes
	 */
	private final Set<String> ISO_LANGUAGES = new HashSet<String>(Arrays.asList(Locale.getISOLanguages()));
	/**
	 * List that contains ISO country codes
	 */
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

	/**
	 * Returns string from the active resource bundle.
	 * @param key key for the string
	 * @return string that can be used in UI
	 */
	public static String getString(String key) {
		return getInstance().bundle.getString(key);
	}

	/**
	 * Returns the locale that is currently active
	 * @return
	 */
	public Locale getCurrentLocale() {
		return curLocale;
	}

	/**
	 * Set locale based on language and country codes. Valid ISO codes have to be used (en and GB etc.)
	 * @param ISOlanguage ISO language code
	 * @param ISOcountry ISO country code
	 * @return
	 */
	public boolean setLanguage(String ISOlanguage, String ISOcountry) {

		boolean success = false;
		if (isValidISOLanguage(ISOlanguage) && isValidISOCountry(ISOcountry)) {
			curLocale = new Locale(ISOlanguage, ISOcountry);
			setBundle(curLocale);
			Properties properties = loadLanguageProperties();
			properties.setProperty("language", ISOlanguage);
			properties.setProperty("country", ISOcountry);

			//save locale settings to a .properties -file
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

			System.out.println("Language set to " + ISOlanguage);
		} else {
			System.out.println("Trying to save invalid locale settings, aborted.");
		}
		return success;
	}

	/**
	 * Loads locale (language and country codes) from a file
	 * @return locale
	 */
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

	/**
	 * Loads the properties file that contains the locale information (language and country codes)
	 * @return properties
	 */
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

	/**
	 * Language code validation - checks that the param string is a valid ISO language code.
	 * @param s
	 * @return true if valid ISO lang code
	 */
	private boolean isValidISOLanguage(String s) {
		return ISO_LANGUAGES.contains(s);
	}

	/**
	 * Country code validation - checks that the param string is a valid ISO country code.
	 * @param s
	 * @return true if valid ISO country code
	 */
	private boolean isValidISOCountry(String s) {
		return ISO_COUNTRIES.contains(s);
	}
}
