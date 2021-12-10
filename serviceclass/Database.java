package serviceclass;

import java.util.List;

public interface Database {
    public List<String> returnAllFile();
    public Object loadFile();
    public void addContentToFile(String file, String input);
    public void deleteContentFromFile(String file, String deleteInput);
    public void changeContentInFile(String file, String oldInput, String newInput);
    public void addFile(List<String> contents, String name);
    public void deleteFile(String file);
}
