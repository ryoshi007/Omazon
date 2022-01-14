package omazon;

public final class UserHolder {
  
    private User user;
    private final static UserHolder INSTANCE = new UserHolder();
    private String favouriteOrderPane;
    private String storePageOwner;

    UserHolder() {}

    public static UserHolder getInstance() {
      return INSTANCE;
    }

    public void setUser(User u) {
      this.user = u;
    }

    public User getUser() {
      return this.user;
    }

    public void setFavouriteOrderPane(String pane) {
        this.favouriteOrderPane = pane;
    }
    
    public String getFavouriteOrderPane() {
        return this.favouriteOrderPane;
    }
    
    public void setStorePageOwner(String owner) {
        this.storePageOwner = owner;
    }
    
    public String getStorePageOwner() {
        return this.storePageOwner;
    }
}