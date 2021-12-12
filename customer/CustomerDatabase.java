package customer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import product.Product;
import product.ProductDatabase;
        
public class CustomerDatabase {
    
    public CustomerList loadFile() {
        List<String> filenameList = returnAllFile();
        CustomerList customerList = new CustomerList();
        
        for (String file : filenameList) {
            List<String> rows = new ArrayList<>();
            try {
                String pathName = "C:/Users/Freyr/Documents/NetBeansProjects/Assignment/customers/" + file;
                     
                BufferedReader fileReader = new BufferedReader(new FileReader(pathName));
                
                for (String line; (line = fileReader.readLine()) != null;) {
                    rows.add(line);
                }
                fileReader.close();               
                
                String username = "";
                String password = "";
                String email = "";
                String address = "";
                String paymentPassword = "";
                double balance = 0;
                
                ArrayList<Product> favouriteList = new ArrayList<>();
                ArrayList<Product> orderHistory = new ArrayList<>();
                
                for (String row: rows) {
                    if (row.contains("Username;")) {
                        String[] usernameRow = row.split(";");
                        username = usernameRow[1];
                    }
                    
                    if (row.contains("Password;")) {
                        String[] passwordRow = row.split(";");
                        password = passwordRow[1];
                    }
                    
                    if (row.contains("Email;")) {
                        String[] emailRow = row.split(";");
                        email = emailRow[1];
                    }
                    
                    if (row.contains("Address;")) {
                        String[] addressRow = row.split(";");
                        address = addressRow[1];
                    }
                    
                    if (row.contains("PaymentPassword;")) {
                        String[] paymentPasswordRow = row.split(";");
                        paymentPassword = paymentPasswordRow[1];
                    }
                    
                    if (row.contains("Balance;")) {
                        String[] balanceRow = row.split(";");
                        balance = Double.valueOf(balanceRow[1]);
                    }
                    
                    if (row.contains("Favourite;")) {
                        String[] favouriteRow = row.split(";");
                        String favouriteProductName = favouriteRow[1];
                        Product favouriteProduct = new ProductDatabase().returnSingleProduct(favouriteProductName);
                        favouriteList.add(favouriteProduct);
                    }
                    
                    if (row.contains("Order;")) {
                        String[] orderHistoryRow = row.split(";");
                        String purchasedProductName = orderHistoryRow[1];
                        Product purchasedProduct = new ProductDatabase().returnSingleProduct(purchasedProductName);
                        orderHistory.add(purchasedProduct);
                    }
                    
                }

                Customer customer = new Customer(username, password, email, address, paymentPassword, balance, favouriteList, orderHistory);
                customerList.addCustomer(customer);
                
            } catch (FileNotFoundException e) {
                System.out.println("File was not found");
            } catch (IOException e) {
                System.out.println("Error reading from file");
            }
        }
        return customerList;
    }
    
    public void addFile(List<String> contents, String name) {
        try {
            String pathName = "C:/Users/Freyr/Documents/NetBeansProjects/Assignment/customers/" + name + ".txt";
            Path filePath = Paths.get(pathName);
            Files.write(filePath, contents, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Cannot craete a new file");
        }
    }

    public void deleteFile(String file) {
        try {
            String pathName = "C:/Users/Freyr/Documents/NetBeansProjects/Assignment/customers/" + file;
            File deleteFile = new File(pathName);
            deleteFile.delete();
        } catch (Exception e) {
            System.out.println("Cannot delete the file");
        }
    }

    public List<String> returnAllFile() {
        List<String> fileList = new ArrayList<>();
        File directory = new File("C:/Users/Freyr/Documents/NetBeansProjects/Assignment/customers");
        for (File file : directory.listFiles()) {
            if (FilenameUtils.getExtension(file.getName()).equals("txt")) {
                fileList.add(file.getName());
            }      
        }
        return fileList;
    }
    
    public void addContentToFile(String file, String input) {
        try {
            String pathName = "C:/Users/Freyr/Documents/NetBeansProjects/Assignment/customers/" + file;
            PrintWriter outputStream = new PrintWriter(new FileOutputStream(pathName, true));
            outputStream.append("\n" + input);
            outputStream.close();
        } catch (IOException e) {
            System.out.println("Problem with file output");
        }
    }
    
    public void deleteContentFromFile(String file, String deleteInput) {
        List<String> rows = new ArrayList<>();
        
        try {
            String pathName = "C:/Users/Freyr/Documents/NetBeansProjects/Assignment/customers/" + file;
            BufferedReader fileReader = new BufferedReader(new FileReader(pathName));

            for (String line; (line = fileReader.readLine()) != null;) {
                rows.add(line);
            }
            
            fileReader.close();
            for (int i = 0; i < rows.size(); i++) {
                if (rows.get(i).equals(deleteInput)) {
                    rows.remove(i);
                }
            }

            Path filePath = Paths.get(pathName);
            Files.write(filePath, rows, StandardCharsets.UTF_8);
            
        } catch (IOException e) {
            System.out.println("The file cannot be edited.");
        }
    }

    public void changeContentInFile(String file, String oldInput, String newInput) {
        List<String> rows = new ArrayList<>();
        List<String> newContent = new ArrayList<>();
        try {
            String pathName = "C:/Users/Freyr/Documents/NetBeansProjects/Assignment/customers/" + file;
            BufferedReader fileReader = new BufferedReader(new FileReader(pathName));

            for (String line; (line = fileReader.readLine()) != null;) {
                rows.add(line);
            }
            
            fileReader.close();
            for (String row : rows) {
                if (row.contains(oldInput)) {
                    String changedRow = row.replace(oldInput, newInput);
                    newContent.add(changedRow);
                } else {
                    newContent.add(row);
                }
            }
            
            Path filePath = Paths.get(pathName);
            Files.write(filePath, newContent, StandardCharsets.UTF_8);
            
        } catch (IOException e) {
            System.out.println("The file cannot be edited.");
        }
    }
    
}
