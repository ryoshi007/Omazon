package productcomponenet;

import product.Product;

public class Transaction {
    private Product product;
    private int amount;
    
    public Transaction(Product product, int amount) {
        this.product = product;
        this.amount = amount;
    }
    
    public Product getProduct() {
        return this.product;
    }
    
    public int getAmount() {
        return this.amount;
    }
}
