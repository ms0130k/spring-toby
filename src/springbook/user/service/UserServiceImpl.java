package springbook.user.service;

import static springbook.user.dao.Level.BASIC;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import springbook.user.dao.Level;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

@Service("userService")
public class UserServiceImpl implements UserService {
    public static final int MIN_RECCOMEND_FOR_GOLD = 30;
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    @Autowired
    private UserDao userDao;
    private MailSender mailSender;
    
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
    
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    public void upgradeLevels() {
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

    @Override
    public User get(String id) {
        return userDao.get(id);
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public void deleteAll() {
        userDao.deleteAll();
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }
}
