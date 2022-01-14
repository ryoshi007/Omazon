package omazon;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SettingPageController implements Initializable {
    private UserHolder holder = UserHolder.getInstance();

    @FXML private Pane LogOut;
    private ImageView profilePictureFrame;
    @FXML private Pane favourite;
    @FXML private Pane cart;
    @FXML private Pane Settings;
    @FXML private Pane history;
    @FXML private Pane homepage;
    private Button storePane;
    private AnchorPane mainPane;
    @FXML private TextField usernameField;
    @FXML private TextArea addressField;
    @FXML private Button updateButton;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField paymentPasswordField;
    @FXML private TextField emailField;
    @FXML private Label updateLabel;
    @FXML private Pane store;
    @FXML private AnchorPane accountPane;
    @FXML private Button deleteButton;
    @FXML private MenuButton categorySelector;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        UserHolder holder = UserHolder.getInstance();
        User user = holder.getUser();
        usernameField.setText(user.getUsername());
        addressField.setText(user.getAddress());
        passwordField.setText(user.getPassword());
        paymentPasswordField.setText(user.getPaymentPassword());
        emailField.setText(user.getEmail());
    }    

    @FXML
    private void goToLoginPage() throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        Stage window = (Stage)LogOut.getScene().getWindow();
        window.setScene(new Scene(loader));
    }

    @FXML
    private void goToSettingsPage() throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("SettingPage.fxml"));
        Stage window = (Stage)LogOut.getScene().getWindow();
        window.setScene(new Scene(loader));
    }

    @FXML
    private void goToFavourite() throws IOException {
        holder.setFavouriteOrderPane("Favourite");
        Parent loader = FXMLLoader.load(getClass().getResource("Favourite.fxml"));
        Stage window = (Stage)favourite.getScene().getWindow();
        window.setScene(new Scene(loader)); 
    }

    @FXML
    private void goToCart() throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("CartPage.fxml"));
        Stage window = (Stage)cart.getScene().getWindow();
        window.setScene(new Scene(loader));
    }

    @FXML
    private void goToHistory() throws IOException {
        holder.setFavouriteOrderPane("Order");
        Parent loader = FXMLLoader.load(getClass().getResource("Favourite.fxml"));
        Stage window = (Stage)history.getScene().getWindow();
        window.setScene(new Scene(loader)); 
    }

    @FXML
    private void goToHomepage() throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("Homepage.fxml"));
        Stage window = (Stage)homepage.getScene().getWindow();
        window.setScene(new Scene(loader));
    }

    @FXML
    private void deleteAccount() throws IOException {
        UserHolder holder = UserHolder.getInstance();
        User user = holder.getUser();
        CustomerDatabase database = new CustomerDatabase();
        database.delete(user.getID());
        
        Parent loader = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        Stage window = (Stage)LogOut.getScene().getWindow();
        window.setScene(new Scene(loader));
    }

    @FXML
    private void updateDetail() throws IOException {
        UserHolder holder = UserHolder.getInstance();
        User user = holder.getUser();
        
        String newUsername = usernameField.getText();
        String newPassword = passwordField.getText();
        String newAddress = addressField.getText();
        String newPaymentPassword = paymentPasswordField.getText();
        String newEmail = emailField.getText();
        
        user.setUsername(newUsername);
        user.setPassword(newPassword);
        user.setAddress(newAddress);
        user.setPaymentPassword(newPaymentPassword);
        user.setEmail(newEmail);
        
        CustomerDatabase database = new CustomerDatabase();
        database.update("Username", newUsername, user.getID());
        database.update("Password", newPassword, user.getID());
        database.update("Address", newAddress, user.getID());
        database.update("PaymentPassword", newPaymentPassword, user.getID());
        database.update("Email", newEmail, user.getID());
        
        Parent loader = FXMLLoader.load(getClass().getResource("SettingPage.fxml"));
        Stage window = (Stage)updateButton.getScene().getWindow();
        window.setScene(new Scene(loader));
    }

    @FXML
    private void goToStore() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Store.fxml"));
        AnchorPane anchorPane = loader.load();
        StoreController controller = loader.getController();
        controller.displaOwnStore();
        Stage window = (Stage)store.getScene().getWindow();
        window.setScene(new Scene(anchorPane));
    }

    @FXML
    private void selectCategory(ActionEvent event) throws IOException {
        MenuItem source = (MenuItem) event.getSource();
        new CategoryHolder().setCategoryName(source.getText());
        categorySelector.setText(source.getText());
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchPage.fxml"));
        AnchorPane anchorPane = loader.load();
        SearchPageController controller = loader.getController();
        controller.searchByCategory();
        Stage window = (Stage)categorySelector.getScene().getWindow();
        window.setScene(new Scene(anchorPane)); 
    }
    
}
