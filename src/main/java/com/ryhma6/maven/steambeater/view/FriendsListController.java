package com.ryhma6.maven.steambeater.view;

import java.net.URL;
import java.util.ResourceBundle;

import com.ryhma6.maven.steambeater.MainApp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class FriendsListController implements Initializable {
	
	@FXML
	private AnchorPane deepAnchor; 
	
	@FXML
	private ListView<String> friendsList;
	
	@FXML
	private ListView<String> friendsListSmall;

	private MainApp mainApp;
	private final Image IMAGE_TEST = new Image("test.png");

	private Image[] listOfImages = {IMAGE_TEST};
	
	double smallWidth = 75.0;
	double normalWidth = 250.0;
	BorderPane borderPane = new BorderPane();
	Button resizeButton = new Button("<-|");
	
	private void loadFriends() {
		ObservableList<String> names = FXCollections.observableArrayList("Friendo", "A what now", "twat", "NAme", "Friendo1", "A what now1", "PUTIN", "Xxx_SuperSlayer69_xxX");
		friendsList.setItems(names);
		friendsListSmall.setItems(names);

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
					
					imageView.setImage(listOfImages[0]);
					
					imageView.setPreserveRatio(true);
		            HBox.setHgrow(pane, Priority.ALWAYS);
					
					label.setText(name);
					label.setMaxWidth(99);
					imageView.setFitHeight(50);
					
					button.setOnAction( new EventHandler<ActionEvent>() {
			            @Override
			            public void handle(ActionEvent event) {
			                toggleComparisonView();
			            }
			        });

					hbox.getChildren().addAll(imageView, label, pane, button);
					setGraphic(hbox);
				}
			}
		});
		
		friendsListSmall.setCellFactory(param -> new ListCell<String>() {

			private HBox hbox = new HBox();
			private ImageView imageView = new ImageView();
			
			@Override
			public void updateItem(String name, boolean empty) {
				
				super.updateItem(name, empty);
				
				if (empty || name == null) {
					setText(null);
					setGraphic(null);
				} else {
					
					imageView.setImage(listOfImages[0]);
					imageView.setPreserveRatio(true);
					imageView.setFitHeight(50);

					hbox.getChildren().addAll(imageView);
					setGraphic(hbox);
				}
			}
		});
	}
	
	public void toggleComparisonView() {
		System.out.println("toggleComparisonView");
		//CODE HERE
	}
	
	public void toggleSize() {
		boolean visible = friendsList.isManaged();
		if (visible == true) {
			friendsList.setManaged(false);
			friendsList.setVisible(false);
			friendsListSmall.setManaged(true);
			friendsListSmall.setVisible(true);
			borderPane.setPrefWidth(smallWidth);
			deepAnchor.setPrefWidth(smallWidth);
			
			resizeButton.setText("|->");
			
		} else {
			friendsListSmall.setManaged(false);
			friendsListSmall.setVisible(false);
			friendsList.setManaged(true);
			friendsList.setVisible(true);
			borderPane.setPrefWidth(normalWidth);
			deepAnchor.setPrefWidth(normalWidth);
			
			resizeButton.setText("<-|");
		}
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		borderPane.setRight(resizeButton);
		borderPane.setPrefWidth(normalWidth);
		borderPane.setPadding(new Insets(5, 5, 5, 5));
		
		resizeButton.setOnAction( new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggleSize();
            }
        });
		
		deepAnchor.getChildren().add(borderPane);
		
		loadFriends();
	}
}
