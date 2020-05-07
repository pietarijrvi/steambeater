package com.ryhma6.maven.steambeater.view;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import com.ryhma6.maven.steambeater.MainApp;
import com.ryhma6.maven.steambeater.model.LanguageProvider;
import com.ryhma6.maven.steambeater.model.SteamAPICalls;
import com.ryhma6.maven.steambeater.model.UserPreferences;
import com.ryhma6.maven.steambeater.model.steamAPI.Achievement;
import com.ryhma6.maven.steambeater.model.steamAPI.GameData;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.collections.FXCollections;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Controller for the gamelist fxml Loads achievements and games and takes care
 * of sorting and filtering
 */
public class GameListController implements Initializable {

	/**
	 * ListView for gamelist that has all your games
	 * 
	 * @param gameList listview for the the list that holds your Steam games
	 * @return gameList listview for the the list that holds your Steam games
	 */
	@FXML
	private ListView<GameData> gameList;

	/**
	 * List that has all your achievements
	 * 
	 * @param achievementList list of your achievements
	 * @return achievementList of your achievements
	 */
	@FXML
	private ListView<Achievement> achievementList;
	
	@FXML
	private Label achievementStatusLabel;

	/**
	 * Hides statsWindow with achievements
	 * 
	 * @param hideStatsButton button that hides statsWindow element with your
	 *                        achievements
	 */
	@FXML
	private Button hideStatsButton;

	/**
	 * Holds your achievements
	 * 
	 * @param statsWindow panel that opens when you click a game on a list, shows
	 *                    your achievements
	 */
	@FXML
	private AnchorPane statsWindow;

	/**
	 * Label in statsWindow that holds the game´s name
	 * 
	 * @param statLabel label that holds the clicked game´s name
	 */
	@FXML
	private Label statLabel;

	/**
	 * ComboBox for sorting the gameList by name or playtime
	 * 
	 * @param sortingChoice ComboBox for sorting the gameList by name or playtime
	 */
	@FXML
	private ComboBox<?> sortingChoice;
	
	@FXML
	private String sortName, sortPlaytime;

	/**
	 * TextField for filtering the gameList by name
	 * 
	 * @param searchField TextField for filtering the gameList by name
	 */
	@FXML
	private TextField searchField;

	/**
	 * Pane in achievementList
	 * 
	 * @param achievementPane Pane in achievementList
	 */
	@FXML
	private Pane achievementPane;

	/**
	 * CheckBoxes for gameList filtering options
	 * 
	 * @param includeUnbeatable CheckBox that includes unbeatable games in list
	 * @param includeIgnored    CheckBox that includes ignored games in list
	 * @param includeBeaten     CheckBox that includes beaten games in list
	 * @param includeUnbeaten   CheckBox that includes unbeaten games in list
	 */
	@FXML
	private CheckBox includeUnbeatable, includeIgnored, includeBeaten, includeUnbeaten;

	/**
	 * Displays game's updated status when the user clicks a status button
	 */
	@FXML
	private Label actionLabel;
	
	/**
	 * Labels for displaying the amount of games marked
	 */
	@FXML
	private Label markedBeaten, markedNothing, markedUnbeatable, markedIgnored;

	/**
	 * MainApp that runs the application
	 * 
	 * @param mainApp MainApp that runs the application
	 */
	private MainApp mainApp;

	/**
	 * Default image for testing gameList
	 * 
	 * @param IMAGE_TEST Image for testing gameList
	 */
	private final Image IMAGE_TEST = new Image("/img/test.png");

	/**
	 * ObservableList for user´s games got from the Steam API
	 * 
	 * @param games list for users games
	 */
	private ObservableList<GameData> games = SteamAPICalls.getOwnedGames();

	/**
	 * FilteredList for the filtered gamelist
	 * 
	 * @param filteredData FilteredList for the filtered gamelist
	 */
	private FilteredList<GameData> filteredData;

	/**
	 * SortedList for the sorted and filtered gamelist
	 * 
	 * @param sortedFilteredData SortedList for the sorted and filtered gamelist
	 */
	private SortedList<GameData> sortedFilteredData;

