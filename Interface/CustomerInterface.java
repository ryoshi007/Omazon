package Interface;

import java.util.Scanner;
import customer.Customer;
import customer.CustomerManagement;

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
        System.out.println("7. Back to Homepage");
        System.out.println("");
        System.out.print("What to do next? (1-7): " + TEXT_GREEN);
        String command = scanner.nextLine();
        System.out.println(TEXT_RESET);
        operation(command);    
    }
    
    private void operation(String command){
        if (command.equals("1")){
            changeUsername();
        }else if(command.equals("2")){
            changePassword();
        }else if(command.equals("3")){
            changeEmail();
        }else if(command.equals("4")){
            changeAddress();
        }else if(command.equals("5")){
            changePaymentPassword();
        }else if(command.equals("6")){
            topUpBalance();
        }else{
            homepage();
        }       
    }
    
    private void changeUsername(){
        String oldUsername = this.customer.setUsername();
        String oldInput = "Username;" + oldUsername;
        String newInput = "Username;" + this.customer.getUsername();
        this.customerManagement.changeDataInCustomerFile(oldInput, newInput, oldUsername);
        new CustomerInterface(scanner, this.customer, this.customerManagement);
    }
    
    private void changePassword(){
        String oldPassword = this.customer.setPassword();
        String oldInput = "Password;" + oldPassword;
        String newInput = "Password;" + this.customer.getPassword();
        this.customerManagement.changeDataInCustomerFile(oldInput, newInput, this.customer.getUsername());
        new CustomerInterface(scanner, this.customer, this.customerManagement);
    }
    
    private void changeEmail(){
        String oldEmail = this.customer.setEmail();
        String oldInput = "Email;" + oldEmail;
        String newInput = "Email;" + this.customer.getEmail();
        this.customerManagement.changeDataInCustomerFile(oldInput, newInput, this.customer.getUsername());
        new CustomerInterface(scanner, this.customer, this.customerManagement);
    }
    
    private void changeAddress(){
        String oldAddress = this.customer.setAddress();
        String oldInput = "Address;" + oldAddress;
        String newInput = "Address;" + this.customer.getAddress();
        this.customerManagement.changeDataInCustomerFile(oldInput, newInput, this.customer.getUsername());
        new CustomerInterface(scanner, this.customer, this.customerManagement);
    }
    
    private void changePaymentPassword(){
        String oldPaymentPassword = this.customer.setPaymentPassword();
        String oldInput = "PaymentPassword;" + oldPaymentPassword;
        String newInput = "PaymentPassword;" + this.customer.getPaymentPassword();
        this.customerManagement.changeDataInCustomerFile(oldInput, newInput, this.customer.getUsername());
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
        System.out.println(oldBalanceInString + " " + newBalanceInString);
        this.customerManagement.changeDataInCustomerFile(oldBalanceInString, newBalanceInString, this.customer.getUsername());
        new CustomerInterface(scanner, this.customer, this.customerManagement);
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

