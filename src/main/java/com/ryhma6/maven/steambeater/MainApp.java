package com.ryhma6.maven.steambeater;

import java.io.IOException;

import com.ryhma6.maven.steambeater.controller.SteamOpenIDSignController;
import com.ryhma6.maven.steambeater.model.DatabaseController;
import com.ryhma6.maven.steambeater.model.LoadingStatus;
import com.ryhma6.maven.steambeater.model.ObservableLoadingStatus;
import com.ryhma6.maven.steambeater.model.SteamAPICalls;
import com.ryhma6.maven.steambeater.model.UserPreferences;
import com.ryhma6.maven.steambeater.model.steamAPI.GameData;
import com.ryhma6.maven.steambeater.view.FriendsListController;
import com.ryhma6.maven.steambeater.view.GameListController;
import com.ryhma6.maven.steambeater.view.ProfileController;
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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The main class of the program
 *
 */
public class MainApp extends Application {

	/**
	 * The window
	 */
	private Stage primaryStage;

	/**
	 * The root layout of the window
	 */
	private BorderPane rootLayout;

	/**
	 * The steam API service
	 */
	private Service<Integer> steamAPIService;

	/**
	 * Used to call the steam API
	 */
	private SteamAPICalls steamAPI = new SteamAPICalls();

	/**
	 * Controls the game list
	 */
	private GameListController gameListController;
	
	/**
	 * 
	 */
	private SteamOpenIDSignController steamOpenIDSignController;
	
	/**
	 * Used to access to the database
	 */
	private DatabaseController databaseController = DatabaseController.getInstance();

	/**
	 * The left sidebar of the window
	 */
	@FXML
	private FlowPane sidebar;

	/**
	 * Used in moving the undecorated app window
	 */
	private double xOffset = 0;

	/**
	 * Used in moving the undecorated app window
	 */
	private double yOffset = 0;


	/**
	 * Starts the app and loads everything in
	 */
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Steambeater");

		primaryStage.initStyle(StageStyle.UNDECORATED);
		
		initRootLayout();
		showProfile();
		loadUI();

		// databaseController.init();
		// initDatabase();
		

		// grab your root here
		rootLayout.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});

		// move around here
		rootLayout.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				primaryStage.setX(event.getScreenX() - xOffset);
				primaryStage.setY(event.getScreenY() - yOffset);
			}
		});

		steamAPIService = new Service<Integer>() {
			@Override
			protected Task<Integer> createTask() {
				return new Task<Integer>() {
					@Override
					protected Integer call() throws Exception {
						steamAPI.loadSignedPlayerProfileFromSteam();
						// load game list from Steam API
						ObservableLoadingStatus.getInstance().setLoadingStatus(LoadingStatus.API_GAMES);
						Long count = databaseController.getUserGameCount(UserPreferences.getSteamID());
						if (count == null)
							ObservableLoadingStatus.getInstance().setDatabaseFailure();
						if (steamAPI.loadSteamGames()) {
							// add game list to database after successful load (new user)
							if (count != null && count < 1) {
								databaseController.addAllGames(steamAPI.getLoadedGames(), UserPreferences.getSteamID());
							} else {
								// load selection data (beaten, unbeatable, ignored) from database
								if (count != null) {
									steamAPI.setSavedSelections(
											databaseController.getAllUserGames(UserPreferences.getSteamID()));
								} else {
									steamAPI.setSavedSelections(null);
								}
							}
						} else {
							// if loading game data from Steam API failed, load (possibly outdated) game
							// list from database
							ObservableLoadingStatus.getInstance().setApiLoadFailure();
							if (count != null) {
								steamAPI.loadGamesFromDatabase(
										databaseController.getAllUserGames(UserPreferences.getSteamID()));
							}
						}
						ObservableLoadingStatus.getInstance().setLoadingStatus(LoadingStatus.API_FRIENDS);
						steamAPI.loadSteamFriends();
						return null;
					}

					@Override
					protected void succeeded() {
						super.succeeded();
						ObservableLoadingStatus.getInstance().setLoadingStatus(LoadingStatus.COMPLETED);
						// steamAPI.setSavedSelections(databaseController.getAllUserGames(UserPreferences.getSteamID()));
					}
				};
			}
		};

		// inits database and loads api data after succeeding
		initDatabase();
	}
	
	public void loadUI() {
		showGameList();
		showFriendsList();
	}

	/**
	 * Adds the given game into the database
	 * 
	 * @param game
	 */
	public void addGameToDatabase(GameData game) {
		databaseController.addGame(game, UserPreferences.getSteamID());
		System.out.println("UserID used for db: " + UserPreferences.getSteamID());
	}

	/**
	 * Starts up the steam API
	 */
	private void loadSteamAPIData() {
		if (!steamAPIService.isRunning()&&!UserPreferences.getSteamID().equals("null")) {
			steamAPIService.reset();
			steamAPIService.start();
		} else {
			System.out.println("Loading SteamData not finished");
		}
	}

	/**
	 * Reloads api data (and initialises database if session factory doesn't exist
	 * yet)
	 */
	public void reloadData() {
		if (databaseController.getSessionFactoryStatus())
			loadSteamAPIData();
		else
			initDatabase();
	}

	/**
	 * Resets the steam API's data
	 */
	public void resetSteamAPIData() {
		steamAPIService.cancel();
		steamAPI.resetItems();
		ObservableLoadingStatus.getInstance().setLoadingStatus(LoadingStatus.PRELOAD);
	}

	/**
	 * Loads the achievement data of the given game
	 * 
	 * @param appID The ID of the game
	 */
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
	 * Initializes database (task/new thread) and after succeeding starts loading
	 * api data
	 */
	private void initDatabase() {
		Task<Boolean> task = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				ObservableLoadingStatus.getInstance().setLoadingStatus(LoadingStatus.DATABASE_CONNECTING);
				databaseController.init();
				return true;
			}
		};
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {
				System.out.println("Database load finish");
				// if logged in (steamID exists and saved to preferences), automatically load
				// user data (SteamAPI and database)
				if (!UserPreferences.getSteamID().equals("null")) {
					loadSteamAPIData();
					System.out.println("Steam login id: " + UserPreferences.getSteamID());
				} else
					ObservableLoadingStatus.getInstance().setLoadingStatus(LoadingStatus.PRELOAD);
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
			steamOpenIDSignController = loader.getController();
			steamOpenIDSignController.setMainApp(this);
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

	/**
	 * Loads the friends list controller and inserts the friendsList.fxml into the
	 * root layout
	 */
	private void showFriendsList() {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/friendsList.fxml"));
			AnchorPane friends = (AnchorPane) loader.load();
			// Set person overview into the center of root layout.
			AnchorPane sidebar = (AnchorPane) rootLayout.lookup("#sidebar");
			
			ObservableList<Node> sidebarChildren = sidebar.getChildren();
			
			if (sidebarChildren.size() > 0) 
				sidebarChildren.remove(0);
			
			sidebarChildren.add(friends);
			FriendsListController controller = loader.getController();
			loadStatComparison(controller);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads the stat comparison controller and inserts statComparison.fxml into the
	 * root layout Gives the stat comparison controller to the the friends list
	 * controller
	 * 
	 * @param flCont friends list controller that works with the stat comparison
	 *               panel
	 */
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
	
	private void showProfile() {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/profile.fxml"));
			VBox profile = (VBox) loader.load();
			// Set person overview into the center of root layout.
			rootLayout.setBottom(profile);
			// Give the controller access to the main app.
			ProfileController controller = loader.getController();
			controller.setMainApp(this);
			steamOpenIDSignController.setProfileController(controller);
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