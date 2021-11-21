package productcomponenet;

import org.apache.commons.lang3.text.WordUtils;

public class Review {
    private String username;
    private String review;
    
    public Review(String username, String review) {
        this.username = username;
        this.review = review;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public String getReview() {
        return this.review;
    }
    
    public String toString() {
        String starter = this.getUsername() + ": ";
        String indentSpace = createIndention(starter.length());
        String review = wrap(getReview(), indentSpace);
        return starter + review;     
    }
    
    private String createIndention(int indentAmount) {
        String space = "";
        for (int i = 0; i < indentAmount; i++) {
            space += " ";
        }
        return space;
    }
    
    private String wrap(String str, String indent) {
        int maxLength = 70 - indent.length();
        return WordUtils.wrap(str, maxLength, "\n" + indent, true);
    }
    
    public String createReviewInput() {
        String input = "Review;";
        input = input + this.username + ";" + this.review;
        return input;
    }
   
}
