package com.ibm.wcts;

import java.util.Properties;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;


public class SendHTMLEmail {
	String host;
	String serverLocation = "https://localhost";
    String SMTP_AUTH_USER = "myusername";
	String SMTP_AUTH_PWD  = "mypwd";
	    
	public SendHTMLEmail() {
		// init server settings

		Util ut = new Util();

		try {
			host = ut.getPropValues("mailserver");
			serverLocation = ut.getPropValues("serverLocation");
			SMTP_AUTH_USER = ut.getPropValues("SMTP_AUTH_USER");
			SMTP_AUTH_PWD = ut.getPropValues("SMTP_AUTH_PWD");
	        
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

		// Recipient's email ID needs to be mentioned.
		String to = "jason@cloudscreening.com.au";

		// Sender's email ID needs to be mentioned
		String from = "admin@cloudscreening.com.au";

		// Assuming you are sending email from localhost
		String host = "intmxeg.corp.dmz";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", host);

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Set Subject: header field
			message.setSubject("This is the Subject Line!");

			// Send the actual HTML message, as big as you like
			message.setContent("<h1>This is actual message</h1>", "text/html");

			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}

	 private class SMTPAuthenticator extends javax.mail.Authenticator {
	        public PasswordAuthentication getPasswordAuthentication() {
	           String username = SMTP_AUTH_USER;
	           String password = SMTP_AUTH_PWD;
	           return new PasswordAuthentication(username, password);
	        }
	    }
	 
	public void sendActivateMail(String to, String from, String username, String firstname, String lastname,String token) {

		// Recipient's email ID needs to be mentioned.
//		to = "jason@cloudscreening.com.au";

		// Sender's email ID needs to be mentioned
//		from = "admin@cloudscreening.com.au";


		// Assuming you are sending email from localhost
		// String host = "intmxeg.corp.dmz";

//		String serverLocation = "https://localhost";

		String activateURL = serverLocation + "/activate?username=" + username + "&token=" + token;

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", host);

		Properties props = new Properties();
		props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", "true");


		Authenticator auth = new SMTPAuthenticator();
//        Session mailSession = Session.getDefaultInstance(props, auth);
        // uncomment for debugging infos to stdout
        // mailSession.setDebug(true);
        
		// Get the default Session object.
		Session session = Session.getDefaultInstance(props, auth);
        session.setDebug(true);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Set Subject: header field
			message.setSubject("Account activation for "+firstname+" "+lastname+"!");

			String content ="Dear "+ firstname +" "+lastname+", <br>" 
					+"<p>Thanks for your interest in Cloud Screening Service!</p>"
					
					+"<p>Please click on the following link to activate your account!</p><br>" + "<a href='"
					+ activateURL + "'>Activate my account</a> <br><br>"
					
					+"Best Regards,<br>"
					+"Cloud Screening Pty.Ltd.<br>";

			// Send the actual HTML message, as big as you like
			message.setContent(content, "text/html");

			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
}