package com.ryhma6.maven.steambeater.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ryhma6.maven.steambeater.model.steamAPI.GameData;

/**
 * Database DAO method tests
 *
 */
class DatabaseTest {

	// concrete DAO
	private DatabaseController dbController;
	private String userID1 = "1";
	private String userID2 = "2";
	private static GameData game1;
	private static GameData game2;

	/**
	 * Creates a new database before each test
	 */
	@BeforeEach
	void init() {
		dbController = new DatabaseController("hibernate.cfg-test.xml");
	}

	/**
	 * Prepares GameData instances used for tests
	 */
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

	/**
	 * Tests that the method for adding a game entry to database prevents inserting
	 * duplicates
	 */
	@Test
	@DisplayName("Tests that the method for adding a game entry to database prevents inserting duplicates")
	void duplicateEntry() {
		Long count = dbController.getUserGameCount(userID1);
		assertEquals(0, count, "Database not empty before starting the test");
		dbController.addGame(game1, userID1);
		dbController.addGame(game1, userID1);
		count = dbController.getUserGameCount(userID1);
		assertEquals(1, count);
	}

	/**
	 * Adds a game to database and the checks that the database includes a new row
	 * matching the inserted data
	 */
	@Test
	@DisplayName("Adds a game to database and the checks that the database includes a new row matching the inserted data")
	void addOneGetOneGameFromDB() {
		Long count = dbController.getUserGameCount(userID1);
		assertEquals(0, count, "Database not empty before starting the test");
		dbController.addGame(game1, userID1);
		GameListEntry game = dbController.getUserGame("1", userID1);
		count = dbController.getUserGameCount(userID1);
		assertEquals(1, count);
		assertEquals(game1.getName(), game.getName());
	}

	/**
	 * Adds two game entries to database and then check that the database includes
	 * two new rows
	 */
	@Test
	@DisplayName("Adds two game entries to database and then check that the database includes two new rows")
	void addMultipleGetMultipleGamesFromDB() {
		Long count = dbController.getUserGameCount(userID1);
		assertEquals(0, count, "Database not empty before starting the test");
		List<GameData> gamedata = Arrays.asList(game1, game2);
		dbController.addAllGames(gamedata, userID1);
		List<GameListEntry> games = dbController.getAllUserGames(userID1);
		count = dbController.getUserGameCount(userID1);
		assertEquals(2, count);
		assertEquals(2, games.size());
	}

	/**
	 * Tests that the count method counts the rows identified by userID (method
	 * attribute)
	 */
	@Test
	@DisplayName("Tests that the count method counts the rows identified by userID (method attribute)")
	void playerOwnGamesFromDB() {
		Long count = dbController.getUserGameCount(userID1);
		assertEquals(0, count, "Database not empty before starting the test");
		dbController.addGame(game1, userID1);
		dbController.addGame(game1, userID2);
		dbController.addGame(game2, userID2);

		List<GameListEntry> games = dbController.getAllUserGames(userID1);
		count = dbController.getUserGameCount(userID1);
		assertEquals(1, count);
		assertEquals(1, games.size());
	}
}
