package com.ryhma6.maven.steambeater.model.steamAPI;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Object model of the player's game statistics (including list of achievements) parsed from SteamAPI JSON.
 * SteamAPI documentation: https://developer.valvesoftware.com/wiki/Steam_Web_API
 */
public class GameStatistics {
	/**
	 * SteamID that the game data is related to.
	 * @param steamID Player Steam ID from SteamAPI.
	 * @return SteamID of the player.
	 */
	@Getter @Setter private String steamID;
	/**
	 * Name of the game.
	 * @param gameName Game name from SteamAPI.
	 * @return Game name
	 */
	@Getter @Setter private String gameName;
	/**
	 * Achievement list for this game.
	 * @param achievements Achievement list parsed from SteamAPI.
	 * @return Achievements list
	 */
	@Getter @Setter private List<Achievement> achievements = new ArrayList<Achievement>();
	/**
	 * Game statistic (non-achiemenent) info of the player/game (may be null if not retrieved from separate API call).
	 * @param stats Game statistics of the player (steamID).
	 * @return List of game statistics (non-achievement data, different stats for each game).
	 */
	@Getter @Setter private List<Stat> stats;
	@Getter @Setter private String success;
}
