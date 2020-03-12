package com.ryhma6.maven.steambeater;

import java.io.IOException;

import com.ryhma6.maven.steambeater.controller.SteamOpenIDSignController;
import com.ryhma6.maven.steambeater.model.DatabaseController;
import com.ryhma6.maven.steambeater.model.SteamAPICalls;
import com.ryhma6.maven.steambeater.model.UserPreferences;
import com.ryhma6.maven.steambeater.model.steamAPI.GameData;
import com.ryhma6.maven.steambeater.view.FriendsListController;
import com.ryhma6.maven.steambeater.view.GameListController;
import com.ryhma6.maven.steambeater.view.StatComparisonController;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private Service<Integer> steamAPIService;
	private SteamAPICalls steamAPI = new SteamAPICalls();
	private GameListController gameListController;
	private DatabaseController databaseController = new DatabaseController();

	@FXML
	private FlowPane sidebar;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Steambeater");

		initRootLayout();
		showGameList();
		showFriendsList();

		steamAPIService = new Service<Integer>() {
			@Override
			protected Task<Integer> createTask() {
				return new Task<Integer>() {
					@Override
					protected Integer call() throws Exception {
						steamAPI.loadSteamGames();
						steamAPI.loadSteamFriends();
						return null;
					}

					@Override
					protected void succeeded() {
						super.succeeded();
						steamAPI.setSavedSelections(databaseController.getAllUserGames(UserPreferences.getSteamID()));
					}
				};
			}
		};

		if (UserPreferences.getSteamID() != null)
			loadSteamAPIData();
	}

	public void addGameToDatabase(GameData game) {
		databaseController.addGame(game, UserPreferences.getSteamID());
		System.out.println("UserID used for db: " + UserPreferences.getSteamID());
	}

	public void loadSteamAPIData() {
		if (!steamAPIService.isRunning()) {
			steamAPIService.reset();
			steamAPIService.start();
		} else {
			System.out.println("Loading SteamData not finished");
		}
	}

	public void resetSteamAPIData() {
		steamAPIService.cancel();
		steamAPI.resetItems();
	}

	public void loadAchievementData(int appID) {
		Task<Boolean> task = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				steamAPI.loadGameSchema(appID);
				return true;
			}
		};// update UI (achievement list) when the info has been retrieved from SteamAPI
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {
				gameListController.refreshAchievementList();
				System.out.println("Achiev load finish, starting refresh");
			}
		});

		// start the background task
		Thread th = new Thread(task);
		th.start();
	}

	/**
	 * Initializes the root layout.
	 */
	private void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			// Give the controller access to the main app.
			SteamOpenIDSignController controller = loader.getController();
			controller.setMainApp(this);
			ListView<String> list = new ListView<String>();
			ObservableList<String> items = FXCollections.observableArrayList("Single", "Double", "Suite", "Family App");
			list.setItems(items);
			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds gamelist view to the root layout
	 */
	private void showGameList() {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/GameList.fxml"));
			AnchorPane gameList = (AnchorPane) loader.load();
			// Set person overview into the center of root layout.
			rootLayout.setCenter(gameList);
			// Give the controller access to the main app.
			gameListController = loader.getController();
			gameListController.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void showFriendsList() {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/friendsList.fxml"));
			AnchorPane friends = (AnchorPane) loader.load();
			// Set person overview into the center of root layout.
			FlowPane sidebar = (FlowPane) rootLayout.lookup("#sidebar");
			sidebar.getChildren().add(friends);
			FriendsListController controller = loader.getController();
			loadStatComparison(controller);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadStatComparison(FriendsListController flCont) {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/statComparison.fxml"));
			AnchorPane statComparison = (AnchorPane) loader.load();
			// Set person overview into the center of root layout.
			rootLayout.setRight(statComparison);
			StatComparisonController controller = loader.getController();
			flCont.setStatComparisonController(controller);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the main stage.
	 * 
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}

}