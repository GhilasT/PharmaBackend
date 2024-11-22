package javaFxTD72211ex4;

import javaFxTD72211ex3.CompteurPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class PanelDate extends Application{
	public void start(Stage stage) {
		stage.setTitle("Date");
		Pane myPane = new CompteurPane();
		Scene scene = new Scene(myPane);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
