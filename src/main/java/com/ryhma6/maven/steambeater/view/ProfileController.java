package com.ryhma6.maven.steambeater.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.ryhma6.maven.steambeater.MainApp;
import com.ryhma6.maven.steambeater.model.DatabaseController;
import com.ryhma6.maven.steambeater.model.GameListEntry;
import com.ryhma6.maven.steambeater.model.LanguageProvider;
import com.ryhma6.maven.steambeater.model.SteamAPICalls;
import com.ryhma6.maven.steambeater.model.UserPreferences;
import com.ryhma6.maven.steambeater.model.steamAPI.GameData;
import com.ryhma6.maven.steambeater.model.steamAPI.PlayerProfile;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ProfileController implements Initializable {

	private SteamAPICalls api;

	/**
	 * Used to access the database
	 */
	private DatabaseController db = DatabaseController.getInstance();

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

	/**
	 * Used to account for games with no database entry when calculating library
	 * completion %
	 */
	private int dbCount;

	/**
	 * Label in the statistic comparison panel for the amount of games you own
	 */
	@FXML
	private Label profileOwnedGames;

	/**
	 * Label in the statistic comparison panel for the amount of playtime you have
	 */
	@FXML
	private Label profilePlaytime;

	/**
	 * Label in the statistic comparison panel for the amount of playtime you have
	 * in the last 2 weeks
	 */
	@FXML
	private Label profilePlaytimeWeeks;

	/**
	 * Label in the statistic comparison panel for the amount of games you have
	 * completed
	 */
	@FXML
	private Label profileCompleted;

	@FXML
	private VBox profileVbox;

	@FXML
	private Button closeProfile;

	private MainApp mainApp;

	@FXML
	private Button loadStatsButton;
	
	@FXML
	private ImageView profilePageImage;
	
	@FXML
	private Label profilePageName;
	
	/**
	 * Label for Statistics text
	 */
	@FXML
	private Label statText;

	public void closeProfile() {
		profileVbox.setManaged(false);
		profileVbox.setVisible(false);
	}

	public void openProfile() {
		profileVbox.setManaged(true);
		profileVbox.setVisible(true);
		loadTexts();
		loadStats();
	}

	/**
	 * Loads the user's stats and the stats of the friend of which ID and name was
	 * given
	 * 
	 * @param friendsID
	 * @param name
	 */
	public void loadStats() {
		resetSums();

		ObservableList<GameData> oGames = SteamAPICalls.getOwnedGames();
		List<GameListEntry> odbGames = db.getAllUserGames(UserPreferences.getSteamID());

		oGames.forEach((n) -> sumUpStats(n, UserPreferences.getSteamID()));
		odbGames.forEach((n) -> sumUpDatabaseStats(n, UserPreferences.getSteamID()));

		// Accounting for games with no database entry
		oBeatable += oCount - dbCount;
		// Avoid division by 0
		if (oBeatable == 0)
			oBeatable = 1;

		profileOwnedGames.setText(String.format(LanguageProvider.getString("compYouOwn"), oCount));
		profilePlaytime.setText(String.format(LanguageProvider.getString("compOverall"), oPlaytime / 60));
		profilePlaytimeWeeks.setText(String.format(LanguageProvider.getString("comp2Weeks"), o2wPlaytime / 60));
		profileCompleted.setText(
				String.format(LanguageProvider.getString("compCompletion"), ((double) oBeaten / oBeatable) * 100));
		
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

		}
	}

	/**
	 * Resets the sum variables
	 */
	private void resetSums() {
		oPlaytime = 0;
		oCount = 0;
		o2wPlaytime = 0;
		oBeaten = 0;
		oBeatable = 0;
		dbCount = 0;
	}

	/**
	 * Initializes the panel by loading in elements and makes sure the panel is
	 * closed by default
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		api = new SteamAPICalls();
		profilePageImage.setManaged(false);
		profilePageImage.setVisible(false);
		
		profilePageName.setManaged(false);
		profilePageName.setVisible(false);
		
		// Give functionality to the close button
		closeProfile.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				closeProfile();
			}
		});
		
		ObjectProperty<PlayerProfile> userProfilePage = SteamAPICalls.getSignedPlayerProfile();
		userProfilePage.addListener(obs -> {
			try {
				loadStats();
				Image avatar = new Image(userProfilePage.get().getAvatarfull());
				profilePageImage.setImage(avatar);
				profilePageName.setText(userProfilePage.get().getPersonaname());
				profilePageImage.setVisible(true);
				profilePageImage.setManaged(true);
				profilePageName.setManaged(true);
				profilePageName.setVisible(true);
			}catch(Exception e) {
				
			}
		});
		closeProfile();

	}
	
	private void loadTexts() {
		closeProfile.setText(LanguageProvider.getString("close"));
		statText.setText(LanguageProvider.getString("statistics"));
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
}
