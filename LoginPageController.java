package omazon;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class LoginPageController implements Initializable {
    private CustomerDatabase database = new CustomerDatabase();
    private MediaPlayer mediaplayer;
    private Media media;
    
    @FXML private TextField inputEmail;
    @FXML private PasswordField inputPassword;
    @FXML private Button signInButton;
    @FXML private Label invalidLogin;
    @FXML private Hyperlink createNewAccount;
    @FXML private MediaView videoPlay;
    
    @FXML
    private void signInButtonClicked() throws IOException {
        if (checkUserInfo(inputEmail.getText(), inputPassword.getText())) {
            User user = database.createUserObject(inputEmail.getText());
            UserHolder holder = UserHolder.getInstance();
            holder.setUser(user);
            Parent loader = FXMLLoader.load(getClass().getResource("Homepage.fxml"));
            Stage window = (Stage)signInButton.getScene().getWindow();
            window.setScene(new Scene(loader));
        }
    }
    
    private boolean checkUserInfo(String email, String password) {
        invalidLogin.setVisible(false);

        if (database.compareItemInRow("email", email) && database.checkSpecific("Email", "Password", password, email)) {
            return true;
        } else {
            invalidLogin.setVisible(true);
            return false;
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String path = new File("C:/Users/Freyr/Documents/NetBeansProjects/Omazon/src/omazon/pictures/login video.mp4").getPath();
        media = new Media(new File(path).toURI().toString());
        mediaplayer = new MediaPlayer(media);
        videoPlay.setMediaPlayer(mediaplayer);
        mediaplayer.setAutoPlay(true);
    }    

    @FXML
    private void createNewAccount() throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("RegisterPage.fxml"));
        Stage window = (Stage)createNewAccount.getScene().getWindow();
        window.setScene(new Scene(loader));
    }
}