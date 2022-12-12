package springbook.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import springbook.user.dao.UserDao;
import static springbootk.user.domain.Level.*;

import springbootk.user.domain.Level;
import springbootk.user.domain.User;

public class UserService {
    public static final int MIN_LOGIN_COUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;
    
    @Autowired
    private UserDao dao;
    
//    @Qualifier("defaultUserLevelUpgradePolicy")
    @Autowired
    private UserLevelUpgradePolicy levelUpgradePolicy;

    public void add(User user) {
        if (user.getLevel() == null)
            user.setLevel(BASIC);
        dao.add(user);
    }
    
    public void upgradeLevels() {
        List<User> users = dao.getAll();
        for (User user : users) {
            upgradeLevel(user);
        }
    }

    private void upgradeLevel(User user) {
//        levelUpgradePolicy.upgrade(user);
        if (canUpgradeLevel(user)) {
            user.upgradeLevel();
            dao.update(user);
        }
    }

    private boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
        case BASIC:
            return user.getLogin() >= MIN_LOGIN_COUNT_FOR_SILVER;
        case SILVER:
            return user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD;
        case GOLD:
            return false;
        default:
            throw new IllegalArgumentException("unkwon level: " + currentLevel);
        }
    }
}