	/**
	 * Gets the user´s games
	 * 
	 * @return ObservableList games with user´s games
	 */
	public ObservableList<GameData> getGames() {
		return games;
	}

	/**
	 * Sets the user´s games
	 * 
	 * @param games ObservableList with user´s games
	 */
	public void setGames(ObservableList<GameData> games) {
		this.games = games;
	}
	
	@FXML
	private Button orderButton;
	
	private boolean clicked = false;


	/**
	 * Initializes gamelist with games, achievements, filtering and sorting Hides
	 * statsWindow by default Sets default options
	 */
	public void loadGames() {
		initGameListCellFactory();
		initAchievementCellFactory();
		initFilterListeners();
		initListenerSortGameList();
		hideStats();
		setSavedOrDefaultOptions();
		countMarks();
		loadTexts();
	}
	
	public void loadTexts() {
		includeUnbeatable.setText(LanguageProvider.getString("includeUnbeatable"));
		includeBeaten.setText(LanguageProvider.getString("includeBeaten"));
		includeUnbeaten.setText(LanguageProvider.getString("includeUnbeaten"));
		includeIgnored.setText(LanguageProvider.getString("includeIgnored"));
		
		searchField.setPromptText(LanguageProvider.getString("searchGame"));
		sortingChoice.setPromptText(LanguageProvider.getString("sortBy"));
		
		List<String> arr = new ArrayList<String>();
		arr.add(LanguageProvider.getString("name"));
		arr.add(LanguageProvider.getString("playtime"));
		ObservableList combox1 = FXCollections.observableList(arr);
		sortingChoice.setItems(combox1);
		
		hideStatsButton.setText(LanguageProvider.getString("close"));
	}

