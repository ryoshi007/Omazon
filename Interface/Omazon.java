package Interface;

import customer.Customer;
import customer.CustomerDatabase;
import customer.CustomerList;
import java.util.Scanner;

public class Omazon {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        //Create customer from info in text file
        CustomerList customerList = new CustomerDatabase().loadFile();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        Customer customer = customerList.searchCustomer(email);
        
        
        new Homepage(scanner, customer);
    }
    
}
