package com.ryhma6.maven.steambeater.view;

import java.net.URL;
import java.util.ResourceBundle;

import com.ryhma6.maven.steambeater.MainApp;
import com.ryhma6.maven.steambeater.model.SteamAPICalls;
import com.ryhma6.maven.steambeater.model.steamAPI.GameData;
import com.ryhma6.maven.steambeater.model.steamAPI.OwnedGames;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class StatComparisonController implements Initializable {
	private MainApp mainApp;
	private SteamAPICalls api;
	
	@FXML
	private AnchorPane statAnchor; 
	
	@FXML
	private Button closeBtn;
	
	@FXML
	private BorderPane topBar;

	@FXML
	private AnchorPane friendStats;
	
	@FXML
	private AnchorPane userStats;
	
	int count;
	Label friendLabel;
	Label userLabel;
	
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
	
	public void loadStats(String friendsID, String name) {
		OwnedGames fGames = api.loadFriendsGames(friendsID);
		System.out.println("friend's games count: " + fGames.getGame_count());
		friendLabel.setText(name + " owns " + fGames.getGame_count() + " games");
		count = 0;
		SteamAPICalls.getOwnedGames().forEach((n) -> count++);
		userLabel.setText("You own " + count + " games");
	}

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
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
}
