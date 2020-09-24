/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hangman;

import static hangman.HangmanMain.PLAYERS;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Bruna
 */
public class MenuController implements Initializable {
    
    @FXML
    private MenuItem newGameMenuButton;

    @FXML
    private MenuItem closeButton;

    @FXML
    private ImageView hangmanImage;

    @FXML
    private Button newGameButton;

    @FXML
    private Button helpButton;

    @FXML
    private void helpButtonPressed(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("About Hangman");
        alert.setContentText("“Hangman” is a game in which the player tries to guess a pre-determined word/phrase (or secret) by choosing letters from the alphabet. Each time an incorrect letter is chosen, a body part is added to a picture of a hanging man, and for every correct letter the player wins a point. The word must be guessed before the man is complete (after 6 incorrect letters) and before the time finishes, or the game is lost. \n" +
                            "\nThe player can choose between playing alone (the game choses a word/phrase) or with another player (one player choses the word/phrase and the other guesses). If playing alone, by default the game will only show words to be guessed, if the player wishes to play with phrases, he can change this in the options menu. Otherwise the game secret is whatever the other player chooses.\n" +
                            "\nInitially, the player has 60 seconds to guess each word and 5 hints to help him to guess the word. While the time is restated for each word, the hints are not. Those amounts can be changed in the options menu.\n" +
                            "To guess a letter, the player can type the desired letter or click in the buttons with the letters. If the player wishes to solve the secret he can click the solve button and type the whole word/phrase.");
        alert.showAndWait();
    }
    @FXML
    private void closeButtonPressed(ActionEvent event) {
        Stage stage = (Stage) helpButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void newGameButtonPressed(ActionEvent event) {
        Button btn = (Button) event.getSource();
        if (btn.getId().equals("newGame2Button"))
            PLAYERS = 2;
        else 
            PLAYERS = 1;
        
        try {
            Stage menu = (Stage) helpButton.getScene().getWindow();
            
            Parent root = FXMLLoader.load(getClass().getResource("Hangman.fxml"));
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Hangman Game");
            stage.setResizable(false);
            stage.show();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event1) {
                            menu.show();
                }
            });
            menu.hide();
            
        } catch (IOException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void optionsButtonPressed(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Options.fxml"));
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Hangman Game - Options");
            stage.setResizable(false);
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
            hangmanImage.setImage(new Image("/0-error.png"));
    }

}
