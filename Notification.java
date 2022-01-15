package omazon;

import java.util.Date;

public class Notification {
    private String productID;
    private String customerID;
    private int amount;
    private int index;
    private Date currentDate;
    private double payAmount;
    
    public Notification(String productID, String customerID, int amount, int index, Date currentDate, double payAmount) {
        this.productID = productID;
        this.customerID = customerID;
        this.amount = amount;
        this.index = index;
        this.currentDate = currentDate;
        this.payAmount = payAmount;
    }
    
    public String getProductID() {
        return this.productID;
    }
    
    public String getCustomerID() {
        return this.customerID;
    }
    
    public int getAmount() {
        return this.amount;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public Date getCurrentDate() {
        return this.currentDate;
    }
    
    public double getPayAmount() {
        return this.payAmount;
    }
}
