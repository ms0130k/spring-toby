package springbook.user.service;

import static springbootk.user.domain.Level.BASIC;

import java.io.UnsupportedEncodingException;
import java.util.List;
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
import javax.sql.DataSource;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import springbook.user.dao.UserDao;
import springbootk.user.domain.User;

public class UserService {
    public static final int MIN_LOGIN_COUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;
    
    private DataSource dataSource;
    private UserDao userDao;
    private UserLevelUpgradePolicy userLevelUpgradePolicy;
    private PlatformTransactionManager transactionManager;
    
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public void setUserDao(UserDao dao) {
        this.userDao = dao;
    }
    
    public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
    }
    
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void add(User user) {
        if (user.getLevel() == null)
            user.setLevel(BASIC);
        userDao.add(user);
    }
    
    public void upgradeLevels() throws Exception {
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        
        try {
            List<User> users = userDao.getAll();
            for (User user : users) {
                upgradeLevel(user);
            }
            this.transactionManager.commit(status);
        } catch (Exception e) {
            this.transactionManager.rollback(status);
            throw e;
        }
    }

    protected void upgradeLevel(User user) {
        userLevelUpgradePolicy.upgrade(user);
        sendUpgradeEmail(user);
    }

    public void sendUpgradeEmail(User user) {
        final String sender = "minseok0130@gmail.com"; 
        final String password = "Kth973897@@";
        final String host = "smtp.gmail.com";
        final String port = "465";
        final String TRUE = "true";
        
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", TRUE);
        props.put("mail.smtp.starttls.enable",TRUE);
        props.put("mail.smtp.ssl.enable", TRUE);
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.starttls.enable",TRUE);
        Session s = 
        Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, password);
            }
        });
        
        MimeMessage message = new MimeMessage(s);
        try {
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setSubject("Upgrade 안내");
            message.setText("사용자님의 등급이 " + user.getLevel().name() + "로 업그레이드 되었습니다.");
            
            Transport.send(message);
        } catch (AddressException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
