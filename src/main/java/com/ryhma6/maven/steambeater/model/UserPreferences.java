package com.ryhma6.maven.steambeater.model;

import java.util.prefs.Preferences;

public class UserPreferences {
	
	// Defines the node where the preferences are stored
	private static Preferences prefs = Preferences.userRoot();
	private static final String steamIDKey = "SteamID";
	
	  public static void setSteamID(String steamID) {    
		prefs.put(steamIDKey, steamID);
		System.out.println("Saved ID: " + prefs.get(steamIDKey, "null"));
	  }
	  
	  public static String getSteamID() {
		  return prefs.get(steamIDKey, "null");
	  }
}
