package com.ryhma6.maven.steambeater.view;

import java.net.URL;
import java.util.ResourceBundle;

import com.ryhma6.maven.steambeater.MainApp;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class StatComparisonController implements Initializable {
	private MainApp mainApp;
	
	@FXML
	private AnchorPane statAnchor; 
	
	@FXML
	private Button closeBtn;
	
	@FXML
	private BorderPane topBar;
	
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
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
