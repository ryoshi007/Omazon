package omazon;

public class User {
    private String username;
    private String password;
    private String email;
    private int id;
    private double balance;
    private String address;
    private String paymentPassword;
    private String favourite;
    private String order;

    public User(String username, String password, String email, int id,
            String address, String paymentPassword, double balance, String favourite,
            String order){
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.paymentPassword = paymentPassword;
        this.balance = balance;
        this.id = id;
        this.favourite = favourite;
        this.order = order;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getAddress() {
        return this.address;
    }
    
    public void setPaymentPassword(String paymentPassword) {
        this.paymentPassword = paymentPassword;
    }
    
    public String getPaymentPassword() {
        return this.paymentPassword;
    }
    
    public String getID() {
        return String.valueOf(this.id);
    }
    
    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }
    
    public String getFavourite() {
        return this.favourite;
    }
    
    public double getBalance() {
        return this.balance;
    }
    
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    public void setOrder(String order) {
        this.order = order;
    }
    
    public String getOrder() {
        return this.order;
    }
    
    public String toString() {
        return "Email: " + this.email + "\nUsername: " + this.username + 
                "\nPassword: " + this.password + "\nPayment Password: " + this.paymentPassword +
                "\nAddress: " + this.address;
    }
}
 
