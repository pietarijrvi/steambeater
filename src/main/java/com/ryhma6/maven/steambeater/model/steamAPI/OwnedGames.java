package com.ryhma6.maven.steambeater.model.steamAPI;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("response")
public class OwnedGames {
	private int game_count;
	private List<GameData> games;
	
	public int getGame_count() {
		return game_count;
	}
	public void setGame_count(int game_count) {
		this.game_count = game_count;
	}
	public List<GameData> getGames() {
		return games;
	}
	public void setGames(List<GameData> games) {
		this.games = games;
	}
	
	
}
