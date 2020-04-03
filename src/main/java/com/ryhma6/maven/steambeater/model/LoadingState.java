package com.ryhma6.maven.steambeater.model;

public enum LoadingState {
	PRELOAD, API_GAMES, API_FRIENDS, COMPLETED;

	public static String getDescription(LoadingState l) {
		String description = "";
		switch (l) {
		case PRELOAD:
			//sign in to show data
			description = "Not signed in";
			break;
		case API_GAMES:
			//loading game data from steam
			description = "Loading Steam games";
			break;
		case API_FRIENDS:
			//loading friend data from steam
			description = "Loading Steam friends";
			break;
		case COMPLETED:
			//Steam data updated: + timestamp
			description = "Completed";
			break;
		}
		return description;
	}
}
