import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
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


public class Mail {
	public void sendEmailWithAttachments(String file) throws Exception {
		ReadProp rp= new ReadProp();	// calling ReadProp class
		Properties prop=rp.getPath();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
		Date date = new Date();
		String timerep=dateFormat.format(date);
		String host = "smtp.miraclesoft.com";
		final String mailFrom = prop.getProperty("mailFrom");
		final String password = prop.getProperty("mailPassword");

		// message info
		
		String subject = prop.getProperty("mailSubject");
		String message = "Hi Team, \r\n \r\n This is automatic email report. \r\n Execution is completed at "+timerep+". Please download the report. \r\n \r\n Thanks, \r\n Vamsi Kattamudi";

		Properties properties = new Properties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", 587);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.user", mailFrom);
		properties.put("mail.password", password);
		StringBuffer body
        = new StringBuffer("<html><center><h2><font color=\"red\">Automation Report</font></h2></center><br><br>");
    body.append("<h4><font color=\"red\">Below screen for testcases status</font></h2> <br><br>");
    body.append("<img src=\"cid:image1\" width=\"150%\" height=\"150%\" /><br>");
//    body.append("The second one is a cube:<br>");
//    body.append("<img src=\"cid:image2\" width=\"50%\" height=\"50%\" /><br>");
    body.append("End of message.");
    body.append("</html>");

    // inline images
    Map<String, String> inlineImages = new HashMap<String, String>();
    inlineImages.put("image1", file);
//    inlineImages.put("image2", "C:/testing/screen/20170609164901.png");

		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mailFrom, password);
			}
		};
		Session session = Session.getInstance(properties, auth);

		Message msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(mailFrom));
			String mailsTo="";
			for(int i=1;i<=50;i++){
				if(prop.getProperty("mailTo"+i)!=null){
				mailsTo=mailsTo+" "+prop.getProperty("mailTo"+i);
				}
				else{
					break;
				}
			}
			mailsTo=mailsTo.trim();
//			String recipient = "sraja@miraclesoft.com ,vkapavarapu@miraclesoft.com ";
			String[] recipientList = mailsTo.split(" ");
			InternetAddress[] recipientAddress = new InternetAddress[recipientList.length];
			int counter = 0;
			for (String recipients : recipientList) {
			    recipientAddress[counter] = new InternetAddress(recipients.trim());
			    counter++;
			}
			msg.setRecipients(Message.RecipientType.TO, recipientAddress);
//			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("abc@abc.com,abc@def.com,ghi@abc.com"));
			msg.setSubject(subject);
			msg.setSentDate(new Date());
			msg.setText(message);

			BodyPart messageBodyPart = new MimeBodyPart();
			
			
			messageBodyPart.setText(message);

			Multipart multipart = new MimeMultipart();

			multipart.addBodyPart(messageBodyPart);

			// **** Attchment Code *******
			
			messageBodyPart = new MimeBodyPart();
			String filename = file;
			DataSource source = new FileDataSource(filename);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(filename);
			multipart.addBodyPart(messageBodyPart);
			
//			if (inlineImages != null && inlineImages.size() > 0) {
//	            Set<String> setImageID = inlineImages.keySet();
//	            MimeBodyPart imagePart = new MimeBodyPart();
//	            imagePart.setContent(body, "text/html");
//	            for (String contentId : setImageID) {
//	                
//	                imagePart.setHeader("Content-ID", "<" + contentId + ">");
//	                imagePart.setDisposition(MimeBodyPart.INLINE);
//	                 
//	                String imageFilePath = inlineImages.get(contentId);
//	                try {
//	                    imagePart.attachFile(imageFilePath);
//	                } catch (IOException ex) {
//	                    ex.printStackTrace();
//	                }
//	 
//	                multipart.addBodyPart(imagePart);
//	            }
//	        }

			// **** Entire Message Sending
			msg.setContent(multipart);
			System.out.println("Email Sent ");
			Transport.send(msg);

		} catch (MessagingException e) {
			System.out.println("Exception Raised :: " + e);
		}

	}

	/**
	 * Test sending e-mail with attachments
	 */
	
}
