package omazon;

public class Product {
    private String name;
    private double price;
    private double rating;
    private int stockCount;
    private int salesCount;
    private String description;
    private String category;
    private int ownerID;
    private int productID;
    private String imagePath;
    
    public Product(String name, double price, double rating, int stockCount, int salesCount,
            String description, String category, int ownerID, int productID, String imagePath) {
        this.name = name;
        this.price = price;
        this.rating = rating;
        this.stockCount = stockCount;
        this.salesCount = salesCount;
        this.description = description;
        this.category = category;
        this.ownerID = ownerID;
        this.productID = productID;
        this.imagePath = imagePath;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public double getPrice() {
        return this.price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public double getRating() {
        return this.rating;
    }
    
    public void setRating(double rating) {
        this.rating = rating;
    }
    
    public int getStock() {
        return this.stockCount;
    }
    
    public void setStock(int stock) {
        this.stockCount = stock;
    }
    
    public int getSales() {
        return this.salesCount;
    }
    
    public void setSales(int sales) {
        this.salesCount = sales;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCategory() {
        return this.category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public int getOwnerID() {
        return this.ownerID;
    }
    
    public int getProductID() {
        return this.productID;
    }
    
    public String getImagePath() {
        return this.imagePath;
    }
    
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
    public String toString() {
        return this.name;
    }
}
