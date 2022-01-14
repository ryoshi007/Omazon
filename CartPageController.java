package omazon;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class CartPageController implements Initializable {
    private UserHolder userHolder = UserHolder.getInstance();
    private User user = userHolder.getUser();
    private ProductDatabase productDatabase = new ProductDatabase();
    private CustomerDatabase customerDatabase = new CustomerDatabase();
    private double totalPayAmount;

    @FXML private Pane favourite;
    @FXML private Pane cart;
    @FXML private Pane LogOut;
    @FXML private MenuButton categorySelector;
    @FXML private Pane Settings;
    @FXML private Pane history;
    @FXML private Pane homepage;
    @FXML private Pane store;
    @FXML private Rectangle checkoutBar;
    @FXML private Label totalLabel;
    @FXML private Label priceLabel;
    @FXML private Button checkoutButton;
    @FXML private FlowPane purchasePane;
    @FXML private ScrollPane scrollPane;
    @FXML private Label noPurchaseLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        String cartItem = customerDatabase.retrieveUserData(Integer.parseInt(user.getID()), "CartItem");
        
        if (cartItem == null) {
            totalLabel.setVisible(false);
            priceLabel.setVisible(false);
            checkoutBar.setVisible(false);
            checkoutButton.setVisible(false);
        } else if (!cartItem.isBlank()) {
            noPurchaseLabel.setVisible(false);
            String[] product = cartItem.split(";");

            for (String info: product) { 
                String[] productInfo = info.split(",");
                String productID = productInfo[0];
                Product currentProduct = productDatabase.retrieveSpecificProduct(productID, "idproduct");
                initializeProductPane(currentProduct, productInfo[1]);
            }
        } else {
            totalLabel.setVisible(false);
            priceLabel.setVisible(false);
            checkoutBar.setVisible(false);
            checkoutButton.setVisible(false);
        }
    }

    private void initializeProductPane(Product product, String amount) {       
        GridPane productPane = new GridPane();
        productPane.setMinWidth(730);
        productPane.setMaxWidth(730);
        productPane.setPrefHeight(125);
        productPane.setPadding(new Insets(10, 5, 0, 10));
        productPane.getColumnConstraints().addAll(new ColumnConstraints(120), new ColumnConstraints(260)
        , new ColumnConstraints(240), new ColumnConstraints(100));
        
        ImageView image = new ImageView(product.getImagePath());
        image.setFitWidth(100);
        image.setFitHeight(100);
        image.setSmooth(true);
        productPane.add(image, 0, 0, 1, 3);
        
        Label removeButton = new Label("Remove");
        removeButton.setStyle("-fx-font-family: Courier; -fx-font-size: 11pt");
        removeButton.setTextFill(Color.web("ff0000"));
        removeButton.setCursor(Cursor.HAND);
        removeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{
            try {
                removeProduct(String.valueOf(product.getProductID()), amount);
            } catch (IOException ex) {
                Logger.getLogger(CartPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        Label productName = new Label(product.getName());
        productName.setStyle("-fx-font-family: Courier; -fx-font-size: 11pt");        
        Label productPrice = new Label(String.format("Price: RM %.2f", product.getPrice()));
        productPrice.setStyle("-fx-font-family: Courier; -fx-font-size: 11pt");       
        Label productAmount = new Label(amount);
        productAmount.setStyle("-fx-font-family: Courier; -fx-font-size: 11pt");
        Label totalAmount = new Label();
        totalAmount.setStyle("-fx-font-family: Courier; -fx-font-size: 11pt");
        double subamount = Integer.parseInt(amount)* product.getPrice();
        totalPayAmount += subamount;
        String subamountInString = String.format("RM %.2f", subamount);
        totalAmount.setText(String.format(subamountInString));
        priceLabel.setText(String.format("RM %.2f", totalPayAmount));
        
        productPane.add(productName, 1, 0);
        productPane.add(productPrice, 1, 1);
        productPane.add(removeButton, 1, 2);
        productPane.add(productAmount, 2, 1);
        productPane.add(totalAmount, 3, 1);
        purchasePane.getChildren().add(productPane);
    }
    
    private void removeProduct(String productID, String amount) throws IOException {
        String removeInput = productID + "," + amount + ";";
        CustomerDatabase database = new CustomerDatabase();
        String currentInput = database.retrieveUserData(Integer.parseInt(user.getID()), "CartItem");       
        String newInput = currentInput.replace(removeInput, "");
        
        database.update("CartItem", newInput, String.valueOf(user.getID()));
        
        Parent loader = FXMLLoader.load(getClass().getResource("CartPage.fxml"));
        Stage window = (Stage)scrollPane.getScene().getWindow();
        window.setScene(new Scene(loader));
    }

    @FXML
    private void goToFavourite() throws IOException {
        userHolder.setFavouriteOrderPane("Favourite");
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
    private void goToSettingsPage() throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("SettingPage.fxml"));
        Stage window = (Stage)Settings.getScene().getWindow();
        window.setScene(new Scene(loader));
    }

    @FXML
    private void goToHistory() throws IOException {
        userHolder.setFavouriteOrderPane("Order");
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
    private void checkout() throws IOException {
        if (user.getPaymentPassword() == null) {
            Alert alertBox = new Alert(AlertType.INFORMATION);
            alertBox.setContentText("Please insert your password payment in Settings");
            alertBox.show();
            goToSettingsPage();        
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Balance.fxml"));
            AnchorPane anchorPane = loader.load();
            BalanceController controller = loader.getController();
            controller.setFromWhere("CartPage");
            controller.setPrice(totalPayAmount);
            Stage window = (Stage)checkoutButton.getScene().getWindow();
            window.setScene(new Scene(anchorPane)); 
        }
    }
    
}
