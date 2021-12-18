package Interface;
https://github.com/ryoshi007/Omazon/blob/main/Interface/Omazon.java
import customer.Customer;
import customer.CustomerDatabase;
import customer.CustomerList;
import java.util.Scanner;

public class Omazon {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        //Create customer from info in text file
        CustomerList customerList = new CustomerDatabase().loadFile();
        customerList.listName();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        Customer customer = customerList.verifyUser(email);
        
        
        new Homepage(scanner, customer);
    }
    
}
