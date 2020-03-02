package com.ryhma6.maven.steambeater.model.steamAPI;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("friendslist")
public class FriendList {
	private List<Friend> friends;

	public List<Friend> getFriends() {
		return friends;
	}

	public void setFriends(List<Friend> friends) {
		this.friends = friends;
	}
}
