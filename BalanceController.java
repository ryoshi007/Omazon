package omazon;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class BalanceController implements Initializable {
    private String selector;
    private static String fromWhere;
    private static double payPrice;
    private static boolean enoughMoney;
    
    private UserHolder holder = UserHolder.getInstance();
    private User user = holder.getUser();

    @FXML private Label price;
    @FXML private Button proceedButton;
    @FXML private Button backButton;
    @FXML private Label balance;
    @FXML private Label warningText;
    @FXML private Pane onlineBankPane;
    @FXML private Pane ewalletPane;
    @FXML private MenuButton eWalletOption;
    @FXML private Pane cardPane;
    @FXML private RadioButton eWalletButton;
    @FXML private RadioButton onlineBankButton;
    @FXML private RadioButton cardButton;
    @FXML private StackPane paymentPane;
    @FXML private MenuButton bankOption;
    @FXML private TextField ccvCodeField;
    @FXML private TextField nameField;
    @FXML private TextField cardNumberField;
    @FXML private TextField bankAmountField;
    @FXML private TextField eWalletAmountField;
    @FXML private TextField cardAmountField;
    @FXML private TextField validDateField;
    @FXML private Label invalidLabel;
    @FXML private Label walletLabel;
    @FXML private Label priceLabel;
    @FXML private Label chooseText;
    private ToggleGroup group;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        warningText.setVisible(false);
        balance.setText(String.format("RM %.2f", user.getBalance()));
        
        group = new ToggleGroup();
        eWalletButton.setToggleGroup(group);
        eWalletButton.setFocusTraversable(false);
        onlineBankButton.setToggleGroup(group);
        onlineBankButton.setFocusTraversable(false);
        cardButton.setToggleGroup(group);
        cardButton.setFocusTraversable(false);
        paymentPane.setVisible(false);
        
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
          public void changed(ObservableValue<? extends Toggle> ov,
              Toggle old_toggle, Toggle new_toggle) {
            if (group.getSelectedToggle() == eWalletButton) {
                paymentPane.setVisible(true);
                displayEwalletPane();
            } else if (group.getSelectedToggle() == onlineBankButton) {
                paymentPane.setVisible(true);
                displayOnlineBankPane();
            } else if (group.getSelectedToggle() == cardButton) {
                paymentPane.setVisible(true);
                displayCardPane();
            }
          }
        });
    }

    private void displayCardPane() {
        onlineBankPane.setVisible(false);
        ewalletPane.setVisible(false);
        cardPane.toFront();
        cardPane.setVisible(true);
        selector = "cardPane";
    }

    private void displayOnlineBankPane() {
        cardPane.setVisible(false);
        ewalletPane.setVisible(false);
        onlineBankPane.toFront();
        onlineBankPane.setVisible(true);
        selector = "onlineBankPane";
    }

    private void displayEwalletPane() {
        cardPane.setVisible(false);
        onlineBankPane.setVisible(false);
        ewalletPane.toFront();
        ewalletPane.setVisible(true);
        selector = "eWalletPane";
    }
    
    // Tell back button to go back to where
    public void setFromWhere(String page) {
        this.fromWhere = page;
        if (page.equals("Homepage")) {
            price.setVisible(false);
            walletLabel.setText("Top-Up Wallet");
            priceLabel.setVisible(false);
        } else if (page.equals("CartPage")) {
            price.setText(String.format("RM %.2f", payPrice));
        }
    }

    @FXML
    private void selectBank(ActionEvent event) {
        MenuItem source = (MenuItem) event.getSource();
        bankOption.setText(source.getText());
    }

    @FXML
    private void selectEWallet(ActionEvent event) {
        MenuItem source = (MenuItem) event.getSource();
        eWalletOption.setText(source.getText());
    }
    
    public void setPrice(double payPrice) {
        this.payPrice = payPrice;
        price.setText(String.format("RM %.2f", payPrice));
        checkEnoughBalance();
    }
    
    private void checkEnoughBalance() {
        if (user.getBalance() >= this.payPrice) {
            chooseText.setVisible(false);
            eWalletButton.setVisible(false);
            onlineBankButton.setVisible(false);
            cardButton.setVisible(false);
            enoughMoney = true;          
        } else {
            enoughMoney = false;
            double minBalance = this.payPrice - user.getBalance();
            String balanceInString = String.format("Insufficient Balance! Please top up at least RM %.2f", minBalance);
            warningText.setText(balanceInString);
            warningText.setVisible(true);
        }
    }
    
    private boolean isValidDouble(String s) {
        boolean isDouble = true;
        double num = 0;
        try {
            num = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            isDouble = false;
        }
        if (num == 0) return false;
        if (Double.parseDouble(s) % 1 < 0) {
            String[] parts = s.split(".");
            System.out.println(parts[1]);
            if (parts[1].length() > 2) return false;
        }
        return isDouble;
    }
    
    private boolean isValidInteger(String s, int radix) {
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
    private void proceedPayment() throws IOException {
        invalidLabel.setVisible(false);
        invalidLabel.setText("Please fill up all information and make sure no mistake!");
        if (enoughMoney) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CheckoutPage.fxml"));
            AnchorPane anchorPane = loader.load();
            CheckoutPageController controller = loader.getController();
            controller.setPrice(payPrice);
            Stage window = (Stage)proceedButton.getScene().getWindow();
            window.setScene(new Scene(anchorPane)); 
        } else {
            if (eWalletButton.isSelected() || onlineBankButton.isSelected() || cardButton.isSelected()) {
                if (selector.equals("eWalletPane")) {
                    if (isValidDouble(eWalletAmountField.getText())) {
                        topUpBalance(eWalletAmountField.getText());
                    } else {
                        invalidLabel.setVisible(true);
                    }
                } else if (selector.equals("onlineBankPane")) {
                    if (isValidDouble(bankAmountField.getText())) {
                        topUpBalance(bankAmountField.getText());
                    } else {
                        invalidLabel.setVisible(true);
                    }
                } else if (selector.equals("cardPane")) {
                    if (isValidDouble(cardAmountField.getText()) && isValidInteger(ccvCodeField.getText(), 10)
                            && isValidInteger(cardNumberField.getText(), 10) && !nameField.getText().isBlank()
                            && !validDateField.getText().isBlank()) {
                        topUpBalance(cardAmountField.getText());
                    } else {
                        invalidLabel.setVisible(true);
                    }
                } else {
                    invalidLabel.setVisible(true);
                }
            } else {
                invalidLabel.setText("Please select a payment method!");
                invalidLabel.setVisible(true);
            }
        }
    }

    @FXML
    private void backButton() throws IOException {
        if (fromWhere.equals("Homepage")) {
            Parent loader = FXMLLoader.load(getClass().getResource("Homepage.fxml"));
            Stage window = (Stage)backButton.getScene().getWindow();
            window.setScene(new Scene(loader));
        } else if (fromWhere.equals("CartPage")) {
            Parent loader = FXMLLoader.load(getClass().getResource("CartPage.fxml"));
            Stage window = (Stage)backButton.getScene().getWindow();
            window.setScene(new Scene(loader));
        }
    }
    
    private void topUpBalance(String amount) throws IOException {
        CustomerDatabase database = new CustomerDatabase();
        String originalBalance = database.retrieveUserData(Integer.parseInt(user.getID()), "Balance");
        double newBalance = Double.parseDouble(amount) + Double.parseDouble(originalBalance);
        database.update("Balance", String.valueOf(newBalance), user.getID());
        user.setBalance(newBalance);
        
        if (fromWhere.equals("Homepage")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Balance.fxml"));
            AnchorPane anchorPane = loader.load();
            BalanceController controller = loader.getController();
            controller.setFromWhere("Homepage");
            Stage window = (Stage)proceedButton.getScene().getWindow();
            window.setScene(new Scene(anchorPane)); 
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Balance.fxml"));
            AnchorPane anchorPane = loader.load();
            BalanceController controller = loader.getController();
            controller.setPrice(payPrice);
            Stage window = (Stage)proceedButton.getScene().getWindow();
            window.setScene(new Scene(anchorPane));      
        }
    }
}
