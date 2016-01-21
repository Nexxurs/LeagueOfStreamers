package main;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import storage.dataStorage;

import java.util.ResourceBundle;

public class MainApplication extends Application {
	//TODO ERROR CODE when no connection 
	//TODO Sync
	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/MainGuiSmall.fxml"));
		Pane rootGroup = fxmlLoader.load();
		
		final MainGuiController guiCont = fxmlLoader.getController();
		guiCont.init();
		Scene scene = new Scene(rootGroup);
		scene.setFill(Color.TRANSPARENT);
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(we -> {
			dataStorage.getStorageObj().setLastRegion(guiCont.getCurrRegion());
			dataStorage.safeStorageObjToFile();
			System.exit(0);
		});
		primaryStage.setTitle("League of Streamers");
		primaryStage.setMinHeight(350);
		primaryStage.setMinWidth(300);
		primaryStage.show();
		
	}

}
