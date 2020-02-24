package com.ryhma6.maven.steambeater.view;

import java.net.URL;
import java.util.ResourceBundle;
import com.ryhma6.maven.steambeater.MainApp;

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
	private ListView<String> gameList;

	@FXML
	Button hideStatsButton;

	@FXML
	AnchorPane statsWindow;
	
	@FXML
	Label statLabel;
	
	@FXML
	ComboBox sortingChoice;
	
	@FXML
	TextField searchField;
	
	private MainApp mainApp;
	private final Image IMAGE_TEST = new Image("test.png");

	private Image[] listOfImages = { IMAGE_TEST };
	private ObservableList<String> names = FXCollections.observableArrayList("Pasianssi", "Minesweeper", "Sudoku");
	private ImageView imageView = new ImageView();
	private Button ignoreButton = new Button();
	private Button setAsBeaten = new Button();
	
	
	private void loadGames() {
		HBox hbox = new HBox();
		Label gameName = new Label();
		hbox.getChildren().addAll(imageView,gameName);
		HBox.setHgrow(imageView, Priority.ALWAYS);
		hbox.setSpacing(20);
		ignoreButton.setText("Ignore this game");
		setAsBeaten.setText("Set game as beaten");
		
		hbox.setAlignment(Pos.CENTER_LEFT);
		gameName.setPadding(new Insets(10,300,10,10));

//		ignoreButton.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent event) {
//				String selectedGame = gameList.getSelectionModel().getSelectedItem();
//				gameList.getItems().remove(selectedGame);
//			}
//		});
		
		gameList.setCellFactory(param -> new ListCell<String>() {
			
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
				
					gameName.setText(name);
					setGraphic(hbox);
					
				}
			}
		});
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
		String text = gameList.getSelectionModel().getSelectedItem();
		statLabel.setText(text);
		showStats();
	}
	
	private String getName(){
		String name = names.toString();
		return name;
	}
	
	@FXML
	/**
	 * Dropdown options to sort gamelist
	 */
	private void sortGameList() {
		sortingChoice.getSelectionModel().selectedItemProperty().addListener(obs->{
			//sorting in alphabetical order
			if(sortingChoice.getSelectionModel().getSelectedIndex() == 0) {
				gameList.setItems(names.sorted());
				loadGames();
			}
		});
	}
	
	
	/**
	 * Filtering gamelist with searchfield
	 */
	private void filterByName() {
        FilteredList<String> filteredData = new FilteredList<>(names, p -> true);
        searchField.textProperty().addListener(obs->{
            String filter = searchField.getText(); 
            if(filter == null || filter.length() == 0) {
                filteredData.setPredicate(s -> true);
            }
            else {
                filteredData.setPredicate(s -> s.contains(filter));
            }
        });
        
        //Wrap the FilteredList in a SortedList. 
        SortedList<String> sortedData = new SortedList<>(filteredData);
        
        //Add sorted (and filtered) data to the table.
        gameList.setItems(sortedData);
    	
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loadGames();
		hideStats();
		filterByName();
	}
}