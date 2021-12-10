package customer;

import java.util.Scanner;

public class Customer {
    private String username;
    private String password;
    private String email;
    private double balance =0;
    private String address;
    private String paymentPassword;
    
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_GREEN = "\u001B[32m";
    
    public Customer(String username, String password, String email, String address,String paymentPassword, Double balance){
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.paymentPassword = paymentPassword;
        this.balance = balance;
    }
    
    //Constructor for new user registration
    public Customer(String username, String password, String email, String address){
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.paymentPassword = "";
        this.balance = 0;
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
    
    public String toString() {
        return "Name: " + this.username + "\nEmail: " + this.email;
    }
}
