package omazon;

public class Content {
    private int index;
    private String content;
    
    public Content(int index, String content) {
        this.index = index;
        this.content = content;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public String getContent() {
        return this.content;
    }
}
