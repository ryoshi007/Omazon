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
        double totalPrice = 0;
        for (Product product : products) {
            ++count;
            System.out.println("No. " + count + " - " + product.getName() + amount + product.getPrice());
            totalPrice++;
        }
        System.out.printf("Total Price : RM %.2f\n",totalPrice);

        System.out.println();
        System.out.println("1. Make Payment");
        System.out.println("2. Top Up Balance");
        System.out.println("3. Back to homepage");
        System.out.println();
        System.out.print("What to do next? (1-3) " + TEXT_YELLOW);
        String input = scanner.nextLine();
        System.out.print(TEXT_RESET);

        if (input.equals("1")) {
            double totalPrice = this.product.getPrice()*amount;
            if (totalPrice > this.customer.getBalance()){
                this.customer.addBalance();
            }else if(totalPrice <= this.customer.getBalance()){
                if (checkPaymentPassword()){
                    double oldBalance = this.customer.getBalance();
                    double newBalance = this.customer.getBalance()-totalPrice;
                    this.customerManagement.changeDataInProductFile(Double.toString(oldBalance),Double.toString(newBalance), this.customer.getUsername());
                    new CustomerInterface(scanner, this.customer, this.customerManagement);
                    System.out.println(TEXT_GREEN + "You have made payment successfully !");
                    System.out.println(TEXT_RESET);
                }else{
                    System.out.println(TEXT_RED + "Incorrect Payment Password. Please try again");
                    System.out.println(TEXT_RESET);
                }
            }
        }else if(input.equals("2")){
            this.customer.addBalance();
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
