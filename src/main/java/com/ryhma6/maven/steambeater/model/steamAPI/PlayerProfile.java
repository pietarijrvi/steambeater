package com.ryhma6.maven.steambeater.model.steamAPI;

import lombok.Getter;
import lombok.Setter;

public class PlayerProfile {
	//public data, always available
	@Getter @Setter private String steamid;
	@Getter @Setter private String personaname="";
	@Getter @Setter private String profileurl;
	@Getter @Setter private String avatar;
	@Getter @Setter private String avatarmedium;
	@Getter @Setter private String avatarfull;
	@Getter @Setter private String personastate;
	@Getter @Setter private String communityvisibilitystate;
	@Getter @Setter private String profilestate;
	@Getter @Setter private String lastlogoff;
	@Getter @Setter private String commentpermission;
	@Getter @Setter private String personastateflags;
	
	//private data hidden if the user has their profile visibility set to "Friends Only" or "Private";
	@Getter @Setter private String realname;
	@Getter @Setter private String primaryclanid;
	@Getter @Setter private String timecreated;
	@Getter @Setter private String gameid;
	@Getter @Setter private String gameserverip;
	@Getter @Setter private String gameextrainfo;
	@Getter @Setter private String cityid;
	@Getter @Setter private String loccountrycode;
	@Getter @Setter private String locstatecode;
	@Getter @Setter private String loccityid;
	@Getter @Setter private String lobbysteamid;
}
