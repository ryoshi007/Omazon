package omazon;

import java.util.Properties;
import javax.mail.Session;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
        
public class Email {

    public void sendEmail(String recipient, String content) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        
        String email = "omazonhelpdesk@gmail.com";
        String password = "omazon123";
        
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });

        Message message = prepareMessage(session, email, recipient, content);
        
        Transport.send(message);   
    }
    
    public String prepareBuyerContent(String productName, String purchaseAmount, double purchasePrice) {
        String header = "<h1> Your order has been confirmed! </h1>";
        String name = "<h2> Product Name - " + productName + "</h2>";
        String amount = "<h3> Purchase Amount - RM " + purchaseAmount + "</h3>";
        String paid = "<h3> Total Price - " + purchasePrice + "</h3>";
        String ending = "<h5> Keep this email as a proof of purchase! </h5>";
        
        String content = header + "\n" + name + "\n" + amount + "\n" + paid + "\n" + ending;
        return content;
    }
    
    public String prepareSellerContent(String productName, String purchaseAmount, double purchasePrice, String buyerName, String buyerAddress) {
        String header = "<h1> An order has been made! </h1>";
        String name = "<h2> Product Name - " + productName + "</h2>";
        String amount = "<h3> Purchase Amount - " + purchaseAmount + "</h3>";
        String paid = "<h3> Total Price - " + purchasePrice + "</h3>";
        String info = "<h2> Buyer's Info </h2>";
        String buyer = "<h3> Buyer's name: " + buyerName + "</h3>";
        String address = "<h3> Buyer's address: " + buyerAddress + "</h3>"; 
        String ending = "<h5> Keep this email as a proof of purchase! </h5>";
        
        String content = header + "\n" + name + "\n" + amount + "\n" + paid + "\n\n" + info + "\n" + buyer + "\n" + address + "\n" + ending;
        return content;
    }
    
    private static Message prepareMessage(Session session, String sender, String recipient, String content) throws AddressException, MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sender));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        message.setSubject("Omazon Order");
        message.setContent(content, "text/html");
        return message;
    }
}
