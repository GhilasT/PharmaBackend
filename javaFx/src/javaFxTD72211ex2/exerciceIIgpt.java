package javaFxTD72211ex2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class exerciceIIgpt extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Créer les deux fenêtres
        Stage listStage = createListWindow();
        Stage formStage = createFormWindow();

        // Afficher les deux fenêtres indépendamment
        listStage.show();
        formStage.show();
    }

    // Méthode pour créer la première fenêtre (Liste)
    private Stage createListWindow() {
        // TextArea pour la liste
        TextArea listArea = new TextArea();
        listArea.setText("1\n2\n3\n4\n5");
        listArea.setEditable(false);

        // Boutons en bas
        Button btnRetour = new Button("Retour");
        Button btnEnvoyer = new Button("Envoyer");
        Button btnAnnuler = new Button("Annuler");

        HBox buttonBox = new HBox(10, btnRetour, btnEnvoyer, btnAnnuler);
        buttonBox.setStyle("-fx-alignment: center; -fx-padding: 10;");

        // Layout principal
        VBox layout = new VBox(10, listArea, buttonBox);
        layout.setStyle("-fx-padding: 10;");

        // Créer une scène et un stage
        Scene scene = new Scene(layout, 400, 300);
        Stage stage = new Stage();
        stage.setTitle("GUI - Liste");
        stage.setScene(scene);

        return stage;
    }

    // Méthode pour créer la deuxième fenêtre (Formulaire)
    private Stage createFormWindow() {
        // État civil (Mr/Mme)
        Label lblEtatCivil = new Label("Etat civil :");
        RadioButton rbMr = new RadioButton("Mr");
        RadioButton rbMme = new RadioButton("Mme");
        ToggleGroup toggleGroup = new ToggleGroup();
        rbMr.setToggleGroup(toggleGroup);
        rbMme.setToggleGroup(toggleGroup);

        HBox civilBox = new HBox(10, lblEtatCivil, rbMr, rbMme);
        civilBox.setStyle("-fx-alignment: center-left;");

        // Nom et Prénom
        Label lblNom = new Label("Nom :");
        TextField txtNom = new TextField();

        Label lblPrenom = new Label("Prénom :");
        TextField txtPrenom = new TextField();

        HBox nomBox = new HBox(10, lblNom, txtNom);
        HBox prenomBox = new HBox(10, lblPrenom, txtPrenom);

        // Bouton Soumettre
        Button btnSoumettre = new Button("Soumettre");

        VBox formBox = new VBox(10, civilBox, nomBox, prenomBox, btnSoumettre);
        formBox.setStyle("-fx-padding: 10;");

        // Créer une scène et un stage
        Scene scene = new Scene(formBox, 400, 300);
        Stage stage = new Stage();
        stage.setTitle("GUI - Formulaire d'inscription");
        stage.setScene(scene);

        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}


