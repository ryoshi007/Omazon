package project;
import java.util.Scanner;
import CustomerInterface.Customer;
import CustomerInterface.CustomerManagement;

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
        System.out.println("");
        System.out.println("What to do next? (1-6)" + TEXT_GREEN);
        String command = scanner.nextLine();
        System.out.println(TEXT_RESET);
        operation(command);    
    }
    
    private void operation(String command){
        if (command.equals("1")){
            ChangeUsername();
        }else if(command.equals("2")){
            ChangePassword();
        }else if(command.equals("3")){
            ChangeEmail();
        }else if(command.equals("4")){
            ChangeAddress();
        }else if(command.equals("5")){
            ChangePaymentPassword();
        }else if(command.equals("6")){
            TopupBalance();
        }else{
            homepage();
        }       
    }
    
    private void ChangeUsername(){
        String oldUsername = this.customer.setusername();
        String oldInput = "Name;" + oldUsername;
        String newInput = "Name;" + this.customer.getusername();
        this.customerManagement.changeDataInProductFile(oldInput, newInput, oldUsername);
        new CustomerInterface(scanner, this.customer, this.customerManagement);
    }
    
    private void ChangePassword(){
        String oldPassword = this.customer.setpassword();
        String oldInput = "Password;" + oldPassword;
        String newInput = "Password;" + this.customer.getpassword();
        this.customerManagement.changeDataInProductFile(oldInput, newInput, this.customer.getusername());
        new CustomerInterface(scanner, this.customer, this.customerManagement);
    }
    
    private void ChangeEmail(){
        String oldEmail = this.customer.setemail();
        String oldInput = "Email;" + oldEmail;
        String newInput = "Email;" + this.customer.getemail();
        this.customerManagement.changeDataInProductFile(oldInput, newInput, this.customer.getusername());
        new CustomerInterface(scanner, this.customer, this.customerManagement);
    }
    
    private void ChangeAddress(){
        String oldAddress = this.customer.setaddress();
        String oldInput = "Address;" + oldAddress;
        String newInput = "Address;" + this.customer.getaddress();
        this.customerManagement.changeDataInProductFile(oldInput, newInput, this.customer.getusername());
        new CustomerInterface(scanner, this.customer, this.customerManagement);
    }
    
    private void ChangePaymentPassword(){
        String oldPaymentPassword = this.customer.setpaymentPassword();
        String oldInput = "Payment Password;" + oldPaymentPassword;
        String newInput = "Payment Password;" + this.customer.getpaymentPassword();
        this.customerManagement.changeDataInProductFile(oldInput, newInput, this.customer.getusername());
        new CustomerInterface(scanner, this.customer, this.customerManagement);
    }
    
    private void TopupBalance(){
        double oldBalance = this.customer.addbalance();
        String oldBalanceInString = "Balance;" + oldBalance;
        String newBalanceInString = "Balance;" + this.customer.getbalance();
        this.customerManagement.changeDataInProductFile(oldBalanceInString, newBalanceInString, this.customer.getusername());
        new CustomerInterface(scanner, this.customer, this.customerManagement);
    }
    
    private void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception E) {
            System.out.println("Sorry there is an error");
        }
    }
}
