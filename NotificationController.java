package omazon;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class NotificationController implements Initializable {
    ReviewDatabase reviewDatabase = new ReviewDatabase();
    CustomerDatabase customerDatabase = new CustomerDatabase();
    ProductDatabase productDatabase = new ProductDatabase();
    
    UserHolder holder = UserHolder.getInstance();
    User user = holder.getUser();

    @FXML private Label backButton;
    @FXML private FlowPane notificationPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList<Notification> notificationList = reviewDatabase.obtainNotification(user.getID());
        if (notificationList.size() == 0) {
        } else {
            createNotificationPane(notificationList);
        }
    }

    private void createNotificationPane(ArrayList<Notification> notificationList) {
        for (int i = 0; i < notificationList.size(); i++) {
            Notification current = notificationList.get(i);
            String buyerName = "Buyer: " + customerDatabase.retrieveUserData(Integer.parseInt(current.getCustomerID()), "Username");
            String address = "Address: " + customerDatabase.retrieveUserData(Integer.parseInt(current.getCustomerID()), "Address");
            String productName = "Product: " + productDatabase.retrieveSpecificProductInfo(current.getProductID(), "productName");
            String orderedAmount = "Amount: " + current.getAmount();
            
            GridPane pane = new GridPane();
            pane.setMinWidth(550);
            pane.setMaxWidth(550);
            pane.setPrefHeight(110);
            pane.setPadding(new Insets(10, 5, 0, 10));
            pane.getColumnConstraints().addAll(new ColumnConstraints(450));
            
            int num = i + 1;
            Label orderLabel = createLabel("Order " + num + ":");
            Label buyerNameLabel = createLabel(buyerName);
            Label addressLabel = createLabel(address);
            Label productNameLabel = createLabel(productName);
            Label orderedAmountLabel = createLabel(orderedAmount);
            Label empty = createLabel("");
            
            Button fullfillButton = createFullfillButton();
            fullfillButton.setUserData(current.getIndex());
            
            pane.add(orderLabel, 0, 0);
            pane.add(buyerNameLabel, 0, 1);
            pane.add(addressLabel, 0, 2);
            pane.add(productNameLabel, 0, 3);
            pane.add(orderedAmountLabel, 0, 4);
            pane.add(fullfillButton, 1, 0);
            pane.add(empty, 0, 5);
            
            Line straightLine = new Line();
            straightLine.setStartX(-100);
            straightLine.setEndX(468);
            straightLine.setStroke(Color.GRAY);
            notificationPane.getChildren().add(pane);
            notificationPane.getChildren().add(straightLine);
        }
    }

    @FXML
    private void goBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Store.fxml"));
        AnchorPane anchorPane = loader.load();
        StoreController controller = loader.getController();
        controller.displaOwnStore();
        Stage window = (Stage)backButton.getScene().getWindow();
        window.setScene(new Scene(anchorPane));
    }
    
    private Label createLabel(String input) {
        Label label = new Label(input);
        label.setStyle("-fx-font-family: Courier; -fx-font-size: 11pt");
        return label;
    }
    
    private void fullfillOrder(int index) {
        System.out.println(index);
    }
    
    private Button createFullfillButton() {
        Font font = Font.font("Dubai", FontWeight.BOLD, 11);
        
        Button fullfillButton = new Button("Fullfill");
        fullfillButton.setTextFill(Color.web("ffffff"));
        fullfillButton.setFont(font);
        fullfillButton.setStyle("-fx-background-color: #378805");
        fullfillButton.setCursor(Cursor.HAND);
        fullfillButton.setPrefSize(100, 50);
        
        fullfillButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            int index = (int) fullfillButton.getUserData();
            fullfillOrder(index);
        });
        return fullfillButton;
    }
    
}
