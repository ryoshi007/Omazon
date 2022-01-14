package omazon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ProductDatabase {
    private String jdbcURL = "jdbc:mysql://localhost:3306/omazondb";
    private String username = "root";
    private String password = "Yjsh2027";
    
    public void createProduct(int idproduct,String productName,double price,
            int stock, int sales, String category, int ownerID, double rating, String description, String imagePath) {
        try{
            Connection connection = DriverManager.getConnection(this.jdbcURL,this.username,this.password);
            
            String sql = "INSERT INTO products (idproduct, productName, price, "
                    + "sales, stock, category, idOwner, rating, description, imagePath)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?)";
            
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,idproduct);
            statement.setString(2,productName);
            statement.setDouble(3,price);
            statement.setInt(4,sales);
            statement.setInt(5,stock);
            statement.setString(6,category);
            statement.setInt(7,ownerID);
            statement.setDouble(8,rating);
            statement.setString(9,description);
            statement.setString(10, imagePath);          
            statement.executeUpdate();
            connection.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    public void update(String columnName, String input, String idproduct) {
        try{
            Connection connection = DriverManager.getConnection(this.jdbcURL,this.username,this.password);
            String sql = "UPDATE products SET "+ columnName +"='" + input +"' WHERE idproduct='" + idproduct + "'";
           
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            
            connection.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    public ArrayList<Product> retrieveProduct(String input, String columnName){
        ArrayList<Product> productList = new ArrayList<>();
        String sql = "";
        try{
            Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            
            if (columnName.equalsIgnoreCase("idOwner")) {
                sql = "SELECT * FROM products WHERE idOwner ='" + input + "'";
            } else if (columnName.equalsIgnoreCase("category")) {
                sql = "SELECT * FROM products WHERE category ='" + input + "'";
            }
        
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            while(result.next()){
                int productID = result.getInt(1);
                String productName = result.getString(2);
                double price = result.getDouble(3);
                int sales = result.getInt(4);
                int stock = result.getInt(5);
                String category = result.getString(6);
                int owner = result.getInt(7);
                double rating = result.getDouble(8);
                String description = result.getString(9);
                String imagePath = result.getString(10);
                
                Product product = new Product(productName, price, rating, stock,
                sales, description, category, owner, productID, imagePath);
                productList.add(product);
            }
            connection.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return productList;
    }
    
    public Product retrieveSpecificProduct(String input, String columnName){
        Product product = null;
        String sql = "";
        try{
            Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            
            if (columnName.equalsIgnoreCase("idproduct")) {
                sql = "SELECT * FROM products WHERE idproduct ='" + input + "'";
            } else if (columnName.equalsIgnoreCase("productName")) {
                sql = "SELECT * FROM products WHERE productName ='" + input + "'";
            }
            
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            while(result.next()){
                int productID = result.getInt(1);
                String productName = result.getString(2);
                double price = result.getDouble(3);
                int sales = result.getInt(4);
                int stock = result.getInt(5);
                String category = result.getString(6);
                int owner = result.getInt(7);
                double rating = result.getDouble(8);
                String description = result.getString(9);
                String imagePath = result.getString(10);
                
                product = new Product(productName, price, rating, stock,
                sales, description, category, owner, productID, imagePath);                
            }
            connection.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return product;
    }

    public String retrieveSpecificProductInfo(String idproduct, String input){
        String output = "";
        try{
            Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            
            String sql = "SELECT * FROM products WHERE idproduct ='" + idproduct + "'";
            
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            while(result.next()){
                if(input.equalsIgnoreCase("productid")){
                    output = result.getString(1);
                }else if(input.equalsIgnoreCase("productName")){
                    output = result.getString(2);
                }else if(input.equalsIgnoreCase("price")){
                    output = result.getString(3);
                }else if(input.equalsIgnoreCase("sales")){
                    output = result.getString(4);
                }else if(input.equalsIgnoreCase("stock")){
                    output = result.getString(5);
                }else if(input.equalsIgnoreCase("category")){
                    output = result.getString(6);
                }else if(input.equalsIgnoreCase("owner")){
                    output = result.getString(7);
                }else if(input.equalsIgnoreCase("rating")){
                    output = result.getString(8);
                }else if(input.equalsIgnoreCase("description")){
                    output = result.getString(9);
                }
            }
            connection.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return output;
    }    
    
    public void delete(String idproduct){
        try{
            Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            
            String sql = "DELETE FROM products WHERE idproduct=?";
            PreparedStatement statement2 = connection.prepareStatement(sql);
            statement2.setString(1,idproduct);
            statement2.executeUpdate();
            connection.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
           
    }
    
    public String retrieveDescription(String idproduct, String input){
        String output = "";
        try{
            Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            
            String sql = "SELECT * FROM products WHERE idproduct ='" + idproduct + "'";
            
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while(result.next()){
                if(input.equalsIgnoreCase("description")){
                    output = result.getString(8);
                }
            }
            connection.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return output;
    }
    
    public boolean compareItemInRow(String input, String comparedItem){
        try{
            Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            
            String sql = "SELECT * FROM products";
            
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            boolean isExist = false;
            
            while(result.next()){
                if(input.equalsIgnoreCase("idproduct")){
                    if (comparedItem.equals(result.getString(1))) {
                        isExist = true;
                    }
                }else if(input.equalsIgnoreCase("productName")){
                    if (comparedItem.equals(result.getString(2))) {
                        isExist = true;
                    }
                }else if(input.equalsIgnoreCase("idOwner")){
                    if (comparedItem.equals(result.getString(7))) {
                        isExist = true;
                    }
                }
            }
            connection.close();
            return isExist;
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }
    
    public ArrayList<String> retrieveSpecificColumn(String input) {
        ArrayList<String> value = new ArrayList<>();
        try{
            Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            
            String sql = "SELECT * FROM products";
            
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            while(result.next()){
                if(input.equalsIgnoreCase("sales")){
                    value.add(result.getString(4));                   
                } else if(input.equalsIgnoreCase("idproduct")){
                    value.add(result.getString(1));
                } else if (input.equalsIgnoreCase("productName")) {
                    value.add(result.getString(2));
                }
            }
            connection.close();
            return value;
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }
}
