package springbook.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import springbook.user.dao.UserDao;
import springbootk.user.domain.Level;
import springbootk.user.domain.User;

public class DefaultUserLevelUpgradePolicy implements UserLevelUpgradePolicy {
    public static final int MIN_LOGIN_COUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    @Autowired
    UserDao dao;
    
    @Override
    public boolean canUpgradeLevel(User user) {
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

    @Override
    public void upgrade(User user) {
        if (canUpgradeLevel(user)) {
            user.upgradeLevel();
            dao.update(user);
        }
    }

}
