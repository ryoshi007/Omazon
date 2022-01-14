package omazon;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CheckoutPageController implements Initializable {
    private UserHolder holder = UserHolder.getInstance();
    private User user = holder.getUser();
    private double payPrice;

    @FXML private TextField nameField;
    @FXML private TextField telephoneField;
    @FXML private TextArea addressField;
    @FXML private PasswordField paymentPasswordField;
    @FXML private RadioButton posLaju;
    @FXML private RadioButton dhl;
    @FXML private RadioButton gde;
    @FXML private RadioButton cityLink;
    @FXML private RadioButton jtExpress;
    @FXML private Button backButton;
    @FXML private Button confirmButton;
    private ToggleGroup group;
    @FXML private Label invalidText;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        group = new ToggleGroup();
        posLaju.setToggleGroup(group);
        dhl.setToggleGroup(group);
        gde.setToggleGroup(group);
        cityLink.setToggleGroup(group);
        jtExpress.setToggleGroup(group);
        
        addressField.setText(user.getAddress());
    }    

    @FXML
    private void proceedPayment() throws IOException {
        invalidText.setVisible(false);
        if (!nameField.getText().isBlank() && !telephoneField.getText().isBlank()
                && !addressField.getText().isBlank() && (user.getPaymentPassword().equals(paymentPasswordField.getText()))) {
            paymentSuccessful();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setContentText("Your order has been confirmed");
            alert.show();
            
            holder.setFavouriteOrderPane("Order");
            Parent loader = FXMLLoader.load(getClass().getResource("Favourite.fxml"));
            Stage window = (Stage)confirmButton.getScene().getWindow();
            window.setScene(new Scene(loader)); 
        } else {
            invalidText.setVisible(true);
        }
    }
    
    public void setPrice(double price) {
        this.payPrice = price;
    }

    @FXML
    private void backToBalancePage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Balance.fxml"));
        AnchorPane anchorPane = loader.load();
        BalanceController controller = loader.getController();
        controller.setFromWhere("CartPage");
        controller.setPrice(this.payPrice);
        Stage window = (Stage)backButton.getScene().getWindow();
        window.setScene(new Scene(anchorPane)); 
    }
    
    private void paymentSuccessful() {
        CustomerDatabase customerDatabase = new CustomerDatabase();
        ProductDatabase productDatabase = new ProductDatabase();
        ReviewDatabase reviewDatabase = new ReviewDatabase();
        
        String order = customerDatabase.retrieveUserData(Integer.parseInt(user.getID()), "CartItem");
        String[] orderInfo = order.split(";");
        for (String productInfo: orderInfo) {
            String[] product = productInfo.split(",");
            String productID = product[0];
            String purchaseAmount = product[1];
            
            String sales = productDatabase.retrieveSpecificProductInfo(productID, "sales");
            int latestSales = Integer.parseInt(sales) + Integer.parseInt(purchaseAmount);
            productDatabase.update("sales", String.valueOf(latestSales), productID);
            
            String stock = productDatabase.retrieveSpecificProductInfo(productID, "stock");
            int latestStock = Integer.parseInt(stock) - Integer.parseInt(purchaseAmount);
            productDatabase.update("stock", String.valueOf(latestStock), productID);
            String productOwnerID = productDatabase.retrieveSpecificProductInfo(productID, "owner");
            
            String orderHistory = customerDatabase.retrieveUserData(Integer.parseInt(user.getID()), "OrderHistory");
            if (orderHistory == null) {
                String latestHistory = productID + ",";
                user.setOrder(latestHistory);
                customerDatabase.update("OrderHistory", latestHistory, user.getID());
                reviewDatabase.createData(productID, productOwnerID, user.getID());
            } else if (!orderHistory.contains(productID)) {
                String latestHistory = orderHistory + productID + ",";
                user.setOrder(latestHistory);
                customerDatabase.update("OrderHistory", latestHistory, user.getID());
                reviewDatabase.createData(productID, productOwnerID, user.getID());
            }
        }
              
        customerDatabase.update("CartItem", "", user.getID());
        String balance = customerDatabase.retrieveUserData(Integer.parseInt(user.getID()), "Balance");
        double latestBalance = Double.valueOf(balance) - payPrice;
        latestBalance = (double) Math.round(latestBalance * 100) / 100;
        user.setBalance(latestBalance);
        customerDatabase.update("Balance", String.valueOf(latestBalance), user.getID());
    }
    
}
