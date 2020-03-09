package com.ryhma6.maven.steambeater.model;

import javax.persistence.*;

@Entity
@Table(name="gamelist")
public class GameListEntry {
	
	@Id
	@Column(name="entryID")
	private String entryID;
	
	@Column(name="gameID")
	private String gameID;
	
	@Column(name="userID")
	private String userID;
	
	public GameListEntry(String gameID, String userID) {
		super();
		this.userID = userID;
		this.gameID = gameID;
		this.entryID = gameID + userID;
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
