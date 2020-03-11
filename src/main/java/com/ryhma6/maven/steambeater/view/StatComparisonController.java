package com.ryhma6.maven.steambeater.view;

import java.net.URL;
import java.util.ResourceBundle;

import com.ryhma6.maven.steambeater.model.SteamAPICalls;
import com.ryhma6.maven.steambeater.model.steamAPI.OwnedGames;

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
	 * Used to count the users games
	 */
	private int count;
	
	/**
	 * Used to show the friend's stats
	 */
	private Label friendLabel;
	
	/**
	 * Used to show the user's stats
	 */
	private Label userLabel;
	
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
	 * Loads the user's stats and the stats of the friend of which ID and name was given
	 * @param friendsID
	 * @param name
	 */
	public void loadStats(String friendsID, String name) {
		OwnedGames fGames = api.loadFriendsGames(friendsID);
		System.out.println("friend's games count: " + fGames.getGame_count());
		friendLabel.setText(name + " owns " + fGames.getGame_count() + " games");
		count = 0;
		SteamAPICalls.getOwnedGames().forEach((n) -> count++);
		userLabel.setText("You own " + count + " games");
	}

	/**
	 * Initializes the panel by loading in elements and makes sure the panel is closed by default
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		api = new SteamAPICalls();
		friendLabel = new Label();
		friendStats.getChildren().add(friendLabel);
		userLabel = new Label();
		userStats.getChildren().add(userLabel);
		
		//Give functionality to the close button
		closeBtn.setOnAction( new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                closeComparison();
            }
        });
		
		closeComparison();
	}
}
