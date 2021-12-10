package customer;

import serviceclass.SimilarityChecker;
import java.util.List;


public class CustomerManagement {
    private CustomerDatabase customerData;
    
    public CustomerManagement() {
        this.customerData = new CustomerDatabase();
    }
    
    public void addContentToCustomerFile(String input, String customerUsername) {
        String fileName = this.checkFileName(customerUsername);
        if (fileName != null) {
            this.customerData.addContentToFile(fileName, input);
        }
    }
    
    public void deleteContentFromCustomerFile(String input, String customerUsername) {
        String fileName = this.checkFileName(customerUsername);
        if (fileName != null) {
            this.customerData.deleteContentFromFile(fileName, input);
        }
    }
    
    public void changeDataInCustomerFile(String oldInput, String newInput, String customerUsername) {
        String fileName = this.checkFileName(customerUsername);
        if (fileName != null) {
            this.customerData.changeContentInFile(fileName, oldInput, newInput);
        }
    }
    
    public void deleteCustomer(String customerUsername) {
        String fileName = this.checkFileName(customerUsername);
        this.customerData.deleteFile(fileName);
    }
    
    private String checkFileName(String customerUsername) {
        List<String> fileList = this.customerData.returnAllFile();
        String possibleFileName = "";
        double existingSimilarityPoint = 0;
        for (String fileName : fileList) {
            SimilarityChecker checker = new SimilarityChecker();
            double similarity = checker.similarity(customerUsername, fileName);
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
