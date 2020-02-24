package com.ryhma6.maven.steambeater;
import java.io.IOException;

import org.expressme.openid.Association;
import org.expressme.openid.Endpoint;
import org.expressme.openid.OpenIdManager;

import com.ryhma6.maven.steambeater.view.FriendsListController;
import com.ryhma6.maven.steambeater.view.GameListController;
import com.ryhma6.maven.steambeater.view.SearchBoxController;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;


public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    
    @FXML
    private FlowPane sidebar;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Steambeater");

        initRootLayout();
        showGameList();
        showFriendsList();
    }
    
    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            ListView<String> list = new ListView<String>();
            ObservableList<String> items =FXCollections.observableArrayList (
                "Single", "Double", "Suite", "Family App");
            list.setItems(items);
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Adds gamelist view to the root layout
     */
    public void showGameList() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/GameList.fxml"));
            AnchorPane gameList = (AnchorPane) loader.load();
            // Set person overview into the center of root layout.
            rootLayout.setCenter(gameList);
         // Give the controller access to the main app.
            GameListController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Adds testview to the root layout
     */
    public void showSearchBox() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/searchBox.fxml"));
            AnchorPane box = (AnchorPane) loader.load();
            // Set person overview into the center of root layout.
            FlowPane sidebar = (FlowPane) rootLayout.lookup("#sidebar");
            sidebar.getChildren().add(box);
         // Give the controller access to the main app.
            SearchBoxController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showFriendsList() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/friendsList.fxml"));
            AnchorPane friends = (AnchorPane) loader.load();
            // Set person overview into the center of root layout.
            FlowPane sidebar = (FlowPane) rootLayout.lookup("#sidebar");
            sidebar.getChildren().add(friends);
         // Give the controller access to the main app.
            FriendsListController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

}