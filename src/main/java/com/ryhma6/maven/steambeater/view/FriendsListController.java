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
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class FriendsListController implements Initializable {
	
	@FXML
	private ListView<String> friendsList;

	private MainApp mainApp;
	private final Image IMAGE_TEST = new Image("test.png");

	private Image[] listOfImages = {IMAGE_TEST};
	
	private void loadFriends() {
		ObservableList<String> names = FXCollections.observableArrayList("Friendo", "A what now", "twat", "NAme");
		friendsList.setItems(names);

		friendsList.setCellFactory(param -> new ListCell<String>() {
			
			private HBox hbox = new HBox();
			private ImageView imageView = new ImageView();
			private Button button = new Button("Compare");
			private Label label = new Label();
			private Pane pane = new Pane();
			
			@Override
			public void updateItem(String name, boolean empty) {
				
				super.updateItem(name, empty);
				
				if (empty || name == null) {
					setText(null);
					setGraphic(null);
				} else {
					
					if (name.equals("twat")) {
						imageView.setImage(listOfImages[0]);
					}
					
					imageView.setPreserveRatio(true);
		            HBox.setHgrow(pane, Priority.ALWAYS);
					
					label.setText(name);
					imageView.setFitHeight(50);

					hbox.getChildren().addAll(imageView, label, pane, button);
					setGraphic(hbox);
				}
			}
		});
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loadFriends();
	}
}
