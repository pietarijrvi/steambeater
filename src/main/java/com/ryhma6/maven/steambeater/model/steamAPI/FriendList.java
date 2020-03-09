package com.ryhma6.maven.steambeater.model.steamAPI;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Object model for friend list data retrieved and parsed from SteamAPI JSON
 * response. Includes the list of all friends of the player. SteamAPI
 * documentation: https://developer.valvesoftware.com/wiki/Steam_Web_API
 */
@JsonRootName("friendslist")
public class FriendList {
	/**
	 * Friends of the player.
	 */
	private List<Friend> friends;

	/**
	 * Gets the list of player's friends.
	 * 
	 * @return List of friends
	 */
	public List<Friend> getFriends() {
		return friends;
	}

	/**
	 * Sets the friend list (from SteamAPI response).
	 * 
	 * @param friends List of friends from SteamAPI.
	 */
	public void setFriends(List<Friend> friends) {
		this.friends = friends;
	}
}
