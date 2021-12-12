package customer;

import java.util.Scanner;
import java.util.ArrayList;
import product.Product;
import productcomponenet.Transaction;

public class Customer {
    private String username;
    private String password;
    private String email;
    private double balance;
    private String address;
    private String paymentPassword;
    private ArrayList<Transaction> purchaseList;
    private ArrayList<Product> favouriteList;
    private ArrayList<Product> orderHistory;   
    
    
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_GREEN = "\u001B[32m";
    
    public Customer(String username, String password, String email, 
            String address, String paymentPassword, double balance, 
            ArrayList<Product> favouriteList, ArrayList<Product> orderHistory){
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.paymentPassword = paymentPassword;
        this.balance = balance;
        this.purchaseList = new ArrayList<>();
        this.favouriteList = favouriteList;
        this.orderHistory = orderHistory;
    }
    
    //Constructor for new user registration
    public Customer(String username, String password, String email, String address){
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.paymentPassword = "";
        this.balance = 0;
        this.purchaseList = new ArrayList<>();
        this.favouriteList = new ArrayList<>();
        this.orderHistory = new ArrayList<>();
    }
    
    public double addBalance(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the amount to topup: RM");
        double newBalance = scanner.nextDouble();
        double oldBalance = balance;
        this.balance += newBalance;
        System.out.println(TEXT_GREEN + "You have topped up succesfully" + TEXT_RESET);
        return oldBalance;
    }
    
    public void setBalance(double newBalance) {
        this.balance = newBalance;
    }
    
    public double getBalance(){
        return this.balance;
    }
    
    public String setUsername(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the new username: ");
        String newUsername = scanner.nextLine();
        String oldUsername = this.username;
        this.username= newUsername;
        System.out.println(TEXT_GREEN +"Your username has beed changed succesfully"+ TEXT_RESET);
        return oldUsername;
    }
    
    public String getUsername(){
        return this.username;
    }
    
    public String setEmail(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the new email: ");
        String newEmail = scanner.nextLine();
        String oldEmail = this.email;
        this.email= newEmail;
        System.out.println(TEXT_GREEN +"Your email has beed changed succesfully"+ TEXT_RESET);
        return oldEmail;
    }
    
    public String getEmail(){
        return this.email;
    }
    
    public String setPassword(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the new password: ");
        String newPassword = scanner.nextLine();
        String oldPassword = this.password;
        this.password= newPassword;
        System.out.println(TEXT_GREEN +"Your password has beed changed succesfully"+ TEXT_RESET);
        return oldPassword;
    }
    
    public String getPassword(){
        return this.password;
    }
    
    public String setAddress(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the new address: ");
        String newAddress = scanner.nextLine();
        String oldAddress = this.address;
        this.address= newAddress;
        System.out.println(TEXT_GREEN +"Your address has beed changed succesfully"+ TEXT_RESET);
        return oldAddress;
    }
    
    public String getAddress(){
        return this.address;
    }
    
    public String setPaymentPassword(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the new Payment Password: ");
        String newPaymentPassword = scanner.nextLine();
        String oldPaymentPassword = this.paymentPassword;
        this.paymentPassword= newPaymentPassword;
        System.out.println(TEXT_GREEN +"Your payment password has beed changed succesfully"+ TEXT_RESET);
        return oldPaymentPassword;
    }
    
    public String getPaymentPassword(){
        return this.paymentPassword;
    }
    
    public void addTransaction(Transaction newTransaction) {
        this.purchaseList.add(newTransaction);
    }
    
    public ArrayList<Transaction> getPurchaseList() {
        return this.purchaseList;
    }
    
    public void addFavourite(Product product) {
        this.favouriteList.add(product);
    }
    
    public ArrayList<Product> getFavouriteList() {
        return this.favouriteList;
    }
    
    public void addProductHistory(Product product) {
        this.orderHistory.add(product);
    }
        
    public ArrayList<Product> getOrderHistory() {
        return this.orderHistory;
    }
    
    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
    
    public String toString() {
        return "Name: " + this.username + "\nEmail: " + this.email;
    }
}
