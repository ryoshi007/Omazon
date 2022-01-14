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
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class TransactionController implements Initializable {
    private ProductDatabase productDatabase = new ProductDatabase();
    private CustomerDatabase customerDatabase = new CustomerDatabase();
    private ReviewDatabase reviewDatabase = new ReviewDatabase();
    
    private UserHolder userHolder = UserHolder.getInstance();
    private User user = userHolder.getUser();

    @FXML private PieChart pieChart;
    @FXML private Label backButton;
    @FXML private FlowPane transactionPane;
    @FXML private ListView<Product> productPane;
    @FXML private Button selectButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList<Product> productList = productDatabase.retrieveProduct(user.getID(), "idOwner");
        ObservableList<Product> list = FXCollections.observableArrayList(productList);
        productPane.setItems(list);
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
        ArrayList<Notification> transactionList = reviewDatabase.obtainNotification(user.getID());
        
        
        
        
    }
    
}
