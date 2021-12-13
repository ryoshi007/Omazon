package Interface;

import java.util.Scanner;
import customer.Customer;
import customer.CustomerManagement;
import java.util.ArrayList;
import product.Product;
import product.ProductManagement;

public class CustomerInterface {
    private Scanner scanner;
    private Customer customer;
    private CustomerManagement customerManagement;
    
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_GREEN = "\u001B[32m";
    public static final String TEXT_YELLOW = "\u001B[32m";
    
    public CustomerInterface(Scanner scanner, Customer customer, CustomerManagement customerManagement){
        this.scanner = scanner;
        this.customer = customer;
        this.customerManagement = customerManagement;
        clearScreen();
        start();
    }
        
    public void start(){
        char bulletSymbol = '\u2023';
        
        System.out.println("---------------------------- Settings ---------------------------");
        System.out.println("");
        System.out.println("1. Change Username");
        System.out.println("2. Change Password");
        System.out.println("3. Change Email");
        System.out.println("4. Change Address");
        System.out.println("5. Change Payment Password");
        System.out.println("6. Top up Balance");
        System.out.println("7. Show Order History");
        System.out.println("8. Show Favourite");
        System.out.println("9. Back to Homepage");
        System.out.println("");
        System.out.print("What to do next? (1-7): " + TEXT_GREEN);
        String command = scanner.nextLine();
        System.out.println(TEXT_RESET);
        operation(command);    
    }
    
    private void operation(String command){
        if (command.equals("1")){
            changeUsername();
        } else if (command.equals("2")){
            changePassword();
        } else if (command.equals("3")){
            changeEmail();
        } else if (command.equals("4")){
            changeAddress();
        } else if (command.equals("5")){
            changePaymentPassword();
        } else if (command.equals("6")){
            topUpBalance();
        } else if (command.equals("7")) {
            orderHistory();
        } else if (command.equals("8")) {
            favouriteProduct();
        } else {
            homepage();
        }       
    }
    
    private void changeUsername(){
        String oldUsername = this.customer.setUsername();
        String oldInput = "Username;" + oldUsername;
        String newInput = "Username;" + this.customer.getUsername();
        this.customerManagement.changeDataInCustomerFile(oldInput, newInput, this.customer.getID());
        new CustomerInterface(scanner, this.customer, this.customerManagement);
    }
    
    private void changePassword(){
        String oldPassword = this.customer.setPassword();
        String oldInput = "Password;" + oldPassword;
        String newInput = "Password;" + this.customer.getPassword();
        this.customerManagement.changeDataInCustomerFile(oldInput, newInput, this.customer.getID());
        new CustomerInterface(scanner, this.customer, this.customerManagement);
    }
    
    private void changeEmail(){
        String oldEmail = this.customer.setEmail();
        String oldInput = "Email;" + oldEmail;
        String newInput = "Email;" + this.customer.getEmail();
        this.customerManagement.changeDataInCustomerFile(oldInput, newInput, this.customer.getID());
        new CustomerInterface(scanner, this.customer, this.customerManagement);
    }
    
    private void changeAddress(){
        String oldAddress = this.customer.setAddress();
        String oldInput = "Address;" + oldAddress;
        String newInput = "Address;" + this.customer.getAddress();
        this.customerManagement.changeDataInCustomerFile(oldInput, newInput, this.customer.getID());
        new CustomerInterface(scanner, this.customer, this.customerManagement);
    }
    
    private void changePaymentPassword(){
        String oldPaymentPassword = this.customer.setPaymentPassword();
        String oldInput = "PaymentPassword;" + oldPaymentPassword;
        String newInput = "PaymentPassword;" + this.customer.getPaymentPassword();
        this.customerManagement.changeDataInCustomerFile(oldInput, newInput, this.customer.getID());
        new CustomerInterface(scanner, this.customer, this.customerManagement);
    }
    
    private void topUpBalance(){
        double oldBalance = this.customer.addBalance();
        String oldBalanceInString, newBalanceInString;
        if (oldBalance % 1 == 0) {
            oldBalanceInString = String.format("%.0f", oldBalance);
        } else {
            oldBalanceInString = String.valueOf(oldBalance);
        }
        
        if (this.customer.getBalance() % 1 == 0) {
            newBalanceInString = String.format("%.0f", this.customer.getBalance());
            System.out.println("");
        } else {
            newBalanceInString = String.valueOf(this.customer.getBalance());
        }
        
        oldBalanceInString = "Balance;" + oldBalanceInString;
        newBalanceInString = "Balance;" + newBalanceInString;
        this.customerManagement.changeDataInCustomerFile(oldBalanceInString, newBalanceInString, this.customer.getID());
        new CustomerInterface(scanner, this.customer, this.customerManagement);
    }
    
    private void orderHistory() {
        clearScreen();
        System.out.println("---------------------------- Order History ----------------------------");
        int count = 0;
        ArrayList<String> products = this.customer.getOrderHistory();
        if (products.isEmpty()) {
            System.out.println("You haven't purchase a product");
        } else {
            for (String productName : products) {
                ++count;
                System.out.println("No. " + count + " - " + productName);
            }
        }

        System.out.println();
        System.out.println("A. Back to user settings");
        System.out.println("B. Back to homepage");
        System.out.println();
        System.out.print("What to do next? (A/B): " + TEXT_YELLOW); 

        String input = scanner.nextLine();
        System.out.print(TEXT_RESET);

        if (input.equals("A")) {
            new CustomerInterface(scanner, customer, customerManagement);
        } else if (input.equals("B")) {
            new Homepage(this.scanner, customer);
        } 
    }
    
    private void favouriteProduct() {
        clearScreen();
        System.out.println("----------------------------- Favourite -----------------------------");
        int count = 0;
        ArrayList<Product> products = this.customer.getFavouriteList();
        if (products.isEmpty()) {
            System.out.println("You haven't add a product into favourite list");
        } else {
            for (Product product : products) {
                ++count;
                System.out.println("No. " + count + " - " + product.getName());
            }
        }

        System.out.println();
        System.out.println("A. Back to user settings");
        System.out.println("B. Back to homepage");
        System.out.println();
        if (products.isEmpty()) {
            System.out.print("What to do next? (A/B): " + TEXT_YELLOW); 
        } else {
            System.out.print("What to do next? (1-" + count + ") / (A/B): " + TEXT_YELLOW);
        }
        String input = scanner.nextLine();
        System.out.print(TEXT_RESET);

        if (input.equals("A")) {
            new CustomerInterface(scanner, customer, customerManagement);
        } else if (input.equals("B")) {
            new Homepage(this.scanner, customer);
        } else {
            int index = Integer.valueOf(input) - 1;
            new ProductInterface(this.scanner, customer, products.get(index), new ProductManagement());
        }
        
    }
    
    private void homepage() {
        new Homepage(this.scanner, this.customer);
    }
    
    private void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception E) {
            System.out.println("Sorry there is an error");
        }
    }
}

