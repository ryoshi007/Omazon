package CustomerInterface;
import ServiceClass.SimilarityChecker;
import java.util.ArrayList;
import java.util.List;


public class CustomerManagement {
    private ArrayList<Customer> customerList;
    private CustomerDatabase customerData;
    
    public CustomerManagement() {
        this.customerList = new ArrayList<>();
        this.customerData = new CustomerDatabase();
    }
    
    public void addCustomer(Customer newCustomer) {
        this.customerList.add(newCustomer);
    }
    
    public int getSize() {
        return this.customerList.size();
    }
    
    public void listName() {
        for (Customer customer : this.customerList) {
            System.out.println(customer.getusername());
        }
    }
    
    public void addContentToCustomerFile(String input, String customerusername) {
        String fileName = this.checkFileName(customerusername);
        if (fileName != null) {
            this.customerData.addContentToFile(fileName, input);
        }
    }
    
    public void deleteContentFromProductFile(String input, String customerusername) {
        String fileName = this.checkFileName(customerusername);
        if (fileName != null) {
            this.customerData.deleteContentFromFile(fileName, input);
        }
    }
    
    public void changeDataInProductFile(String oldInput, String newInput, String customerusername) {
        String fileName = this.checkFileName(customerusername);
        if (fileName != null) {
            this.customerData.changeContentInFile(fileName, oldInput, newInput);
        }
    }
    
    public void deleteCustomer(String customerusername) {
        String fileName = this.checkFileName(customerusername);
        this.customerData.deleteFile(fileName);
    }
    
    private String checkFileName(String customerusername) {
        List<String> fileList = this.customerData.returnAllFile();
        String possibleFileName = "";
        double existingSimilarityPoint = 0;
        for (String fileName : fileList) {
            SimilarityChecker checker = new SimilarityChecker();
            double similarity = checker.similarity(customerusername, fileName);
            if (similarity > existingSimilarityPoint) {
                possibleFileName = fileName;
                existingSimilarityPoint = similarity;
            }           
        }
        
        if (possibleFileName.isBlank()) {
            System.out.println("The file name cannot be found");
            return null;
        } else {
            return possibleFileName;
        }
    }
}
