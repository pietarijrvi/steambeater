package com.ryhma6.maven.steambeater.model.steamAPI;

import lombok.Getter;
import lombok.Setter;

/**
 * Object model of the game details from SteamAPI JSON response. SteamAPI
 * documentation: https://developer.valvesoftware.com/wiki/Steam_Web_API
 *
 */
public class GameData {
	
	/**
	 * Steam id of the game.
	 * 
	 * @param appid appid of the game from SteamAPI
	 * @return App ID of the game.
	 */
	@Getter @Setter private int appid;
	
	/**
	 * Name of the game.
	 * 
	 * @param name Name of the game from SteamAPI
	 * @return Name of the game.
	 */
	@Getter @Setter private String name;
	
	/**
	 * The URL of the game's icon
	 */
	private String img_icon_url;
	
	/**
	 * The URL of the game's logo
	 */
	private String img_logo_url;
	
	/**
	 * The visibility status of the game's owner's profile
	 * 
	 * @param has_community_visible_stats Visibilty status of the user's profile from SteamAPI
	 * @return Visibilty status of the user's profile
	 */
	@Getter @Setter private String has_community_visible_stats;
	
	/**
	 * User's overall playtime for the game 
	 * 
	 * @param playtime_forever User's overall playtime of the game from SteamAPI
	 * @return User's overall playtime of the game in minutes
	 */
	@Getter @Setter private int playtime_forever;
	
	/**
	 * User's overall playtime for the game on windows
	 * 
	 * @param playtime_windows_forever User's overall playtime of the game on windows from SteamAPI
	 * @return User's overall playtime of the game on windows in minutes
	 */
	@Getter @Setter private int playtime_windows_forever;
	
	/**
	 * User's overall playtime for the game on mac
	 * 
	 * @param playtime_mac_forever User's overall playtime of the game on mac from SteamAPI
	 * @return User's overall playtime of the game on mac in minutes
	 */
	@Getter @Setter private int playtime_mac_forever;
	
	/**
	 * User's overall playtime for the game on linux
	 * 
	 * @param playtime_linux_forever User's overall playtime of the game on linux from SteamAPI
	 * @return User's overall playtime of the game on linux in minutes
	 */
	@Getter @Setter private int playtime_linux_forever;
	
	/**
	 * User's playtime for the game in the last two weeks
	 * 
	 * @param playtime_2weeks User's overall playtime of the game in the last two weeks from SteamAPI
	 * @return User's overall playtime of the game in the last two weeks in minutes
	 */
	@Getter @Setter private int playtime_2weeks;
	
	/**
	 * User's game's stats
	 * 
	 * @param gameStatistics The user's stats for the game from SteamAPI
	 * @return The user's stats for the game
	 */
	@Getter @Setter private GameStatistics gameStatistics = new GameStatistics();
	
	/**
	 * Boolean value indicating the unbeatable status of the game
	 * 
	 * @param unbeateable Boolean value indicating the unbeatable status of the game set by the user
	 * @return Boolean value indicating the unbeatable status of the game
	 */
	@Getter @Setter private boolean unbeatable = false;
	
	/**
	 * Boolean value indicating the ignored status of the game
	 * 
	 * @param unbeateable Boolean value indicating the ignored status of the game set by the user
	 * @return Boolean value indicating the ignored status of the game
	 */
	@Getter @Setter private boolean ignored = false;
	
	/**
	 * Boolean value indicating the completion status of the game
	 * 
	 * @param unbeateable Boolean value indicating the completion status of the game set by the user
	 * @return Boolean value indicating the completion status of the game
	 */
	@Getter @Setter private boolean beaten = false;
	
	/**
	 * Creates an empty GameData object
	 */
	public GameData() {
	}
	
	/**
	 * Creates a GameData object with basic stats
	 * @param appid
	 * @param name
	 * @param playtime_forever
	 */
	public GameData(int appid, String name, int playtime_forever) {
		this.appid=appid;
		this.name=name;
		this.playtime_forever=playtime_forever;
	}
	
	/**
	 * @return The game's icon's URL
	 */
	public String getImg_icon_url() {
		String imgUrl = null;
		if(img_logo_url!=null)
			imgUrl = String.format("http://media.steampowered.com/steamcommunity/public/images/apps/%s/%s.jpg", appid, img_icon_url);
		return imgUrl;
	}
	
	/**
	 * Sets the game's icon's URL
	 * @param img_icon_url
	 */
	public void setImg_icon_url(String img_icon_url) {
		this.img_icon_url = img_icon_url;
	}
	
	/**
	 * @return The game's logo's URL
	 */
	public String getImg_logo_url() {
		return img_logo_url;
	}
	
	public String getImgLogoFullUrl() {
		String imgUrl = null;
		if(img_logo_url!=null)
			imgUrl = String.format("http://media.steampowered.com/steamcommunity/public/images/apps/%s/%s.jpg", appid, img_logo_url);
		return imgUrl;	
	}
	
	/**
	 * Sets the game's logo's URL
	 * @param img_logo_url
	 */
	public void setImg_logo_url(String img_logo_url) {
		this.img_logo_url = img_logo_url;
	}
}
