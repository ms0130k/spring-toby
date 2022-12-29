package springbook.user.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static springbook.user.dao.Level.BASIC;
import static springbook.user.dao.Level.GOLD;
import static springbook.user.dao.Level.SILVER;
import static springbook.user.service.UserService.MIN_RECCOMEND_FOR_GOLD;
import static springbook.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import springbook.user.dao.UserDao;
import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    UserDao userDao;
    @Autowired
    DataSource dataSource;
    @Autowired
    PlatformTransactionManager transactionManager;
    
    List<User> users;

    @Before
    public void setUp() {
        users = Arrays.asList(
                new User("id1", "name1", "password1", null, MIN_LOGCOUNT_FOR_SILVER - 1, 200, "test1@test.com"),
                new User("id2", "name2", "password2", BASIC, MIN_LOGCOUNT_FOR_SILVER, 200, "test2@test.com"),
                new User("id3", "name3", "password3", SILVER, 50, MIN_RECCOMEND_FOR_GOLD - 1, "test3@test.com"),
                new User("id4", "name4", "password4", SILVER, 29, MIN_RECCOMEND_FOR_GOLD, "test4@test.com"),
                new User("id5", "name5", "password5", GOLD, 50, 200, "test5@test.com")
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
    public void upgradeLevels() throws Exception {
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
        if (user.getLevel() == null) {
            user.setLevel(BASIC);
        }
        User updatedUser = userDao.get(user.getId());
        if (upgraded) {
            assertEquals(updatedUser.getLevel(), user.getLevel().nextLevel());
        } else {
            assertEquals(updatedUser.getLevel(), user.getLevel());
        }
    }
    
    @Test
    public void upgradeAllOrNothing() throws Exception {
        UserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(userDao);
        testUserService.setTransactionManager(transactionManager);
        
        userDao.deleteAll();
        for (User user : users) {
            userService.add(user);
        }
        try {
            testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {
        }
        checkLevel(users.get(1), false);
    }
    
    static class TestUserService extends UserService {
        private String id;

        public TestUserService(String id) {
            this.id = id;
        }

        @Override
        protected void upgradeLevel(User user) {
            if (user.getId().equals(id)) {
                throw new TestUserServiceException();
            }
            super.upgradeLevel(user);
        }
    }
    
    static class TestUserServiceException extends RuntimeException {
        private static final long serialVersionUID = -6951044591013051063L;
    }
}
