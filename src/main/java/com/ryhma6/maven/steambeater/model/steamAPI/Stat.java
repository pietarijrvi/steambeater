package com.ryhma6.maven.steambeater.model.steamAPI;

import lombok.Getter;
import lombok.Setter;

/**
 * Object model of a single stat details from SteamAPI JSON response. SteamAPI
 * documentation: https://developer.valvesoftware.com/wiki/Steam_Web_API
 *
 */
public class Stat {
	/**
	 * Name of the stat (api name)
	 * 
	 * @param name Stat name in api format from SteamAPI
	 * @return name Api name of the stat
	 */
	@Getter
	@Setter
	private String name;
	/**
	 * Default value of the stat (usually 0).
	 * 
	 * @param defaultValue Default value of the stat from SteamAPI
	 * @return defaultValue Default value of the stat
	 */
	@Getter
	@Setter
	private int defaultvalue;
	/**
	 * Display name can be used in UI
	 * 
	 * @param displayName Display name of the stat from SteamAPI
	 * @return displayName Display name of the stat
	 */
	@Getter
	@Setter
	private String displayName;
}
