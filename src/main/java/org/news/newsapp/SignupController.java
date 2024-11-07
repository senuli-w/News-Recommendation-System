package org.news.newsapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignupController {
    @FXML
    public Label loginSignupPageTitle;
    @FXML
    public Button loginSignupPageButton;
    public TextField emailField;
    public PasswordField passwordField;
    public Label errorLabel;

//    public void logIn(ActionEvent event) {
//        System.out.println(validateInputs());
//    }

    /*private boolean validateInputs(){
        String emailInput = emailField.getText();
        String passwordInput = passwordField.getText();
        if (emailInput.trim().isEmpty() && passwordInput.trim().isEmpty()){
            errorLabel.setText("Invalid email or password.");
            return false;
        }
        if (!emailInput.contains("@")) {
            if (emailParts.length == 2){
                if (emailParts[1].contains(".")){

                }
            }
        }
            String[] emailParts = emailInput.split("@");

        }

        return false;
    }*/
}
