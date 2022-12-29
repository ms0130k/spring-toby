package springbook.user.service;

import static org.junit.Assert.assertEquals;
import static springbook.user.dao.Level.BASIC;
import static springbook.user.dao.Level.GOLD;
import static springbook.user.dao.Level.SILVER;
import static springbook.user.service.UserService.MIN_RECCOMEND_FOR_GOLD;
import static springbook.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.dao.UserDao;
import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    UserDao userDao;
    
    List<User> users;

    @Before
    public void setUp() {
        users = Arrays.asList(
                new User("id1", "name1", "password1", null, MIN_LOGCOUNT_FOR_SILVER - 1, 200),
                new User("id2", "name2", "password2", BASIC, MIN_LOGCOUNT_FOR_SILVER, 200),
                new User("id3", "name3", "password3", SILVER, 50, MIN_RECCOMEND_FOR_GOLD - 1),
                new User("id4", "name4", "password4", SILVER, 29, MIN_RECCOMEND_FOR_GOLD),
                new User("id5", "name5", "password5", GOLD, 50, 200)
                );
    }
    
    @Test
    public void add() {
        userDao.deleteAll();
        
        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        
        userService.add(userWithLevel);
        userService.add(userWithoutLevel);
        
        assertEquals(GOLD, userDao.get(userWithLevel.getId()).getLevel());
        assertEquals(BASIC, userDao.get(userWithoutLevel.getId()).getLevel());
    }
    
    @Test
    public void upgradeLevels() {
        userDao.deleteAll();
        for (User user : users) {
            userService.add(user);
        }
        
        userService.upgradeLevels();
        
        checkLevel(users.get(0), false);
        checkLevel(users.get(1), true);
        checkLevel(users.get(2), false);
        checkLevel(users.get(3), true);
        checkLevel(users.get(4), false);
    }

    private void checkLevel(User user, boolean upgraded) {
        User updatedUser = userDao.get(user.getId());
        if (upgraded) {
            assertEquals(updatedUser.getLevel(), user.getLevel().nextLevel());
        } else {
            assertEquals(updatedUser.getLevel(), user.getLevel());
        }
    }
    
    static class TestUserService extends UserService {
        private String id;

        @Override
        protected void upgradeLevel(User user) {
            
            super.upgradeLevel(user);
        }

        
    }
}
