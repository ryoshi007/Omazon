package omazon;

public class Notification {
    private String productID;
    private String customerID;
    private int amount;
    private int index;
    
    public Notification(String productID, String customerID, int amount, int index) {
        this.productID = productID;
        this.customerID = customerID;
        this.amount = amount;
        this.index = index;
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
}
