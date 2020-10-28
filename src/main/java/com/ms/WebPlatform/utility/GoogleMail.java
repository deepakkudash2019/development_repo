package com.ms.WebPlatform.utility;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.security.Security;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.gson.Gson;
import com.sun.mail.smtp.SMTPTransport;

	
	

public class GoogleMail {
	
	
    private GoogleMail() {
    }
    
    final static String fromEmail = "msrtcmapping@gmail.com"; //requires valid gmail id
	final static String mailPassword = "msrtcmapping123"; // correct password for gmail id
	//final static String recipientEmail = "dash.deepak156@gmail.com"; 

	
    
    public static void Send(String recipientEmail, String title, String message) {
        System.out.println("google ,ail send ---- >>>> ");
    	try {
			GoogleMail.Send(fromEmail, mailPassword, recipientEmail, "", title, message);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

   
    public static void Send(final String username, final String password, String recipientEmail, String ccEmail, String title, String message) throws AddressException, MessagingException {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        // Get a Properties object
        Properties props = System.getProperties();
        props.setProperty("mail.smtps.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.setProperty("mail.smtps.auth", "true");

       
        props.put("mail.smtps.quitwait", "false");

        Session session = Session.getInstance(props, null);

        // -- Create a new message --
        final MimeMessage msg = new MimeMessage(session);

        // -- Set the FROM and TO fields --
        msg.setFrom(new InternetAddress(fromEmail));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail, false));

        if (ccEmail.length() > 0) {
            msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccEmail, false));
        }

        msg.setSubject(title);
        msg.setText(message, "utf-8");
        msg.setSentDate(new Date());

        SMTPTransport t = (SMTPTransport)session.getTransport("smtps");

        t.connect("smtp.gmail.com", username, password);
        t.sendMessage(msg, msg.getAllRecipients());      
        t.close();
    }
    
    public static void sendMails(String title,String mailbody) {
		InputStream _inputApplicationFile;
		try {
			_inputApplicationFile = new FileInputStream("msrtc_graph_application.properties");
			Properties _prop = new Properties();
		    _prop.load(_inputApplicationFile);
		    
		    
		    
		    String mailIds[] = _prop.get("server.mailIds").toString().split(",");
		    for(String ss : mailIds) {
		    	
		    	
		    	Thread t = new Thread() { 
					public void run()
					{
						
							String mailIdStr = ss.trim();
					    	Send(mailIdStr, title, mailbody);
						

					}
				};t.start();
		    	
		    	
		    }
		    
		    
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	    
		
		
	}
    
    public static void main(String[] args) {

       sendMails("tttttt", "bodyyyyyyyyyy");
       
	}
    
}

