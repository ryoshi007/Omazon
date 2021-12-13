package Interface;

import customer.Customer;
import customer.CustomerDatabase;
import customer.CustomerList;
import customer.CustomerManagement;
import productcomponenet.Review;
import productcomponenet.Description;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import product.Product;
import product.ProductManagement;
import productcomponenet.Transaction;

public class ProductInterface {
    private Scanner scanner;
    private Customer customer;
    private Product product;
    private ProductManagement productManagement;
    private CustomerManagement customerManagement;
    
    public static final String TEXT_GREEN = "\u001B[32m";
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_YELLOW = "\u001B[32m";
    public static final String TEXT_RED = "\u001B[31m";
    
    public ProductInterface(Scanner scanner, Customer customer, Product product, ProductManagement productManagement) {
        this.scanner = scanner;
        this.customer = customer;
        this.product = product;
        this.productManagement = productManagement;
        this.customerManagement = new CustomerManagement();
        clearScreen();
        start();
    }
    
    public void start() {
        char bulletSymbol = '\u2023';
        
        System.out.println("---------------------------- Product Details ---------------------------");
        System.out.println("");
        System.out.println(bulletSymbol + " Product name: " + product.getName());
        System.out.printf("%s Price: RM%.2f\n", bulletSymbol, this.product.getPrice());
        System.out.println(bulletSymbol + " Stock: " + this.product.getStock());
        System.out.println(bulletSymbol + " Seller: " + this.product.getOwner().getUsername());
        System.out.println(bulletSymbol + " Sales: " + this.product.getSalesVolume());
        System.out.println(bulletSymbol + " Product ratings: " + this.product.getRating());
        System.out.println(bulletSymbol + " Description:");
        showDescriptions();
        
        System.out.println(bulletSymbol + " Product reviews:");
        System.out.println("-------------------------------------------------------------------------");
        showReviews();
        
        System.out.println("");
        System.out.println("1. Add to favourite");
        System.out.println("2. Add to cart");
        System.out.println("3. Buy now");
        System.out.println("4. Add review");
        System.out.println("5. Back to homepage");
        
        //Should only be available to seller
        if (this.customer.getUsername().equals(product.getOwner().getUsername())) {
            System.out.println("6. Add description");
            System.out.println("7. Change product name");
            System.out.println("8. Change product price");
            System.out.println("9. Change stock amount");
            System.out.println("10. Delete product");
            System.out.println("11. Delete description");
            System.out.println("12. Delete review");
            System.out.println("");
            System.out.print("What to do next? (1-12): " + TEXT_GREEN);
        } else {
            System.out.println("");
            System.out.print("What to do next? (1-5): " + TEXT_GREEN);
        }
        
        String command = scanner.nextLine();
        System.out.print(TEXT_RESET);
        operation(command);
        
        
    }
    
    private void showReviews() {
        ArrayList<Review> reviewList = this.product.getReviews();
        if (!reviewList.isEmpty()) {      
            for (Review review: reviewList) {
                System.out.println(review);
                System.out.println("-------------------------------------------------------------------------");
            }
        }
    }
    
    private void showDescriptions() {
        ArrayList<Description> descriptionList = this.product.getDescription();
        if (!descriptionList.isEmpty()) {
            for (Description description: descriptionList) {
                System.out.println(description);
            }
        }
    }
    
    private void operation(String command) {       
        if (command.equals("1")) {
            addToFavourite();
        } else if (command.equals("2")) {
            addToCart();
        } else if (command.equals("3")) {
            buyNow();
        } else if (command.equals("4")) {
            addReview();
        } else if (command.equals("5")) {
            homepage();
        } else if (command.equals("6")) {
            addDescription();
        } else if (command.equals("7")) {
            changeName();
        } else if (command.equals("8")) {
            changePrice();
        } else if (command.equals("9")) {
            changeStockAmount();
        } else if (command.equals("10")) {
            deleteProduct();
        } else if (command.equals("11")) {
            deleteDescription();
        } else if (command.equals("12")) {
            deleteReview();
        } else {
            homepage();
        }
        
    }
    
