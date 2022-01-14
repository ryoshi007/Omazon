package omazon;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Omazon extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml")); 
        Scene scene = new Scene(root);
        stage.setTitle("Omazon");
        stage.setResizable(false);
        
        Image icon = new Image("C:\\Users\\Freyr\\Documents\\NetBeansProjects\\Omazon\\src\\omazon\\pictures\\appicon.jfif");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
