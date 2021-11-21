package product;

import java.util.List;
import product.ProductManagement;

public interface Database {
    public List<String> returnAllFile();
    public ProductManagement loadFile();
    public void addContentToFile(String file, String input);
    public void deleteContentFromFile(String file, String deleteInput);
    public void changeContentInFile(String file, String oldInput, String newInput);
    public void addFile(List<String> contents, String name);
    public void deleteFile(String file);
}
