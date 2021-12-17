package Interface;

import customer.Customer;
import customer.CustomerManagement;
import product.Product;
import product.ProductManagement;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import product.ProductDatabase;
import productcomponenet.Category;
import productcomponenet.CategoryList;

public class Homepage {
    private Scanner scanner;
    private ProductManagement productManagement;
    private CustomerManagement customerManagement;
    private ArrayList<Product> topThree;
    private Customer customer;
    
    public static final String TEXT_YELLOW = "\u001B[32m";
    public static final String TEXT_RESET = "\u001B[0m";
        
    public Homepage(Scanner scanner, Customer customer) {
        this.scanner = scanner;
        this.customer = customer;
        this.productManagement = new ProductDatabase().loadFile();
        this.customerManagement = new CustomerManagement();
        this.topThree = productManagement.topThreeProduct();
        clearScreen();
        start();
    }
    
    public void start() {
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ Welcome to Omazon App ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~");
        System.out.println("-------------------------------------------------------------------------");
        
        System.out.println("User: " + customer.getUsername());
        System.out.printf("%s%.2f\n", "Current Balance: RM ", customer.getBalance());
        
        System.out.println();
        System.out.println("Best-Selling Products");
        System.out.println("No. 1 - " + this.topThree.get(0).getName());
        System.out.println("No. 2 - " + this.topThree.get(1).getName());
        System.out.println("No. 3 - " + this.topThree.get(2).getName());
        
        System.out.println();
        System.out.println("A. Search");
        System.out.println("B. Go to cart");
        System.out.println("C. Go to category");
        System.out.println("D. User Settings");
        System.out.println("E. Manage Store");
        System.out.println("F. Exit app");       
        System.out.println();
        System.out.println();
        String command = askAndCheckInput("What to do next? (1-3) / (A-F): ", "[a-fA-F1-3]{1}", false);
        operate(command.toLowerCase());
    }
    
    private String askAndCheckInput(String askStatement, String checkRegex, boolean containSymbol) {
        do {
            System.out.print(askStatement);
            String command = scanner.nextLine();
            if (!containSymbol) {
                command = command.replaceAll("[^a-zA-Z0-9]", "");
            }
            if (Pattern.matches(checkRegex, command)) {
                return command;
            } else {
                System.out.println("Wrong input! Please try again!\n");
            }
        } while(true);
    }
    
    private void operate(String command) {
        switch (command) {
            case "1":
                new ProductInterface(this.scanner, customer, this.topThree.get(0), this.productManagement);
                break;
            case "2":
                new ProductInterface(this.scanner, customer, this.topThree.get(1), this.productManagement);
                break;
            case "3":
                new ProductInterface(this.scanner, customer, this.topThree.get(2), this.productManagement);
                break;
            case "a":
                search();
                break;
            case "b":
                viewCart();
                break;
            case "c":
                category();
                break;
            case "d":
                userSettings();
                break;
            case "e":
                sellerStore(customer);
                break;
            case "f":
                exit();
                break;
        }
    }
    
    private void search() {
        clearScreen();
        System.out.print("Type 'A' to go back to homepage\n\nPlease enter the word: " + TEXT_YELLOW);
        String searchItem = scanner.nextLine();
        System.out.print(TEXT_RESET);
        
        if (searchItem.equalsIgnoreCase("a")) {
            new Homepage(this.scanner, this.customer);
        } else {
            ArrayList<Product> relatedProduct = this.productManagement.searchProduct(searchItem);
            ArrayList<Customer> relatedSeller = this.customerManagement.searchSeller(searchItem);
            if (relatedProduct.size() == 0 && relatedSeller.size() == 0) {
                System.out.println("The product or seller with the keyword '" + searchItem + "' was not found. Please try again!");
                System.out.println();
                System.out.println("Going back to search page...");
                waitAWhile();
                search();
            } else {
                searchResultPage(relatedProduct, relatedSeller);
            }
        }
    }
    
    private void category() {
        clearScreen();
        CategoryList categoryData = new CategoryList();
        ArrayList<Category> categoryList = categoryData.getCategoryList();
        System.out.println("------------------------------ Categories ------------------------------");
        for (int i = 1; i <= categoryList.size(); i++) {
            System.out.println(i + ". " + categoryList.get(i - 1).getName());
        }
        
        System.out.println();
        String input = askAndCheckInput("A. Back to homepage\n\nWhat to do next? (1-16)/ (A): ", "[aA]|[0-9]|1[0-6]", false).toLowerCase();

        if (input.equals("a")) {
            new Homepage(this.scanner, customer);
        } else {
            int index = Integer.valueOf(input) - 1;
            ArrayList<Product> categoryProduct = this.productManagement.getProductByCategory(categoryList.get(index));
            if (!categoryProduct.isEmpty()) { 
                searchResultPage(categoryProduct, null);
            } else {
                System.out.println("Sorry there is no product!");
                System.out.println("Going back to category page...");
                waitAWhile();
                category();
            }
        } 
    }
    
