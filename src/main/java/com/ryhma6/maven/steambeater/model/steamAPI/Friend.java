package com.ryhma6.maven.steambeater.model.steamAPI;

public class Friend {
	private String steamid;
	private String relationship;
	private String friend_since;
	private PlayerProfile playerProfile = new PlayerProfile();
	
	public String getSteamid() {
		return steamid;
	}
	public void setSteamid(String steamid) {
		this.steamid = steamid;
	}
	public String getRelationship() {
		return relationship;
	}
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	public String getFriend_since() {
		return friend_since;
	}
	public void setFriend_since(String friend_since) {
		this.friend_since = friend_since;
	}
	public PlayerProfile getPlayerProfile() {
		return playerProfile;
	}
	public void setPlayerProfile(PlayerProfile playerProfile) {
		this.playerProfile = playerProfile;
	}
}
