package com.ryhma6.maven.steambeater.view;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import com.ryhma6.maven.steambeater.model.steamAPI.GameData;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

class JavaFXGameListTest extends ApplicationTest {

	// JavaFX scene of GameList.fxml
	private Scene scene;
	// JavaFX controller of GameList.fxml
	private GameListController gameListController;
	// GameData entries created before tests
	private List<GameData> testGameList;

	/**
	 * Starting JavaFX environment (loading GameList.fxml) as a base for testing
	 */
	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GameList.fxml"));
		stage.setScene(scene = new Scene(loader.load(), 300, 300));
		stage.show();
		gameListController = loader.getController();
	}

	@BeforeAll
	public static void setupSpec() throws Exception {
		System.setProperty("java.awt.headless", "true");
		System.setProperty("testfx.robot", "glass");
		System.setProperty("testfx.headless", "true");
		System.setProperty("prism.order", "sw");
		System.setProperty("prism.text", "t2k");
		// System.setProperty("prism.verbose", "true");
	}

	/**
	 * Create test data (unordered list of GameData entries) for gameListController.
	 * Test data represents the game list that would be retrieved from SteamAPI.
	 */
	@BeforeEach
	public void testSetupGameList() {
		testGameList = new ArrayList<GameData>();
		testGameList.add(new GameData(8, "AdaGG", 8));
		testGameList.add(new GameData(2, "Bill", 7));
		testGameList.add(new GameData(3, "Cathy", 6));
		testGameList.add(new GameData(4, "DaveGG", 5));
		testGameList.add(new GameData(5, "Ethan", 4));
		testGameList.add(new GameData(6, "Fred", 3));
		testGameList.add(new GameData(7, "Gideon", 2));
		testGameList.add(new GameData(1, "Ann", 4));

		gameListController.setGames(FXCollections.observableList(testGameList));

		// FX application thread
		Platform.runLater(() -> {
			gameListController.loadGames();
		});
	}

	@Test
	@DisplayName("Test that the sorting selection (name) sorts the UI game list")
	public void testOrderByName() {

		/*
		 * Copying test data list and arranging it based on the game names of the
		 * entries. This list is used as a reference - expected result after sorting.
		 */
		List<GameData> orderedByNameList = new ArrayList<GameData>(testGameList);
		orderedByNameList.sort(Comparator.comparing(GameData::getName));

		// FX application thread
		Platform.runLater(() -> {
			@SuppressWarnings("unchecked")
			ComboBox<String> combo = (ComboBox<String>) scene.lookup("#sortingChoice");
			combo.getSelectionModel().select(1);
			combo.getSelectionModel().select(0);
		});

		try {
			// Tests will be run after the events on FX application thread have finished
			assertAfterJavaFxPlatformEventsAreDone(() -> {
				@SuppressWarnings("unchecked")
				ListView<GameData> gameList = (ListView<GameData>) scene.lookup("#gameList");
				List<GameData> actual = gameList.getItems();
				System.out.println(gameList);
				System.out.println("actual size: " + actual.size());

				assertEquals(orderedByNameList, actual, "ordering by name failed - gameList in unexpected order");
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Test that the sorting selection (playtime) sorts the UI game list")
	public void testOrderByPlaytime() {
		/*
		 * Copying test data list and arranging it based on the game names of the
		 * entries. This list is used as a reference - expected result after sorting.
		 */
		List<GameData> orderedByPlaytimeList = new ArrayList<GameData>(testGameList);
		orderedByPlaytimeList.sort(Comparator.comparing(GameData::getPlaytime_forever).reversed());

		// FX application thread
		Platform.runLater(() -> {
			@SuppressWarnings("unchecked")
			ComboBox<String> combo = (ComboBox<String>) scene.lookup("#sortingChoice");
			combo.getSelectionModel().select(0);
			combo.getSelectionModel().select(1);
		});

		try {
			// Tests will be run after the events on FX application thread have finished
			assertAfterJavaFxPlatformEventsAreDone(() -> {
				@SuppressWarnings("unchecked")
				ListView<GameData> gameList = (ListView<GameData>) scene.lookup("#gameList");
				List<GameData> actual = gameList.getItems();
				System.out.println(gameList);
				System.out.println("actual size: " + actual.size());

				assertEquals(orderedByPlaytimeList, actual,
						"ordering by playtime failed - gameList in unexpected order");
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testFilterByName() {

		// FX application thread
		Platform.runLater(() -> {
			TextField searchField = (TextField) scene.lookup("#searchField");
			searchField.setText("GG");
		});

		try {
			// Tests will be run after the events on FX application thread have finished
			assertAfterJavaFxPlatformEventsAreDone(() -> {
				@SuppressWarnings("unchecked")
				ListView<GameData> gameList = (ListView<GameData>) scene.lookup("#gameList");

				List<GameData> actual = gameList.getItems();
				System.out.println(gameList);
				System.out.println("actual size: " + actual.size());
				/*
				 * Copying test data list and arranging it based on the game names of the
				 * entries. This list is used as a reference - expected result after sorting.
				 */
				List<String> gameNames = new ArrayList<String>();
				for (GameData gd : actual) {
					gameNames.add(gd.getName());
				}

				assertThat(gameNames, containsInAnyOrder("AdaGG", "DaveGG"));
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Helper method for ensuring that the actual tests won't be run until Runnables
	 * on the JavaFX application thread have finished
	 */
	private void assertAfterJavaFxPlatformEventsAreDone(Runnable runnable) throws InterruptedException {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		Platform.runLater(countDownLatch::countDown);
		countDownLatch.await();
		runnable.run();
	}
}
