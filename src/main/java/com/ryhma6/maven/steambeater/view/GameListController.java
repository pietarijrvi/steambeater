package com.ryhma6.maven.steambeater.view;

import java.net.URL;
import java.util.ResourceBundle;

import com.ryhma6.maven.steambeater.MainApp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameListController implements Initializable {

	@FXML
	private ListView<String> gameList;

	private MainApp mainApp;
	private final Image IMAGE_TEST = new Image("test.png");

	private Image[] listOfImages = {IMAGE_TEST};

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

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loadGames();
	}
}