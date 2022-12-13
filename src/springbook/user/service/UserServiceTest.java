package springbook.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static springbootk.user.domain.Level.BASIC;
import static springbootk.user.domain.Level.GOLD;
import static springbootk.user.domain.Level.SILVER;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.dao.UserDao;
import springbootk.user.domain.Level;
import springbootk.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    UserDao dao;
    List<User> users;
    @Autowired
    UserLevelUpgradePolicy userLevelUpgradePolicy;

    @Before
    public void setup() {
        users = Arrays.asList(
                new User("aa", "123", "44", null, UserService.MIN_LOGIN_COUNT_FOR_SILVER - 1, 11),
                new User("bb", "77", "123", BASIC, UserService.MIN_LOGIN_COUNT_FOR_SILVER, 10),
                new User("cc", "66", "22", SILVER, 99, UserService.MIN_RECOMMEND_FOR_GOLD - 1),
                new User("dd", "55", "33", SILVER, 100, UserService.MIN_RECOMMEND_FOR_GOLD),
                new User("ee", "44", "55", GOLD, 20, Integer.MAX_VALUE)
        );
        for (User user : users) {
            userService.add(user);
        }
    }
    
    @After
    public void after() {
        dao.deleteAll();
    }
    
    @Test
    public void bean() {
        assertThat(userService, is(notNullValue()));
    }
    
    @Test
    public void add() {
        checkLevel(users.get(0), BASIC);
        checkLevel(users.get(1), BASIC);
        checkLevel(users.get(2), SILVER);
        checkLevel(users.get(3), SILVER);
        checkLevel(users.get(4), GOLD);
    }
    
    @Test
    public void upgradeLevels() {
        userService.upgradeLevels();
        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);
    }
    
    private void checkLevelUpgraded(User user, boolean upgraded) {
        User updatedUser = dao.get(user.getId());
        if (upgraded)
            assertThat(updatedUser.getLevel(), is(user.getLevel().nextLevel()));
        else
            assertThat(updatedUser.getLevel(), is(user.getLevel()));
    }
    
    private void checkLevel(User user, Level level) {
        assertThat(user.getLevel(), is(level));
    }
    
    static class TestUserService extends UserService {
        private String id;
        private TestUserService(String id) {
            this.id = id;
        }
        
        @Override
        protected void upgradeLevel(User user) {
            if (user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }
    
    static class TestUserServiceException extends RuntimeException {
        
    }
    @Test
    public void upgradeAllOrNothing() {
        try {
            UserService testService = new TestUserService(users.get(3).getId());
            testService.setUserDao(dao);
            testService.setUserLevelUpgradePolicy(userLevelUpgradePolicy);
            testService.upgradeLevels();
            fail("TestUserServiceException is expected");
        } catch (TestUserServiceException e) {
        }
        
        for (User user : users) {
            checkLevelUpgraded(user, false);
        }
    }
}
