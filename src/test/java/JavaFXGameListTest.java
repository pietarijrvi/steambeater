import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import com.ryhma6.maven.steambeater.model.steamAPI.GameData;
import com.ryhma6.maven.steambeater.view.GameListController;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

class JavaFXGameListTest extends ApplicationTest {

	private Scene scene;
	private GameListController gameListController;
	private List<GameData> gameList;

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("com/ryhma6/maven/steambeater/view/GameList.fxml"));
		stage.setScene(scene = new Scene(loader.load(), 300, 300));
		stage.show();
		gameListController = loader.getController();
	}

	@BeforeAll
	public static void setupSpec() throws Exception {
		if(Boolean.getBoolean("headless")) {
			System.setProperty("testfx.robot", "glass");
			System.setProperty("testfx.headless", "true");
			System.setProperty("prism.order", "sw");
			System.setProperty("prism.text", "t2k");
		}
	}
	
	@BeforeEach
	public void testSetupGameList() {
		gameList = new ArrayList<GameData>();
		gameList.add(new GameData(8, "Ada", 8));
		gameList.add(new GameData(2, "Bill", 7));
		gameList.add(new GameData(3, "Cathy", 6));
		gameList.add(new GameData(4, "Dave", 5));
		gameList.add(new GameData(5, "Ethan", 4));
		gameList.add(new GameData(6, "Fred", 3));
		gameList.add(new GameData(7, "Gideon", 2));
		gameList.add(new GameData(1, "Ann", 4));

		gameListController.setGames(FXCollections.observableArrayList(gameList));
		gameListController.loadGames();
	}

	@Test
	public void testOrderByName() {

		List<GameData> orderedByNameList = new ArrayList<GameData>(gameList);
		orderedByNameList.sort(Comparator.comparing(GameData::getName));

		Platform.runLater(() -> {
			ComboBox<String> combo = (ComboBox<String>) scene.lookup("#sortingChoice");
			combo.getSelectionModel().select(0);
			System.out.println("Selected sorting by: " + combo.getSelectionModel().selectedItemProperty().getValue());

		});
		
		try {
			assertAfterJavaFxPlatformEventsAreDone(() -> {
				ListView<GameData> gameList = (ListView<GameData>) scene.lookup("#gameList");
				List<GameData> actual = gameList.getItems();
				
				System.out.println("nameExp: " + orderedByNameList.get(orderedByNameList.size() - 1).getName());
				System.out.println("first nameAct: " + actual.get(0).getName());
				System.out.println("last nameAct: " + actual.get(actual.size() - 1).getName());
				
				assertEquals(orderedByNameList, actual, "ordering by name failed");
			});
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Disabled("not implemented")
	@Test
	public void testOrderByPlaytime() {

	}

	@Disabled("not implemented")
	@Test
	public void testFilterByName() {

	}

	@Test
	void test2() {
		assertTrue(true);
	}

	private void assertAfterJavaFxPlatformEventsAreDone(Runnable runnable) throws InterruptedException {
		waitOnJavaFxPlatformEventsDone();
		runnable.run();
	}

	private void waitOnJavaFxPlatformEventsDone() throws InterruptedException {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		Platform.runLater(countDownLatch::countDown);
		countDownLatch.await();
	}

}
