import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

//com.sun.mail.smtp.SMTPSendFailedException: 530 5.7.0 Must issue a STARTTLS command first. y12-20020a17090a1f4c00b00218fa115f83sm211498pjy.57 - gsmtp
//com.sun.mail.smtp.SMTPSendFailedException: 530 5.7.0 Must issue a STARTTLS command first. a15-20020a1709027e4f00b0018b61ecf36fsm554208pln.287 - gsmtp

//https://ivvve.github.io/2019/02/09/java/Spring/mail_AuthenticationFailedException/
public class MyMail {
    
    public static void gmailSend() {
        System.out.println("끝");
        String user = "minseok0130@gmail.com"; // gmail 계정
        String password = "Kth973897@@";   // gmail 패스워드

        // SMTP 서버 정보를 설정한다.
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com"); 
        prop.put("mail.smtp.port", 465); 
        prop.put("mail.smtp.auth", "true"); 
        prop.put("mail.smtp.ssl.enable", "true"); 
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        prop.put("mail.smtp.starttls.enable","true");
        
        Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));

            //수신자메일주소
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("junghamoon@cj.net")); 

            // Subject
            message.setSubject("제목을 입력하세요"); //메일 제목을 입력

            // Text
            message.setText("내용을 입력하세요");    //메일 내용을 입력

            // send the message
            Transport.send(message); ////전송
            System.out.println("message sent successfully...");
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        System.out.println("끝");
    }

    public static void main(String[] args) {
        MyMail.gmailSend();
    }

}