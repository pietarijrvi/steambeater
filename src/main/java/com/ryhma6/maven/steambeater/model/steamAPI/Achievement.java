package com.ryhma6.maven.steambeater.model.steamAPI;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * Object representation of the achievement data retrieved and parsed from
 * SteamAPI JSON responses related to achievements. Data is parsed from
 * GetSchemaForGame (v2) and GetPlayerAchievements (v0001). SteamAPI
 * documentation: https://developer.valvesoftware.com/wiki/Steam_Web_API
 *
 */
public class Achievement {
	/**
	 * Name of the achievement in api format that is used for accessing the game data
	 * @param apiname Achievement name in API format from SteamAPI JSON
	 * @return Achievement name in API format
	 */
	@Getter @Setter private String apiname;
	/**
	 * Whether or not the achievement has been completed (completed: 1, not completed: 0)
	 * @param achieved Achievement completion status from SteamAPI (completed: 1, not completed: 0)
	 * @return Completion status of this achievement (completed: 1, not completed: 0)
	 */
	@Getter @Setter private String achieved;
	/**
	 * Unlock timestamp (unix). Defaults to "0" if achievement was not unlocked or time is unknown. 
	 * @param timestamp(unix) Achievement unlock time from SteamAPI.
	 * @return Achievement unlock time (unix timestamp).
	 */
	@Getter @Setter private int unlocktime;
	/**
	 * Localised achievement name, may be null (optional)
	 * @param name Name from SteamAPI.
	 * @return Localised achievement name or null
	 */
	@Getter @Setter private String name;
	/**
	 * Default value (optional) not in use, retrieved from SteamAPI (optional)
	 */
	@Getter @Setter private int defaultvalue;
	/**
	 * Readable achievement name that can be used in UI
	 * @param displayName Display name from SteamAPI
	 * @return Achievement name
	 */
	@Getter @Setter private int displayName;
	/**
	 * Achievement description text.
	 * @param description Description from SteamAPI
	 * @return Achievement description.
	 */
	@Getter @Setter private String description;
	/**
	 * Html link to the achievement icon
	 * @param icon Achievement icon link from SteamAPI
	 * @return Achievement icon link
	 */
	@Getter @Setter private String icon;
	/**
	 * Html link to the achievement icon (grayscale)
	 * @param icongray Achievement icon link (grayscale) from SteamAPI
	 * @return Achievement grayscale icon link
	 */
	@Getter @Setter private String icongray;
}
