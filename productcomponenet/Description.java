package productcomponenet;

import org.apache.commons.lang3.text.WordUtils;

public class Description {
    private String headline;
    private String description;
    
    public Description(String headline, String description) {
        this.headline = headline;
        this.description = description;
    }
    
    public String getHeadline() {
        return this.headline;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String toString() {
        String headline = "  -> " + getHeadline() + "\n";
        String starter = "     - ";
        String description = wrap(getDescription());
        return headline + starter + description;
    }
    
    private String wrap(String str) {
        return WordUtils.wrap(str, 64, "\n       ", true);
    }
    
    public String createDescriptionInput() {
        String input = "Description;";
        input = input + this.headline + ";" + this.description;
        return input;
    }
    
}
