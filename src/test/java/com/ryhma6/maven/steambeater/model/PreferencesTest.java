package com.ryhma6.maven.steambeater.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * User preferences tests (setting and getting settings).
 */
class PreferencesTest {

	/**
	 * Clears preferences before each test.
	 */
	@BeforeEach
	void test() {
		UserPreferences.clearPreferences();
	}
	
	/**
	 * Set and get SteamID to and from preferences.
	 */
	@Test
	void testSetGetSteamID() {
		String steamID = "1234567890";
		UserPreferences.setSteamID(steamID);
		assertEquals(steamID, UserPreferences.getSteamID());
	}
	
	/**
	 * Test game sort option setting.
	 */
	@Test
	void testSetGetGameListSort() {
		UserPreferences.setGamelistSort(0);
		assertEquals(0,UserPreferences.getGamelistSort());
		UserPreferences.setGamelistSort(1);
		assertEquals(1,UserPreferences.getGamelistSort());
	}
	
	/**
	 * Set and get game list filter (beaten).
	 */
	@Test
	void testSetGetBeaten() {
		UserPreferences.setFilterIncludeBeaten(true);
		assertEquals(true, UserPreferences.getFilterIncludeBeaten());
		UserPreferences.setFilterIncludeBeaten(false);
		assertEquals(false, UserPreferences.getFilterIncludeBeaten());
	}
	
	/**
	 * Set and get game list filter (ignored).
	 */
	@Test
	void testSetGetIgnored() {
		UserPreferences.setFilterIncludeIgnored(true);
		assertEquals(true, UserPreferences.getFilterIncludeIgnored());
		UserPreferences.setFilterIncludeIgnored(false);
		assertEquals(false, UserPreferences.getFilterIncludeIgnored());
	}
	
	/**
	 * Set and get game list filter (unbeatable).
	 */
	@Test
	void testSetGetUnbeatable() {
		UserPreferences.setFilterIncludeUnbeatable(true);
		assertEquals(true, UserPreferences.getFilterIncludeUnbeatable());
		UserPreferences.setFilterIncludeUnbeatable(false);
		assertEquals(false, UserPreferences.getFilterIncludeUnbeatable());
	}
	
	/**
	 * Set and get game list filter (unbeaten).
	 */
	@Test
	void testSetGetUnbeaten() {
		UserPreferences.setFilterIncludeUnbeaten(true);
		assertEquals(true, UserPreferences.getFilterIncludeUnbeaten());
		UserPreferences.setFilterIncludeUnbeaten(false);
		assertEquals(false, UserPreferences.getFilterIncludeUnbeaten());
	}

}
