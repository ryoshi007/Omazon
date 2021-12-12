
package Interface;

import java.util.Scanner;
import Interface.Homepage;
        
public class Logout {
    
    public Logout(){
        
        Scanner input = new Scanner(System.in);
        
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ Omazon online shopping app ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~");
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("1. Continue shopping ");
        System.out.println("2. Logout");
        System.out.print("What to do next?(1/2)");
        int command = input.nextInt();
        if (command == 1){
            clearScreen();
            start();
            
        }else if (command == 2){
            clearScreen();
            System.out.println(" #######                                #    #               ");
            System.out.println("    #    #    #   ##   #    # #    #    #   #   ####  #    # ");
            System.out.println("    #    #    #  #  #  ##   # #   #      # #   #    # #    # ");
            System.out.println("    #    ###### #    # # #  # ####        #    #    # #    # ");
            System.out.println("    #    #    # ###### #  # # #  #        #    #    # #    # ");
            System.out.println("    #    #    # #    # #   ## #   #       #    #    # #    # ");
            System.out.println("    #    #    # #    # #    # #    #      #     ####   ####  ");
            
            System.out.println("");
            System.out.println("\t You've been logged out successfully! ");
        }
    }
    
    private void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception E) {
            System.out.println("Sorry there is an error.");
        }
    }
    
}
