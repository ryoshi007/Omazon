package customer;

import java.util.ArrayList;

public class CustomerList {
    private ArrayList<Customer> customerList;
    
    public CustomerList() {
        this.customerList = new ArrayList<>();
    }
    
    public void addCustomer(Customer newCustomer) {
        this.customerList.add(newCustomer);
    }
    
    public int getSize() {
        return this.customerList.size();
    }
    
    public void listName() {
        for (Customer customer : this.customerList) {
            System.out.println(customer.getUsername());
        }
    }
    
    public Customer getCustomer(int index) {
        return customerList.get(index);
    }
    
    public Customer verifyUser(String email) {
        for(int i = 0; i < customerList.size(); i++) {
            if (customerList.get(i).getEmail().equals(email)) {
                return customerList.get(i);
            }
        }
        System.out.println("The customer isn't registered");
        return null;
    }
}
