package omazon;

public final class ProductHolder {
  
  private Product product;
  private final static ProductHolder INSTANCE = new ProductHolder();
  
  ProductHolder() {}
  
  public static ProductHolder getInstance() {
    return INSTANCE;
  }
  
  public void setProduct(Product p) {
    this.product = p;
  }
  
  public Product getProduct() {
    return this.product;
  }
}
