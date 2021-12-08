package Interface;

import product.Product;
import product.ProductManagement;
import product.ProductDatabase;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import product.ProductDatabase;
import productcomponenet.Category;
import productcomponenet.CategoryList;
import productcomponenet.Description;

public class Homepage {
    private Scanner scanner;
    private ProductManagement productManagement;
    private ArrayList<Product> topThree;
    
    public static final String TEXT_YELLOW = "\u001B[32m";
    public static final String TEXT_RESET = "\u001B[0m";
        
    public Homepage(Scanner scanner) {
        this.scanner = scanner;
        this.productManagement = new ProductDatabase().loadFile();
        this.topThree = productManagement.topThreeProduct();
        clearScreen();
        start();
    }
    
    public void start() {
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ Welcome to Omazon App ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~");
        System.out.println("-------------------------------------------------------------------------");
        
        System.out.println();
        System.out.println("Best-Selling Products");
        System.out.println("No. 1 - " + this.topThree.get(0).getName());
        System.out.println("No. 2 - " + this.topThree.get(1).getName());
        System.out.println("No. 3 - " + this.topThree.get(2).getName());
        
        System.out.println();
        System.out.println("A. Search");
        System.out.println("B. Check balance");
        System.out.println("C. Go to cart");
        System.out.println("D. Go to category");
        System.out.println("F. Add new product");
        System.out.println("E. Exit app");
        
        System.out.println();
        System.out.print("What to do next? (1-3) / (A-E): ");
        String command = this.scanner.nextLine();
        System.out.println();
        operate(command);
    }
    
    public void operate(String command) {
        if (command.equals("1")) {
            new ProductInterface(this.scanner, this.topThree.get(0), this.productManagement);
        } else if (command.equals("2")) {
            new ProductInterface(this.scanner, this.topThree.get(1), this.productManagement);
        } else if (command.equals("3")) {
            new ProductInterface(this.scanner, this.topThree.get(2), this.productManagement);
        } else if (command.equals("A")) {
            search();
        } else if (command.equals("D")){
            category();
        } else if (command.equals("E")) {
            exit();
        } else if (command.equals("F")) {
            addNewProduct();
        }
    }
    
    public void search() {
        clearScreen();
        System.out.print("Please enter the word: " + TEXT_YELLOW);
        String searchItem = scanner.nextLine();
        System.out.print(TEXT_RESET);

        ArrayList<Product> relatedProduct = this.productManagement.search(searchItem);
        if (relatedProduct.size() == 0) {
            System.out.println("The product with the keyword '" + searchItem + "' was not found. Please try again!");
            System.out.println();
            search();
        } else {
            inputOperationInAFunction(relatedProduct);
        }
    }
    
    public void category() {
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
            new Homepage(this.scanner);
        } else {
            int index = Integer.valueOf(input) - 1;
            ArrayList<Product> categoryProduct = this.productManagement.getProductByCategory(categoryList.get(index));
            inputOperationInAFunction(categoryProduct);
        } 
    }
    
    public void addNewProduct() {
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
        Product newProduct = new Product(name, price, stock, productCategory);
        this.productManagement.addNewProduct(newProduct);
        
        System.out.println();
        System.out.println("------------------------ Describe your product -----------------------");
        while (true) {
            System.out.print("Type 'E' to quit or 'Y' to add new description: ");
            String userOperate = scanner.nextLine();
            System.out.println(userOperate);
            if (userOperate.equals("E")) {
                System.out.println("Here");
                break;
            } else if (userOperate.equals("Y")) {
                String newDescription = newProduct.addNewDescription();
                this.productManagement.addContentToProductFile(newDescription, newProduct.getName());
                System.out.println();
            }
        }
        
        new ProductInterface(this.scanner, newProduct, this.productManagement);
    }
    
    public void exit() {
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
    
    private void inputOperationInAFunction(ArrayList<Product> products) {
        clearScreen();
        System.out.println("---------------------------- Search Results ----------------------------");
        int count = 0;
        for (Product product : products) {
            ++count;
            System.out.println("No. " + count + " - " + product.getName());
        }

        System.out.println();
        System.out.println("A. Back to homepage");
        System.out.println();
        System.out.print("What to do next? (1-" + count + ") / (A): " + TEXT_YELLOW);
        String input = scanner.nextLine();
        System.out.print(TEXT_RESET);

        if (input.equals("A")) {
            new Homepage(this.scanner);
        } else {
            int index = Integer.valueOf(input) - 1;
            new ProductInterface(this.scanner, products.get(index), this.productManagement);
        }
    }
    
}