package omazon;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Session;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
        
public class Email {

    public static void sendEmail(String recipient, String content, String imagePath) {
        String from = "omazonhelpdesk@gmail.com";

        final String password = "omazon123";

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
           Message message = new MimeMessage(session);
           message.setFrom(new InternetAddress(from));
           message.setRecipients(Message.RecipientType.TO,
              InternetAddress.parse(recipient));

           message.setSubject("Omazon Order");
           BodyPart messageBodyPart = new MimeBodyPart();
           messageBodyPart.setContent(content, "text/html");
           Multipart multipart = new MimeMultipart();
           multipart.addBodyPart(messageBodyPart);


           messageBodyPart = new MimeBodyPart();
           String filename = imagePath;
           DataSource source = new FileDataSource(filename);
           messageBodyPart.setDataHandler(new DataHandler(source));
           messageBodyPart.setFileName(filename);
           multipart.addBodyPart(messageBodyPart);
           message.setContent(multipart);

           Transport.send(message);

           System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
           throw new RuntimeException(e);
        }
    }
    
    public String prepareBuyerContent(String productName, String purchaseAmount, double purchasePrice) {
        String header = "<h1> Your order has been confirmed! </h1>";    
        String name = "<h2> Product Name - " + productName + "</h2>";
        String amount = "<h3> Purchase Amount - " + purchaseAmount + "</h3>";
        String paid = "<h3> Total Price - RM " + purchasePrice + "</h3>";
        String ending = "<h5> Keep this email as a proof of purchase! </h5>";
        
        String content = header + "\n" + name + "\n" + amount + "\n" + paid + "\n" + ending;
        return content;
    }
    
    public String prepareSellerContent(String productName, String purchaseAmount, double purchasePrice, String buyerName, String buyerAddress) {
        String header = "<h1> An order has been made! </h1>";
        String name = "<h2> Product Name - " + productName + "</h2>";
        String amount = "<h3> Purchase Amount - " + purchaseAmount + "</h3>";
        String paid = "<h3> Total Price - RM " + purchasePrice + "</h3>";
        String info = "<h2> Buyer's Info </h2>";
        String buyer = "<h3> Buyer's name: " + buyerName + "</h3>";
        String address = "<h3> Buyer's address: " + buyerAddress + "</h3>"; 
        String ending = "<h5> Keep this email as a proof of purchase! </h5>";
        
        String content = header + "\n" + name + "\n" + amount + "\n" + paid + "\n\n" + info + "\n" + buyer + "\n" + address + "\n" + ending;
        return content;
    }

}
