package com.ryhma6.maven.steambeater.model.steamAPI;

import lombok.Getter;
import lombok.Setter;

/**
 * Object model of the friend details from SteamAPI JSON response. SteamAPI
 * documentation: https://developer.valvesoftware.com/wiki/Steam_Web_API
 */
public class Friend {
	/**
	 * Steam id of the friend.
	 * 
	 * @param steamid Steamid of the friend from SteamAPI
	 * @return Steam ID of the friend.
	 */
	@Getter
	@Setter
	private String steamid;
	/**
	 * Relationship filter. Possibles values: all, friend.
	 * 
	 * @param relationship Relationship value from SteamAPI.
	 * @return Relationship value ("all" or "friend").
	 */
	@Getter
	@Setter
	private String relationship;
	/**
	 * Unix timestamp of the time when the friend relationship was created.
	 * 
	 * @param friend_since Timestamp from SteamAPI.
	 * @return Unix timestamp of the friend relationship start.
	 */
	@Getter
	@Setter
	private String friend_since;
	/**
	 * Profile details of this friend.
	 * 
	 * @param playerProfile Profile information from SteamAPI.
	 * @return Player profile information.
	 */
	@Getter
	@Setter
	private PlayerProfile playerProfile = new PlayerProfile();

	public Friend() {
		playerProfile = new PlayerProfile();
	}
}
