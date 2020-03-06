package com.ryhma6.maven.steambeater.view;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import com.ryhma6.maven.steambeater.MainApp;
import com.ryhma6.maven.steambeater.model.SteamAPICalls;
import com.ryhma6.maven.steambeater.model.steamAPI.GameData;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class GameListController implements Initializable {

	@FXML
	private ListView<GameData> gameList;

	@FXML
	private Button hideStatsButton;

	@FXML
	private AnchorPane statsWindow;
	
	@FXML
	private Label statLabel;
	
	@FXML
	private ComboBox sortingChoice;
	
	@FXML
	private TextField searchField;
	
	private MainApp mainApp;
	private final Image IMAGE_TEST = new Image("/test.png");

	private ObservableList<GameData> games = SteamAPICalls.getOwnedGames();
	
	private FilteredList<GameData> filteredData;
	private SortedList<GameData>sortedFilteredData;
	
	public ObservableList<GameData> getGames() {
		return games;
	}

	public void setGames(ObservableList<GameData> games) {
		this.games = games;
	}

	public void loadGames() {	
		abstract class CustomCell extends ListCell<GameData>{
			public Label gameName = new Label();
			public Label timePlayed = new Label();
			public HBox hbox = new HBox();
			public Button ignoreButton = new Button();
			public Button setAsBeaten = new Button();
			public ImageView imageView = new ImageView();
			public GameData cellGame;
			
			public CustomCell() {
				super();
				EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						cellGame.setIgnored(true);
						System.out.println("ignored: " +  cellGame.getName());
					}
				};
				ignoreButton.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
			}
		}
		
		gameList.setCellFactory(param -> new CustomCell() {

			@Override
			public void updateItem(GameData game, boolean empty) {
				super.updateItem(game, empty);
				if (empty) {
					setText(null);
					setGraphic(null);
				} else {
					cellGame = game;
					ignoreButton.setText("Ignore this game");
					setAsBeaten.setText("Set game as beaten");
					timePlayed.setText("Time played: " + game.getPlaytime_forever() + " hours");
					gameName.setText(game.getName());
					hbox.setSpacing(50);
					hbox.setAlignment(Pos.CENTER_LEFT);
					try {
						imageView.setImage(new Image(game.getImg_logo_url(), true)); // true: load in background
					} catch (Exception e) {
						System.out.println("Loading game img failed (null or invalid url)");
						imageView.setImage(IMAGE_TEST);
					}
					hbox.getChildren().clear();
					hbox.getChildren().addAll(imageView, gameName, timePlayed, ignoreButton);
					setGraphic(hbox);
				}
			}
		});
		filterByName();
		initListenerSortGameList();
		hideStats();
	}

	private void hideStats() {
		statsWindow.setManaged(false);
		statsWindow.setVisible(false);
	}

	private void showStats() {
		gameList.maxWidth(250);
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
		 GameData game = gameList.getSelectionModel().getSelectedItem();
		 statLabel.setText(game.getName() + " ignored: " + game.isIgnored()); 
		 showStats();
	}


	/**
	 * Dropdown options to sort gamelist
	 */
	private void initListenerSortGameList(){
		sortingChoice.getSelectionModel().clearSelection();
		sortingChoice.getSelectionModel().selectedItemProperty().addListener(obs->{
			System.out.println("sorting games");
			//sorting in alphabetical order
			if(sortingChoice.getSelectionModel().getSelectedIndex() == 0) {
				sortedFilteredData=filteredData.sorted(Comparator.comparing(GameData::getName));
			}else if(sortingChoice.getSelectionModel().getSelectedIndex() == 1){
				sortedFilteredData=filteredData.sorted(Comparator.comparing(GameData::getPlaytime_forever).reversed());
			}
			gameList.setItems(sortedFilteredData);
		});
		sortingChoice.getSelectionModel().select(0);
	}
	

	/**
	 * Filtering gamelist with searchfield
	 */
	private void filterByName() {
		filteredData = new FilteredList<>(games, p -> true);
        
        searchField.textProperty().addListener(obs->{
            String filter = searchField.getText(); 
            if(filter == null || filter.length() == 0) {
                filteredData.setPredicate(s -> true);
            }
            else {
                filteredData.setPredicate(s -> s.getName().toLowerCase().contains(filter.toLowerCase()));
            }
            //filteredData.setPredicate(s -> s.isIgnored());
        });
        
        //Wrap the FilteredList in a SortedList. 
        //SortedList<GameData> sortedData = new SortedList<>(filteredData);
        
        //Add sorted (and filtered) data to the table.
        //gameList.setItems(sortedData);
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loadGames();
	}
}
