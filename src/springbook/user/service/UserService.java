package springbook.user.service;

import static springbootk.user.domain.Level.BASIC;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import springbook.user.dao.UserDao;
import springbootk.user.domain.User;

public class UserService {
    public static final int MIN_LOGIN_COUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private UserLevelUpgradePolicy userLevelUpgradePolicy;
    
    public void setUserDao(UserDao dao) {
        this.userDao = dao;
    }
    
    public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
    }

    public void add(User user) {
        if (user.getLevel() == null)
            user.setLevel(BASIC);
        userDao.add(user);
    }
    
    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            upgradeLevel(user);
        }
    }

    protected void upgradeLevel(User user) {
        userLevelUpgradePolicy.upgrade(user);
    }
}
