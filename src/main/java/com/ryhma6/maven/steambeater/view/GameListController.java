package com.ryhma6.maven.steambeater.view;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import com.ryhma6.maven.steambeater.MainApp;
import com.ryhma6.maven.steambeater.model.SteamAPICalls;
import com.ryhma6.maven.steambeater.model.steamAPI.GameData;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
	
	@FXML
	private CheckBox includeUnbeatable,includeIgnored,includeBeaten,includeUnbeaten;
	
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
			public Button setUnbeatable = new Button();
			public Button setAsBeaten = new Button();
			public ImageView imageView = new ImageView();
			public GameData cellGame;
			public Pane pane = new Pane();
			
			public CustomCell() {
				super();
				EventHandler<MouseEvent> eventIgnored = new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						cellGame.setIgnored(!cellGame.isIgnored());
						filterGameData();
						System.out.println(cellGame.getName() + " ignored: " + cellGame.isIgnored());
					}
				};
				EventHandler<MouseEvent> eventBeaten = new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						cellGame.setBeaten(!cellGame.isBeaten());
						filterGameData();
						System.out.println(cellGame.getName() + " beaten: " + cellGame.isBeaten());
						setAsBeaten.setStyle("-fx-border-color: #34eb40; -fx-border-width: 2 2 2 2");
					}
				};
				EventHandler<MouseEvent> eventUnbeatable = new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						cellGame.setUnbeatable(!cellGame.isBeaten());
						filterGameData();
						System.out.println(cellGame.getName() + " beatable: " + cellGame.isBeaten());
						setUnbeatable.setStyle("-fx-border-color: red; -fx-border-width: 2 2 2 2");
					}
				};
				ignoreButton.addEventFilter(MouseEvent.MOUSE_CLICKED, eventIgnored);
				setAsBeaten.addEventFilter(MouseEvent.MOUSE_CLICKED, eventBeaten);
				setUnbeatable.addEventFilter(MouseEvent.MOUSE_CLICKED, eventUnbeatable);
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
					ImageView ignoreImage = new ImageView("/hide.png");
					ignoreImage.setFitHeight(40);
				    ignoreImage.setFitWidth(40);
				    ignoreButton.setGraphic(ignoreImage);
				    if(game.isIgnored()) {
					ignoreButton.setStyle("-fx-border-color: red; -fx-border-width: 2 2 2 2");
				    }
				    Tooltip ignoreTip = new Tooltip();
				    Tooltip beatenTip = new Tooltip();
				    Tooltip unbeatableTip = new Tooltip();
				    ignoreTip.setText("Ignore game");
				    beatenTip.setText("Set game as beaten");
				    unbeatableTip.setText("Set game as unbeatable");
				    ignoreButton.setTooltip(ignoreTip);
				    setAsBeaten.setTooltip(beatenTip);
				    setUnbeatable.setTooltip(unbeatableTip);
				    ImageView beatenImage = new ImageView("/trophy.png");
					beatenImage.setFitHeight(40);
				    beatenImage.setFitWidth(40);
					setAsBeaten.setGraphic(beatenImage);
					ImageView unbeatableImage = new ImageView("/unbeatable.png");
					unbeatableImage.setFitHeight(40);
				    unbeatableImage.setFitWidth(40);
				    setUnbeatable.setGraphic(unbeatableImage);
					timePlayed.setText("Time played: " + game.getPlaytime_forever() + " hours");
					gameName.setText(game.getName());
					hbox.setSpacing(35);
					hbox.setAlignment(Pos.CENTER_LEFT);
					try {
						imageView.setImage(new Image(game.getImg_logo_url(), true)); // true: load in background
					} catch (Exception e) {
						System.out.println("Loading game img failed (null or invalid url)");
						imageView.setImage(IMAGE_TEST);
					}
					hbox.getChildren().clear();
					hbox.getChildren().addAll(imageView, gameName, timePlayed, pane, setAsBeaten,setUnbeatable, ignoreButton);
					HBox.setHgrow(pane, Priority.ALWAYS);
					setGraphic(hbox);
				}
			}
		});
		
		initFilterListeners();
		initListenerSortGameList();
		hideStats();
		setDefaultOptions();
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
	}
	
	/**
	 * Filtering gamelist with searchfield
	 */
	private Predicate<GameData> searchFilter(){
		Predicate<GameData> filter = game -> true;
		if(searchField.getText().length()>0)
			filter = game -> game.getName().toLowerCase().contains(searchField.getText().toLowerCase());
		return filter;	
	}
	private Predicate<GameData> includeUnbeatableFilter(){
		Predicate<GameData> filter = game -> true;
		if(!includeUnbeatable.isSelected())
			filter = game -> game.isUnbeatable() == false;
		return filter;	
	}
	private Predicate<GameData> includeIgnoredFilter(){
		Predicate<GameData> filter = game -> true;
		if(!includeIgnored.isSelected())
			filter = game -> game.isIgnored() == false;
		return filter;	
	}
	private Predicate<GameData> includeBeatenFilter(){
		Predicate<GameData> filter = game -> true;
		if(!includeBeaten.isSelected())
			filter = game -> game.isBeaten() == false;
		return filter;	
	}
	private Predicate<GameData> includeUnbeatenFilter(){
		Predicate<GameData> filter = game -> true;
		if(!includeUnbeaten.isSelected())
			filter = game -> game.isBeaten() == true;
		return filter;	
	}
	
	private void filterGameData() {
        filteredData.setPredicate(searchFilter().and(includeUnbeatableFilter().and(includeIgnoredFilter().and(includeBeatenFilter().and(includeUnbeatenFilter())))));
	}

	private void initFilterListeners() {
		filteredData = new FilteredList<>(games, p -> true);
		
        searchField.textProperty().addListener(obs->{
            filterGameData();
        });
        
        includeUnbeatable.selectedProperty().addListener(obs->{
            filterGameData();
        });
        
        includeIgnored.selectedProperty().addListener(obs->{
            filterGameData();
        });
        
        includeBeaten.selectedProperty().addListener(obs->{
            filterGameData();
        });
        
        includeUnbeaten.selectedProperty().addListener(obs->{
            filterGameData();
        });
	}
	private void setDefaultOptions() {
		includeUnbeaten.setSelected(true);
		sortingChoice.getSelectionModel().select(0);
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loadGames();
	}
}
