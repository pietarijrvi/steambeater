package com.ryhma6.maven.steambeater.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.ryhma6.maven.steambeater.model.DatabaseController;
import com.ryhma6.maven.steambeater.model.GameListEntry;
import com.ryhma6.maven.steambeater.model.LanguageProvider;
import com.ryhma6.maven.steambeater.model.SteamAPICalls;
import com.ryhma6.maven.steambeater.model.UserPreferences;
import com.ryhma6.maven.steambeater.model.steamAPI.GameData;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * Controls the stat comparison panel, used by the FriendsListController
 * 
 * @author KimW
 *
 */
public class StatComparisonController implements Initializable {
	private SteamAPICalls api;

	/**
	 * Deepest pane of the panel
	 */
	@FXML
	private AnchorPane statAnchor;

	/**
	 * Button that closes the panel
	 */
	@FXML
	private Button closeBtn;

	/**
	 * Bar used to store the close button and used to lower the panel
	 */
	@FXML
	private BorderPane topBar;

	/**
	 * Pane into which the friend's stats are put into
	 */
	@FXML
	private AnchorPane friendStats;

	/**
	 * Pane where the user's stats are put into
	 */
	@FXML
	private AnchorPane userStats;

	/**
	 * Used to show the friend's stats
	 */
	private Label friendLabel;

	/**
	 * Used to show the user's stats
	 */
	private Label userLabel;

	/**
	 * Used to access the database
	 */
	private DatabaseController db = DatabaseController.getInstance();

	////////////////////////// SUM VARIABLES
	/// CURRENT USER

	/**
	 * Sum variable Used to count the user's games
	 */
	private int oCount;

	/**
	 * Sum variable Used to count the user's overall playtime
	 */
	private int oPlaytime;

	/**
	 * Sum variable Used to count the user's playtime in the last two weeks
	 */
	private int o2wPlaytime;

	/**
	 * Sum variable Used to calculate the user's library completion %
	 */
	private int oBeaten, oBeatable;

	/// FRIEND

	/**
	 * Sum variable Amount of games the given friend's owns
	 */
	private int fCount;

	/**
	 * Sum variable Used to count the friend's overall playtime
	 */
	private int fPlaytime;

	/**
	 * Sum variable Used to count the friend's playtime in the last two weeks
	 */
	private int f2wPlaytime;

	/**
	 * Sum variable Used to calculate the friend's library completion %
	 */
	private int fBeaten, fBeatable;

	/**
	 * Used to account for games with no database entry when calculating library
	 * completion %
	 */
	private int dbCount;

	/**
	 * Label in the statistic comparison panel for the amount of games you own 
	 */
	@FXML
	private Label youOwnedGames;
	
	/**
	 * Label in the statistic comparison panel for the amount of games your friend owns 
	 */
	@FXML
	private Label friendOwnedGames;
	
	/**
	 * Label in the statistic comparison panel for the amount of playtime you have
	 */
	@FXML
	private Label youPlaytime;
	
	/**
	 * Label in the statistic comparison panel for the amount of playtime your friend has
	 */
	@FXML
	private Label friendPlaytime;
	
	/**
	 * Label in the statistic comparison panel for the amount of playtime you have in the last 2 weeks
	 */
	@FXML
	private Label youPlaytimeWeeks;
	
	/**
	 * Label in the statistic comparison panel for the amount of playtime your friend has in the last 2 weeks
	 */
	@FXML
	private Label friendPlaytimeWeeks;
	
	/**
	 * Label in the statistic comparison panel for the amount of games your friend has completed
	 */
	@FXML
	private Label friendCompleted;
	
	/**
	 * Label in the statistic comparison panel for the amount of games you have completed
	 */
	@FXML
	private Label youCompleted;

	/**
	 * Label in the statistic comparison panel that displays the friend's Steam ID you are comparing with
	 */
	@FXML
	private Label friendTopLabel;

	/***
	 * Closes comparison stat window
	 */
	public void closeComparison() {
		statAnchor.setManaged(false);
		statAnchor.setVisible(false);
	}

	/***
	 * Opens comparison stat window
	 */
	public void openComparison() {
		statAnchor.setManaged(true);
		statAnchor.setVisible(true);
	}

