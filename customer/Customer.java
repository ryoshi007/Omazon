
package CustomerInterface;
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
    
    public Customer(String Username, String Password, String email, String address,String paymentPassword){
        this.username = Username;
        this.password = Password;
        this.email = email;
        this.address = address;
        this.paymentPassword = paymentPassword;
    }
    
    public double addbalance(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the amount to topup: ");
        double newBalance = scanner.nextDouble();
        double oldBalance = balance;
        this.balance += newBalance;
        System.out.println(TEXT_GREEN + "You have topped up succesfully" + TEXT_RESET);
        return oldBalance;
    }
    
    public double getbalance(){
        return this.balance;
    }
    
    public String setusername(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the new username: ");
        String newUsername = scanner.nextLine();
        String oldUsername = this.username;
        this.username= newUsername;
        System.out.println(TEXT_GREEN +"Your username has beed changed succesfully"+ TEXT_RESET);
        return oldUsername;
    }
    
    public String getusername(){
        return this.username;
    }
    
    public String setemail(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the new email: ");
        String newEmail = scanner.nextLine();
        String oldEmail = this.email;
        this.email= newEmail;
        System.out.println(TEXT_GREEN +"Your email has beed changed succesfully"+ TEXT_RESET);
        return oldEmail;
    }
    
    public String getemail(){
        return this.email;
    }
    
    public String setpassword(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the new password: ");
        String newPassword = scanner.nextLine();
        String oldPassword = this.password;
        this.password= newPassword;
        System.out.println(TEXT_GREEN +"Your password has beed changed succesfully"+ TEXT_RESET);
        return oldPassword;
    }
    
    public String getpassword(){
        return this.password;
    }
    
    public String setaddress(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the new address: ");
        String newAddress = scanner.nextLine();
        String oldAddress = this.address;
        this.address= newAddress;
        System.out.println(TEXT_GREEN +"Your address has beed changed succesfully"+ TEXT_RESET);
        return oldAddress;
    }
    
    public String getaddress(){
        return this.address;
    }
    
    public String setpaymentPassword(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the new Payment Password: ");
        String newPaymentPassword = scanner.nextLine();
        String oldPaymentPassword = this.paymentPassword;
        this.paymentPassword= newPaymentPassword;
        System.out.println(TEXT_GREEN +"Your payment password has beed changed succesfully"+ TEXT_RESET);
        return oldPaymentPassword;
    }
    
    public String getpaymentPassword(){
        return this.paymentPassword;
    }
}
