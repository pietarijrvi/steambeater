package com.ryhma6.maven.steambeater.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ryhma6.maven.steambeater.model.steamAPI.GameData;

class DatabaseTest {

	private DatabaseController dbController;
	private String userID1 = "1";
	private String userID2 = "2";
	private static GameData game1;
	private static GameData game2;
	
	
	
	@BeforeEach
	void init() {
		dbController = new DatabaseController();
	}
	
	@BeforeAll
	static void setGameData() {
		game1 = new GameData();
		game1.setAppid(1);
		game1.setName("testGame1");
		game1.setBeaten(true);
		game1.setUnbeatable(true);
		game1.setIgnored(true);
		game1.setImg_logo_url("testUrl");
		
		game2 = new GameData();
		game2.setAppid(2);
		game2.setName("testGame2");
		game2.setBeaten(false);
		game2.setUnbeatable(false);
		game2.setIgnored(false);
		game2.setImg_logo_url("testUrl2");
	}
	
	@Test
	void duplicateEntry() {
		Long count = dbController.getUserGameCount(userID1);
		assertEquals(0, count);
		dbController.addGame(game1, userID1);
		dbController.addGame(game1, userID1);
		count = dbController.getUserGameCount(userID1);
		assertEquals(1, count);
	}
	
	@Test
	void addOneGetOneGameFromDB() {
		Long count = dbController.getUserGameCount(userID1);
		assertEquals(0, count);
		dbController.addGame(game1, userID1);
		GameListEntry game = dbController.getUserGame("1", userID1);
		count = dbController.getUserGameCount(userID1);
		assertEquals(1, count);
		assertEquals(game1.getName(), game.getName());
	}
	
	@Test
	void addMultipleGetMultipleGamesFromDB() {
		Long count = dbController.getUserGameCount(userID1);
		assertEquals(0, count);
		List<GameData> gamedata =  Arrays.asList(game1,  game2);
		dbController.addAllGames(gamedata, userID1);
		List<GameListEntry> games = dbController.getAllUserGames(userID1);
		count = dbController.getUserGameCount(userID1);
		assertEquals(2, count);
		assertEquals(2, games.size());
	}
	
	@Test
	void playerOwnGamesFromDB(){
		Long count = dbController.getUserGameCount(userID1);
		assertEquals(0, count);
		dbController.addGame(game1, userID1);
		dbController.addGame(game1, userID2);
		dbController.addGame(game2, userID2);
		
		List<GameListEntry> games = dbController.getAllUserGames(userID1);
		count = dbController.getUserGameCount(userID1);
		assertEquals(1, count);
		assertEquals(1, games.size());
	}
}
