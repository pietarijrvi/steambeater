package com.ryhma6.maven.steambeater.model;

import javax.persistence.*;

@Entity
@Table(name="GameList")
public class GameListEntry {
	
	@Id
	@Column(name="entryID")
	private String entryID;
	
	@Column(name="gameID")
	private String gameID;
	
	@Column(name="userID")
	private String userID;
	
	@Column(name="beaten")
	private Boolean beaten;
	
	@Column(name="unbeatable")
	private Boolean unbeatable;
	
	@Column(name="ignored")
	private Boolean ignored;
	
	public GameListEntry(String gameID, String userID, Boolean beaten, Boolean unbeatable, Boolean ignored) {
		super();
		this.userID = userID;
		this.gameID = gameID;
		this.beaten = beaten;
		this.unbeatable = unbeatable;
		this.ignored = ignored;
		this.entryID = gameID + userID;
	}
	
	public Boolean getBeaten() {
		return beaten;
	}

	public void setBeaten(Boolean beaten) {
		this.beaten = beaten;
	}

	public Boolean getUnbeatable() {
		return unbeatable;
	}

	public void setUnbeatable(Boolean unbeatable) {
		this.unbeatable = unbeatable;
	}

	public Boolean getIgnored() {
		return ignored;
	}

	public void setIgnored(Boolean ignored) {
		this.ignored = ignored;
	}

	public GameListEntry() {
		super();
	}
	
	public String getGameID() {
		return gameID;
	}

	public void setGameID(String gameID) {
		this.gameID = gameID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
}
