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
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SearchPageController implements Initializable {
    private ProductDatabase productDatabase = new ProductDatabase();
    private CustomerDatabase customerDatabase = new CustomerDatabase();
    private UserHolder holder = UserHolder.getInstance();

    @FXML private Pane favourite;
    @FXML private Pane cart;
    @FXML private Pane LogOut;
    @FXML private Pane Settings;
    @FXML private Pane history;
    @FXML private Pane homepage;
    @FXML private Pane store;
    @FXML private TextField searchField;
    @FXML private Button searchButton;
    private List<BorderPane> productPane;
    @FXML private FlowPane container;
    @FXML private MenuButton categorySelector;
    @FXML private Label noResultText;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String categoryName = new CategoryHolder().getCategoryName();
        categorySelector.setText(categoryName);
        noResultText.setVisible(false);
    }
    
    public void searchByName(String input) {
        ArrayList<String> productNameList = productDatabase.retrieveSpecificColumn("productName");
        ArrayList<String> results = new ArrayList<>();
        for (int i = 0; i < productNameList.size(); i++) {
            String currentName = productNameList.get(i).toLowerCase();
            if (currentName.contains(input.toLowerCase())) {
                results.add(productNameList.get(i));
            }
        }
        
        ArrayList<Product> relatedProduct = new ArrayList<>();
        for (String name: results) {
            relatedProduct.add(productDatabase.retrieveSpecificProduct(name, "productName"));
        }
        
        if (relatedProduct.size() == 0) {
            noResultText.setVisible(true);
        }
        createProductPane(relatedProduct, "name", input);
        showSellerStore(input);
    }
    
    public void searchByCategory() {
        String categoryName = new CategoryHolder().getCategoryName();
        ArrayList<Product> relatedProduct = productDatabase.retrieveProduct(categoryName, "category");
        
        if (relatedProduct.size() == 0) {
            noResultText.setVisible(true);
        }
        createProductPane(relatedProduct, "category", ""); 
    }
    
    public void showSellerStore(String input) {
        ArrayList<String> sellerName = customerDatabase.retrieveSpecificColumn("Username");
        ArrayList<String> relatedSeller = new ArrayList<>();
        for (int i = 0; i < sellerName.size(); i++) {
            if (sellerName.get(i).toLowerCase().contains(input.toLowerCase())) {
                relatedSeller.add(sellerName.get(i));
            }
        }
        
        if (relatedSeller.size() == 0) {
            noResultText.setVisible(true);
        } else {
            noResultText.setVisible(false);
        }
        
        for(int i = 0; i < relatedSeller.size(); i++) {
            BorderPane sellerStoreLayout = new BorderPane();
            sellerStoreLayout.setMinHeight(250);
            sellerStoreLayout.setMinWidth(250);
            sellerStoreLayout.setPadding(new Insets(2, 12, 2, 12));
            sellerStoreLayout.setStyle("-fx-border-color: #ffffff");

            ImageView image = new ImageView("C:\\Users\\Freyr\\Documents\\NetBeansProjects\\Omazon\\src\\omazon\\pictures\\storelogo.png");
            image.setStyle("-fx-background-color: #ffffff");
            image.setFitHeight(220);
            image.setFitWidth(240);
            image.setSmooth(true);
            
            Label storeName = new Label(relatedSeller.get(i) + "'s Store");
            storeName.setStyle("-fx-font-family: Courier; -fx-font-size: 11pt; -fx-font-weight: bold;");
            BorderPane.setAlignment(storeName, Pos.CENTER);
            
            sellerStoreLayout.setCenter(image);
            sellerStoreLayout.setBottom(storeName);
            sellerStoreLayout.setCursor(Cursor.HAND);
            sellerStoreLayout.setUserData(relatedSeller.get(i));
            sellerStoreLayout.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                String seller = (String) sellerStoreLayout.getUserData();
                String sellerID = customerDatabase.retrieveUserID("idCustomer", seller);
                
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Store.fxml"));
                    AnchorPane anchorPane = loader.load();
                    StoreController controller = loader.getController();
                    controller.displayOtherStore(sellerID, seller);
                    Stage window = (Stage)container.getScene().getWindow();
                    window.setScene(new Scene(anchorPane));
                } catch (IOException ex) {
                    Logger.getLogger(SearchPageController.class.getName()).log(Level.SEVERE, null, ex);
                } 
            });
            
            container.getChildren().add(sellerStoreLayout);
        }
        
    }
    
    public void createProductPane(ArrayList<Product> relatedProduct, String type, String searchInput) {
        productPane = new ArrayList<>();
        for (int i = 0; i < relatedProduct.size(); i++) {
            BorderPane productLayout = new BorderPane();
            productLayout.setMinHeight(250);
            productLayout.setMinWidth(250);
            productLayout.setPadding(new Insets(2, 12, 2, 12));
            productLayout.setStyle("-fx-border-color: #ffffff");
            
            ImageView image = new ImageView(relatedProduct.get(i).getImagePath());
            image.setFitWidth(240);
            image.setFitHeight(240);
            image.setSmooth(true);
            productLayout.setCenter(image);
            productLayout.setCursor(Cursor.HAND);
            productLayout.setUserData(relatedProduct.get(i));
            productLayout.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                ProductHolder holder = ProductHolder.getInstance();
                holder.setProduct((Product)productLayout.getUserData());
                
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("ProductPage.fxml"));
                    AnchorPane anchorPane = loader.load();
                    ProductPageController controller = loader.getController();
                    if (type.equals("name")) {
                        controller.setSearchInput(searchInput);
                    } else if (type.equals("category")) {
                        controller.setCategoryInput();
                    }
                    Stage window = (Stage)container.getScene().getWindow();
                    window.setScene(new Scene(anchorPane));
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
    private void search() throws IOException {
        String searchInput = searchField.getText();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchPage.fxml"));
        AnchorPane anchorPane = loader.load();
        SearchPageController controller = loader.getController();
        controller.searchByName(searchInput);
        Stage window = (Stage)searchButton.getScene().getWindow();
        window.setScene(new Scene(anchorPane));
    }

    @FXML
    private void selectCategory(ActionEvent event) throws IOException {
        MenuItem source = (MenuItem) event.getSource();
        new CategoryHolder().setCategoryName(source.getText());
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchPage.fxml"));
        AnchorPane anchorPane = loader.load();
        SearchPageController controller = loader.getController();
        controller.searchByCategory();
        Stage window = (Stage)categorySelector.getScene().getWindow();
        window.setScene(new Scene(anchorPane));  
    }
    
}