	/**
	 * Sets games to gamelist
	 */
	private void initGameListCellFactory() {
		abstract class CustomCell extends ListCell<GameData> {
	
			public Label gameName = new Label();
			public Label timePlayed = new Label();
			public HBox hbox = new HBox();
			public Button ignoreButton = new Button();
			public Button setUnbeatable = new Button();
			public Button setAsBeaten = new Button();
			public ImageView imageView = new ImageView();
			public GameData cellGame;
			public Pane pane = new Pane();

			/**
			 * EventsHandlers for statusbuttons
			 */
			public CustomCell() {
				super();
				EventHandler<MouseEvent> eventIgnored = new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						cellGame.setIgnored(!cellGame.isIgnored());
						mainApp.addGameToDatabase(cellGame);
						System.out.println(cellGame.getName() + " ignored: " + cellGame.isIgnored());
						actionLabel.setText(cellGame.getName() + " ignored: " + cellGame.isIgnored());
						activateFade();
						filterGameData();
						countMarks();
					}
				};
				EventHandler<MouseEvent> eventBeaten = new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						cellGame.setBeaten(!cellGame.isBeaten());
						mainApp.addGameToDatabase(cellGame);
						System.out.println(cellGame.getName() + " beaten: " + cellGame.isBeaten());
						actionLabel.setText(cellGame.getName() + " beaten: " + cellGame.isBeaten());
						activateFade();
						filterGameData();
						countMarks();
					}
				};
				EventHandler<MouseEvent> eventUnbeatable = new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						cellGame.setUnbeatable(!cellGame.isUnbeatable());
						mainApp.addGameToDatabase(cellGame);
						System.out.println(cellGame.getName() + " unbeatable: " + cellGame.isUnbeatable());
						actionLabel.setText(cellGame.getName() + " unbeatable: " + cellGame.isUnbeatable());
						activateFade();
						filterGameData();
						countMarks();
					}
				};
				ignoreButton.addEventFilter(MouseEvent.MOUSE_CLICKED, eventIgnored);
				setAsBeaten.addEventFilter(MouseEvent.MOUSE_CLICKED, eventBeaten);
				setUnbeatable.addEventFilter(MouseEvent.MOUSE_CLICKED, eventUnbeatable);
			}
		}

		// Setting items in each gamelist cell
		gameList.setCellFactory(param -> new CustomCell() {

			@Override
			public void updateItem(GameData game, boolean empty) {
				super.updateItem(game, empty);
				if (empty) {
					setText(null);
					setGraphic(null);
				} else {
					cellGame = game;
					ImageView ignoreImage = new ImageView("/img/hide.png");
					ignoreImage.setFitHeight(40);
					ignoreImage.setFitWidth(40);
					ignoreButton.setGraphic(ignoreImage);

					String defaultCSSButtonStyle = "-fx-border-color: lightgray; -fx-border-width: 2 2 2 2";
					if (game.isIgnored()) {
						ignoreButton.setStyle("-fx-border-color: red; -fx-border-width: 2 2 2 2");
					} else {
						ignoreButton.setStyle(defaultCSSButtonStyle);
					}
					if (game.isUnbeatable()) {
						setUnbeatable.setStyle("-fx-border-color: red; -fx-border-width: 2 2 2 2");
					} else {
						setUnbeatable.setStyle(defaultCSSButtonStyle);
					}
					if (game.isBeaten()) {
						setAsBeaten.setStyle("-fx-border-color: #34eb40; -fx-border-width: 2 2 2 2");
					} else {
						setAsBeaten.setStyle(defaultCSSButtonStyle);
					}

					// tooltips for statusbuttons
					Tooltip ignoreTip = new Tooltip();
					Tooltip beatenTip = new Tooltip();
					Tooltip unbeatableTip = new Tooltip();
					ignoreTip.setText(LanguageProvider.getString("tipIgnore"));
					beatenTip.setText(LanguageProvider.getString("tipSetBeaten"));
					unbeatableTip.setText(LanguageProvider.getString("tipSetUnbeatable"));
					ignoreButton.setTooltip(ignoreTip);
					setAsBeaten.setTooltip(beatenTip);
					setUnbeatable.setTooltip(unbeatableTip);

					// images for statusbuttons
					ImageView beatenImage = new ImageView("/img/trophy.png");
					beatenImage.setFitHeight(40);
					beatenImage.setFitWidth(40);
					setAsBeaten.setGraphic(beatenImage);
					ImageView unbeatableImage = new ImageView("/img/unbeatable.png");
					unbeatableImage.setFitHeight(40);
					unbeatableImage.setFitWidth(40);
					setUnbeatable.setGraphic(unbeatableImage);

					int timePlayedInHours = game.getPlaytime_forever() / 60;
					if (game.getPlaytime_forever() < 60) {
						timePlayed.setText(String.format(LanguageProvider.getString("timePlayedMinutes"), game.getPlaytime_forever()));
					}
					if (game.getPlaytime_forever() < 1) {
						timePlayed.setText(LanguageProvider.getString("timePlayedNone"));
					} else if (timePlayedInHours > 1) {
						timePlayed.setText(String.format(LanguageProvider.getString("timePlayed"), timePlayedInHours));
					}

					if (timePlayedInHours == 1) {
						timePlayed.setText(String.format(LanguageProvider.getString("timePlayed1"), timePlayedInHours));
					}
					gameName.setText(game.getName());
					hbox.setSpacing(30);
					hbox.setAlignment(Pos.CENTER_LEFT);
					try {
						imageView.setImage(new Image(game.getImgLogoFullUrl(), true)); // true: load in background
					} catch (Exception e) {
						System.out.println("Loading game img failed (null or invalid url)");
						imageView.setImage(IMAGE_TEST);
					}
					hbox.getChildren().clear();
					
					imageView.setFitWidth(184);
					gameName.setPrefWidth(270);
					gameName.setPrefHeight(50);
					gameName.setWrapText(true);
					
					hbox.getChildren().addAll(imageView, gameName, timePlayed, pane, setAsBeaten, setUnbeatable,
							ignoreButton);
					HBox.setHgrow(pane, Priority.ALWAYS);
					setGraphic(hbox);
				}
			}
		});
	}

	/**
	 * Sets statsWindow hidden
	 */
	private void hideStats() {
		statsWindow.setManaged(false);
		statsWindow.setVisible(false);
	}

	/**
	 * Initializes achievementList with achievements
	 */
	private void initAchievementCellFactory() {
		achievementList.setCellFactory(param -> new ListCell<Achievement>() {

			public Label achievementName = new Label();
			public Label description = new Label();
			public Label unlockTime = new Label();
			public HBox hbox = new HBox();
			public ImageView imageView = new ImageView();
			public VBox vbox = new VBox();
			public Pane pane = new Pane();

			/**
			 * Sets achievementList cell items
			 * 
			 * @param ach   achievements
			 * @param empty
			 */
			@Override
			public void updateItem(Achievement ach, boolean empty) {
				super.updateItem(ach, empty);
				if (empty) {
					setText(null);
					setGraphic(null);
				} else {
					achievementName.setText(ach.getDisplayName());
					description.setText(ach.getDescription());
					hbox.setSpacing(30);
					hbox.setAlignment(Pos.CENTER_LEFT);
					try {
						imageView.setImage(new Image(ach.getIcon(), true)); // true: load in background
					} catch (Exception e) {
						System.out.println("Loading ach icon failed (null or invalid url)");
						imageView.setImage(IMAGE_TEST);
					}
					hbox.getChildren().clear();
					vbox.getChildren().clear();
					vbox.getChildren().addAll(achievementName, description);
					hbox.getChildren().addAll(imageView, vbox, pane, unlockTime);
					setGraphic(hbox);
					HBox.setHgrow(achievementPane, Priority.ALWAYS);
					HBox.setHgrow(pane, Priority.ALWAYS);

					if (ach.getUnlocktime() == 0) {
						unlockTime.setText("Achievement not unlocked");
					} else {
						String unlockTimeString = Integer.toString(ach.getUnlocktime());
						long epoch = Long.parseLong(unlockTimeString);
						Date expiry = new Date(epoch * 1000);
						SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
						unlockTime.setText("Achievement unlocked in: " + sdf.format(expiry));
					}
				}
			}
		});
	}

	/**
	 * Refreshes the achievementlist
	 */
	public void refreshAchievementList() {
		GameData game = gameList.getSelectionModel().getSelectedItem();
		System.out.println("Ach list size: " + game.getGameStatistics().getAchievements().size());
		achievementStatusLabel.setText("Achievements: " + String.valueOf(game.getGameStatistics().getAchievements().size()));
		achievementList.setItems(FXCollections.observableArrayList(game.getGameStatistics().getAchievements()));
		countMarks();
	}

	/**
	 * Sets statsWindow visible
	 */
	private void showStats() {
		gameList.maxWidth(250);
		statsWindow.setManaged(true);
		statsWindow.setVisible(true);
		countMarks();
	}

	/**
	 * Sets if the statsWindow is visible
	 */
	public void setStatsVisibility() {
		System.out.println("Button clicked!");
		boolean visible = statsWindow.isManaged();
		gameList.setVisible(true);
		if (visible == true) {
			hideStats();
		} else {
			showStats();
		}
	}

	/**
	 * Loads achievement data when a game is clicked
	 * 
	 * @param arg0 MouseEvent for click
	 */
	@FXML
	private void handleMouseClick(MouseEvent arg0) {
		GameData game = gameList.getSelectionModel().getSelectedItem();
		if (game != null) {
			achievementStatusLabel.setText("Loading achievements...");
			//clears the list so it doesn't show the achievements of the previously loaded game
			achievementList.setItems(null);
			statLabel.setText(game.getName());
			mainApp.loadAchievementData(game.getAppid());

			showStats();
			gameList.setVisible(false);
		}
	}
	
	@FXML
	private void handleOrderButton(MouseEvent arg0) {
		if(clicked) {
			clicked = false;
		}else {
			clicked = true;
		}
		initListenerSortGameList();
		setSavedOrDefaultOptions();
	}

	/**
	 * Dropdown options to sort gamelist
	 */
	private void initListenerSortGameList() {
		sortingChoice.getSelectionModel().clearSelection();
		sortingChoice.getSelectionModel().selectedItemProperty().addListener(obs -> {
			// sorting in alphabetical order
			if (sortingChoice.getSelectionModel().getSelectedIndex() == 0 && clicked == false) {
				UserPreferences.setGamelistSort(0);
				sortedFilteredData = filteredData.sorted(Comparator.comparing(GameData::getName));
				ImageView orderAz = new ImageView("/img/az.png");
				orderButton.setGraphic(orderAz);
				orderAz.setFitHeight(18);
				orderAz.setFitWidth(18);
			}else if(sortingChoice.getSelectionModel().getSelectedIndex() == 0 && clicked) {
				UserPreferences.setGamelistSort(0);
				sortedFilteredData = filteredData.sorted(Comparator.comparing(GameData::getName).reversed());
				ImageView orderZa = new ImageView("/img/za.png");
				orderButton.setGraphic(orderZa);
				orderZa.setFitHeight(18);
				orderZa.setFitWidth(18);
			} else if (sortingChoice.getSelectionModel().getSelectedIndex() == 1 && clicked == false) {
				UserPreferences.setGamelistSort(1);
				sortedFilteredData = filteredData
						.sorted(Comparator.comparing(GameData::getPlaytime_forever).reversed());
				ImageView order21 = new ImageView("/img/21.png");
				orderButton.setGraphic(order21);
				order21.setFitHeight(18);
				order21.setFitWidth(18);
			} else if(sortingChoice.getSelectionModel().getSelectedIndex() == 1 && clicked){
				UserPreferences.setGamelistSort(1);
				sortedFilteredData = filteredData
						.sorted(Comparator.comparing(GameData::getPlaytime_forever));
				ImageView order12 = new ImageView("/img/12.png");
				orderButton.setGraphic(order12);
				order12.setFitHeight(18);
				order12.setFitWidth(18);
			}
			gameList.setItems(sortedFilteredData);
		});
	}

	/**
	 * Filtering gamelist with searchfield
	 */
	private Predicate<GameData> searchFilter() {
		Predicate<GameData> filter = game -> true;
		if (searchField.getText().length() > 0)
			filter = game -> game.getName().toLowerCase().contains(searchField.getText().toLowerCase());
		return filter;
	}

	/**
	 * Predicate for including unbeatable games
	 * 
	 * @return predicate for including unbeatable games
	 */
	private Predicate<GameData> includeUnbeatableFilter() {
		Predicate<GameData> filter = game -> true;
		if (!includeUnbeatable.isSelected())
			filter = game -> game.isUnbeatable() == false;
		return filter;
	}

	/**
	 * Predicate for including ignored games
	 * 
	 * @return predicate for including ignored games
	 */
	private Predicate<GameData> includeIgnoredFilter() {
		Predicate<GameData> filter = game -> true;
		if (!includeIgnored.isSelected())
			filter = game -> game.isIgnored() == false;
		return filter;
	}

	/**
	 * Predicate for including beaten games
	 * 
	 * @return predicate for including beaten games
	 */
	private Predicate<GameData> includeBeatenFilter() {
		Predicate<GameData> filter = game -> true;
		if (!includeBeaten.isSelected())
			filter = game -> game.isBeaten() == false;
		return filter;
	}

	/**
	 * Predicate for including unbeaten games
	 * 
	 * @return predicate for including unbeaten games
	 */
	private Predicate<GameData> includeUnbeatenFilter() {
		Predicate<GameData> filter = game -> true;
		if (!includeUnbeaten.isSelected())
			filter = game -> game.isBeaten() == true;
		return filter;
	}

	/**
	 * Filters gamelist using predicates
	 */
	private void filterGameData() {
		filteredData.setPredicate(searchFilter().and(includeUnbeatableFilter()
				.and(includeIgnoredFilter().and(includeBeatenFilter().and(includeUnbeatenFilter())))));
	}

	/**
	 * Adds listeners to gamelist´s filter options
	 */
	private void initFilterListeners() {
		filteredData = new FilteredList<>(games, p -> true);

		searchField.textProperty().addListener(obs -> {
			filterGameData();
		});

		includeUnbeatable.selectedProperty().addListener(obs -> {
			filterGameData();
			UserPreferences.setFilterIncludeUnbeatable(includeUnbeatable.selectedProperty().getValue());
		});

		includeIgnored.selectedProperty().addListener(obs -> {
			filterGameData();
			UserPreferences.setFilterIncludeIgnored(includeIgnored.selectedProperty().getValue());
		});

		includeBeaten.selectedProperty().addListener(obs -> {
			filterGameData();
			UserPreferences.setFilterIncludeBeaten(includeBeaten.selectedProperty().getValue());
		});

		includeUnbeaten.selectedProperty().addListener(obs -> {
			filterGameData();
			UserPreferences.setFilterIncludeUnbeaten(includeUnbeaten.selectedProperty().getValue());
		});
	}

	/**
	 * Checks includeUnbeaten filter and selects sorting by name by default when the
	 * program is started
	 */
	private void setSavedOrDefaultOptions() {
		includeUnbeaten.setSelected(UserPreferences.getFilterIncludeUnbeaten());
		includeBeaten.setSelected(UserPreferences.getFilterIncludeBeaten());
		includeIgnored.setSelected(UserPreferences.getFilterIncludeIgnored());
		includeUnbeatable.setSelected(UserPreferences.getFilterIncludeUnbeatable());

		sortingChoice.getSelectionModel().select(UserPreferences.getGamelistSort());
	}
	
	/**
	 * Counts the amount of marked games and inserts the amount into the Labels meant for it
	 */
	private void countMarks() {
		
		int iBeaten = 0, iNothing = 1, iUnbeatable = 2, iIgnored = 3;
		double beatPercent = 0;
		int[] counts = new int[4];
		
		for (GameData game : getGames()) {
			if (!game.isBeaten() && !game.isUnbeatable() && !game.isIgnored()) {
				counts[iNothing]++;
			}
			else {
				if (game.isBeaten()) {
					counts[iBeaten]++;
				}
				if (game.isUnbeatable()) {
					counts[iUnbeatable]++;
				}
				if (game.isIgnored()) {
					counts[iIgnored]++;
				}				
			}
		}
		
		if (counts[iBeaten] > 0 || counts[iNothing] > 0) {
			beatPercent = counts[iBeaten]*1.0 / (counts[iBeaten] + counts[iNothing]);
		}
		System.out.println("library completion: " + beatPercent);
		
		markedBeaten.setText(String.format(LanguageProvider.getString("markedBeaten"), counts[iBeaten]));
		markedNothing.setText(String.format(LanguageProvider.getString("markedNothing"), counts[iNothing]));
		markedUnbeatable.setText(String.format(LanguageProvider.getString("markedUnbeatable"), counts[iUnbeatable]));
		markedIgnored.setText(String.format(LanguageProvider.getString("markedIgnored"), counts[iIgnored]));
		
	}

	/**
	 * Is called by the main application to give a reference back to itself
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	/**
	 * Fades in and out actionLabel with the game's updated status
	 */
	private void activateFade() {
		// SequentialTransition for multiple transitions in a row
		SequentialTransition t = new SequentialTransition();

		// Fades in the label, added to SequentialTransition
		FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), actionLabel);
		fadeIn.setFromValue(0.0);
		fadeIn.setToValue(1.0);
		t.getChildren().add(fadeIn);

		// Fades out the label, added to SequentialTransition
		FadeTransition fadeOut = new FadeTransition(Duration.seconds(1.5), actionLabel);
		fadeOut.setFromValue(1.0);
		fadeOut.setToValue(0.0);
		t.getChildren().add(fadeOut);

		// Plays the SequentialTransition with both transitions
		t.play();
	}

	/**
	 * Initializes the controller class. This method is automatically called after
	 * the fxml life has been loaded
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loadGames();
	}
}
