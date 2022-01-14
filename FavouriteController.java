package omazon;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class FavouriteController implements Initializable {
    private UserHolder holder = UserHolder.getInstance();
    private User user = holder.getUser();
    private ProductDatabase productDatabase = new ProductDatabase();
    private String pageName = holder.getFavouriteOrderPane();

    @FXML private Pane favourite;
    @FXML private Pane cart;
    @FXML private Pane LogOut;
    @FXML private Pane Settings;
    @FXML private Pane history;
    @FXML private Pane homepage;
    @FXML private MenuButton categorySelector;
    @FXML private Pane store;
    private List<BorderPane> productPane;
    @FXML private FlowPane container;
    @FXML private Label pageTitle;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList<Product> productList = new ArrayList<>();
        if (pageName.equals("Favourite")) {
            if (user.getFavourite() == null) {                
            } else if (!user.getFavourite().isBlank()) {
                String[] favouriteProduct = user.getFavourite().split(",");
                for (String productID : favouriteProduct) {
                    productList.add(productDatabase.retrieveSpecificProduct(productID, "idproduct"));
                }
                initializeProductPane(productList);
            }
        } else if (pageName.equals("Order")) {
            pageTitle.setText("Order");
            pageTitle.setTextFill(Color.web("001c57"));
            if (user.getOrder() == null) {                
            } else if (!user.getOrder().isBlank()) {
                String[] orderedProduct = user.getOrder().split(",");
                for (String productID : orderedProduct) {
                    productList.add(productDatabase.retrieveSpecificProduct(productID, "idproduct"));
                }
                initializeProductPane(productList);
            }
        }
    }
    
    private void initializeProductPane(ArrayList<Product> productList) {
        productPane = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            BorderPane productLayout = new BorderPane();
            productLayout.setMinHeight(260);
            productLayout.setMaxHeight(260);
            productLayout.setMinWidth(260);
            productLayout.setMaxWidth(260);
            productLayout.setPadding(new Insets(2, 12, 2, 12));
            productLayout.setStyle("-fx-border-color: #ffffff");
            
            ImageView image = new ImageView(productList.get(i).getImagePath());
            image.setFitWidth(240);
            image.setFitHeight(240);
            image.setSmooth(true);
            productLayout.setCenter(image);
            productLayout.setCursor(Cursor.HAND);
            productLayout.setUserData(productList.get(i));
            productLayout.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                ProductHolder holder = ProductHolder.getInstance();
                holder.setProduct((Product)productLayout.getUserData());
                
                try {
                    Parent loader = FXMLLoader.load(getClass().getResource("ProductPage.fxml"));
                    Stage window = (Stage)container.getScene().getWindow();
                    window.setScene(new Scene(loader));
                } catch (IOException ex) {
                    Logger.getLogger(StoreController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            productPane.add(productLayout);
            container.getChildren().add(productLayout);
        }
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
    private void goToLoginPage() throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        Stage window = (Stage)LogOut.getScene().getWindow();
        window.setScene(new Scene(loader));
    }

    @FXML
    private void goToSettingsPage() throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("SettingPage.fxml"));
        Stage window = (Stage)Settings.getScene().getWindow();
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

    @FXML
    private void goToStore() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Store.fxml"));
        AnchorPane anchorPane = loader.load();
        StoreController controller = loader.getController();
        controller.displaOwnStore();
        Stage window = (Stage)store.getScene().getWindow();
        window.setScene(new Scene(anchorPane));
    }
    
}
