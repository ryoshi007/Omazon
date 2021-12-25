package product;

import customer.Customer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import productcomponenet.Category;

public class ProductManagement {
    private ArrayList<Product> productList;
    private ProductDatabase productData;
    
    public ProductManagement() {
        this.productList = new ArrayList<>();
        this.productData = new ProductDatabase();
    }
    
    public ArrayList<Product> topThreeProduct() {
        Collections.sort(this.productList, (p1, p2) -> p2.getSalesVolume() - p1.getSalesVolume());
        ArrayList<Product> topThree = new ArrayList<>();
        topThree.add(this.productList.get(0));
        topThree.add(this.productList.get(1));
        topThree.add(this.productList.get(2));
        return topThree;
    }
    
    public void addProduct(Product newProduct) {
        this.productList.add(newProduct);
    }
    
    public int getSize() {
        return this.productList.size();
    }
    
    public void listName() {
        for (Product product : this.productList) {
            System.out.println(product.getName());
        }
    }
    
    public ArrayList<Product> searchProduct(String searchItem) {
        ArrayList<Product> relatedProduct = new ArrayList<>();
        for (Product product : this.productList) {
            if (product.getName().toLowerCase().contains(searchItem.toLowerCase())) {
                relatedProduct.add(product);
            }
        }
        return relatedProduct;
    }
    
    public ArrayList<Product> getProductByCategory (Category category) {
        ArrayList<Product> relatedCategory = new ArrayList<>();
        for (Product product : this.productList) {
            if (product.getCategory().getName().equals(category.getName())) {
                relatedCategory.add(product);
            }
        }
        return relatedCategory;
    }
    
    public void addContentToProductFile(String input, int productID) {
        this.productData.addContentToFile(productID, input);
    }
    
    public void deleteContentFromProductFile(String input, int productID) {
        this.productData.deleteContentFromFile(productID, input);
    }
    
    public void changeDataInProductFile(String oldInput, String newInput, int productID) {
        this.productData.changeContentInFile(productID, oldInput, newInput);
    }
    
    public void addNewProduct(Product newProduct) {
        List<String> contents = new ArrayList<>();
        String name = "Name;" + newProduct.getName();
        
        String priceInString;
        if (newProduct.getPrice() % 1 == 0) {
            priceInString = String.format("%.0f", newProduct.getPrice());
        } else {
            priceInString = String.valueOf(newProduct.getPrice());
        }
        
        String price = "Price;" + priceInString;
        String sales = "Sales;" + newProduct.getSalesVolume();
        String stock = "Stock;" + newProduct.getStock();
        String category = "Category;" + newProduct.getCategory().getName();
        String owner = "Owner;" + newProduct.getOwner().getID();
        int id = createUniqueID();
        String idString = "ID;" + id;
        
        contents.add(name);
        contents.add(price);
        contents.add(sales);
        contents.add(stock);
        contents.add(category);
        contents.add(owner);
        contents.add(idString);
        
        this.productData.addFile(contents, id);
    }
    
    public void deleteProduct(int productID) {
        this.productData.deleteFile(productID);
    }
    
    public int createUniqueID() {
        int id = 0;
        Random random = new Random();
        id = random.nextInt(99999999);
        
        for (int i = 0; i < this.productList.size(); i++) {
            if (productList.get(i).getID() == id) {
                id = random.nextInt(99999999);
                i = 0;
            }
        }
        return id;
    }
    
    public ArrayList<Product> returnSellerProduct(Customer seller) {
        ArrayList<Product> sellerProducts = new ArrayList<>();
        for (Product product : productList) {
            if (product.getOwner().getID() == seller.getID()) {
                sellerProducts.add(product);
            }
        }
        return sellerProducts;
    }
    
    //Create this first in main file
    //ProductManagement productManagement = new ProductDatabase().loadFile();
    public Product searchProductByID(int productID) {
        for (Product product : productList) {
            if (product.getID() == productID) {
                return product;
            }
        }
        return null;
    }
    
}
