
import java.util.*;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;

public class SendEmail {

	
public static void send(String toAdresse, String subject, String bodyText) {
		
	//Commen Variablen
	String host = "213.165.67.108";
	String from = "info-zef@web.de";
	String to = toAdresse;
	
       MailAuthenticator auth = new MailAuthenticator("info-zef@web.de", "Hecom166");
	
	// Set properties
	Properties props = new Properties();
	props.put("mail.smtp.host", host);
	props.put("mail.smtp.port", "587");
	props.put("mail.smtp.auth", "true");
		
	props.put("mail.smtp.starttls.enable", "true");

	//get Session
	Session session = Session.getInstance(props, auth);
	
	try{
		
		Message msg = new MimeMessage(session);
			
		msg.setFrom(new InternetAddress(from));
		msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
		// Inhalt der Nachricht vorbereiten
		msg.setSubject(subject);
		msg.setSentDate(new Date());
		
		MimeBodyPart msgBody = new MimeBodyPart();
		msgBody.setText(bodyText);
		
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(msgBody);
			
		msgBody = new MimeBodyPart();
			
		//Anhang vorbereiten
		String filename = auswertung.ExcelWriter.getFilename();
		//String filepath = Settings.getPath().replace('/', '\\')+filename;
		DataSource source = new FileDataSource(filename);
		System.out.println("Source: "+filename);
		msgBody.setDataHandler(new DataHandler(source));
		msgBody.setFileName(filename);
		multipart.addBodyPart(msgBody);
		
		msg.setContent(multipart);
		
		// Send the message
		Transport.send(msg);
	}
	catch (MessagingException mex) {
		mex.printStackTrace();
	}
	}
}


class MailAuthenticator extends Authenticator {
	  
    private final String user;
    private final String password;

    public MailAuthenticator(String user, String password) {
        this.user = user;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(this.user, this.password);
    }
}
