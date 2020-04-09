package com.ryhma6.maven.steambeater.model;

/**
 * Data object containing loading status options for defining the current load
 * status descriptions. One status should be always active.
 *
 */
public enum LoadingStatus {
	// PRELOAD when not logged in, SteamApi data loading, COMPLETED when loading
	// completed
	PRELOAD, API_GAMES, API_FRIENDS, COMPLETED, FAILURE;

	/**
	 * Returns the description of the loading state.
	 * 
	 * @param loadingState
	 * @return description of the loadingState (arg)
	 */
	public static String getDescription(LoadingStatus loadingStatus) {
		String description = "";
		switch (loadingStatus) {
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
		case FAILURE:
			// Failed to load steam data
			description = "Failed to load up-to-date Steam data, refresh to try again";
			break;
		}
		return description;
	}
}