    private void addToFavourite() {
        boolean existInFavourite = false;
        for (Product favProduct: this.customer.getFavouriteList()) {
           if (favProduct.getName().equals(product.getName())) {
               existInFavourite = true;
           }
        }

        if (!existInFavourite) {
            this.customer.addFavourite(product);
            CustomerManagement customerManagement = new CustomerManagement();
            String input = "Favourite;" + product.getID();
            customerManagement.addContentToCustomerFile(input, this.customer.getID());
            System.out.println();
            System.out.println(TEXT_GREEN + "The product is added to your favourite list!" + TEXT_RESET);
        } else {
            System.out.println(TEXT_RED + "The product is already in your favourite list!" + TEXT_RESET);
        }
        scanner.nextLine();
        goBackToProductPage(); 
    }
    
    private void addToCart() {        
        if (this.customer.getUsername().equals(product.getOwner().getUsername())) {
            System.out.println(TEXT_RED + "You cannot buy your own item!" + TEXT_RESET);
            goBackToProductPage();
        } else {
            purchasePage();
            System.out.println(TEXT_GREEN + "The product is added to your cart!" + TEXT_RESET);
            System.out.println(TEXT_RED + "*Please pay the items before quitting the app*" + TEXT_RESET);
            scanner.nextLine();
            goBackToHomepage();
        }
    }
    
    private void buyNow() {
        if (this.customer.getUsername().equals(product.getOwner().getUsername())) {
            System.out.println(TEXT_RED + "You cannot buy your own item!" + TEXT_RESET);
            goBackToProductPage();
        } else {
            purchasePage();
            new TransactionInterface(customer);
        }
    }
    
    private void purchasePage() {
        System.out.println();
        System.out.println("---------------------------------- Purchase -----------------------------------");
        boolean amountSufficient = false;
        int amount;
        do {
            System.out.print("Enter the amount: ");
            amount = scanner.nextInt();
            System.out.println();
            if (amount > this.product.getStock()) {
                System.out.println("The requested amount is exceed the stock! Please try again!"); 
            } else {
                amountSufficient = true;
            }
        } while (!amountSufficient);
        Transaction newTransaction = new Transaction(this.product, amount);
        this.customer.addTransaction(newTransaction);
    }
    
    private void addReview() {
        boolean purchasedProduct = false;
        //comparing name, probably better with ID -> Create a class that store name and id
        for (String orderedProduct: this.customer.getOrderHistory()) {
            if (orderedProduct.equals(this.product.getName())) {
                purchasedProduct = true;
            }
        }

        if (purchasedProduct) {
            String newReview = this.product.addNewReview(this.customer.getUsername());
            this.productManagement.addContentToProductFile(newReview, this.product.getID());
            new ProductInterface(scanner, customer, this.product, this.productManagement);
        } else {
            System.out.println(TEXT_RED + "You have to purchase the product to review it!" + TEXT_RESET);
            goBackToProductPage();
        }
    }
    
    private void addDescription() {
        String newDescription = this.product.addNewDescription();
        this.productManagement.addContentToProductFile(newDescription, this.product.getID());
        new ProductInterface(scanner, customer, this.product, this.productManagement);  
    }
    
    private void homepage() {
        new Homepage(this.scanner, customer);
    }
    
    private void changeName() {
        String oldName = this.product.changeProductName();
        String oldInput = "Name;" + oldName;
        String newInput = "Name;" + this.product.getName();
        this.productManagement.changeDataInProductFile(oldInput, newInput, this.product.getID());
        new ProductInterface(scanner, customer, this.product, this.productManagement);
    }
    
