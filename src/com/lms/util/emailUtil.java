/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lms.util;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.PasswordAuthentication;
import java.io.InputStream;
import com.lms.mail.EmailEntity;

/**
 *
 * @author ariefiandinugraha
 */
public class emailUtil {
     private static Properties emailProp;

    
    public emailUtil(){
        emailProp = new Properties();
    }
    
    private void loadProperties(){
        ClassLoader loader = Thread.currentThread().getContextClassLoader();           
        InputStream stream = loader.getResourceAsStream("lms.mail.properties");
        try{
            emailProp.load(stream);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Session mailSession(){
       loadProperties();
       Session session = Session.getInstance(emailProp,
       new javax.mail.Authenticator() {
         protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(emailProp.getProperty("mail.sender.username"),emailProp.getProperty("mail.sender.password"));
         }
      });
       return session;
    }
    
    public boolean sendEmail(EmailEntity mail){
       boolean result = false;
       loadProperties();
       Session session = Session.getInstance(emailProp,
       new javax.mail.Authenticator() {
         protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(emailProp.getProperty("mail.sender.username"),emailProp.getProperty("mail.sender.password"));
         }
      });
       
       try {
         // Create a default MimeMessage object.
         Message message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(emailProp.getProperty("mail.sender.address")));

         // Set To: header field of the header.
         message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(mail.getToAddress()));
         if(mail.getCcAddress()!= null)
             message.setRecipients(Message.RecipientType.CC,InternetAddress.parse(mail.getToAddress()));
         // Set Subject: header field
         message.setSubject(mail.getSubject());

         // Now set the actual message
         message.setContent(mail.getMessage(),"text/html");
         //message.setText("Hello, this is sample for to check send email using JavaMailAPI ");

         // Send message
         Transport.send(message);

         System.out.println("Sent message successfully....");
         
         result = true;

      } catch (MessagingException e) {
            throw new RuntimeException(e);
      }
        
        return result;
    }
    
}
