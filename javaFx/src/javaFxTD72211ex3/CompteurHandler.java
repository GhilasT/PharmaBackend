package javaFxTD72211ex3;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;

public class CompteurHandler implements EventHandler<ActionEvent> {
	private Label lab ;
	private Compteur compteur ;
	
	public CompteurHandler(Label lab, Compteur cpt) {
		this.lab = lab ;
		compteur = cpt;
	}
	@Override
	public void handle(ActionEvent event) {
		compteur.incrementer();
		lab.setText("−−> " + compteur.getValeur());
	}
}
