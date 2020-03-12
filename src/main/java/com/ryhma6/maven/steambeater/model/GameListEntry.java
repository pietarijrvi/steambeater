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
	private String gameID;

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
	 * Creates an empty GameListEntry object
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
	public GameListEntry(String gameID, String userID, Boolean beaten, Boolean unbeatable, Boolean ignored) {
		super();
		this.userID = userID;
		this.gameID = gameID;
		this.beaten = beaten;
		this.unbeatable = unbeatable;
		this.ignored = ignored;
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
	public String getGameID() {
		return gameID;
	}

	/**
	 * Sets the game's ID
	 * 
	 * @param gameID
	 */
	public void setGameID(String gameID) {
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
}
