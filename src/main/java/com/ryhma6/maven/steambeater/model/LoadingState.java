package com.ryhma6.maven.steambeater.model;

/**
 * Data object containing loading state options for defining the current load
 * status state descriptions. One state should be always active.
 *
 */
public enum LoadingState {
	// PRELOAD when not logged in, SteamApi data loading, COMPLETED when loading
	// completed
	PRELOAD, API_GAMES, API_FRIENDS, COMPLETED;

	/**
	 * Returns the description of the loading state.
	 * 
	 * @param loadingState
	 * @return description of the loadingState (arg)
	 */
	public static String getDescription(LoadingState loadingState) {
		String description = "";
		switch (loadingState) {
		case PRELOAD:
			// sign in to show data
			description = "Not signed in";
			break;
		case API_GAMES:
			// loading game data from steam
			description = "Loading Steam games";
			break;
		case API_FRIENDS:
			// loading friend data from steam
			description = "Loading Steam friends";
			break;
		case COMPLETED:
			// Steam data updated: + timestamp
			description = "Steam data loading completed";
			break;
		}
		return description;
	}
}
