package springbook.user.service;

import static springbook.user.dao.Level.BASIC;

import java.util.List;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import springbook.user.dao.Level;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

public class UserServiceImpl implements UserService {
    public static final int MIN_RECCOMEND_FOR_GOLD = 30;
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    private UserDao userDao;
    private PlatformTransactionManager transactionManager;
    private MailSender mailSender;
    
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
    
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
    
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    public void upgradeLevels() {
        
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        
        try {
            upgradeLevelsInternal();
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    private void upgradeLevelsInternal() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            if (canUpgradeLevel(user)) {
                upgradeLevel(user);
            }
        }
    }

    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
        sendUpgradeMail(user);
    }

    private void sendUpgradeMail(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailSender.send(mailMessage);
        
//        JavaMailSender javaMailSender = new JavaMailSenderImpl();
//        System.out.println("발송 to " + user.getName() + " " + user.getEmail());
//        javaMailSender.createMimeMessage();
    }

    private boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();   
        switch (currentLevel) {
        case BASIC:
            return user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER;
        case SILVER:
            return user.getRecommend() >= MIN_RECCOMEND_FOR_GOLD;
        case GOLD:
            return false;
        default:
            throw new IllegalArgumentException("Unknown Level: " + currentLevel);
        }
    }

    public void add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(BASIC);
        }
        userDao.add(user);
    }
}
