package com.ryhma6.maven.steambeater.model.steamAPI;

public class PlayerProfile {
	//public data, always available
	private String steamid;
	private String personaname;
	private String profileurl;
	private String avatar;
	private String avatarmedium;
	private String avatarfull;
	private String personastate;
	private String communityvisibilitystate;
	private String profilestate;
	private String lastlogoff;
	private String commentpermission;
	
	//private data hidden if the user has their profile visibility set to "Friends Only" or "Private";
	private String realname;
	private String primaryclanid;
	private String timecreated;
	private String gameid;
	private String gameserverip;
	private String gameextrainfo;
	private String cityid;
	private String loccountrycode;
	private String locstatecode;
	private String loccityid;
	
	public String getSteamid() {
		return steamid;
	}
	public void setSteamid(String steamid) {
		this.steamid = steamid;
	}
	public String getPersonaname() {
		return personaname;
	}
	public void setPersonaname(String personaname) {
		this.personaname = personaname;
	}
	public String getProfileurl() {
		return profileurl;
	}
	public void setProfileurl(String profileurl) {
		this.profileurl = profileurl;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getAvatarmedium() {
		return avatarmedium;
	}
	public void setAvatarmedium(String avatarmedium) {
		this.avatarmedium = avatarmedium;
	}
	public String getAvatarfull() {
		return avatarfull;
	}
	public void setAvatarfull(String avatarfull) {
		this.avatarfull = avatarfull;
	}
	public String getPersonastate() {
		return personastate;
	}
	public void setPersonastate(String personastate) {
		this.personastate = personastate;
	}
	public String getCommunityvisibilitystate() {
		return communityvisibilitystate;
	}
	public void setCommunityvisibilitystate(String communityvisibilitystate) {
		this.communityvisibilitystate = communityvisibilitystate;
	}
	public String getProfilestate() {
		return profilestate;
	}
	public void setProfilestate(String profilestate) {
		this.profilestate = profilestate;
	}
	public String getLastlogoff() {
		return lastlogoff;
	}
	public void setLastlogoff(String lastlogoff) {
		this.lastlogoff = lastlogoff;
	}
	public String getCommentpermission() {
		return commentpermission;
	}
	public void setCommentpermission(String commentpermission) {
		this.commentpermission = commentpermission;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getPrimaryclanid() {
		return primaryclanid;
	}
	public void setPrimaryclanid(String primaryclanid) {
		this.primaryclanid = primaryclanid;
	}
	public String getTimecreated() {
		return timecreated;
	}
	public void setTimecreated(String timecreated) {
		this.timecreated = timecreated;
	}
	public String getGameid() {
		return gameid;
	}
	public void setGameid(String gameid) {
		this.gameid = gameid;
	}
	public String getGameserverip() {
		return gameserverip;
	}
	public void setGameserverip(String gameserverip) {
		this.gameserverip = gameserverip;
	}
	public String getGameextrainfo() {
		return gameextrainfo;
	}
	public void setGameextrainfo(String gameextrainfo) {
		this.gameextrainfo = gameextrainfo;
	}
	public String getCityid() {
		return cityid;
	}
	public void setCityid(String cityid) {
		this.cityid = cityid;
	}
	public String getLoccountrycode() {
		return loccountrycode;
	}
	public void setLoccountrycode(String loccountrycode) {
		this.loccountrycode = loccountrycode;
	}
	public String getLocstatecode() {
		return locstatecode;
	}
	public void setLocstatecode(String locstatecode) {
		this.locstatecode = locstatecode;
	}
	public String getLoccityid() {
		return loccityid;
	}
	public void setLoccityid(String loccityid) {
		this.loccityid = loccityid;
	}
}
