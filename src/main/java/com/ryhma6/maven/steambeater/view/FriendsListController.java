package com.ryhma6.maven.steambeater.view;

import java.net.URL;
import java.util.ResourceBundle;

import com.ryhma6.maven.steambeater.model.SteamAPICalls;
import com.ryhma6.maven.steambeater.model.steamAPI.Friend;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

/**
 * Controls the friends list side bar, works with StatComparisonController which
 * needs to be set using the setStatComparisonController
 * 
 * @author KimW
 *
 */
public class FriendsListController implements Initializable {

	/**
	 * The deepest AnchorPane in the fxml
	 */
	@FXML
	private AnchorPane deepAnchor;

	@FXML
	private AnchorPane friendsAnchor;

	@FXML
	private Label friendsLabel;

	/**
	 * The expanded version of the friends list
	 */
	@FXML
	private ListView<Friend> friendsList;

	/**
	 * The minimized version of the friends list
	 */
	@FXML
	private ListView<Friend> friendsListSmall;

	private StatComparisonController scCont;

	private final Image IMAGE_TEST = new Image("test.png");

	double smallWidth = 85.0;
	double normalWidth = 250.0;
	BorderPane borderPane = new BorderPane();
	Button resizeButton = new Button();

	/**
	 * loads the friends into the friends list, minimized one is separate from the
	 * expanded one
	 */
	private void loadFriends() {
		ObservableList<Friend> names = SteamAPICalls.getFriendList();
		friendsList.setItems(names);
		friendsListSmall.setItems(names);

		friendsList.setCellFactory(param -> new ListCell<Friend>() {

			private HBox hbox = new HBox();
			private ImageView imageView = new ImageView();
			private Button button = new Button("Compare");
			private Label label = new Label();
			private Pane pane = new Pane();
			private int smallWidth = 85;

			@Override
			public void updateItem(Friend name, boolean empty) {

				super.updateItem(name, empty);

				if (empty || name == null) {
					setText(null);
					setGraphic(null);
				} else {

					Image profileImage;
					try {
						profileImage = new Image(name.getPlayerProfile().getAvatarmedium(), true); // true: load in
																									// background
					} catch (Exception e) {
						profileImage = IMAGE_TEST;
						e.printStackTrace();
					}
					imageView.setImage(profileImage);

					imageView.setPreserveRatio(true);
					HBox.setHgrow(pane, Priority.ALWAYS);

					try {
						label.setText(name.getPlayerProfile().getPersonaname());
					} catch (Exception e) {
						e.printStackTrace();
					}

					label.setPrefWidth(82);
					label.setWrapText(true);
					imageView.setFitHeight(50);

					button.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							toggleComparisonView(name.getSteamid(), name.getPlayerProfile().getPersonaname());
						}
					});

					hbox.getChildren().clear();
					hbox.getChildren().addAll(imageView, label, pane, button);
					setGraphic(hbox);
					label.setStyle("-fx-padding: 0 0 0 5");
					hbox.setAlignment(Pos.CENTER_LEFT);

					// Removing label, pane and compare button when friendslist is smaller
					if (friendsList.getPrefWidth() == smallWidth) {
						hbox.getChildren().removeAll(label, pane, button);
					}

				}
			}
		});

//		friendsListSmall.setCellFactory(param -> new ListCell<Friend>() {
//
//			private HBox hbox = new HBox();
//			private ImageView imageView = new ImageView();
//
//			@Override
//			public void updateItem(Friend name, boolean empty) {
//
//				super.updateItem(name, empty);
//
//				if (empty || name == null) {
//					setText(null);
//					setGraphic(null);
//				} else {
//
//					Image profileImage;
//					try {
//						profileImage = new Image(name.getPlayerProfile().getAvatarmedium(), true); // true: load in
//																									// background
//					} catch (Exception e) {
//						profileImage = IMAGE_TEST;
//						e.printStackTrace();
//					}
//					imageView.setImage(profileImage);
//
//					imageView.setPreserveRatio(true);
//					imageView.setFitHeight(50);
//
//					hbox.getChildren().clear();
//					hbox.getChildren().addAll(imageView);
//					setGraphic(hbox);
//				}
//			}
//		});
	}

	/**
	 * Opens the Comparison view and updates it with the stats of the given ID
	 * 
	 * @param id
	 * @param name
	 */
	public void toggleComparisonView(String id, String name) {
		scCont.loadStats(id, name);
		scCont.openComparison();
	}

	/**
	 * toggles the friends list side bar between the minimized and expanded versions
	 */
	public void toggleSize() {
		int largeWidth = 250;
		if (friendsList.getPrefWidth() == largeWidth) {
			loadFriends();
//			friendsList.setManaged(false);
//			friendsList.setVisible(false);
//			friendsListSmall.setManaged(true);
//			friendsListSmall.setVisible(true);
			friendsList.setPrefWidth(85);
			borderPane.setPrefWidth(smallWidth);
			friendsAnchor.setPrefWidth(95);
			deepAnchor.setPrefWidth(85);
			friendsLabel.setManaged(false);
			friendsLabel.setVisible(false);

			ImageView back = new ImageView("/forward_64px.png");
			back.setFitHeight(25);
			back.setFitWidth(20);
			resizeButton.setGraphic(back);
		} else {
			loadFriends();
//			friendsListSmall.setManaged(false);
//			friendsListSmall.setVisible(false);
//			friendsList.setManaged(true);
//			friendsList.setVisible(true);
			borderPane.setPrefWidth(normalWidth);
			friendsList.setPrefWidth(250);
			friendsAnchor.setPrefWidth(265);
			deepAnchor.setPrefWidth(250);
			friendsLabel.setManaged(true);
			friendsLabel.setVisible(true);

			ImageView back = new ImageView("/back_64px.png");
			back.setFitHeight(25);
			back.setFitWidth(20);
			resizeButton.setGraphic(back);
		}
	}

	/**
	 * Sets the StatComparisonController so that the friends list's compare buttons
	 * work
	 * 
	 * @param scCont
	 */
	public void setStatComparisonController(StatComparisonController scCont) {
		this.scCont = scCont;
	}

	/**
	 * Initializes the side bar by creating the toggle button and giving it its
	 * function, then calls the method to load the users friends in
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		borderPane.setRight(resizeButton);
		borderPane.setPrefWidth(normalWidth);
		borderPane.setPadding(new Insets(5, 5, 5, 5));
		ImageView back = new ImageView("/back_64px.png");
		resizeButton.setGraphic(back);
		back.setFitHeight(25);
		back.setFitWidth(20);
		resizeButton.setPadding(new Insets(5));

		// button for toggling the friends list between the minimized and expanded
		// versions
		resizeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				toggleSize();
			}
		});

		deepAnchor.getChildren().add(borderPane);

		loadFriends();
	}
}
