package com.ryhma6.maven.steambeater.model;

/**
 * Data object containing loading status options for defining the current load
 * status descriptions. One status should be always active.
 *
 */
public enum LoadingStatus {
	/*
	 * PRELOAD when not logged in, SteamApi data loading, COMPLETED when loading
	 * completed, FAILURE when any data load has failed
	 */
	PRELOAD, API_GAMES, API_FRIENDS, COMPLETED, FAILURE, API_FAILURE, DATABASE_CONNECTING, DATABASE_FAILURE;

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
			description = LanguageProvider.getString("statusPreload");
			break;
		case API_GAMES:
			// loading game data from steam
			description = LanguageProvider.getString("statusApiGames");
			break;
		case API_FRIENDS:
			// loading friend data from steam
			description = LanguageProvider.getString("statusApiFriends");
			break;
		case COMPLETED:
			// Steam data updated: + timestamp
			description = LanguageProvider.getString("statusCompletedLoad");
			break;
		case FAILURE:
			// Failed to load data
			description = LanguageProvider.getString("statusFailureLoad");
			break;
		case API_FAILURE:
			// Failed to load steam data
			description = LanguageProvider.getString("statusFailureAPI");
			break;
		case DATABASE_CONNECTING:
			// connecting to database
			description = LanguageProvider.getString("statusDatabaseConnecting");
			break;
		case DATABASE_FAILURE:
			// Failed to load database data
			description = LanguageProvider.getString("statusFailureDatabase");
			break;
		}
		return description;
	}
}
