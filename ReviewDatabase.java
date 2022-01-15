package omazon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class ReviewDatabase {
    private String jdbcURL = "jdbc:mysql://localhost:3306/omazondb";
    private String username = "root";
    private String password = "Yjsh2027";
    
    //Insert data to database
    public void createData(String idProduct, String idOwner, String idCustomer, int orderedAmount, int index, double payAmount) {

        try{
            Connection connection = DriverManager.getConnection(this.jdbcURL,this.username,this.password);
            
            String sql = "INSERT INTO review (idproduct,idowner,idcustomer,id,orderamount,fullfill,payamount)"
                    + " VALUES (?,?,?,?,?,?,?)";
            
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,idProduct);
            statement.setString(2,idOwner);
            statement.setString(3,idCustomer);
            statement.setInt(4, index);
            statement.setInt(5, orderedAmount);
            statement.setBoolean(6, false);
            statement.setDouble(7, payAmount);
            statement.executeUpdate();
            connection.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    //Update data in database
    public void update(String columnName, String input, String idproduct, String idcustomer) {
        try{
            Connection connection = DriverManager.getConnection(this.jdbcURL,this.username,this.password);
                
            String sql = "UPDATE review SET "+ columnName +"='" + input +"' WHERE idproduct='" + idproduct +"' AND idcustomer='" + idcustomer + "'";
           
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            
            connection.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }    
    
    //Delete record from review database
    public void delete(String idproduct, int index){
        try{
            Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            
            String sql = "DELETE FROM review WHERE idproduct='" + idproduct + "' AND index ='" + index + "'";
            PreparedStatement statement2 = connection.prepareStatement(sql);
            statement2.setString(1,idproduct);

            
            int rows = statement2.executeUpdate();
            
            if(rows>0){
                System.out.println("The review has been deleted succesfully");
            }
            connection.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }          
    }
    
    public boolean checkSpecific(String columnName, String retrievedColumn, String checkItem, String keyInput){
        boolean isMatch = false;
        try{
            Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            
            String sql = "SELECT * FROM review WHERE " + columnName + "='" + keyInput + "'";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            while(result.next()){
                if(retrievedColumn.equalsIgnoreCase("idproduct")){
                    if(result.getString(1).equals(checkItem)) {
                        isMatch = true;
                    }
                }else if(retrievedColumn.equalsIgnoreCase("idowner")){
                    if(result.getString(2).equals(checkItem)) {
                        isMatch = true;
                    }
                }else if(retrievedColumn.equalsIgnoreCase("idcustomer")){
                    if(result.getString(3).equals(checkItem)) {
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

    public ArrayList<String> retrieveSpecificColumn(int productID, String columnName) {
        ArrayList<String> output = new ArrayList<>();
        try{
            Connection connection = DriverManager.getConnection(jdbcURL,username,password);            
            String sql = "SELECT * FROM review WHERE idproduct ='" + productID + "'";
           
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                if (columnName.equalsIgnoreCase("idcustomer")) {
                    output.add(result.getString(3));
                } else if (columnName.equalsIgnoreCase("review")) {
                    output.add(result.getString(4));
                } else if (columnName.equalsIgnoreCase("ownerreply")) {
                    output.add(result.getString(5));
                } else if (columnName.equalsIgnoreCase("index")) {
                    output.add(result.getString(7));
                }
            }
            connection.close();
            return output;
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }
    
    public void updateNull(String columnName, String idproduct, String idcustomer) {
        try{
            Connection connection = DriverManager.getConnection(this.jdbcURL,this.username,this.password);

            String sql = "UPDATE review SET "+ columnName +"=null WHERE idproduct='" + idproduct +"' AND idcustomer='" + idcustomer + "'";

            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);

            connection.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    public ArrayList<Notification> obtainNotification(String ownerID) {
        ArrayList<Notification> notificationList = new ArrayList<>();
        try{
            Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            
            String sql = "SELECT * FROM review WHERE idowner ='" + ownerID + "' AND fullfill='" + 0 + "'";
            
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            while(result.next()){
                String idproduct = result.getString(1);
                String idcustomer = result.getString(3);
                int orderAmount = result.getInt(8);
                int index = result.getInt(7);
                Date datetime = result.getTimestamp(10);
                double payAmount = result.getDouble(11);
                
                Notification newNotification = new Notification(idproduct, idcustomer, orderAmount, index, datetime, payAmount);
                notificationList.add(newNotification);
            }
            connection.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return notificationList;
    }
    
    public ArrayList<Notification> obtainTransaction(String ownerID, String productID) {
        ArrayList<Notification> notificationList = new ArrayList<>();
        try{
            Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            
            String sql = "SELECT * FROM review WHERE idproduct='" + productID + "' AND idowner='" + ownerID + "'";
            
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            while(result.next()){
                String idproduct = result.getString(1);
                String idcustomer = result.getString(3);
                int orderAmount = result.getInt(8);
                int index = result.getInt(7);
                Date datetime = result.getTimestamp(10);
                double payAmount = result.getDouble(11);
                
                Notification newNotification = new Notification(idproduct, idcustomer, orderAmount, index, datetime, payAmount);
                notificationList.add(newNotification);
            }
            connection.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return notificationList;
    }
    
    public void updateDate(int index) {
        try{
            Connection connection = DriverManager.getConnection(this.jdbcURL,this.username,this.password);
            Date date = new Date();
            java.sql.Date sql1;date = new java.sql.Timestamp(date.getTime());
            String sql = "UPDATE review SET ordertime ='"+ date + "' WHERE id='" + index + "'";
           
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            
            connection.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    public void updateFullfill(int index) {
        try{
            Connection connection = DriverManager.getConnection(this.jdbcURL,this.username,this.password);
            String sql = "UPDATE review SET fullfill = '1' WHERE id='" + index + "'";
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            
            connection.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    public int retrieveIndex() {
        int index = 1;
        try{
            Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            
            String sql = "SELECT * FROM review";
            
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            while(result.next()){
                int currentIndex = result.getInt(7);
                if (currentIndex > index) {
                    index = currentIndex;
                }
            }
            connection.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return index;
    }
    
    public double calculateSum(int idproduct){
        double output =0.0;
        try{
            Connection connection = DriverManager.getConnection(this.jdbcURL,this.username,this.password);
            
            String sql = "SELECT SUM(payamount) FROM review WHERE idproduct='" + idproduct +"'";
           
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while(result.next()){
                output += result.getDouble(1);
            }
            connection.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return output;
    }

}
