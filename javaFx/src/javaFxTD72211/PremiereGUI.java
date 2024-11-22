package javaFxTD72211;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PremiereGUI extends Application {
@Override

	public void start(Stage stage) throws Exception {
		stage.setTitle("Premiere GUI");
		BorderPane pane = new BorderPane();
		Scene scene = new Scene(pane);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}
}
