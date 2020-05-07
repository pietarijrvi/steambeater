package com.ryhma6.maven.steambeater.model.steamAPI;

import lombok.Getter;
import lombok.Setter;

/**
 * Profile data from the Steam API
 */
public class PlayerProfile {
	// public data, always available

	/**
	 * 64bit SteamID of the user
	 * 
	 * @param steamid SteamID of the user
	 * @return SteamID of the user
	 */
	@Getter
	@Setter
	private String steamid;
	
	@Getter
	@Setter
	private String avatarhash;

	/**
	 * The player's persona name (display name)
	 * 
	 * @param personaname player´s display name
	 * @return player´s display name
	 */
	@Getter
	@Setter
	private String personaname = "";

	/**
	 * The full URL of the player's Steam Community profile
	 * 
	 * @param profileurl player´s Steam Community profile URL
	 * @return player´s Steam Community profile URL
	 */
	@Getter
	@Setter
	private String profileurl;

	/**
	 * The full URL of the player's 32x32px avatar
	 * 
	 * @param avatar player´s 32x32px avatar
	 * @return player´s 32x32px avatar
	 */
	@Getter
	@Setter
	private String avatar;

	/**
	 * The full URL of the player's 64x64px avatar
	 * 
	 * @param avatarmedium player´s 64x64px avatar
	 * @return player´s 64x64px avatar
	 */
	@Getter
	@Setter
	private String avatarmedium;

	/**
	 * The full URL of the player's 184x184px avatar.
	 * 
	 * @param player's 184x184px avatar
	 * @return player's 184x184px avatar
	 */
	@Getter
	@Setter
	private String avatarfull;

	/**
	 * The user's current status. 0 - Offline, 1 - Online, 2 - Busy, 3 - Away, 4 -
	 * Snooze, 5 - looking to trade, 6 - looking to play. If the player's profile is
	 * private, this will always be "0", except if the user has set their status to
	 * looking to trade or looking to play, because a bug makes those status appear
	 * even if the profile is private
	 * 
	 * @param personastate The user's current status
	 * @return The user's current status
	 */
	@Getter
	@Setter
	private String personastate;

	/**
	 * This represents whether the profile is visible or not
	 * 
	 * @param communityvisibilitystate whether the profile is visible or not
	 * @return whether the profile is visible or not
	 */
	@Getter
	@Setter
	private String communityvisibilitystate;

	/**
	 * If set, indicates the user has a community profile configured (will be set to
	 * '1')
	 * 
	 * @param profilestate If set, indicates the user has a community profile
	 *                     configured
	 * @return if user has a community profile configured
	 */
	@Getter
	@Setter
	private String profilestate;

	/**
	 * The last time the user was online, in unix time. Only available when you are
	 * friends with the requested user
	 * 
	 * @param lastlogoff the last time the user was online
	 * @return the last time the user was online
	 */
	@Getter
	@Setter
	private String lastlogoff;

	/**
	 * If set, indicates the profile allows public comments
	 * 
	 * @param commentpermission if the profile allows public comments
	 * @return if the profile allows public comments
	 */
	@Getter
	@Setter
	private String commentpermission;

	/**
	 * The user's current state flag. 0 - Not available, 1 - Offline, 2 - Online, 4
	 * - Golden, 64 - Online using big picture, 256 - Online using web client, 512 -
	 * Online using mobile, 1024 - Online using steam controller
	 * 
	 * @param personastateflags user´s current state flag
	 * @return user´s current state flag
	 */
	@Getter
	@Setter
	private String personastateflags;

	// private data hidden if the user has their profile visibility set to "Friends
	// Only" or "Private";

	/**
	 * The player's "Real Name", if they have set it.
	 * 
	 * @param realname player´s real name if set
	 * @return player´s real name if set
	 */
	@Getter
	@Setter
	private String realname;

	/**
	 * The player's primary group, as configured in their Steam Community profile.
	 * 
	 * @param primaryclanid The player's primary group
	 * @return The player's primary group
	 */
	@Getter
	@Setter
	private String primaryclanid;

	/**
	 * The time the player's account was created
	 * 
	 * @param timecreated The time the player's account was created
	 * @return The time the player's account was created
	 */
	@Getter
	@Setter
	private String timecreated;

	/**
	 * If the user is currently in-game, this value will be returned and set to the
	 * gameid of that game.
	 * 
	 * @param gameid If the user is currently in-game, this value will be returned
	 *               and set to the gameid of that game.
	 * @return If the user is currently in-game, this value will be returned and set
	 *         to the gameid of that game.
	 */
	@Getter
	@Setter
	private String gameid;

	/**
	 * The ip and port of the game server the user is currently playing on, if they
	 * are playing on-line in a game using Steam matchmaking. Otherwise will be set
	 * to "0.0.0.0:0".
	 * 
	 * @param gameserverip The ip and port of the game server the user is currently
	 *                     playing on
	 * @return The ip and port of the game server the user is currently playing on
	 */
	@Getter
	@Setter
	private String gameserverip;

	/**
	 * If the user is currently in-game, this will be the name of the game they are
	 * playing. This may be the name of a non-Steam game shortcut.
	 * 
	 * @param gameextrainfo If the user is currently in-game, this will be the name
	 *                      of the game they are playing.
	 * @return If the user is currently in-game, this will be the name of the game
	 *         they are playing.
	 */
	@Getter
	@Setter
	private String gameextrainfo;

	/**
	 * This value will be removed in a future update (see loccityid)
	 * 
	 * @param cityid This value will be removed in a future update (see loccityid)
	 * @return This value will be removed in a future update (see loccityid)
	 */
	@Getter
	@Setter
	private String cityid;

	/**
	 * 
	 * @param
	 * @return
	 */
	@Getter
	@Setter
	private String loccountrycode;

	/**
	 * If set on the user's Steam Community profile, The user's country of
	 * residence, 2-character ISO country code
	 * 
	 * @param locstatecode The user's country of residence, 2-character ISO country
	 *                     code
	 * @return The user's country of residence, 2-character ISO country code
	 */
	@Getter
	@Setter
	private String locstatecode;

	/**
	 * An internal code indicating the user's city of residence. A future update
	 * will provide this data in a more useful way.
	 * 
	 * @param loccityid An internal code indicating the user's city of residence.
	 * @return An internal code indicating the user's city of residence.
	 */
	@Getter
	@Setter
	private String loccityid;

	/**
	 * No Steam API documentation found
	 * @param lobbysteamid
	 * @return lobbysteamid
	 */
	@Getter
	@Setter
	private String lobbysteamid;

	/**
	 * No Steam API documentation found
	 * @param gameserversteamid
	 * @return gameserversteamid
	 */
	@Getter
	@Setter
	private String gameserversteamid;
}
