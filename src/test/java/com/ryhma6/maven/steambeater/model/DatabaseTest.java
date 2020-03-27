package com.ryhma6.maven.steambeater.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.ryhma6.maven.steambeater.model.steamAPI.GameData;

class DatabaseTest {

	private DatabaseController dbController;
	private String userID = "1";
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
	void addOnegetOneGameFromDBTest() {
		Long count = dbController.getUserGameCount(userID);
		assertEquals(0, count);
		dbController.addGame(game1, userID);
		GameListEntry game = dbController.getUserGame("1", userID);
		count = dbController.getUserGameCount(userID);
		assertEquals(1, count);
		assertEquals(game1.getName(), game.getName());
	}
	
	@Test
	void testB(){
		//dbController.addAllGames(games, userID)
		//dbController.getUserGame(gameID, userID)
		//dbController.getAllUserGames(userID)
	}
	
	@Test
	void test2() {
		Long count = dbController.getUserGameCount(userID);
		assertEquals(0, count);
		
		//GameListEntry game = new GameListEntry();
		GameData game = new GameData();
		
		dbController.addGame(game, userID);
		count = dbController.getUserGameCount(userID);
		assertEquals(1, count);
		List<GameListEntry> games = dbController.getAllUserGames(userID);
		assertEquals(game.getName(), games.get(0).getName());
	}

}
