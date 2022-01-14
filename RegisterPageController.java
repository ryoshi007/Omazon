package omazon;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class RegisterPageController implements Initializable {
    CustomerDatabase database = new CustomerDatabase();

    @FXML private Pane backButton;
    @FXML private Button createAccountButton;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField usernameField;
    @FXML private TextArea addressField;
    @FXML private Label invalidLabel;
    @FXML private Label overlapLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {        
    }    

    @FXML
    private void goToLoginPage() throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        Stage window = (Stage)backButton.getScene().getWindow();
        window.setScene(new Scene(loader));
    }

    @FXML
    private void createAccount() throws IOException {
        invalidLabel.setVisible(false);
        overlapLabel.setVisible(false);
        
        if (emailField.getText().isBlank() || passwordField.getText().isBlank() ||
                usernameField.getText().isBlank() || addressField.getText().isBlank()) {
            invalidLabel.setVisible(true);
        } else {
            String email = emailField.getText();
            String password = passwordField.getText();
            String username = usernameField.getText();
            String address = addressField.getText();
            int uniqueID = createUniqueID();
            
            if (!checkIfNotUnique("Username", username) && !checkIfNotUnique("Email", email)) {
               database.createAccount(uniqueID, username, email, password, address); 
               goToLoginPage();
            } else {
                overlapLabel.setVisible(true);
            }
        }
    }
    
    private int createUniqueID() {
        Random random = new Random();
        int uniqueID = random.nextInt(99999999) + 1;
        String idInString = String.valueOf(uniqueID);
        while (true) {
            if (database.compareItemInRow("idCustomer", idInString) == false) {
                break;
            } else {
                uniqueID = random.nextInt(99999999) + 1;
                idInString = String.valueOf(uniqueID);
            }
        }
        return uniqueID;
    }
    
    private boolean checkIfNotUnique(String columnName, String input) {
        return database.compareItemInRow(columnName, input);
    }

}
