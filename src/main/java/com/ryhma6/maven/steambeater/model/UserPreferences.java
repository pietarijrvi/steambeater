package com.ryhma6.maven.steambeater.model;

import java.util.prefs.Preferences;

public class UserPreferences {
	
	// Defines the node where the preferences are stored
	private Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
	String steamIDKey = "SteamID";
	
	  public void setSteamID(String steamID) {    
		prefs.put(steamIDKey, steamID);
		System.out.println("Saved ID: " + prefs.get(steamIDKey, "null"));
	  }
	  
	  public String getSteamID() {
		  return prefs.get(steamIDKey, "null");
	  }
}
