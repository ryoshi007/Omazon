package product;

import customer.Customer;
import customer.CustomerDatabase;
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
import productcomponenet.Review;
import productcomponenet.Description;
import productcomponenet.Category;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
        
public class ProductDatabase {
    
    //Load the file and create a new class based on the content in a text file
    public ProductManagement loadFile() {
        List<String> filenameList = returnAllFile();
        ProductManagement productList = new ProductManagement();
        
        for (String file : filenameList) {
            Product loadedProduct = constructObject(file);
            productList.addProduct(loadedProduct);
        }
        return productList;
    }
    
    public Product constructObject (String fileID) {
        List<String> rows = new ArrayList<>();
        try {
            String pathName = "C:/Users/Freyr/Documents/NetBeansProjects/Assignment/products/" + fileID;

            BufferedReader fileReader = new BufferedReader(new FileReader(pathName));

            for (String line; (line = fileReader.readLine()) != null;) {
                rows.add(line);
            }
            fileReader.close();               
            String name = "";
            double price = 0; 
            int stock = 0, sales = 0, id = 0;
            Category category = null;
            Customer owner = null;

            for (String row: rows) {
                if (row.contains("Name;")) {
                    String[] nameRow = row.split(";");
                    name = nameRow[1];
                }

                if (row.contains("Price;")) {
                    String[] priceRow = row.split(";");
                    price = Double.valueOf(priceRow[1]);
                }

                if (row.contains("Sales;")) {
                    String[] salesRow = row.split(";");
                    sales = Integer.valueOf(salesRow[1]);
                }

                if (row.contains("Stock;")) {
                    String[] stockRow = row.split(";");
                    stock = Integer.valueOf(stockRow[1]);
                }

                if (row.contains("Category;")) {
                    String[] categoryRow = row.split(";");
                    category = new Category(categoryRow[1]);
                }
                
                if (row.contains("Owner;")) {
                    String[] ownerRow = row.split(";");
                    int ownerID = Integer.valueOf(ownerRow[1]);
                    owner = new CustomerDatabase().returnSingleObject(ownerID);
                }
                
                if (row.contains("ID;")) {
                    String[] idRow = row.split(";");
                    id = Integer.valueOf(idRow[1]);
                }
            }

            Product product = new Product(name, price, stock, sales, category, owner, id);

            for (String row: rows) {

                if (row.contains("Description")) {
                    String[] descContent = row.split(";");
                    Description description = new Description(descContent[1], descContent[2]);
                    product.addDescription(description);

                } else if (row.contains("Review")) {
                    String[] reviewContent = row.split(";");
                    Review review = new Review(reviewContent[1], reviewContent[2]);
                    product.addReviews(review);
                }
            }

            return product;

        } catch (FileNotFoundException e) {
            System.out.println("File was not found");
        } catch (IOException e) {
            System.out.println("Error reading from file");
        }
        
        return null;
    }
    
    //Add a new text file to a folder
    public void addFile(List<String> contents, int id) {
        try {
            String pathName = "C:/Users/Freyr/Documents/NetBeansProjects/Assignment/products/" + id + ".txt";
            Path filePath = Paths.get(pathName);
            Files.write(filePath, contents, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Cannot craete a new file");
        }
    }

    //Delete an existing text file from a folder
    public void deleteFile(int id) {
        try {
            String pathName = "C:/Users/Freyr/Documents/NetBeansProjects/Assignment/products/" + id + ".txt";
            File deleteFile = new File(pathName);
            deleteFile.delete();
        } catch (Exception e) {
            System.out.println("Cannot delete the file");
        }
    }

    //Return all of the files name in a folder
    public List<String> returnAllFile() {
        List<String> fileList = new ArrayList<>();
        File directory = new File("C:/Users/Freyr/Documents/NetBeansProjects/Assignment/products");
        for (File file : directory.listFiles()) {
            if (FilenameUtils.getExtension(file.getName()).equals("txt")) {
                fileList.add(file.getName());
            }      
        }
        return fileList;
    }
    
    //Add additional thing to a text file
    public void addContentToFile(int id, String input) {
        try {
            String pathName = "C:/Users/Freyr/Documents/NetBeansProjects/Assignment/products/" + id + ".txt";
            PrintWriter outputStream = new PrintWriter(new FileOutputStream(pathName, true));
            outputStream.append("\n" + input);
            outputStream.close();
        } catch (IOException e) {
            System.out.println("Problem with file output");
        }
    }
    
    //Delete specify item in a text file
    public void deleteContentFromFile(int id, String deleteInput) {
        List<String> rows = new ArrayList<>();
        
        try {
            String pathName = "C:/Users/Freyr/Documents/NetBeansProjects/Assignment/products/" + id + ".txt";
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

    //Change a specific item in a text file
    public void changeContentInFile(int id, String oldInput, String newInput) {
        List<String> rows = new ArrayList<>();
        
        try {
            String pathName = "C:/Users/Freyr/Documents/NetBeansProjects/Assignment/products/" + id + ".txt";
            BufferedReader fileReader = new BufferedReader(new FileReader(pathName));

            for (String line; (line = fileReader.readLine()) != null;) {
                rows.add(line);
            }
            
            fileReader.close();
            
            for (int i = 0; i < rows.size(); i++) {
                if (rows.get(i).contains(oldInput)) {
                    rows.set(i, newInput);
                }
            }
            
            Path filePath = Paths.get(pathName);
            Files.write(filePath, rows, StandardCharsets.UTF_8);
            
        } catch (IOException e) {
            System.out.println("The file cannot be edited.");
        }
    }
    
    //Recurssive problem with product database
    public Product returnSingleObject(int id) {
        List<String> filenameList = returnAllFile();
        for (String file : filenameList) {
            Product loadedProduct = constructObject(file);

            if (loadedProduct.getID() == id) {
                return loadedProduct;
            }
        }
        return null;
    }
    
}

