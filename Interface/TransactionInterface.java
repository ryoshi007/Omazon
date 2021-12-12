package Interface;

import customer.Customer;
import customer.CustomerManagement;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import product.Product;
import product.ProductManagement;
import productcomponenet.Transaction;

public class TransactionInterface {
    private Scanner scanner;
    private Customer customer;
    private CustomerManagement customerManagement;
    private ProductManagement productManagement;
    private ArrayList<Transaction> purchasedProduct;
    
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_GREEN = "\u001B[32m";
    public static final String TEXT_YELLOW = "\u001B[32m";
    
    public TransactionInterface(Customer customer) {
        this.customer = customer;
        scanner = new Scanner(System.in);
        customerManagement = new CustomerManagement();
        productManagement = new ProductManagement();
        this.purchasedProduct = customer.getPurchaseList();
        clearScreen();
        showTransaction();
    }
    
    private boolean checkPaymentPassword() {
        System.out.print("Enter payment password: ");
        String inputPassword = scanner.nextLine();
        return this.customer.getPaymentPassword().equals(inputPassword);
    }
    
    private void showTransaction (){
        try {
            clearScreen();
            Scanner scanner = new Scanner(System.in);
            System.out.println("---------------------------- Transaction ----------------------------");
            int count = 0;
            double totalPayAmount = 0;
            for (Transaction transaction : this.purchasedProduct) {
                ++count;
                double totalProductPrice = transaction.getProduct().getPrice()*transaction.getAmount();
                totalPayAmount += totalProductPrice;
                System.out.println("No. " + count + " - " + transaction.getProduct().getName() + 
                        "\nAmount - " + transaction.getAmount() + "\n");
                System.out.printf("%s%.2f\n", "Total Price - RM", totalProductPrice);
                System.out.println();
            }
            System.out.println("------------------------------------------------------------------------");
            System.out.printf("Total Price : RM %.2f\n",totalPayAmount);


            System.out.println();
            System.out.printf("%s%.2f\n", "Current Balance: RM ", customer.getBalance());
            System.out.println();
            System.out.println("1. Make Payment");
            System.out.println("2. Top Up Balance");
            System.out.println("3. Back to homepage");
            System.out.println();
            System.out.print("What to do next? (1-3): " + TEXT_YELLOW);
            String input = scanner.nextLine();
            System.out.print(TEXT_RESET);

            if (input.equals("1")) {
                if (totalPayAmount > this.customer.getBalance()){
                    double requiredAmount = totalPayAmount - this.customer.getBalance();
                    System.out.printf("%s%.2f\n","Your current balance isn't enough to pay! Please top up at least RM", requiredAmount);
                    addBalance();                  
                }else {
                    if (checkPaymentPassword()){
                        double oldBalance = this.customer.getBalance();
                        double newBalance = this.customer.getBalance() - totalPayAmount;
                        this.customer.setBalance(newBalance);
                        this.customerManagement.changeDataInCustomerFile(convertBalanceToString(oldBalance), convertBalanceToString(newBalance), this.customer.getUsername());
                        System.out.println(TEXT_GREEN + "You have made payment successfully!");
                        
                        addToOrderHistory();
                        System.out.println(TEXT_RESET);
                        new Homepage(this.scanner, customer);
                        
                    }else{
                        System.out.println(TEXT_RED + "Incorrect payment password! Please try again!");
                        System.out.println(TEXT_RESET);
                        Thread.sleep(2000);
                        new TransactionInterface(customer);
                    }
                }
            }else if(input.equals("2")){
                addBalance();             
            }else {
                System.out.println("You still can purchase the products as long as you're not quitting the app :)");
                Thread.sleep(2000);
                new Homepage(this.scanner, customer);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Homepage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String convertBalanceToString(double balance) {
        return "Balance;" + String.valueOf(balance);
    }
    
    private void addToOrderHistory() {
        boolean existInOrder = false;
        ArrayList<Product> orderHistory = customer.getOrderHistory();
        for(Transaction transaction: purchasedProduct) {
            for(Product product: orderHistory) {
                if (product.getName().equals(transaction.getProduct().getName())) {
                    existInOrder = true;
                }
            }
            
            if (!existInOrder) {
                customer.addProductHistory(transaction.getProduct());
                this.customerManagement.addContentToCustomerFile(addOrderHistory(transaction.getProduct().getName()), this.customer.getUsername());
            }          
            changesInProductMeta(transaction);
        }
    }
    
    private void changesInProductMeta(Transaction transaction) {
        Product currentProduct = transaction.getProduct();
        int oldStock = currentProduct.deduceStockAmount(transaction.getAmount());
        String oldStockInput = "Stock;" + oldStock;
        String newStockInput = "Stock;" + currentProduct.getStock();
        this.productManagement.changeDataInProductFile(oldStockInput, newStockInput, currentProduct.getName());
        
        int oldSales = currentProduct.changeSalesAmount(transaction.getAmount());
        String oldSalesInput = "Sales;" + oldSales;
        String newSalesInput = "Sales;" + currentProduct.getSalesVolume();
        this.productManagement.changeDataInProductFile(oldSalesInput, newSalesInput, currentProduct.getName());
    }
    
    private void addBalance() {
        double oldBalance = this.customer.addBalance();
        double newBalance = this.customer.getBalance();
        this.customerManagement.changeDataInCustomerFile(convertBalanceToString(oldBalance), convertBalanceToString(newBalance), this.customer.getUsername());
        new TransactionInterface(customer);
    }
    
    private String addOrderHistory(String productName) {
        return "Order;" + productName;
    }
        
    private void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception E) {
            System.out.println("Sorry there is an error");
        }
    }
    
}