	/**
	 * Loads the user's stats and the stats of the friend of which ID and name was
	 * given
	 * 
	 * @param friendsID
	 * @param name
	 */
	public void loadStats(String friendsID, String name) {

		resetSums();

		api.loadFriendsGames(friendsID);
		ObservableList<GameData> fGames = SteamAPICalls.getFriendsGames();
		List<GameListEntry> fdbGames = db.getAllUserGames(friendsID);
		friendTopLabel.setText(name);
		
		if (fGames.size() == 0) {
			friendOwnedGames.setText(String.format(LanguageProvider.getString("compPrivate"), name));
			friendPlaytime.setText(LanguageProvider.getString("compNotRetrieved"));
			friendPlaytimeWeeks.setText("");
			friendCompleted.setText("");

		} else {
			fGames.forEach((n) -> sumUpStats(n, friendsID));
			fdbGames.forEach((n) -> sumUpDatabaseStats(n, friendsID));

			// Accounting for games with no database entry
			fBeatable += fCount - dbCount;
			dbCount = 0;
			// Avoid division by 0
			if (fBeatable == 0)
				fBeatable = 1;

//			friendLabel.setText(name + " owns " +  fCount + " games"
//					+ "\nOverall playtime: " + fPlaytime/60 + " hours"
//					+ "\nPlaytime in the last 2 weeks: "+ f2wPlaytime/60 + " hours"
//					+ "\nLibrary completion: " + (100*fBeaten)/(100*fBeatable)*100 + "%");

			friendOwnedGames.setText(String.format(LanguageProvider.getString("compFriendOwn"), name, fCount));
			friendPlaytime.setText(String.format(LanguageProvider.getString("compOverall"), fPlaytime / 60));
			friendPlaytimeWeeks.setText(String.format(LanguageProvider.getString("comp2Weeks"), f2wPlaytime / 60));
			friendCompleted.setText(String.format(LanguageProvider.getString("compCompletion"), ((double)fBeaten / fBeatable) * 100));
		}

		ObservableList<GameData> oGames = SteamAPICalls.getOwnedGames();
		List<GameListEntry> odbGames = db.getAllUserGames(UserPreferences.getSteamID());

		oGames.forEach((n) -> sumUpStats(n, UserPreferences.getSteamID()));
		odbGames.forEach((n) -> sumUpDatabaseStats(n, UserPreferences.getSteamID()));

		// Accounting for games with no database entry
		oBeatable += oCount - dbCount;
		// Avoid division by 0
		if (oBeatable == 0)
			oBeatable = 1;

//		userLabel.setText("You own " + oCount + " games"
//				+ "\nOverall playtime: " + oPlaytime/60 + " hours"
//				+ "\nPlaytime in the last 2 weeks: "+ o2wPlaytime/60 + " hours"
//				+ "\nLibrary completion: " + libComp + "%");

		youOwnedGames.setText(String.format(LanguageProvider.getString("compYouOwn"), oCount));
		youPlaytime.setText(String.format(LanguageProvider.getString("compOverall"), oPlaytime / 60));
		youPlaytimeWeeks.setText(String.format(LanguageProvider.getString("comp2Weeks"), o2wPlaytime / 60));
		youCompleted.setText(String.format(LanguageProvider.getString("compCompletion"), ((double)oBeaten / oBeatable) * 100));

	}

	/**
	 * Adds the given games stats into the sum variables, variables chosen based on
	 * if the game is stated to be the current user's or not
	 * 
	 * @param game The game data that is added into the sum variables
	 * @param user Is the game owned by the current user or not
	 */
	private void sumUpStats(GameData game, String userID) {

		if (userID.equals(UserPreferences.getSteamID())) {
			oCount++;
			oPlaytime += game.getPlaytime_forever();
			o2wPlaytime += game.getPlaytime_2weeks();

		} else {
			fCount++;
			fPlaytime += game.getPlaytime_forever();
			f2wPlaytime += game.getPlaytime_2weeks();

		}
	}

	/**
	 * Adds up the sum variables with stats gotten from the database
	 */
	private void sumUpDatabaseStats(GameListEntry gle, String userID) {

		dbCount++;

		if (userID.equals(UserPreferences.getSteamID())) {

			if (gle.getBeaten()) {
				oBeaten++;
			}
			if (!gle.getIgnored() && !gle.getUnbeatable()) {
				oBeatable++;
			}

		} else {

			if (gle.getBeaten()) {
				fBeaten++;
			} else if (!gle.getIgnored() && !gle.getUnbeatable()) {
				fBeatable++;
			}

		}
	}

	/**
	 * Resets the sum variables
	 */
	private void resetSums() {
		fPlaytime = 0;
		oPlaytime = 0;
		fCount = 0;
		oCount = 0;
		o2wPlaytime = 0;
		f2wPlaytime = 0;
		oBeaten = 0;
		fBeaten = 0;
		oBeatable = 0;
		fBeatable = 0;
		dbCount = 0;
	}

	/**
	 * Initializes the panel by loading in elements and makes sure the panel is
	 * closed by default
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		api = new SteamAPICalls();
		friendLabel = new Label();
		friendStats.getChildren().add(friendLabel);
		userLabel = new Label();
		userStats.getChildren().add(userLabel);

		// Give functionality to the close button
		closeBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				closeComparison();
			}
		});

		closeComparison();
	}
}