    private void changePrice() {
        double oldPrice = this.product.changeProductPrice();
        String oldPriceInString, newPriceInString;
        if (oldPrice % 1 == 0) {
            oldPriceInString = String.format("%.0f", oldPrice);
        } else {
            oldPriceInString = String.valueOf(oldPrice);
        }
        
        if (this.product.getPrice() % 1 == 0) {
            newPriceInString = String.format("%.0f", this.product.getPrice());
        } else {
            newPriceInString = String.valueOf(this.product.getPrice());
        }
        
        String oldInput = "Price;" + oldPriceInString;
        String newInput = "Price;" + newPriceInString;
        this.productManagement.changeDataInProductFile(oldInput, newInput, this.product.getID());
        new ProductInterface(scanner, customer, this.product, this.productManagement);
    }
    
    private void changeStockAmount() {
        int oldStock = this.product.changeProductStockAmount();
        String oldInput = "Stock;" + oldStock;
        String newInput = "Stock;" + this.product.getStock();
        this.productManagement.changeDataInProductFile(oldInput, newInput, this.product.getID());
        new ProductInterface(scanner, customer, this.product, this.productManagement); 
    }
    
    //Situation where customer refer to product in favourite
    private void deleteProduct() {
        System.out.print("You sure want to delete this product? (" + TEXT_RED + "Yes" + TEXT_RESET + "/" + TEXT_GREEN + "No" + TEXT_RESET + "): ");
        String respond = this.scanner.nextLine();
        if (respond.equals("Yes")) {
            CustomerList customerList = new CustomerDatabase().loadFile();
            for (int i = 0; i < customerList.getSize(); i++) {
                Customer customer = customerList.getCustomer(i);
                
                if (!customer.getFavouriteList().isEmpty()) {
                    for (Product favProduct: customer.getFavouriteList()) {
                        if (favProduct.equals(this.product)) {
                            customerManagement.deleteContentFromCustomerFile("Favourite;" + this.product.getID(), customer.getID());
                        }
                    }
                }               
            }
            
            this.productManagement.deleteProduct(this.product.getID());
            System.out.println("Deleting the product...");
            goBackToHomepage();
        } else {
            System.out.println("The product will not be deleted");
            goBackToProductPage();
        }
        
    }
    
    private void deleteDescription() {
        clearScreen();
        ArrayList<Description> description = this.product.getDescription();
        for (int i = 0; i < description.size(); i++) {
            System.out.println((i+1) + ". " + description.get(i).getHeadline() + ": " + description.get(i).getDescription());
        }
        System.out.println();
        System.out.print("Which description that you want to delete (1 - " + description.size() + "): ");
        int input = scanner.nextInt();
        String deleteInput = description.get(input - 1).createDescriptionInput();
        this.product.removeDescription(input - 1);
        this.productManagement.deleteContentFromProductFile(deleteInput, this.product.getID());
        scanner.nextLine();
        new ProductInterface(scanner, customer, this.product, this.productManagement);
    }
    
    private void deleteReview() {
        clearScreen();
        ArrayList<Review> reviews = this.product.getReviews();
        for (int i = 0; i < reviews.size(); i++) {
            System.out.println((i+1) + ". " + reviews.get(i).getUsername() + ": " + reviews.get(i).getReview());
        }
        System.out.println();
        System.out.print("Which review that you want to delete (1 - " + reviews.size() + "): ");
        int input = scanner.nextInt();
        String deleteInput = reviews.get(input - 1).createReviewInput();
        this.product.removeReview(input - 1);
        this.productManagement.deleteContentFromProductFile(deleteInput, this.product.getID());
        scanner.nextLine();
        new ProductInterface(scanner, customer, this.product, this.productManagement);
    }
    
    private void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception E) {
            System.out.println("Sorry there is an error");
        }
    }
    
    public void goBackToProductPage() {
        try {
            System.out.println("Going back to product page...");
            Thread.sleep(1500);
            scanner.nextLine();
            new ProductInterface(scanner, customer, this.product, this.productManagement);
        } catch (InterruptedException ex) {
            Logger.getLogger(ProductInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void goBackToHomepage() {
        try {
            System.out.println("Going back to homepage...");
            Thread.sleep(1500);
            new Homepage(this.scanner, this.customer);
        } catch (InterruptedException ex) {
            Logger.getLogger(ProductInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
