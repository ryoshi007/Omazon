package omazon;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class TransactionController implements Initializable {
    private ProductDatabase productDatabase = new ProductDatabase();
    private CustomerDatabase customerDatabase = new CustomerDatabase();
    private ReviewDatabase reviewDatabase = new ReviewDatabase();
    private double profit = 0;
    
    private UserHolder userHolder = UserHolder.getInstance();
    private User user = userHolder.getUser();

    @FXML private Label backButton;
    @FXML private FlowPane transactionPane;
    @FXML private ListView<Product> productPane;
    @FXML private Button selectButton;
    @FXML private Label profitText;
    @FXML private Button viewProfitButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList<Product> productList = productDatabase.retrieveProduct(user.getID(), "idOwner");
        ObservableList<Product> list = FXCollections.observableArrayList(productList);
        productPane.setItems(list);
        profitText.setVisible(false);
        
        if (productList.size() == 0) {
            selectButton.setVisible(false);
            viewProfitButton.setVisible(false);
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

    @FXML
    private void selectProduct() {
        Product selectedProduct;
        ObservableList<Product> list = productPane.getSelectionModel().getSelectedItems();
        selectedProduct = list.get(0);
        int productID = selectedProduct.getProductID();
        ArrayList<Notification> transactionList = reviewDatabase.obtainTransaction(user.getID(), String.valueOf(productID));
        initializeTransactionList(transactionList);
    }
    
    private void initializeTransactionList(ArrayList<Notification> transactionList) {
        transactionPane.getChildren().clear();
        profit = 0;
        for (int i = 0; i < transactionList.size(); i++) {
            Notification current = transactionList.get(i);
            String buyerName = "Buyer: " + customerDatabase.retrieveUserData(Integer.parseInt(current.getCustomerID()), "Username");
            String productName = "Product: " + productDatabase.retrieveSpecificProductInfo(current.getProductID(), "productName");
            String orderedAmount = "Amount: " + current.getAmount();
            String orderedTime = "Order Time: " + current.getCurrentDate();
            String profit = String.format("+RM %.2f", current.getPayAmount());
            
            this.profit += current.getPayAmount();
            
            GridPane pane = new GridPane();
            pane.setMinWidth(330);
            pane.setMaxWidth(330);
            pane.setPrefHeight(110);
            
            int num = i + 1;
            Label orderLabel = createLabel("Order " + num + ":");
            Label buyerNameLabel = createLabel(buyerName);
            Label productNameLabel = createLabel(productName);
            Label orderedAmountLabel = createLabel(orderedAmount);
            Label orderedTimeLabel = createLabel(orderedTime);
            Label profitLabel = createLabel(profit);
            Label empty = createLabel("");           
            
            pane.add(orderLabel, 0, 0);
            pane.add(buyerNameLabel, 0, 1);
            pane.add(productNameLabel, 0, 3);
            pane.add(orderedAmountLabel, 0, 4);
            pane.add(orderedTimeLabel, 0, 5);
            pane.add(profitLabel, 0, 6);
            pane.add(empty, 0, 7);
            
            Line straightLine = new Line();
            straightLine.setStartX(-100);
            straightLine.setEndX(262);
            straightLine.setStroke(Color.GRAY);
            transactionPane.getChildren().add(pane);
            transactionPane.getChildren().add(straightLine);
        }
        profitText.setVisible(true);
        profitText.setText(String.format("Profit - RM %.2f", profit));
    }
        
    private Label createLabel(String input) {
        Label label = new Label(input);
        label.setStyle("-fx-font-family: Courier; -fx-font-size: 11pt");
        return label;
    }

    @FXML
    private void viewChart() {
        ArrayList<Product> productList = productDatabase.retrieveProduct(user.getID(), "idOwner");
        ArrayList<Double> profitList = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            double profit = reviewDatabase.calculateSum(productList.get(i).getProductID());
            profitList.add(profit);
        }
        
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Total Profit Chart");
        barChart.setLegendVisible(false);
        barChart.setCategoryGap(100);
        
        XYChart.Series profitData = new XYChart.Series();
        for (int i = 0; i < productList.size(); i++) {
            profitData.getData().add(new XYChart.Data(productList.get(i).getName(), profitList.get(i)));
        }
        
        barChart.getData().add(profitData); 
        Stage stage = new Stage();
        stage.setTitle("Profit Chart");
        Scene view = new Scene(barChart, 800, 600);
        stage.setScene(view);
        stage.show();
    }
    
}
