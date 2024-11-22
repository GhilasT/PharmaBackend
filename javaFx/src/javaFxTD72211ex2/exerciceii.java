package javaFxTD72211ex2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class exerciceii extends Application{
	public void stage(Stage stage) {
		Stage listeStage = listeStage();
	}
	
	public Stage listeStage() {
		TextArea listeArea = new TextArea();
		listeArea.setText("1\n2\n3\n4\n5");
		
		
		Button retour = new Button("Retour");
		Button envoyer = new Button("Envoyer");
		Button annuler = new Button("Annuler");
		
		
		Scene scene = new Scene();
        Stage stage = new Stage();
        stage.setTitle("GUI - Liste");
        stage.setScene(scene);

        return stage;
	}
	
	
	public static void main(String[]args) {
		launch(args);
	}
}

