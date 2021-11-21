package product;

import serviceclass.SimilarityChecker;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    
    public ArrayList<Product> search(String searchItem) {
        ArrayList<Product> relatedProduct = new ArrayList<>();
        for (Product product : this.productList) {
            if (product.getName().toLowerCase().contains(searchItem)) {
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
    
    public void addContentToProductFile(String input, String productName) {
        String fileName = this.checkFileName(productName);
        if (fileName != null) {
            this.productData.addContentToFile(fileName, input);
        }
    }
    
    public void deleteContentFromProductFile(String input, String productName) {
        String fileName = this.checkFileName(productName);
        if (fileName != null) {
            this.productData.deleteContentFromFile(fileName, input);
        }
    }
    
    public void changeDataInProductFile(String oldInput, String newInput, String productName) {
        String fileName = this.checkFileName(productName);
        if (fileName != null) {
            this.productData.changeContentInFile(fileName, oldInput, newInput);
        }
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
        
        contents.add(name);
        contents.add(price);
        contents.add(sales);
        contents.add(stock);
        contents.add(category);
        this.productData.addFile(contents, newProduct.getName());
    }
    
    public void deleteProduct(String productName) {
        String fileName = this.checkFileName(productName);
        this.productData.deleteFile(fileName);
    }
    
    private String checkFileName(String productName) {
        List<String> fileList = this.productData.returnAllFile();
        String possibleFileName = "";
        double existingSimilarityPoint = 0;
        for (String fileName : fileList) {
            SimilarityChecker checker = new SimilarityChecker();
            double similarity = checker.similarity(productName, fileName);
            if (similarity > existingSimilarityPoint) {
                possibleFileName = fileName;
                existingSimilarityPoint = similarity;
            }           
        }
        
        if (possibleFileName.isBlank()) {
            System.out.println("The file name cannot be found");
            return null;
        } else {
            return possibleFileName;
        }
    }
    
}
