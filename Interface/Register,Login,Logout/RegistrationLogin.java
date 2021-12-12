
package Interface;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import serviceclass.Database;

public class RegistrationLogin {
    private ArrayList<String> username;
    private ArrayList<String> email;
    private ArrayList<String> password;
  
    public RegistrationLogin(){
        this.username = new ArrayList<>();
        this.email = new ArrayList<>();
        this.password = new ArrayList<>();
    }
    
    public static final String TEXT_GREEN = "\u001B[32m";
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_RED = "\u001B[31m";

    Scanner input = new Scanner(System.in);
    
    public void RegistrationLogin(){
    
        String inputEmail, inputPassword;
        
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ Omazon online shopping app ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~");
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Before showing your shopping ability, please choose to ");
        System.out.println("");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.print("What to do next?(1/2): "); 
        int command = input.nextInt();
        if (command == 1){
           Login();
        } else if (command == 2){
           Registration();
        }
    }
    
    public void Login(){
         
            String inputPassword, inputEmail;
            
            System.out.println("------------------------------- Login Page -------------------------------");
            System.out.println("Please login with your credentials");
            System.out.print("Email: " + TEXT_GREEN);
            inputEmail  = input.nextLine();
            input.next();
            System.out.print(TEXT_RESET + "Password: " + TEXT_GREEN);
            inputPassword = input.nextLine();
            input.next();
            System.out.println("");
            System.out.print("Validating...");
            System.out.println("");
            
            loadfile();
            if (inputEmail.equals(username) && inputPassword.equals(password)){
                System.out.println("You have logged in successfully, user " + username + " !!!");
                
            } else {
                System.out.println(TEXT_RED + "Please enter correct email and password." + TEXT_RESET);
            }
            
        }
    
    //STILL LEARNING HOW TO READ TEXT FILE AND ADD NEW TEXT FILE, HVNT COMPLETE, SORRY TAT
    public void Registration(){
        
            System.out.println("--------------------------- Registration Page ----------------------------");
            System.out.print("Username: " + TEXT_GREEN);
            input.next();
            System.out.print(TEXT_RESET + "Email: " + TEXT_GREEN);
            email.add(input.nextLine());
            input.next();
            System.out.print(TEXT_RESET + "Password: " + TEXT_GREEN);
            password.add(input.nextLine());
            input.next();
            
            System.out.println("You have register successfully, user " + " !");
        
    }
    
    
}
            
       
