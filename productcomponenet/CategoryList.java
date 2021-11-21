package productcomponenet;

import java.util.ArrayList;

public class CategoryList {
    private ArrayList<Category> categoryList;
    
    public CategoryList() {
        this.categoryList = new ArrayList<>();
        String[] list = {"Automotive Parts & Accessories", "Electronics", "Groceries", "Arts, Crafts & Sewing", "Home & Kitchen"
            ,"Office supplies", "Pet supplies", "Books, Magazines & Newspapers", "Style & Fashion", "Beauty & Personal Care", "Toys & Games",
            "Garden & Outdoor", "Exercise/Fitness supplies", "Medical supplies", "Vitamins & Dietary Supplements", "Others"
        };
        createCategoryList(list);
    }
    
    public ArrayList<Category> getCategoryList() {
        return this.categoryList;
    }
    
    public void createCategoryList(String[] list) {
        for (int i = 0; i < list.length; i++) {
            Category newCategory = new Category(list[i]);
            this.categoryList.add(newCategory);
        }
    }
    
}
