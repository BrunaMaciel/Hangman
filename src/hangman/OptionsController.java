/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hangman;

import static hangman.HangmanMain.LEVEL;
import static hangman.HangmanMain.MAX_HINTS;
import static hangman.HangmanMain.MAX_TIME;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Bruna
 */
public class OptionsController implements Initializable {
     @FXML
    private TextField timerTextField;

    @FXML
    private TextField hintsTextField;
    
    @FXML
    private Button applyButton;

    @FXML
    private Button cancelButton;
        @FXML
    private RadioButton wordsRadioButton;

    @FXML
    private ToggleGroup radioGroup;

    @FXML
    private RadioButton phrasesRadioButton;
    

    @FXML
    void applyButtonPressed(ActionEvent event) {
        //checks if the textfields inputs have value before apply
        if (!timerTextField.getText().equals(""))
            MAX_TIME = Integer.parseInt(timerTextField.getText());
        
        if (!hintsTextField.getText().equals(""))
            MAX_HINTS = Integer.parseInt(hintsTextField.getText());
        
        RadioButton rb = (RadioButton) radioGroup.getSelectedToggle();
        LEVEL = rb.getText();

        closeWindow();
    }

    @FXML
    void cancelButtonPressed(ActionEvent event) {
        closeWindow();
    }
    private void closeWindow(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage,WindowEvent.WINDOW_CLOSE_REQUEST));
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        timerTextField.setText(String.valueOf(MAX_TIME));
        hintsTextField.setText(String.valueOf(MAX_HINTS));
        if (LEVEL.equals("Words"))
            radioGroup.selectToggle(wordsRadioButton);
        else
            radioGroup.selectToggle(phrasesRadioButton);
        
        timerTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("^[0-9]*$")) {
                    timerTextField.setText(oldValue);
                }
            }
        });
        hintsTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("^[0-9]*$")) {
                    hintsTextField.setText(oldValue);
                }
            }
        });

    }
}
