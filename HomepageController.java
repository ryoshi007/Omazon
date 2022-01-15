package omazon;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HomepageController implements Initializable {
    private ProductDatabase database = new ProductDatabase();
    private UserHolder holder = UserHolder.getInstance();

    @FXML private Pane LogOut;
    @FXML private Pane Settings;
    @FXML private Pane favourite;
    @FXML private Pane cart;
    @FXML private Pane history;
    @FXML private Pane homepage;
    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private ImageView bestSeller1;
    @FXML private ImageView bestSeller2;
    @FXML private ImageView bestSeller3;
    @FXML private Text bestSellerName1;
    @FXML private Text bestSellerName2;
    @FXML private Text bestSellerName3;
    @FXML private Pane store;
    @FXML private BorderPane borderPane1;
    @FXML private BorderPane borderPane2;
    @FXML private BorderPane borderPane3;
    @FXML private MenuButton categorySelector;
    @FXML private Button balanceButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CategoryHolder category = new CategoryHolder();
        category.setCategoryName("Categories");
        categorySelector.setText(category.getCategoryName());
        
        ArrayList<String> salesVolume = database.retrieveSpecificColumn("sales");
        ArrayList<String> productID = database.retrieveSpecificColumn("idproduct");
        HashMap<Integer, Integer> categorised = new HashMap<>();
        for (int i = 0; i < salesVolume.size(); i++) {
            categorised.put(Integer.parseInt(salesVolume.get(i)), Integer.parseInt(productID.get(i)));
        }
        
        Map<Integer, Integer> sortedList = categorised.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByKey()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
        
        Set<Integer> sales = sortedList.keySet();
        ArrayList<Integer> salesList = new ArrayList<Integer>(sales);
        
        for (int i = salesList.size() - 1; salesList.size() > 3; i--) {
            salesList.remove(i);
        }
        
        ArrayList<Product> productList = new ArrayList<>();
        for (int i = salesList.size() - 1; i >= 0; i--) {
            String keyInString = String.valueOf(categorised.get(salesList.get(i)));
            Product product = database.retrieveSpecificProduct(keyInString, "idproduct");
            productList.add(product);
        }
        displayBestSelling(productList);       
    }
    
    private void displayBestSelling(ArrayList<Product> productList) {
        ArrayList<ImageView> images = new ArrayList<>();
        images.add(bestSeller3);images.add(bestSeller2);images.add(bestSeller1);
        
        ArrayList<Text> text = new ArrayList<>();
        text.add(bestSellerName3);text.add(bestSellerName2);text.add(bestSellerName1);
        
        ArrayList<BorderPane> pane = new ArrayList<>();
        pane.add(borderPane3);pane.add(borderPane2);pane.add(borderPane1);
        
        for (int i = 0; i < productList.size(); i++) {
            BorderPane currentPane = pane.get(i);
            ImageView currentImagePane = images.get(i);
            Text currentText = text.get(i);
            
            currentPane.setPrefHeight(245);
            currentPane.setPrefWidth(236);
            currentPane.setPadding(new Insets(2, 12, 2, 12));           
            currentPane.setStyle("-fx-border-color: #ffffff");
            currentPane.setCursor(Cursor.HAND);
            currentImagePane = new ImageView(productList.get(i).getImagePath());
            currentImagePane.setFitWidth(235);
            currentImagePane.setFitHeight(235);
            currentImagePane.setSmooth(true);
            currentPane.setCenter(currentImagePane);
            currentText.setText(productList.get(i).getName());
            
            currentPane.setUserData(productList.get(i));
            currentPane.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                ProductHolder holder = ProductHolder.getInstance();
                holder.setProduct((Product)currentPane.getUserData());
                
                Parent loader;
                try {
                    loader = FXMLLoader.load(getClass().getResource("ProductPage.fxml"));
                    Stage window = (Stage)currentPane.getScene().getWindow();
                    window.setScene(new Scene(loader));
                } catch (IOException ex) {
                    Logger.getLogger(HomepageController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
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
        Stage window = (Stage)favourite.getScene().getWindow();
        window.setScene(new Scene(loader)); 
    }

    @FXML
    private void goToHomepage() throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("Homepage.fxml"));
        Stage window = (Stage)homepage.getScene().getWindow();
        window.setScene(new Scene(loader));
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
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchPage.fxml"));
        AnchorPane anchorPane = loader.load();
        SearchPageController controller = loader.getController();
        controller.searchByCategory();
        Stage window = (Stage)categorySelector.getScene().getWindow();
        window.setScene(new Scene(anchorPane));      
    }

    @FXML
    private void goToBalance() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Balance.fxml"));
        AnchorPane anchorPane = loader.load();
        BalanceController controller = loader.getController();
        controller.setFromWhere("Homepage");
        Stage window = (Stage)balanceButton.getScene().getWindow();
        window.setScene(new Scene(anchorPane));      
    }
    
}