    private void addNewProduct() {
        clearScreen();
        System.out.println("---------------------------- New Product ------------------------------");
        System.out.print("Name: ");
        String name = this.scanner.nextLine();
        double price = Double.valueOf(askAndCheckInput("Price (i.e 2.10, 2) : RM", "^[0-9]*(\\.[0-9]{0,2})?$", true));
        int stock = Integer.valueOf(askAndCheckInput("Stock Available: ", "^[1-9][0-9]*$", false));
        
        System.out.println();
        System.out.println("Category: ");
        CategoryList categoryData = new CategoryList();
        ArrayList<Category> categoryList = categoryData.getCategoryList();
        for (int i = 1; i <= categoryList.size(); i++) {
            System.out.println(i + ". " + categoryList.get(i - 1).getName());
        }
        System.out.println();
        int input = Integer.valueOf(askAndCheckInput("Please choose a category that suits the product (1-16): ", "[1-9]|1[0-6]", false));
        Category productCategory = new Category(categoryList.get(input).getName());
        Product newProduct = new Product(name, price, stock, productCategory, customer);
        this.productManagement.addNewProduct(newProduct);
        
        System.out.println();
        System.out.println("------------------------ Describe your product -----------------------");
        while (true) {
            System.out.print("Type 'E' to quit or 'Y' to add new description: ");
            String userOperate = scanner.nextLine().toLowerCase();
            if (userOperate.equals("e")) {
                System.out.println("You can change the description on the product page");
                waitAWhile();
                break;
            } else if (userOperate.equals("y")) {
                String newDescription = newProduct.addNewDescription();
                this.productManagement.addContentToProductFile(newDescription, newProduct.getID());
                System.out.println();
            } else {
                System.out.println("Wrong input! Please try again!");
            }
        }
        
        new ProductInterface(this.scanner, customer, newProduct, this.productManagement);
    }
    
    private void userSettings() {
        new CustomerInterface(this.scanner, this.customer, this.customerManagement);
    }
    
    private void viewCart() {
        new TransactionInterface(customer);
    }
    
    private void exit() {
        try {
            System.out.println("Exiting the app ....");
            System.out.println("\n\n");
            Thread.sleep(2000);
            System.out.println(" #######                                #    #               ");
            System.out.println("    #    #    #   ##   #    # #    #    #   #   ####  #    # ");
            System.out.println("    #    #    #  #  #  ##   # #   #      # #   #    # #    # ");
            System.out.println("    #    ###### #    # # #  # ####        #    #    # #    # ");
            System.out.println("    #    #    # ###### #  # # #  #        #    #    # #    # ");
            System.out.println("    #    #    # #    # #   ## #   #       #    #    # #    # ");
            System.out.println("    #    #    # #    # #    # #    #      #     ####   ####  ");
            System.out.println("\n\n");
        } catch (InterruptedException ex) {
            Logger.getLogger(Homepage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception E) {
            System.out.println("Sorry there is an error");
        }
    }
    
    private void waitAWhile() {
       try {
            Thread.sleep(1500);
        } catch (InterruptedException ex) {
            Logger.getLogger(Homepage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void searchResultPage(ArrayList<Product> products, ArrayList<Customer> sellers) {
        clearScreen();
        System.out.println("---------------------------- Search Results ----------------------------");
        int count = 0;
        for (Product product : products) {
            ++count;
            System.out.println("No. " + count + " - " + product.getName());
        }
        
        if (sellers != null) {
            for (Customer seller : sellers) {
                ++count;
                System.out.println("No. " + count + " - " + seller.getUsername() + "'s store");
            }
        }

        System.out.println();
        String fieldRange = "What to do next? (1-" + count + ") / (A): ";
        String numberRegex = produceNumberRegex(count);
        String input = askAndCheckInput("A. Back to homepage\n\n" + fieldRange, "[aA]|" + numberRegex, false).toLowerCase();

        if (input.equals("a")) {
            new Homepage(this.scanner, customer);
        } else {
            if (Integer.valueOf(input) > products.size()) {
                int index = Integer.valueOf(input) - (products.size() + 1);
                sellerStore(sellers.get(index));
            } else {
                int index = Integer.valueOf(input) - 1;
                new ProductInterface(this.scanner, customer, products.get(index), this.productManagement);
            }
        }
    }
    
    private void sellerStore(Customer seller) {
        clearScreen();
        System.out.println("------------------------- " + seller.getUsername() + "'s Store -------------------------");
        int count = 0;
        ArrayList<Product> sellerProducts = productManagement.returnSellerProduct(seller);
        for (Product product: sellerProducts) {
            ++count;
            System.out.println("No. " + count + " - " + product.getName());
        }
        
        System.out.println();
        //Not implemented yet
//        System.out.println("C. Delete product");
        String fieldRange = "", numberRegex = "";
        if (sellerProducts.isEmpty()) {
            fieldRange = "What to do next? (A-C): ";
        } else {
            fieldRange = "What to do next? (1-" + count + ") / (A-C): ";
            numberRegex = produceNumberRegex(count);
        }
        String input = askAndCheckInput("A. Back to homepage\nB. Add new product\nC. Delete product\n\n" + fieldRange, "[a-cA-C]|" + numberRegex, false).toLowerCase();

        if (input.equals("a")) {
            new Homepage(this.scanner, customer);
        } else if (input.equals("b")) {
            addNewProduct();
        } else {
            int index = Integer.valueOf(input) - 1;
            new ProductInterface(this.scanner, customer, sellerProducts.get(index), this.productManagement);
        }
    }
    
    private String produceNumberRegex(int count) {
        String numberRegex = "";
        if (count < 10) {
            numberRegex = "[1-" + count + "]";
        } else {
            for (int i = 10; i < count + 1; i++) {
                numberRegex = numberRegex + "|" + i; 
            }
            numberRegex = "[1-9]" + numberRegex;
        }
        return numberRegex;
    }
    
}
