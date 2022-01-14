package omazon;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CustomerDatabase {
    private String jdbcURL = "jdbc:mysql://localhost:3306/omazondb";
    private String username = "root";
    private String password = "Yjsh2027";
    
    //Insert data to database
    public void createAccount(int idCustomer, String username, String email, String password, String address) {

        try{
            Connection connection = DriverManager.getConnection(this.jdbcURL,this.username,this.password);
            
            String sql = "INSERT INTO users (idCustomer, Username, Email, Password, "
                    + "Address, Balance)"
                    + " VALUES (?,?,?,?,?,?)";
            
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,idCustomer);
            statement.setString(2,username);
            statement.setString(3,email);
            statement.setString(4,password);
            statement.setString(5,address);
            statement.setDouble(6, 0.0);
            
            statement.executeUpdate();
            connection.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    //Update data in database
    public void update(String columnName, String input, String customerID) {
       
        try{
            Connection connection = DriverManager.getConnection(this.jdbcURL,this.username,this.password);
            
            String sql = "UPDATE users SET "+ columnName +"='" + input +"' WHERE idCustomer='" + customerID + "'";          
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            connection.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    public boolean compareItemInRow(String input, String comparedItem){
        try{
            Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            
            String sql = "SELECT * FROM users";
            
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            boolean isExist = false;
            
            while(result.next()){
                if(input.equalsIgnoreCase("idCustomer")){
                    if (comparedItem.equals(result.getString(1))) {
                        isExist = true;
                    }
                }else if(input.equalsIgnoreCase("username")){
                    if (comparedItem.equals(result.getString(2))) {
                        isExist = true;
                    }
                }else if(input.equalsIgnoreCase("email")){
                    if (comparedItem.equals(result.getString(3))) {
                        isExist = true;
                    }
                }else if(input.equalsIgnoreCase("password")){
                    if (comparedItem.equals(result.getString(4))) {
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
    
    public void concatData(String input, String columnName, String customerID) {
        try{
            Connection connection = DriverManager.getConnection(jdbcURL,username,password);           
            String sql = "UPDATE users SET " + columnName + " = CONCAT(IFNULL(" + columnName + ",''), '" + input + "') WHERE idCustomer='" + customerID + "'";
            
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            connection.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    
    //Retrieve a specific row data from customer database
    public User createUserObject(String email){
        try{
            Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            
            String sql = "SELECT * FROM users WHERE Email ='" + email + "'";
            
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            int idCustomer = 0;
            double balance = 0;
            String name = "", userPassword = "", address = "", 
                    paymentPassword = "", favourite = "", orderHistory = "";
            while(result.next()){
                idCustomer = result.getInt(1);
                name = result.getString(2);
                userPassword = result.getString(4);
                address = result.getString(5);
                paymentPassword = result.getString(6);
                balance = result.getDouble(7);
                favourite = result.getString(9);
                orderHistory = result.getString(8);

            }
            connection.close();
            return new User(name, userPassword, email, idCustomer, address, paymentPassword, balance, favourite, orderHistory);
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public boolean checkSpecific(String columnName, String retrievedColumn, String checkItem, String keyInput){
        boolean isMatch = false;
        try{
            Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            
            String sql = "SELECT * FROM users WHERE " + columnName + "='" + keyInput + "'";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            while(result.next()){
                if(retrievedColumn.equalsIgnoreCase("idCustomer")){
                    if(result.getString(1).equals(checkItem)) {
                        isMatch = true;
                    }
                }else if(retrievedColumn.equalsIgnoreCase("username")){
                    if(result.getString(2).equals(checkItem)) {
                        isMatch = true;
                    }
                }else if(retrievedColumn.equalsIgnoreCase("email")){
                    if(result.getString(3).equals(checkItem)) {
                        isMatch = true;
                    }
                }else if(retrievedColumn.equalsIgnoreCase("password")){
                    if(result.getString(4).equals(checkItem)) {
                        isMatch = true;
                    }
                }else if(retrievedColumn.equalsIgnoreCase("address")){
                    if(result.getString(5).equals(checkItem)) {
                        isMatch = true;
                    }
                }else if(retrievedColumn.equalsIgnoreCase("payment password")){
                    if(result.getString(6).equals(checkItem)) {
                        isMatch = true;
                    }
                }else if(retrievedColumn.equalsIgnoreCase("balance")){
                    if(result.getString(7).equals(checkItem)) {
                        isMatch = true;
                    }
                }else if(retrievedColumn.equalsIgnoreCase("order history")){
                    if(result.getString(8).equals(checkItem)) {
                        isMatch = true;
                    }
                }else if(retrievedColumn.equalsIgnoreCase("favourite history")){
                    if(result.getString(9).equals(checkItem)) {
                        isMatch = true;
                    }
                }
            }
            connection.close();
            return isMatch;
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }    
    
    //Delete record from customer database
    public void delete(String idCustomer){
        try{
            Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            
            String sql = "DELETE FROM users WHERE idCustomer=?";
            PreparedStatement statement2 = connection.prepareStatement(sql);
            statement2.setString(1,idCustomer);          
            statement2.executeUpdate();

            connection.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }           
    }  
    
    public String retrieveUserData(int customerID, String columnName) {
        String output = "";
        try{
            Connection connection = DriverManager.getConnection(jdbcURL,username,password);            
            String sql = "SELECT * FROM users WHERE idCustomer ='" + customerID + "'";
           
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                if (columnName.equalsIgnoreCase("Username")) {
                    output = result.getString(2);
                } else if (columnName.equalsIgnoreCase("CartItem")) {
                    output = result.getString(10);
                } else if (columnName.equalsIgnoreCase("Balance")) {
                    output = result.getString(7);
                } else if (columnName.equalsIgnoreCase("OrderHistory")) {
                    output = result.getString(8);
                } else if (columnName.equalsIgnoreCase("Address")) {
                    output = result.getString(5);
                }
            }
            connection.close();
            return output;
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }
    
    public ArrayList<String> retrieveSpecificColumn(String input) {
        ArrayList<String> value = new ArrayList<>();
        try{
            Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            
            String sql = "SELECT * FROM users";
            
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            while(result.next()){
                if(input.equalsIgnoreCase("Username")){
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
    
    public String retrieveUserID(String columnName, String user){
        String output = "";
        try{
            Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            
            String sql = "SELECT * FROM users WHERE Username ='" + user + "'";
            
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            while(result.next()){
                if(columnName.equalsIgnoreCase("idCustomer")){
                    output = result.getString(1);
                }   
            }
            connection.close();
            return output;
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return null;
    } 
}
