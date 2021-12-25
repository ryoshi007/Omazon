package customer;

import java.util.ArrayList;


public class CustomerManagement {
    private CustomerDatabase customerData;
    
    public CustomerManagement() {
        this.customerData = new CustomerDatabase();
    }
    
    public void addContentToCustomerFile(String input, int customerID) {
        this.customerData.addContentToFile(customerID, input);
    }
    
    public void deleteContentFromCustomerFile(String input, int customerID) {
        this.customerData.deleteContentFromFile(customerID, input);
    }
    
    public void changeDataInCustomerFile(String oldInput, String newInput, int customerID) {
        this.customerData.changeContentInFile(customerID, oldInput, newInput);
    }
    
    public void deleteCustomer(int customerID) {
        this.customerData.deleteFile(customerID);
    }
    
    public ArrayList<Customer> searchSeller(String input) {
        CustomerList customerList = customerData.loadFile();
        ArrayList<Customer> seller = new ArrayList<>(); 
        for (int i = 0; i < customerList.getSize(); i++) {
            Customer customer = customerList.getCustomer(i);
            if(customer.getUsername().toLowerCase().contains(input.toLowerCase())) {
                seller.add(customer);
            }
        }
        return seller;
    }
    
    public Customer searchSellerById(int sellerID) {
        CustomerList customerList = customerData.loadFile();
        for (int i = 0; i < customerList.getSize(); i++) {
            if (customerList.getCustomer(i).getID() == sellerID) {
                return customerList.getCustomer(i);
            }
        }
        return null;
    }
    
}

