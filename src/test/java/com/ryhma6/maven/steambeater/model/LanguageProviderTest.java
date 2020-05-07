package com.ryhma6.maven.steambeater.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;

import org.junit.jupiter.api.Test;

/**
 * 
 * Testing that the language provider works as intended (saving and retrieving
 * locale from a file, using String-resources based on the active locale)
 *
 */
public class LanguageProviderTest {
	private LanguageProvider languageProvider = LanguageProvider.getInstance();

	/**
	 * Setting invalid country code
	 */
	@Test
	void testSetInvalidCountry() {
		String language = "en";
		String country = "EN";
		boolean success = languageProvider.setLanguage(language, country);
		assertEquals(false, success, "Invalid country code accepted");
	}

	/**
	 * Setting valid ISO locale EN-gb
	 */
	@Test
	void testSetValidLocale_enGB() {
		String language = "en";
		String country = "GB";
		boolean success = languageProvider.setLanguage(language, country);
		assertEquals(true, success, "Valid locale was not accepted");
	}

	/**
	 * Setting valid ISO locale FI-fi
	 */
	@Test
	void testSetValidLocale_fiFI() {
		String language = "fi";
		String country = "FI";
		boolean success = languageProvider.setLanguage(language, country);
		assertEquals(true, success, "Valid locale was not accepted");
	}

	/**
	 * Testing that a string is loaded from properties based on the selected locale
	 * (different string for different locale)
	 */
	@Test
	void testGetLocaleString() {
		String key = "friends";
		String language = "fi";
		String country = "FI";
		languageProvider.setLanguage(language, country);
		String text1 = LanguageProvider.getString(key);
		language = "en";
		country = "GB";
		languageProvider.setLanguage(language, country);
		String text2 = LanguageProvider.getString(key);
		assertNotEquals(text1, text2, "Different locales returned same string");
	}

	/**
	 * Testing that the saved locale can be retrieved from the file
	 */
	@Test
	void testGetSavedLocale() {
		String language = "fi";
		String country = "FI";
		languageProvider.setLanguage(language, country);
		Locale l = languageProvider.getCurrentLocale();
		String loadedLanguage = l.getLanguage();
		String loadedCountry = l.getCountry();
		assertEquals(language, loadedLanguage);
		assertEquals(country, loadedCountry);

		language = "en";
		country = "GB";
		languageProvider.setLanguage(language, country);
		l = languageProvider.getCurrentLocale();
		loadedLanguage = l.getLanguage();
		loadedCountry = l.getCountry();
		assertEquals(language, loadedLanguage);
		assertEquals(country, loadedCountry);
	}
}
