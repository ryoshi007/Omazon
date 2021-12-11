/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interface;

import customer.Customer;
import customer.CustomerManagement;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author honti
 */
public class TransactionInterface {
    private Scanner scanner;
    private Customer customer;
    private CustomerManagement customerManagement;
    
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_GREEN = "\u001B[32m";
    public static final String TEXT_YELLOW = "\u001B[32m";
    
    private void inputOperationInAFunction(ArrayList<Product> Transaction) {
        clearScreen();
        System.out.println("---------------------------- Transaction ----------------------------");
        int count = 0;
        for (Product product : products) {
            ++count;
            System.out.println("No. " + count + " - " + product.getName());
        }

        System.out.println();
        System.out.println("1. Make Payment");
        System.out.println("2. Top Up Balance");
        System.out.println("3. Back to homepage");
        System.out.println();
        System.out.print("What to do next? (1-3) " + TEXT_YELLOW);
        String input = scanner.nextLine();
        System.out.print(TEXT_RESET);

        if (input.equals("1")) {
            double totalPrice = 
        }else if(input.equals("2")){
            
        }else {
            new Homepage(this.scanner, customer);
        }
    }
        
        private void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception E) {
            System.out.println("Sorry there is an error");
            }
        }
}
