package Interface;

import customer.Customer;
import customer.CustomerManagement;
import product.Product;
import product.ProductManagement;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        System.out.print("What to do next? (1-3) / (A-H): ");
        String command = this.scanner.nextLine();
        System.out.println();
        operate(command);
    }
    
    private void operate(String command) {
        if (command.equals("1")) {
            new ProductInterface(this.scanner, customer, this.topThree.get(0), this.productManagement);
        } else if (command.equals("2")) {
            new ProductInterface(this.scanner, customer, this.topThree.get(1), this.productManagement);
        } else if (command.equals("3")) {
            new ProductInterface(this.scanner, customer, this.topThree.get(2), this.productManagement);
        } else if (command.equals("A")) {
            search();
        } else if (command.equals("B")) {
            viewCart();
        } else if (command.equals("C")){
            category();
        } else if (command.equals("D")) {
            userSettings();
        }  else if (command.equals("E")) {
            sellerStore(customer);
        } else if (command.equals("F")) {
            exit();
        }
    }
    
    private void search() {
        clearScreen();
        System.out.print("Please enter the word: " + TEXT_YELLOW);
        String searchItem = scanner.nextLine();
        System.out.print(TEXT_RESET);

        ArrayList<Product> relatedProduct = this.productManagement.searchProduct(searchItem);
        ArrayList<Customer> relatedSeller = this.customerManagement.searchSeller(searchItem);
        if (relatedProduct.size() == 0 && relatedSeller.size() == 0) {
            System.out.println("The product or seller with the keyword '" + searchItem + "' was not found. Please try again!");
            System.out.println();
            System.out.println("Going back to search page...");
            waitAWhile();
            search();
        } else {
            inputOperationInAFunction(relatedProduct, relatedSeller);
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
        System.out.println("A. Back to homepage");
        System.out.println();
        System.out.print("What to do next? (1-16) / (A): " + TEXT_YELLOW);
        String input = scanner.nextLine();
        System.out.print(TEXT_RESET);

        if (input.equals("A")) {
            new Homepage(this.scanner, customer);
        } else {
            int index = Integer.valueOf(input) - 1;
            ArrayList<Product> categoryProduct = this.productManagement.getProductByCategory(categoryList.get(index));
            inputOperationInAFunction(categoryProduct, null);
        } 
    }
    
    private void addNewProduct() {
        clearScreen();
        System.out.println("---------------------------- New Product ------------------------------");
        System.out.print("Name: ");
        String name = this.scanner.nextLine();
        System.out.print("Price: RM");
        double price = this.scanner.nextInt();
        System.out.print("Stock Available: ");
        int stock = this.scanner.nextInt();
        
        System.out.println();
        System.out.println("Category: ");
        CategoryList categoryData = new CategoryList();
        ArrayList<Category> categoryList = categoryData.getCategoryList();
        for (int i = 1; i <= categoryList.size(); i++) {
            System.out.println(i + ". " + categoryList.get(i - 1).getName());
        }
        System.out.println();
        System.out.print("Please choose a category that suits the product (1-16): " + TEXT_YELLOW);
        int input = scanner.nextInt() - 1;
        System.out.print(TEXT_RESET);
        scanner.nextLine();
        Category productCategory = new Category(categoryList.get(input).getName());
        Product newProduct = new Product(name, price, stock, productCategory, customer);
        this.productManagement.addNewProduct(newProduct);
        
        System.out.println();
        System.out.println("------------------------ Describe your product -----------------------");
        while (true) {
            System.out.print("Type 'E' to quit or 'Y' to add new description: ");
            String userOperate = scanner.nextLine();
            if (userOperate.equals("E")) {
                break;
            } else if (userOperate.equals("Y")) {
                String newDescription = newProduct.addNewDescription();
                this.productManagement.addContentToProductFile(newDescription, newProduct.getID());
                System.out.println();
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
    
    private void inputOperationInAFunction(ArrayList<Product> products, ArrayList<Customer> sellers) {
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
                System.out.println("No. " + count + " - " + seller.getUsername());
            }
        }

        System.out.println();
        System.out.println("A. Back to homepage");
        System.out.println();
        System.out.print("What to do next? (1-" + count + ") / (A/B): " + TEXT_YELLOW);
        String input = scanner.nextLine();
        System.out.print(TEXT_RESET);

        if (input.equals("A")) {
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
        System.out.println("A. Back to homepage");
        System.out.println("B. Add new product");
        //Not implemented yet
        System.out.println("C. Delete product");
        System.out.println();
        System.out.print("What to do next? (1-" + count + ") / (A-C): " + TEXT_YELLOW);
        String input = scanner.nextLine();
        System.out.print(TEXT_RESET);

        if (input.equals("A")) {
            new Homepage(this.scanner, customer);
        } else if (input.equals("B")) {
            addNewProduct();
        } else {
            int index = Integer.valueOf(input) - 1;
            new ProductInterface(this.scanner, customer, sellerProducts.get(index), this.productManagement);
        }
    }
    
}
