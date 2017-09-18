import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;


public class Sending {
public void mail(String file) throws Exception{
	ReadProp rp=new ReadProp();
	  String p=rp.getPath().getProperty("path");
	 EmailAttachment attachment = new EmailAttachment();
	  attachment.setPath(file);
	  attachment.setDisposition(EmailAttachment.ATTACHMENT);
	  attachment.setDescription("Kohler Report");
	  attachment.setName("Automation report");

	  // Create the email message
	  MultiPartEmail email = new MultiPartEmail();
	  email.setHostName("smtp.miraclesoft.com");
	  email.setSmtpPort(587);
	  email.setAuthenticator(new DefaultAuthenticator(rp.getPath().getProperty("mailFrom"), rp.getPath().getProperty("mailPassword")));
	  email.setSSLOnConnect(true);
	  
	  
	  	for(int i=1;i<=50;i++){
	  		String mailTo=rp.getPath().getProperty("mailTo"+i);
	  		if(mailTo!=null){
	  		System.out.println(mailTo);
	  		 email.addTo("sraja@miraclesoft.com");
	  		}
	  		else{
	  			break;
	  		}
	  	}
	  	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
		Date date = new Date();
		String timerep=dateFormat.format(date);
	  email.setFrom(rp.getPath().getProperty("mailFrom"));
	  email.setSubject(rp.getPath().getProperty("mailSubject"));
	  email.setMsg("Hi Team,"+"<html><body><br><h3>Build executed Succussfully</h3><br></body></html>"+rp.getPath().getProperty("mailMessage") +" Executed at "+timerep);

	  // add the attachment
	  email.attach(attachment);

	  // send the email
	  email.send();
}

}
