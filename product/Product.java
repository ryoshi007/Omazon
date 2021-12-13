package product;

import customer.Customer;
import productcomponenet.Review;
import productcomponenet.Description;
import productcomponenet.Category;
import java.util.ArrayList;
import java.util.Scanner;


public class Product {
    private String name;
    private double price;
    private String rating;
    private ArrayList<Review> reviews;
    private int stockCount;
    private int salesCount;
    private ArrayList<Description> description;
    private boolean bestSelling;
    private Category category;
    private Customer owner;
    private int id;
    
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_GREEN = "\u001B[32m";
    
    public Product(String name, double price, int stock, int salesCount, Category category, Customer owner, int id) {
        this.name = name;
        this.price = price;
        this.rating = "* * * * *";
        this.reviews = new ArrayList<>();
        this.stockCount = stock;
        this.salesCount = salesCount;
        this.bestSelling = false;
        this.description = new ArrayList<>();
        this.category = category;
        this.owner = owner;
        this.id = id;
    }
    
    //Initialize new product
    public Product(String name, double price, int stock, Category category, Customer owner) {
        this.name = name;
        this.price = price;
        this.rating = "";
        this.reviews = new ArrayList<>();
        this.stockCount = stock;
        this.salesCount = 0;
        this.bestSelling = false;
        this.description = new ArrayList<>();
        this.category = category;
        this.owner = owner;
    }
    
    public String getName() {
        return this.name;
    }
    
    public double getPrice() {
        return this.price;
    }
    
    public String getRating() {
        return this.rating;
    }
    
    public void setRating(String rating) {
        this.rating = rating;
    }
    
    public void addReviews(Review review) {
        this.reviews.add(review);
    }
    
    public ArrayList<Review> getReviews() {
        return this.reviews;
    }
    
    public int getStock() {
        return this.stockCount;
    }
    
    public int getSalesVolume() {
        return this.salesCount;
    }
    
    public void increaseSalesVolume(int amount) {
        this.salesCount += amount;
    }
    
    public void addDescription(Description newDescription) {
        this.description.add(newDescription);
    }
    
    public ArrayList<Description> getDescription() {
        return this.description;
    }
    
    public Category getCategory() {
        return this.category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }
    
    public void setName(String newName) {
        this.name = newName;
    }
    
    public Customer getOwner() {
        return this.owner;
    }
    
    public int getID() {
        return this.id;
    }
    
    public String changeProductName() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the new name: ");
        String newName = scanner.nextLine();
        String oldName = this.name;
        setName(newName);
        System.out.println(TEXT_GREEN + "The name of the product has been changed successfully" + TEXT_RESET);
        return oldName;
    }
    
    public void setPrice(double newPrice) {
        this.price = newPrice;
    }
    
    public Double changeProductPrice() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the new price: RM");
        double newPrice = scanner.nextDouble();
        double oldPrice = this.price; 
        setPrice(newPrice);
        System.out.println(TEXT_GREEN + "The price of the product has been changed successfully" + TEXT_RESET);
        return oldPrice;
    }
    
    public void setStockAmount(int newStockAmount) {
        this.stockCount = newStockAmount;
    }
    
    public int changeProductStockAmount() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the latest stock amount available: ");
        int newStockAmount = scanner.nextInt();
        
        int oldStockAmount = this.stockCount;
        setStockAmount(newStockAmount);
        System.out.println(TEXT_GREEN + "The stock amount of the product has been changed successfully" + TEXT_RESET);
        return oldStockAmount;
    }
    
    public int deduceStockAmount(int amount) {
        int oldStock = this.stockCount;
        this.stockCount -= amount;
        return oldStock;
    }
    
    public int changeSalesAmount(int amount) {
        int oldSales = this.salesCount;
        this.salesCount += amount;
        return oldSales;
    }
    
    public String addNewReview(String username) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your review: ");
        String review = scanner.nextLine();
        Review newReview = new Review(username, review);
        String reviewInput = newReview.createReviewInput();
        this.reviews.add(newReview);
        System.out.println(TEXT_GREEN + "Your review has been added successfully." + TEXT_RESET);
        return reviewInput;
    }
    
    public String addNewDescription() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Headline: ");
        String headline = scanner.nextLine();
        System.out.print("Description of headline: ");
        String description = scanner.nextLine();
        Description newDescription = new Description(headline, description);
        String descriptionInput = newDescription.createDescriptionInput();
        this.description.add(newDescription);
        System.out.println(TEXT_GREEN + "Your description has been added successfully." + TEXT_RESET);
        return descriptionInput;
    }
    
    public void removeReview(int index) {
        this.reviews.remove(index);
        System.out.println(TEXT_GREEN + "The review has been deleted successfully." + TEXT_RESET);
    }
    
    public void removeDescription(int index) {
        this.description.remove(index);
        System.out.println(TEXT_GREEN + "The description has been deleted successfully." + TEXT_RESET);
    }
    
    public boolean equals(Object comparedObject) {
        if(this == comparedObject) {
            return true;
        }
        
        if(!(comparedObject instanceof Product)) {
            return false;
        }
        
        Product comparedProduct = (Product) comparedObject;
        
        if(this.name.equals(comparedProduct.name) && (this.price == comparedProduct.price) 
                && (this.salesCount == comparedProduct.salesCount) 
                && (this.stockCount == comparedProduct.stockCount 
                && this.category.getName().equals(comparedProduct.category.getName()))) {
            return true;
        }
        return false;
    }
    
}

