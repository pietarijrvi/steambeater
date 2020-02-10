package com.ryhma6.maven.steambeater.view;

import java.net.URL;
import java.util.ResourceBundle;

import com.ryhma6.maven.steambeater.MainApp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class GameListController implements Initializable {

	@FXML
	private ListView<String> gameList;

	@FXML
	Button hideStatsButton;

	@FXML
	AnchorPane statsWindow;
	
	@FXML
	Label statLabel;

	private MainApp mainApp;
	private final Image IMAGE_TEST = new Image("test.png");

	private Image[] listOfImages = { IMAGE_TEST };

	private void loadGames() {
		ObservableList<String> names = FXCollections.observableArrayList("Pasianssi", "Minesweeper", "Sudoku");
		gameList.setItems(names);

		gameList.setCellFactory(param -> new ListCell<String>() {
			private ImageView imageView = new ImageView();

			@Override
			public void updateItem(String name, boolean empty) {
				super.updateItem(name, empty);
				if (empty || name == null) {
					setText(null);
					setGraphic(null);
				} else {
					if (name.equals("Sudoku")) {
						imageView.setImage(listOfImages[0]);
					} else if (name.equals("Pasianssi"))
						imageView.setImage(listOfImages[0]);

					setText(name);
					setGraphic(imageView);
				}
			}
		});
	}
	
	private void hideStats() {
		statsWindow.setManaged(false);
		statsWindow.setVisible(false);
	}
	
	private void showStats() {
		statsWindow.setManaged(true);
		statsWindow.setVisible(true);
	}

	public void setStatsVisibility() {
		System.out.println("Button clicked!");
		boolean visible = statsWindow.isManaged();
		if (visible == true) {
			hideStats();
		} else {
			showStats();
		}
	}

	@FXML
	private void handleMouseClick(MouseEvent arg0) {
		VBox statsBox = (VBox) statsWindow.lookup("#statsBox");
		String text = gameList.getSelectionModel().getSelectedItem();
		statLabel.setText(text);
		showStats();
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loadGames();
		hideStats();
	}
}