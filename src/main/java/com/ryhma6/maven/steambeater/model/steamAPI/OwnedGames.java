package com.ryhma6.maven.steambeater.model.steamAPI;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * Object model of the JSON response from the SteamAPI (list of games owned by a player).
 * Data is parsed from GetOwnedGames (v0001).
 * SteamAPI documentation: https://developer.valvesoftware.com/wiki/Steam_Web_API
 *
 */
@JsonRootName("response")
public class OwnedGames {
	/**
	 * The amount of games owned by a player.
	 * @param game_count Game count from SteamAPI.
	 * @return Number of games
	 */
	@Getter @Setter private int game_count;
	/**
	 * List of game data details
	 * @param games List of games from SteamAPI
	 * @return List of game details
	 */
	@Getter @Setter private List<GameData> games;
}
