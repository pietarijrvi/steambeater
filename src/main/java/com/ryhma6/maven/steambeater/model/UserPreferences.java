package com.ryhma6.maven.steambeater.model;

import java.util.prefs.Preferences;

/**
 * 
 * Utility class for saving and accessing used data.
 *
 */
public class UserPreferences {
	/**
	 * Defines the node where the preferences are stored.
	 */
	private static Preferences prefs = Preferences.userRoot();
	private static final String STEAMID_KEY = "SteamID";
	private static final String INCLUDE_IGNORED_KEY = "ignoreFilter";
	private static final String INCLUDE_UNBEATABLE_KEY = "UnbeatableFilter";
	private static final String INCLUDE_BEATEN_KEY = "beatenFilter";
	private static final String INCLUDE_UNBEATEN_KEY = "unbeatenFilter";
	private static final String GAMELIST_SORT_KEY = "gamelistSort";

	/**
	 * Save steamID (retrieved using Steam OpenID) to preferences
	 * @param steamID
	 */
	public static void setSteamID(String steamID) {
		prefs.put(STEAMID_KEY, steamID);
		System.out.println("Saved ID: " + prefs.get(STEAMID_KEY, "null"));
	}

	/**
	 * Get SteamID saved in preferences. Default value "null".
	 * @return steamID
	 */
	public static String getSteamID() {
		return prefs.get(STEAMID_KEY, "null");
	}
	
	/**
	 * Set game list filter option (include ignored) 
	 * @param isIncluded
	 */
	public static void setFilterIncludeIgnored(boolean isIncluded) {
		prefs.putInt(INCLUDE_IGNORED_KEY, isIncluded ? 1 : 0);
	}
	
	/**
	 * Get game list filter option (include ignored), default false.
	 * @return includeIgnored
	 */
	public static boolean getFilterIncludeIgnored() {
		int isIncluded = prefs.getInt(INCLUDE_IGNORED_KEY, 0);
		return isIncluded==1 ? true : false;
	}
	
	/**
	 * Set game list filter option (include unbeatable games)
	 * @param isIncluded
	 */
	public static void setFilterIncludeUnbeatable(boolean isIncluded) {
		prefs.putInt(INCLUDE_UNBEATABLE_KEY, isIncluded ? 1 : 0);
	}
	
	/**
	 * Get game list filter option (include unbeatable games), default false.
	 * @return isIncluded
	 */
	public static boolean getFilterIncludeUnbeatable() {
		int isIncluded = prefs.getInt(INCLUDE_UNBEATABLE_KEY, 0);
		return isIncluded==1 ? true : false;
	}
	
	/**
	 * Set game list filter option (include beaten games)
	 * @param isIncluded
	 */
	public static void setFilterIncludeBeaten(boolean isIncluded) {
		prefs.putInt(INCLUDE_BEATEN_KEY, isIncluded ? 1 : 0);
	}
	
	/**
	 * Get game list filter option (include beaten games), default false.
	 * @return isIncluded
	 */
	public static boolean getFilterIncludeBeaten() {
		int isIncluded = prefs.getInt(INCLUDE_BEATEN_KEY, 0);
		return isIncluded==1 ? true : false;
	}
	
	/**
	 * Set game list filter option (include unbeaten games)
	 * @param isIncluded
	 */
	public static void setFilterIncludeUnbeaten(boolean isIncluded) {
		prefs.putInt(INCLUDE_UNBEATEN_KEY, isIncluded ? 1 : 0);
	}
	
	/**
	 * Get game list filter option (include unbeaten games), default true.
	 * @return
	 */
	public static boolean getFilterIncludeUnbeaten() {
		int isIncluded = prefs.getInt(INCLUDE_UNBEATEN_KEY, 1);
		return isIncluded==1 ? true : false;
	}
	
	/**
	 * Set game list sort choice.
	 * @param selection
	 */
	public static void setGamelistSort(int selection) {
		prefs.putInt(GAMELIST_SORT_KEY, selection);
	}
	
	/**
	 * Get game list sort choice, default 0.
	 * @return
	 */
	public static int getGamelistSort() {
		return prefs.getInt(GAMELIST_SORT_KEY, 0);
	}
}
