package javaFxTD72211ex3;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;

public class CompteurPane extends FlowPane {
	Label lab ;
	Button button ;
	Compteur compteur ;
	public CompteurPane() {
		lab = new Label("−−> 0");
		button = new Button("OK");
		compteur = new Compteur();
		button.setOnAction(new CompteurHandler(lab, compteur));
		this.getChildren().addAll(button,lab);
	}
}