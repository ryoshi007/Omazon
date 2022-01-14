package omazon;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class StoreController implements Initializable {
    private ProductDatabase productDatabase = new ProductDatabase();
    private UserHolder holder = UserHolder.getInstance();
    private User user = holder.getUser();
    private boolean otherStore = false;

    @FXML private Pane favourite;
    @FXML private Pane cart;
    @FXML private Pane LogOut;
    @FXML private Pane Settings;
    @FXML private Pane history;
    @FXML private Pane homepage;
    @FXML private Pane store;
    @FXML private FlowPane container;
    @FXML private Button notificationButton;
    @FXML private Button transactionButton;
    private List<BorderPane> productPane;
    @FXML private Button addProductButton;
    @FXML private Label storeLabel;
    @FXML private MenuButton categorySelector;

    @Override
    public void initialize(URL url, ResourceBundle rb) {     
    }
    
    public void displaOwnStore() {
        String shopName = user.getUsername() + "'s Store";
        storeLabel.setText(shopName);
        initialiseProductPane(user.getID());
    }
    
    public void displayOtherStore(String ownerID, String ownerName) {
        otherStore = true;
        String shopName = ownerName + "'s Store";
        storeLabel.setText(shopName);
        initialiseProductPane(ownerID);
        notificationButton.setVisible(false);
        transactionButton.setVisible(false);
        addProductButton.setVisible(false);
    }
    
    private void initialiseProductPane(String userID) {
        ArrayList<Product> productList = productDatabase.retrieveProduct(userID, "idOwner");
        
        productPane = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            BorderPane productLayout = new BorderPane();
            productLayout.setMinHeight(250);
            productLayout.setMinWidth(250);
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
                    if (otherStore == false) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddProduct.fxml"));
                        AnchorPane anchorPane = loader.load();
                        AddProductController controller = loader.getController();
                        controller.updateInitialization();
                        Stage window = (Stage)container.getScene().getWindow();
                        window.setScene(new Scene(anchorPane));
                    } else {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("ProductPage.fxml"));
                        AnchorPane anchorPane = loader.load();
                        ProductPageController controller = loader.getController();
                        controller.setStoreInput(userID);
                        Stage window = (Stage)container.getScene().getWindow();
                        window.setScene(new Scene(anchorPane));
                    }
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
    private void goToStore() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Store.fxml"));
        AnchorPane anchorPane = loader.load();
        StoreController controller = loader.getController();
        controller.displaOwnStore();
        Stage window = (Stage)store.getScene().getWindow();
        window.setScene(new Scene(anchorPane));
    }

    @FXML
    private void goToNotification() throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("Notification.fxml"));
        Stage window = (Stage)addProductButton.getScene().getWindow();
        window.setScene(new Scene(loader));
    }

    @FXML
    private void goToTransaction() throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("Transaction.fxml"));
        Stage window = (Stage)transactionButton.getScene().getWindow();
        window.setScene(new Scene(loader));
    }

    @FXML
    private void addNewProduct() throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("AddProduct.fxml"));
        Stage window = (Stage)addProductButton.getScene().getWindow();
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
    
}
