package omazon;

public class CategoryHolder {
    private static String categoryName;
    
    public void setCategoryName(String name) {
        categoryName = name;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
}
