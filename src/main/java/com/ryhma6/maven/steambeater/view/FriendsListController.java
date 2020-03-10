package com.ryhma6.maven.steambeater.view;

import java.net.URL;
import java.util.ResourceBundle;

import com.ryhma6.maven.steambeater.MainApp;
import com.ryhma6.maven.steambeater.model.SteamAPICalls;
import com.ryhma6.maven.steambeater.model.steamAPI.Friend;

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
	private ListView<Friend> friendsList;
	
	@FXML
	private ListView<Friend> friendsListSmall;

	private MainApp mainApp;
	private StatComparisonController scCont;
	
	private final Image IMAGE_TEST = new Image("test.png");
	
	double smallWidth = 75.0;
	double normalWidth = 250.0;
	BorderPane borderPane = new BorderPane();
	Button resizeButton = new Button("<-|");
	
	private void loadFriends() {
		//ObservableList<String> names = FXCollections.observableArrayList("Friendo", "A what now", "twat", "NAme");
		ObservableList<Friend> names = SteamAPICalls.getFriendList();
		friendsList.setItems(names);
		friendsListSmall.setItems(names);

		friendsList.setCellFactory(param -> new ListCell<Friend>() {

			private HBox hbox = new HBox();
			private ImageView imageView = new ImageView();
			private Button button = new Button("Compare");
			private Label label = new Label();
			private Pane pane = new Pane();
			
			@Override
			public void updateItem(Friend name, boolean empty) {
				
				super.updateItem(name, empty);
				
				if (empty || name == null) {
					setText(null);
					setGraphic(null);
				} else {
					
					Image profileImage;
					try {
						profileImage = new Image(name.getPlayerProfile().getAvatarmedium(), true); //true: load in background
					}catch (Exception e) {
						profileImage = IMAGE_TEST;
						//e.printStackTrace();
					}
					imageView.setImage(profileImage); 
					
					imageView.setPreserveRatio(true);
		            HBox.setHgrow(pane, Priority.ALWAYS);
					
		            try {
					label.setText(name.getPlayerProfile().getPersonaname());
		            }catch(Exception e) {
		            	System.out.println("ERR - PlayerProfile: " + name.getPlayerProfile());
		            	System.out.println("ERR - personaname: " + name.getPlayerProfile().getPersonaname());
		            }
					
					label.setPrefWidth(82);
					label.setWrapText(true);
					imageView.setFitHeight(50);
					
					button.setOnAction( new EventHandler<ActionEvent>() {
			            @Override
			            public void handle(ActionEvent event) {
			                toggleComparisonView(name.getSteamid(), name.getPlayerProfile().getPersonaname());
			            }
			        });

					hbox.getChildren().clear();
					hbox.getChildren().addAll(imageView, label, pane, button);
					setGraphic(hbox);
				}
			}
		});
		
		friendsListSmall.setCellFactory(param -> new ListCell<Friend>() {

			private HBox hbox = new HBox();
			private ImageView imageView = new ImageView();
			
			@Override
			public void updateItem(Friend name, boolean empty) {
				
				super.updateItem(name, empty);
				
				if (empty || name == null) {
					setText(null);
					setGraphic(null);
				} else {
					
					Image profileImage;
					try {
						profileImage = new Image(name.getPlayerProfile().getAvatarmedium(), true); //true: load in background
					}catch (Exception e) {
						profileImage = IMAGE_TEST;
						//e.printStackTrace();
					}
					imageView.setImage(profileImage); 
					
					imageView.setPreserveRatio(true);
					imageView.setFitHeight(50);

					hbox.getChildren().clear();
					hbox.getChildren().addAll(imageView);
					setGraphic(hbox);
				}
			}
		});
	}
	
	public void toggleComparisonView(String id, String name) {
		scCont.loadStats(id, name);
		scCont.openComparison();
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
	
	public void setStatComparisonController(StatComparisonController scCont) {
		this.scCont = scCont;
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
