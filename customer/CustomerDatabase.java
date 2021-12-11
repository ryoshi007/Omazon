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
import serviceclass.Database;
import org.apache.commons.io.FilenameUtils;
        
public class CustomerDatabase implements Database{
    
    @Override
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
                    if( row.contains("favourite;")){
                        String[] favouriteRow = row.split(";");
                        if (favouriteRow[1].equals(product.getName())){
                            Product product2 = product.getName();
                            favourite.add(product2);
                        }
                    }
                    if( row.contains("orderHistory;")){
                        String[] orderHistoryRow = row.split(";");
                        if (orderHistoryRow[1].equals(product.getName())){
                            Product product2 = product.getName();
                            orderHistory.add(product2);
                        }
                    }
                }

                Customer customer = new Customer(username, password, email, address, paymentPassword, balance, favourite, orderHistory);
                customerList.addCustomer(customer);
                
            } catch (FileNotFoundException e) {
                System.out.println("File was not found");
            } catch (IOException e) {
                System.out.println("Error reading from file");
            }
        }
        return customerList;
    }
    
    @Override
    public void addFile(List<String> contents, String name) {
        try {
            String pathName = "C:/Users/Freyr/Documents/NetBeansProjects/Assignment/customers/" + name + ".txt";
            Path filePath = Paths.get(pathName);
            Files.write(filePath, contents, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Cannot craete a new file");
        }
    }

    @Override
    public void deleteFile(String file) {
        try {
            String pathName = "C:/Users/Freyr/Documents/NetBeansProjects/Assignment/customers/" + file;
            File deleteFile = new File(pathName);
            deleteFile.delete();
        } catch (Exception e) {
            System.out.println("Cannot delete the file");
        }
    }

    @Override
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
    
    @Override
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
    
    @Override
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

    @Override
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

