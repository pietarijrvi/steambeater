package com.ryhma6.maven.steambeater.model;

import javax.persistence.*;

/**
 * Table where all the user set game data is saved in
 * 
 * @author KimW
 *
 */
@Entity
@Table(name = "GameList")
public class GameListEntry {

	/**
	 * Acts as the ID for the database table, formed from gameID and userID
	 */
	@Id
	@Column(name = "entryID")
	private String entryID;

	/**
	 * ID of the game
	 */
	@Column(name = "gameID")
	private int gameID;

	/**
	 * Game owners ID
	 */
	@Column(name = "userID")
	private String userID;

	/**
	 * Boolean indicating if the game is beaten
	 */
	@Column(name = "beaten")
	private Boolean beaten;

	/**
	 * Boolean indicating if the game is unbeatable
	 */
	@Column(name = "unbeatable")
	private Boolean unbeatable;

	/**
	 * Boolean indicating if the game is ignored
	 */
	@Column(name = "ignored")
	private Boolean ignored;
	
	/**
	 * Game logo image url
	 */
	@Column(name = "logoImageUrl")
	private String logoImageUrl;
	
	/**
	 * Name of the game
	 */
	@Column(name = "name")
	private String name;
	
	/**
	 * Player's total played time of the game
	 */
	@Column(name = "playtimeForever")
	private int playtimeForever;

	/**
	 * Creates an empty GameListEntry object, entry ID has to be set separately after setting the gameID and the userID
	 */
	public GameListEntry() {
		super();
	}

	/**
	 * Creates a complete GameListEntry object
	 * 
	 * @param gameID
	 * @param userID
	 * @param beaten
	 * @param unbeatable
	 * @param ignored
	 */
	public GameListEntry(int gameID, String userID, Boolean beaten, Boolean unbeatable, Boolean ignored, String logoImageUrl, String name) {
		super();
		this.userID = userID;
		this.gameID = gameID;
		this.beaten = beaten;
		this.unbeatable = unbeatable;
		this.ignored = ignored;
		this.logoImageUrl = logoImageUrl;
		this.name = name;
		this.entryID = gameID + userID;
	}

	/**
	 * sets the entryID using the object's gameID and userID, remember to set them
	 * before setting this
	 */
	public void setEntryID() {
		this.entryID = gameID + userID;
	}

	/**
	 * @return Beaten status of the game
	 */
	public Boolean getBeaten() {
		return beaten;
	}

	/**
	 * Sets the beaten status of the game
	 * 
	 * @param beaten
	 */
	public void setBeaten(Boolean beaten) {
		this.beaten = beaten;
	}

	/**
	 * @return Unbeatable status of the game
	 */
	public Boolean getUnbeatable() {
		return unbeatable;
	}

	/**
	 * Sets the unbeatable status of the game
	 * 
	 * @param unbeatable
	 */
	public void setUnbeatable(Boolean unbeatable) {
		this.unbeatable = unbeatable;
	}

	/**
	 * @return Ignored status of the game
	 */
	public Boolean getIgnored() {
		return ignored;
	}

	/**
	 * Sets the ignored status of the game
	 * 
	 * @param ignored
	 */
	public void setIgnored(Boolean ignored) {
		this.ignored = ignored;
	}

	/**
	 * @return game's ID
	 */
	public int getGameID() {
		return gameID;
	}

	/**
	 * Sets the game's ID
	 * 
	 * @param gameID
	 */
	public void setGameID(int gameID) {
		this.gameID = gameID;
	}

	/**
	 * @return user's ID
	 */
	public String getUserID() {
		return userID;
	}

	/**
	 * Sets the user's ID
	 * 
	 * @param userID
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}

	/**
	 * Returns web url of the game image
	 * @return url
	 */
	public String getLogoImageUrl() {
		return logoImageUrl;
	}

	/**
	 * Sets the web url of the game image
	 * @param logoImageUrl web url of the image
	 */
	public void setLogoImageUrl(String logoImageUrl) {
		this.logoImageUrl = logoImageUrl;
	}
	
	/**
	 * @return name of the game
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the game
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return playtime total playtime
	 */
	public int getPlaytimeForever() {
		return playtimeForever;
	}

	/**
	 * Sets the total played time of the game
	 * 
	 * @param name
	 */
	public void setPlaytimeForever(int playtimeForever) {
		this.playtimeForever = playtimeForever;
	}
}
