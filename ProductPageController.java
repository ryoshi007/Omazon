package omazon;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ProductPageController implements Initializable {
    private UserHolder userHolder = UserHolder.getInstance();
    private User user = userHolder.getUser();
    private ProductHolder productHolder = ProductHolder.getInstance();
    private Product product = productHolder.getProduct();
    private String searchInput;
    private String storeInput = null;
    private boolean isFavourited = false;
    
    private ReviewDatabase reviewDatabase = new ReviewDatabase();
    private CustomerDatabase customerDatabase = new CustomerDatabase();
    
    private String fontStyle = "-fx-font-family: Courier; -fx-font-size: 11pt";
    
    @FXML private Pane favourite;
    @FXML private Pane cart;
    @FXML private Pane LogOut;
    @FXML private Pane Settings;
    @FXML private Pane history;
    @FXML private Pane homepage;
    @FXML private Text productName;
    @FXML private Text productPrice;
    @FXML private Text productAmount;
    @FXML private Text rating;
    @FXML private Text productSold;
    @FXML private Text category;
    @FXML private Text seller;
    @FXML private TextArea description;
    @FXML private Button addToFavouriteButton;
    @FXML private Button addToCartButton;
    @FXML private Button buyNowButton;
    @FXML private TextField purchaseAmount;
    @FXML private MenuButton rateButton;
    @FXML private Text invalidAmount;
    @FXML private Pane store;
    @FXML private Label purchaseAmountLabel;
    @FXML private BorderPane imagePane;
    @FXML private Label backButton;
    @FXML private Label goBackByCategory;
    @FXML private MenuButton categorySelector;
    @FXML private Label addToCartLabel;
    @FXML private FlowPane reviewPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        backButton.setVisible(false);
        goBackByCategory.setVisible(false);
        rateButton.setVisible(false);
        
        if (Integer.parseInt(user.getID()) == product.getOwnerID()) {
            buyNowButton.setVisible(false);
            addToCartButton.setVisible(false);
            purchaseAmount.setVisible(false);
            addToFavouriteButton.setVisible(false);
            purchaseAmountLabel.setVisible(false);
        }
        
        if (reviewDatabase.checkSpecific("idproduct", "idcustomer", user.getID(), String.valueOf(product.getProductID()))) {
            rateButton.setVisible(true);
        }
        
        if (user.getFavourite() == null || user.getFavourite().isBlank()) {
            addToFavouriteButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                try {
                    addToFavourite();
                } catch (IOException ex) {
                    Logger.getLogger(ProductPageController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } else {
            determineFavourited();
        }
        
        ImageView image = new ImageView(product.getImagePath());
        image.setFitWidth(250);
        image.setFitHeight(250);
        image.setSmooth(true);
        imagePane.setCenter(image);        
        productName.setText(product.getName());
        productPrice.setText(String.format("%.2f", product.getPrice()));
        productAmount.setText(String.valueOf(product.getStock()));
        productSold.setText(String.valueOf(product.getSales()));
        rating.setText(String.format("%.2f", product.getRating()));
        category.setText(product.getCategory());
        
        CustomerDatabase database = new CustomerDatabase();
        String ownerName = database.retrieveUserData(product.getOwnerID(), "Username");
        seller.setText(ownerName);
        description.setText(product.getDescription());
        description.setEditable(false);
        
        ArrayList<String> customerList = reviewDatabase.retrieveSpecificColumn(product.getProductID(), "idcustomer");
        if (customerList == null) {            
        } else {
            try {
                initializeReviewPane(customerList);
            } catch (IOException ex) {
                Logger.getLogger(ProductPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void determineFavourited() {
        if (user.getFavourite() != null || !user.getFavourite().isBlank()) {
            String[] favourited = user.getFavourite().split(",");
            for (String productID : favourited) {
                if (Integer.parseInt(productID) == product.getProductID()) {
                    isFavourited = true;
                }
            }
        } else if (user.getFavourite() == null) {};
        
        if (isFavourited) {
            addToFavouriteButton.setText("Unfavourite");
            addToFavouriteButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                try {
                    removeFavourite();
                } catch (IOException ex) {
                    Logger.getLogger(ProductPageController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } else {
            addToFavouriteButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                try {
                    addToFavourite();
                } catch (IOException ex) {
                    Logger.getLogger(ProductPageController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }
    
    public void setSearchInput(String input) {
        this.searchInput = input;
        backButton.setVisible(true);
    }
    
    public void setCategoryInput() {
        goBackByCategory.setVisible(true);
    }
    
    public void setStoreInput(String input) {
        this.storeInput = input;
        backButton.setVisible(true);
    }
    
    private void removeFavourite() throws IOException {
        String removeInput = product.getProductID() + ",";
        String newFavourite = user.getFavourite().replace(removeInput, "");
        user.setFavourite(newFavourite);
        
        CustomerDatabase database = new CustomerDatabase();
        database.update("Favourite", user.getFavourite(), user.getID());
        
        Parent loader = FXMLLoader.load(getClass().getResource("ProductPage.fxml"));
        Stage window = (Stage)addToFavouriteButton.getScene().getWindow();
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

    private void addToFavourite() throws IOException {
        CustomerDatabase database = new CustomerDatabase();
        String input = product.getProductID() + ",";
        if (user.getFavourite() == null) {
            user.setFavourite(input);
        } else {
            user.setFavourite(user.getFavourite() + input);
        }
        database.concatData(input, "favourite", user.getID());
        
        Parent loader = FXMLLoader.load(getClass().getResource("ProductPage.fxml"));
        Stage window = (Stage)addToFavouriteButton.getScene().getWindow();
        window.setScene(new Scene(loader));
    }

    @FXML
    private void addToCart() {
        invalidAmount.setVisible(false);
        if (purchaseAmount.getText().isBlank() || (Integer.parseInt(purchaseAmount.getText()) > product.getStock())) {
            invalidAmount.setVisible(true);
            invalidAmount.setText("Please insert a valid amount!");
        } else if (isValidInteger(purchaseAmount.getText(), 10)) {
            String input = product.getProductID() + "," + Integer.parseInt(purchaseAmount.getText()) + ";";
            CustomerDatabase database = new CustomerDatabase();
            database.concatData(input, "CartItem", user.getID());
            addToCartLabel.setVisible(true);    
        } else {
            invalidAmount.setVisible(true);
            invalidAmount.setText("Please insert a valid amount!");
        }   
    }

    @FXML
    private void buyNow() throws IOException {
        invalidAmount.setVisible(false);

        if (purchaseAmount.getText().isBlank() || !isValidInteger(purchaseAmount.getText(), 10)) {
            invalidAmount.setVisible(true);
            invalidAmount.setText("Please insert a valid amount!");
        } else if (isValidInteger(purchaseAmount.getText(), 10) && Integer.parseInt(purchaseAmount.getText()) <= product.getStock()) {
            if (checkIfDuplicateOrder(String.valueOf(product.getProductID()))) {
                removeDuplicateOrder(purchaseAmount.getText());
            } else {
               String input = product.getProductID() + "," + Integer.parseInt(purchaseAmount.getText()) + ";";
                CustomerDatabase database = new CustomerDatabase();
                database.concatData(input, "CartItem", user.getID());
            }
            Parent loader = FXMLLoader.load(getClass().getResource("CartPage.fxml"));
            Stage window = (Stage)buyNowButton.getScene().getWindow();
            window.setScene(new Scene(loader));
            
        } else {
            invalidAmount.setVisible(true);
            invalidAmount.setText("Please insert available amount!");
        }
    }
    
    private boolean checkIfDuplicateOrder(String productID) {
        CustomerDatabase database = new CustomerDatabase();
        String currentCart = database.retrieveUserData(Integer.parseInt(user.getID()), "CartItem");
        String[] productItem = currentCart.split(";");
        for (String productInfo : productItem) {
            String[] productOrder = productInfo.split(",");
            if (productID.equals(productOrder[0])) {
                return true;
            }
        }
        return false;
    }
    
    private void removeDuplicateOrder(String amountPurchased) {
        CustomerDatabase database = new CustomerDatabase();
        String currentCart = database.retrieveUserData(Integer.parseInt(user.getID()), "CartItem");
        String[] productItem = currentCart.split(";");
        for (String productInfo : productItem) {
            String[] productOrder = productInfo.split(",");
            if (product.getProductID() == Integer.parseInt(productOrder[0])) {
                int latestOrder = Integer.parseInt(productOrder[1]) + Integer.valueOf(amountPurchased);
                String removeString = productOrder[0] + "," + productOrder[1] + ";";
                currentCart = currentCart.replace(removeString, "");
                String newOrder = product.getProductID() + "," + latestOrder + ";";
                currentCart = currentCart + newOrder;
                database.update("CartItem", currentCart, String.valueOf(user.getID()));
            }
        }
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
    private void goBack() throws IOException {
        if (storeInput != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Store.fxml"));
            AnchorPane anchorPane = loader.load();
            StoreController controller = loader.getController();
            
            String productOwnerName = customerDatabase.retrieveUserData(product.getOwnerID(), "Username");
            controller.displayOtherStore(String.valueOf(product.getOwnerID()), productOwnerName);
            Stage window = (Stage)backButton.getScene().getWindow();
            window.setScene(new Scene(anchorPane));
        } else {
            String searchInput = this.searchInput;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchPage.fxml"));
            AnchorPane anchorPane = loader.load();
            SearchPageController controller = loader.getController();
            controller.searchByName(searchInput);
            Stage window = (Stage)backButton.getScene().getWindow();
            window.setScene(new Scene(anchorPane));
        }
    }

    @FXML
    private void goBackToSearchByCategory() throws IOException {
        String categoryName = new CategoryHolder().getCategoryName();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchPage.fxml"));
        AnchorPane anchorPane = loader.load();
        SearchPageController controller = loader.getController();
        controller.searchByCategory();
        Stage window = (Stage)backButton.getScene().getWindow();
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
    
    private boolean isValidInteger(String s, int radix) {
        try {
            double d = Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        if (s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) return false;
                else continue;
            }
            if (Character.digit(s.charAt(i), radix) < 0) return false;
        }
        
        if (Integer.valueOf(s) == 0) return false;
        return true;
    }

    @FXML
    private void rate(ActionEvent event) throws IOException {
        MenuItem source = (MenuItem) event.getSource();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText("Are you sure with your rate?");
        
        Optional<ButtonType> result = alert.showAndWait();
	if(!result.isPresent() || result.get() != ButtonType.OK) {
	} else {
            ProductDatabase productDatabase = new ProductDatabase();
            double currentRate = product.getRating();
            int sales = product.getSales();
            double newRate = ((currentRate * (sales - 1)) + Integer.valueOf(source.getText())) / sales;
            newRate = (double) Math.round(newRate * 100) / 100;
            product.setRating(newRate);
            productDatabase.update("rating", String.valueOf(newRate), String.valueOf(product.getProductID()));
            
            Parent loader = FXMLLoader.load(getClass().getResource("ProductPage.fxml"));
            Stage window = (Stage)rateButton.getScene().getWindow();
            window.setScene(new Scene(loader));
	}
    }
    
    public void initializeReviewPane(ArrayList<String> customerList) throws IOException {
        ArrayList<String> reviewList = reviewDatabase.retrieveSpecificColumn(product.getProductID(), "review");
        ArrayList<String> replyList = reviewDatabase.retrieveSpecificColumn(product.getProductID(), "ownerreply");

        for (int i = 0; i < customerList.size(); i++) {
            String customerName = customerDatabase.retrieveUserData(Integer.parseInt(customerList.get(i)), "Username");
            String customerID = customerList.get(i);
            //is seller himself
            if (user.getID().equals(String.valueOf(product.getOwnerID()))) {
                if (reviewList.get(i) == null) {
                } else {
                    GridPane reviewBox = prepareReviewBox();
                    Label review = new Label(customerName + ": " + reviewList.get(i));
                    review.setStyle(fontStyle);
                    reviewBox.add(review, 0, 0);
                    Button deleteButton = createDeleteButton();
                    deleteButton.setUserData(customerID);
                    reviewBox.add(deleteButton, 1, 0);
                    
                    if (replyList.get(i) == null) {
                        Button replyButton = createReplyButton();
                        replyButton.setUserData(customerID);
                        reviewBox.add(replyButton, 1, 1);
                    } else {
                        Label reply = new Label("Seller: " + replyList.get(i));
                        reply.setStyle(fontStyle);
                        reviewBox.add(reply, 0, 1);
                        Button editReplyButton = createEditReplyButton();
                        editReplyButton.setUserData(customerID);
                        reviewBox.add(editReplyButton, 1, 1);
                    }
                    
                    reviewPane.getChildren().add(reviewBox);
                }
            //Normal customer
            }else if (!user.getID().equals(customerList.get(i))) {
                if (reviewList.get(i) == null) {
                } else {
                    GridPane reviewBox = prepareReviewBox();
                    Label review = new Label(customerName + ": " + reviewList.get(i));
                    review.setStyle(fontStyle);
                    reviewBox.add(review, 0, 0);
                    if (replyList.get(i) == null) {
                    } else {
                        Label reply = new Label("Seller: " + replyList.get(i));
                        reply.setStyle(fontStyle);
                        reviewBox.add(reply, 0, 1);
                    }
                    reviewPane.getChildren().add(reviewBox);
                }
            // If is the buyer himself
            } else if (user.getID().equals(customerList.get(i))) {
                GridPane reviewBox = prepareReviewBox();
                if (reviewList.get(i) == null) {
                    Button commentButton = createCommentButton();
                    commentButton.setUserData(customerID);
                    reviewBox.add(commentButton, 1, 0);
                } else {
                    Label review = new Label(customerName + ": " + reviewList.get(i));
                    review.setStyle(fontStyle);
                    reviewBox.add(review, 0, 0);
                    Button editCommentButton = createEditCommentButton();
                    editCommentButton.setUserData(customerID);
                    reviewBox.add(editCommentButton, 1, 0);
                }
                
                if (replyList.get(i) == null) {
                } else {
                    Label reply = new Label("Seller: " + replyList.get(i));
                    reply.setStyle(fontStyle);
                    reviewBox.add(reply, 0, 1);
                }
                reviewPane.getChildren().add(reviewBox);
            } 

        }   
    }

    private void addNewComment(String index) throws IOException {
        String input = textDialog();
        if (!input.isBlank()) {
            reviewDatabase.update("review", input, String.valueOf(product.getProductID()), index);           
            goToProductPageItself();
        }
    }
    
    private void editComment(String index) throws IOException { 
        String input = textDialog();
        if (!input.isBlank()) {
            reviewDatabase.update("review", input, String.valueOf(product.getProductID()), index);           
            goToProductPageItself();
        }
    }
    
    private void addNewReply(String index) throws IOException { 
        String input = textDialog();
        if (!input.isBlank()) {
            reviewDatabase.update("ownerreply", input, String.valueOf(product.getProductID()), index);           
            goToProductPageItself();
        }
    }
    
    private void editReply(String index) throws IOException {
        String input = textDialog();
        if (!input.isBlank()) {
            reviewDatabase.update("ownerreply", input, String.valueOf(product.getProductID()), index);           
            goToProductPageItself();
        }
    }
    
    private void deleteReview(String index) throws IOException {
        reviewDatabase.updateNull("review", String.valueOf(product.getProductID()), index);
        reviewDatabase.updateNull("ownerreply", String.valueOf(product.getProductID()), index);
        goToProductPageItself();
    }
    
    private void goToProductPageItself() throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("ProductPage.fxml"));
        Stage window = (Stage)reviewPane.getScene().getWindow();
        window.setScene(new Scene(loader));
    }
    
    private Button createCommentButton() {
        Font font = Font.font("Dubai", FontWeight.BOLD, 16);
        
        Button firstButton = new Button("Comment");
        firstButton.setTextFill(Color.web("ffffff"));
        firstButton.setFont(font);
        firstButton.setStyle("-fx-background-color: #00172d");
        firstButton.setCursor(Cursor.HAND);
        firstButton.setPrefSize(100, 50);
        
        firstButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                String index = (String) firstButton.getUserData();
                addNewComment(index);
            } catch (IOException ex) {
                Logger.getLogger(ProductPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        return firstButton;
    }
    
    private Button createEditCommentButton() {
        Font font = Font.font("Dubai", FontWeight.BOLD, 16);
        
        Button firstButton = new Button("Edit");
        firstButton.setTextFill(Color.web("ffffff"));
        firstButton.setFont(font);
        firstButton.setStyle("-fx-background-color: #00172d");
        firstButton.setCursor(Cursor.HAND);
        firstButton.setPrefSize(100, 50);
        
        firstButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                String index = (String) firstButton.getUserData();
                editComment(index);
            } catch (IOException ex) {
                Logger.getLogger(ProductPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        return firstButton;
    }
    
    private Button createEditReplyButton() {
        Font font = Font.font("Dubai", FontWeight.BOLD, 16);
        
        Button firstButton = new Button("Edit");
        firstButton.setTextFill(Color.web("ffffff"));
        firstButton.setFont(font);
        firstButton.setStyle("-fx-background-color: #00172d");
        firstButton.setCursor(Cursor.HAND);
        firstButton.setPrefSize(100, 50);
        
        firstButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                String index = (String) firstButton.getUserData();
                editReply(index);
            } catch (IOException ex) {
                Logger.getLogger(ProductPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        return firstButton;
    }
    
    private Button createReplyButton() {
        Font font = Font.font("Dubai", FontWeight.BOLD, 16);
        
        Button secondButton = new Button("Reply");
        secondButton.setTextFill(Color.web("ffffff"));
        secondButton.setFont(font);
        secondButton.setStyle("-fx-background-color: #ff781f");
        secondButton.setCursor(Cursor.HAND);
        secondButton.setPrefSize(100, 50);
        
        secondButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                String index = (String) secondButton.getUserData();
                addNewReply(index);
            } catch (IOException ex) {
                Logger.getLogger(ProductPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        return secondButton;
    }
    
    private Button createDeleteButton() {
        Font font = Font.font("Dubai", FontWeight.BOLD, 16);
        
        Button secondButton = new Button("Delete");
        secondButton.setTextFill(Color.web("ffffff"));
        secondButton.setFont(font);
        secondButton.setStyle("-fx-background-color: #dc1c13");
        secondButton.setCursor(Cursor.HAND);
        secondButton.setPrefSize(100, 50);
        
        secondButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                String index = (String) secondButton.getUserData();
                deleteReview(index);
            } catch (IOException ex) {
                Logger.getLogger(ProductPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        return secondButton;
    }
    
    private GridPane prepareReviewBox() {
        GridPane reviewBox = new GridPane();
        reviewBox.setMinWidth(700);
        reviewBox.setMaxWidth(700);
        reviewBox.setPadding(new Insets(10, 5, 0, 10));
        reviewBox.getColumnConstraints().addAll(new ColumnConstraints(590));
        return reviewBox;
    }
    
    private String textDialog() {
        String input = "";
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Review / Reply");
        dialog.setHeaderText("Please write your review / reply");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            input = dialog.getEditor().getText();
        }
        return input;
    }
}
