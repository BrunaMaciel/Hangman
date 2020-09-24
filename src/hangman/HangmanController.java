/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hangman;

import static hangman.HangmanMain.LEVEL;
import static hangman.HangmanMain.MAX_HINTS;
import static hangman.HangmanMain.MAX_TIME;
import static hangman.HangmanMain.PLAYERS;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

/**
 * //
 * @author Bruna
 */
public class HangmanController implements Initializable {
    public static final String[] WORDS = {"geography", "cat", "yesterday", "java", "truck", "opportunity",
                "fish", "token", "transportation", "bottom", "apple", "cake",
                "remote", "boots", "terminology", "arm", "cranberry", "tool",
                "caterpillar", "spoon", "watermelon", "laptop", "toe", "toad",
                "fundamental", "capitol", "garbage", "anticipate", "pesky"};
    
    public static final String[] PHRASES = {"BRUSH YOUR TEETH","BUILDING A SANDCASTLE", "SHE DRIVES A CAR", 
    "OPENING A GIFT", "THEY LIKE BASEBALL", "LET'S PLAY", "THE SNOW IS WHITE", "HANGMAN IS FUN"};
    
    public static final Random RANDOM = new Random();
    public static final int MAX_ERRORS = 6;
    
    private String stringToFind;	
    private char[] stringFound;
    private int nErrors;
    private int totalScore;
    private String guess = "";
    private ArrayList<String> letters = new ArrayList<>();
    private Timeline timer;
    private int seconds;
    private boolean isRunning = false;
    private int hints;
    private int MAX_TIME_WORD;
    
    
    @FXML
    private Label guessedLetters;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label errorsLabel;
    @FXML
    private FlowPane lettersPane;
    @FXML
    private ImageView hangmanImage;
    @FXML
    private Label timerLabel;
    @FXML
    private Button timerButton, guessButton;
    @FXML
    private TextField letterTextField;
    @FXML
    void optionsButtonPressed(ActionEvent event) {
        try {
            timerPause();
            Parent root = FXMLLoader.load(getClass().getResource("Options.fxml"));
            
            Scene scene = new Scene(root);
            Stage optionsStage = new Stage();
            optionsStage.setScene(scene);
            optionsStage.setTitle("Hangman Game - Options");
            optionsStage.setResizable(false);
            optionsStage.show();
            optionsStage.setOnCloseRequest(new EventHandler<WindowEvent>(){
                @Override
                public void handle(WindowEvent event) {
                    timerPlay();
                }
            });
            

        } catch (IOException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void solveButtonPressed(ActionEvent event) {
        Dialog<String> dialog = new TextInputDialog();
        dialog.setTitle("Solve word");
        dialog.setHeaderText("Enter the word to solve ans save the man.");

        Optional<String> result = dialog.showAndWait();
        

        if (result.isPresent()) {

            guess = result.get().toUpperCase();
            if (!StringFound(guess))
            nErrors++;
        }
        
        
        updateGame();
        
    }
    @FXML
    private void aboutButtonPressed(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("About Hangman");
        alert.setContentText("Hangman is a guessing game where the computer or a player choses a word" +
                    " and the other player tries to guess it by suggesting letters." +
                    "\n\nObjetive: Guess the word before the character is hung.The character is hung if the player guess letters that are not in the chosen word 6 times"
                    + "\n\nHow to play:  the player will guess a letter. If that letter is in the word then the computer write the letter in everywhere it would appear," +
                    " if the letter isn't in the word then a body part is added to the gallows. The player will continue guessing letters until they can either solve the word " + 
                    "or all six body parts are on the gallows.");
            
        alert.showAndWait();
    }
    @FXML
    private void closeButtonPressed(ActionEvent event) {
        closeGame();
    }
    @FXML
    private void newGameButtonPressed(ActionEvent event) {
        MenuItem btn = (MenuItem) event.getSource();
        
        if (btn.getId().equals("newGame2Button"))
            PLAYERS = 2;
        else
            PLAYERS = 1;
        
        newGame();
    }
    @FXML
    private void guessLetterButtonPressed(ActionEvent event) {
        String l;
        if(!letterTextField.getText().equals("")){
            l = letterTextField.getText(0,1).toUpperCase();
            for(Node n : lettersPane.getChildren()){
                    if( n instanceof Button) {
                        if(n.getId().startsWith(l.toLowerCase()+"_"))
                            n.setDisable(true);
                    }
                }
            checkLetter(l);
            letterTextField.clear();
        }
        else{
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Letter field empty!");
            alert.setHeaderText(null);
            alert.setContentText("To guess a letter type in the correct field.");
            alert.show();
        }
    }
     @FXML
    private void hintButtonPressed(ActionEvent event) {
        char l;
        int index;
        if (hints > 0){
            do{
              index = RANDOM.nextInt(stringFound.length);
              l = stringFound[index];   
            }
            while (l != '_');
            String letter = String.valueOf(stringToFind.charAt(index));
            checkLetter(letter);
            hints--;
        }
        else{
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("No Hints!");
            alert.setHeaderText(null);
            alert.setContentText("You don't have any hints left.");
            alert.show();
        }
    }
    @FXML
    private void letterPressed(ActionEvent event) {
        Button btn = (Button) event.getSource();
        String l = btn.getText();
        btn.setDisable(true);
        
        checkLetter(l);
    }
    @FXML
    private void timerButtonPressed(ActionEvent event) {
        if(isRunning){
            timerPause();
        }
        else{
            timerPlay();
        }
        
    }
    private void timerPlay(){
        timerButton.setText("Pause");
            startTimer();
            disableLetterButtons(false);
            guessButton.setDisable(false);
            letterTextField.setDisable(false);
            guessedLetters.setVisible(true);
    }
    private void timerPause(){
        timerButton.setText("Play");
            timer.pause();
            isRunning=false;
            disableLetterButtons(true);
            guessButton.setDisable(true);
            letterTextField.setDisable(true);
            guessedLetters.setVisible(false);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        newGame();
        
        letterTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("^[a-zA-Z]?")) {
                    letterTextField.setText(oldValue);
                }
            }
        });
    }    
    
    private void checkLetter(String l){
        if (!letters.contains(l)) {
          if (stringToFind.contains(l)) {
            int index = stringToFind.indexOf(l);
            while (index >= 0) {
              stringFound[index] = l.charAt(0);
              totalScore+=1;
              index = stringToFind.indexOf(l, index + 1);
            }
          } else {
            nErrors++;
          }

          letters.add(l);
        }
        updateGame();
    }
    private void disableLetterButtons(boolean enable){
        for(Node n : lettersPane.getChildren()){
            if( n instanceof Button) {
                n.setDisable(enable);
                for (String s : letters){
                    if(n.getId().startsWith(s.toLowerCase()))
                        n.setDisable(true);
                }
            }
        }      
        
    }
    private void newStringToGuess (int score){
        seconds = MAX_TIME_WORD;
        nErrors = 0;
        totalScore = score;
        letters.clear();
          disableLetterButtons(false);
        stringToFind = nextStringToFind();

        // word found initialization
        stringFound = new char[stringToFind.length()];
        
        //transform all letter characters in underscore in the stringFound
        for (int i = 0; i < stringFound.length; i++) {
          if(Character.toString(stringToFind.charAt(i)).matches("[a-zA-Z]")){
            stringFound[i] = '_';
          }
          else{
              stringFound[i] = stringToFind.charAt(i);
          }
        }
        
        guessedLetters.setText(stringFoundContent());
        updateGame();
        startTimer();
        
    }
    private void newGame(){
        timer = null;
        totalScore = 0;
        hints = MAX_HINTS;
        MAX_TIME_WORD = MAX_TIME;
        newStringToGuess(totalScore); 
    }
    
    private void updateGame(){
        
        guessedLetters.setText(stringFoundContent());
        scoreLabel.setText(String.valueOf(totalScore));
        errorsLabel.setText(String.valueOf(nErrors));
        
        if (stringFound() || StringFound(guess)) {
            hangmanImage.setImage(new Image("/win.png"));
            endGame(true);
        }else {
            hangmanImage.setImage(new Image("/"+nErrors+"-error.png"));
            if (nErrors == MAX_ERRORS) {endGame(false);}
        }
        
    }
    private void endGame(boolean win){
        timerPause();
        if (!win){
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("You lose!");
            alert.setHeaderText(null);
            alert.setContentText("Word to find was : " + stringToFind + 
                                    "\nTotal score : " + totalScore + 
                                    "\n\nStart new game?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                newGame();
            } else {
                closeGame();
            }
        }else{
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("You win!");
            alert.setHeaderText(null);
            alert.setContentText("Word to find was : " + stringToFind + 
                                    "\nTotal score : " + totalScore + 
                                    "\n\nContinue game with new word?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                newStringToGuess(totalScore);
            } else {
                closeGame();
            }
        }
    }
    private String nextStringToFind() {
        String s = "";
        do{
            if (LEVEL.equals("Words")){
                s = WORDS[RANDOM.nextInt(WORDS.length)];
            }
            else{
                s = PHRASES[RANDOM.nextInt(PHRASES.length)];
            }

            if(PLAYERS == 2){
                Stage stage = (Stage) lettersPane.getScene().getWindow();
                stage.hide();
                
                Dialog<String> dialog = new TextInputDialog();
                dialog.setTitle("Multiplayer - Select secret");
                dialog.setHeaderText("Enter the word or phrase to be guessed");

                Optional<String> result = dialog.showAndWait();

                if (result.isPresent()) {
                    if (!result.get().matches("^[.]{3,}$")) {
                        s = result.get();
                    }
                    else s="";
                }

            }
        }
        while (s.equals(""));
        
        return s.toUpperCase();
    }
    public boolean stringFound() {
         return stringToFind.contentEquals(new String(stringFound));
    }
    public boolean StringFound(String guess) {
         return stringToFind.contentEquals(guess);
    }
    private String stringFoundContent() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < stringFound.length; i++) {
          builder.append(stringFound[i]);

          if (i < stringFound.length - 1) {
            builder.append(" ");
          }
        }

        return builder.toString();
      }
    private void closeGame(){
        Stage stage = (Stage) lettersPane.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage,WindowEvent.WINDOW_CLOSE_REQUEST));
    }
    private void startTimer(){
        if (timer == null){
            timer = new Timeline(new KeyFrame(Duration.seconds(1), e-> timeLabelUpdate()));
            timer.setCycleCount(MAX_TIME_WORD);
            timer.setOnFinished(e -> {
                Platform.runLater(()-> endGame(false));
                    });
            
        }
        if(!isRunning){
            timer.play();
            isRunning=true;
            timerButton.setText("Pause");
        }
        else timer.play();
         
    }
    private void timeLabelUpdate(){
        if (seconds > 0){
            seconds--;
            
        }
        timerLabel.setText(String.valueOf(seconds));
    }
}
