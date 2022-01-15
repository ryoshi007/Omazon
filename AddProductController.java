package omazon;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class AddProductController implements Initializable {
    private ProductDatabase database = new ProductDatabase();
    private UserHolder holder = UserHolder.getInstance();
    private String imageURL;

    @FXML private Pane favourite;
    @FXML private Pane cart;
    @FXML private Pane LogOut;
    @FXML private Pane Settings;
    @FXML private Pane history;
    @FXML private Pane homepage;
    @FXML private ImageView imageView;
    @FXML private TextArea nameField;
    @FXML private TextField priceField;
    @FXML private TextField stockField;
    @FXML private TextArea descriptionField;
    @FXML private Button addProductButton;
    @FXML private Label invalidLabel;
    @FXML private MenuButton categoryField;
    @FXML private Pane store;
    @FXML private BorderPane imagePane;
    @FXML private Button deleteButton;
    @FXML private Button updateButton;
    @FXML private MenuButton categorySelector;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        deleteButton.setVisible(false);
        updateButton.setVisible(false);
    }

    public void updateInitialization() {
        UserHolder userHolder = UserHolder.getInstance();
        User user = userHolder.getUser();
        ProductHolder productHolder = ProductHolder.getInstance();
        Product product = productHolder.getProduct();
        this.imageURL = product.getImagePath();
        
        categoryField.setVisible(false);
        addProductButton.setVisible(false);
        deleteButton.setVisible(true);
        updateButton.setVisible(true);
        
        ImageView image = new ImageView(product.getImagePath());
        image.setFitWidth(250);
        image.setFitHeight(250);
        image.setSmooth(true);
        imagePane.setCenter(image);
        
        nameField.setText(product.getName());
        priceField.setText(String.format("%.2f", product.getPrice()));
        stockField.setText(String.valueOf(product.getStock()));
        descriptionField.setText(product.getDescription());
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
    private void addProduct() throws IOException {
        String category = new CategoryHolder().getCategoryName();
        if (!nameField.getText().isBlank() && !priceField.getText().isBlank() && !stockField.getText().isBlank() &&
                !descriptionField.getText().isBlank() && category != null && !imageURL.isBlank()) {
            if (isValidInteger(stockField.getText(), 10) && isValidDouble(priceField.getText())) {
                invalidLabel.setVisible(false);
                String productName = nameField.getText();
                double productPrice = Double.parseDouble(priceField.getText());
                int productStock = Integer.parseInt(stockField.getText());
                String description = descriptionField.getText();

                int productID = createUniqueID();
                UserHolder holder = UserHolder.getInstance();
                User user = holder.getUser();
                database.createProduct(productID, productName, productPrice, productStock, 0, category, Integer.parseInt(user.getID()), 0, description, imageURL);
                
                goToStore();
            }
        } else {
            invalidLabel.setVisible(true);
        }
    }
    
    private int createUniqueID() {
        Random random = new Random();
        int uniqueID = random.nextInt(99999999) + 1;
        String idInString = String.valueOf(uniqueID);
        while (true) {
            if (database.compareItemInRow("idproduct", idInString) == false) {
                break;
            } else {
                uniqueID = random.nextInt(99999999) + 1;
                idInString = String.valueOf(uniqueID);
            }
        }
        return uniqueID;
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
    
    private Image ResizeImage(Image img, int w, int h) {
       BufferedImage resizedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
       Graphics2D g2 = resizedImage.createGraphics();
       g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
       g2.drawImage(img, 0, 0, w, h, null);
       g2.dispose();
       return resizedImage;
    }

    @FXML
    private void uploadPicture() throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter(".IMAGE", "jpg", "gif", "png", "jpeg");
        fileChooser.addChoosableFileFilter(filter);
        int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            File folderInput = new File(selectedFile.getAbsoluteFile().toString());
            BufferedImage image = ImageIO.read(folderInput);
            Image scaledImage = ResizeImage(image, (int) imageView.getFitWidth(), (int) imageView.getFitHeight());          
            ImageView currentImage = new ImageView(selectedFile.getAbsoluteFile().toString());
            currentImage.setFitWidth(250);
            currentImage.setFitHeight(250);
            currentImage.setSmooth(true);
            imagePane.setCenter(currentImage);           
            this.imageURL = selectedFile.getAbsolutePath().toString();
        } else if (result == JFileChooser.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(null, "Image is not selected.");
        }
    }
    
    @FXML
    private void selectProductCategory(ActionEvent event) {
        MenuItem source = (MenuItem) event.getSource();
        new CategoryHolder().setCategoryName(source.getText());
        categoryField.setText(source.getText());
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
    private void goToStore() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Store.fxml"));
        AnchorPane anchorPane = loader.load();
        StoreController controller = loader.getController();
        controller.displaOwnStore();
        Stage window = (Stage)store.getScene().getWindow();
        window.setScene(new Scene(anchorPane));
    }

    @FXML
    private void deleteProduct() throws IOException {
        ProductHolder productHolder = ProductHolder.getInstance();
        Product product = productHolder.getProduct();
        
        String productID = product.getProductID() + ",";
        CustomerDatabase customerDatabase = new CustomerDatabase();
        ArrayList<String> customerIDList = customerDatabase.retrieveSpecificColumn("idCustomer");
        for (String customer: customerIDList) {
            int customerID = Integer.parseInt(customer);
            String favourite = customerDatabase.retrieveUserData(customerID, "Favourite");
            if (favourite == null) {}
            else if (favourite.contains(product.getProductID() + ",")) {
                favourite = favourite.replace(productID, "");
                customerDatabase.update("favourite", favourite, customer);
            }
            
            String order = customerDatabase.retrieveUserData(customerID, "OrderHistory");
            if (order == null) {}
            else if (order.contains(product.getProductID() + ",")) {
                order = order.replace(productID, "");
                customerDatabase.update("OrderHistory", order, customer);
            }
            
            String cartItem = customerDatabase.retrieveUserData(customerID, "CartItem");
            if (cartItem == null) {}
            else if (cartItem.contains(product.getProductID() + ",")) {
                String[] item = cartItem.split(";");
                String newItem = "";
                for (String productInfo: item) {
                    if (productInfo.contains(product.getProductID() + ",")) {
                        productInfo = "";
                    } else {
                        newItem += product + ";";
                    }
                }
                customerDatabase.update("CartItem", newItem, customer);
            }
        }
        ProductDatabase database = new ProductDatabase();
        database.delete(String.valueOf(product.getProductID()));
        
        goToStore();
    }

    @FXML
    private void updateProduct() throws IOException {
        ProductHolder productHolder = ProductHolder.getInstance();
        Product product = productHolder.getProduct();
        
        String productName = nameField.getText();
        double productPrice = Double.parseDouble(priceField.getText());
        int productStock = Integer.parseInt(stockField.getText());
        String description = descriptionField.getText();
        int productID = product.getProductID();
        
        product.setName(productName);
        product.setPrice(productPrice);
        product.setStock(productStock);
        product.setDescription(description);
        product.setImagePath(imageURL);
        imageURL = imageURL.replace("\\", "\\\\");
        
        database.update("productName", productName, String.valueOf(productID));
        database.update("price", String.valueOf(productPrice), String.valueOf(productID));
        database.update("stock", String.valueOf(productStock), String.valueOf(productID));
        database.update("description", description, String.valueOf(productID));
        database.update("imagePath", imageURL, String.valueOf(productID));
        
        goToStore();
    }
    
}
