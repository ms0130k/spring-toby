package springbook.user.service;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static springbook.user.dao.Level.BASIC;
import static springbook.user.dao.Level.GOLD;
import static springbook.user.dao.Level.SILVER;
import static springbook.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserServiceImpl.MIN_RECCOMEND_FOR_GOLD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import springbook.user.dao.Level;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceTest {
    @Autowired
    UserServiceImpl userServiceImpl;
    @Autowired
    UserService userService;
    @Autowired
    UserDao userDao;
    @Autowired
    DataSource dataSource;
    @Autowired
    PlatformTransactionManager transactionManager;
    @Autowired
    MailSender mailSender;
    
    List<User> users;

    @Before
    public void setUp() {
        users = Arrays.asList(
                new User("id1", "name1", "password1", BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 200, "test1@test.com"),
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
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        
        MockUserDao mockUserDao = new MockUserDao(users);
        userServiceImpl.setUserDao(mockUserDao);
        
        MockMailSender mockMailSender = new MockMailSender();
        userServiceImpl.setMailSender(mockMailSender);
        
        userServiceImpl.upgradeLevels();
        
        List<User> updated = mockUserDao.getUpdated();
        assertThat(updated.size(), is(2));
        checkUserAndLevel(updated.get(0), "id2", SILVER);
        checkUserAndLevel(updated.get(1), "id4", GOLD);
        
        List<String> requests = mockMailSender.getRequests();
        assertEquals(2, requests.size());
        assertEquals(users.get(1).getEmail(), requests.get(0));
        assertEquals(users.get(3).getEmail(), requests.get(1));
    }

    private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
        assertThat(updated.getId(), is(expectedId));
        assertThat(updated.getLevel(), is(expectedLevel));
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
        TestUserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(userDao);
        testUserService.setMailSender(mailSender);
        
        UserServiceTx userServiceTx = new UserServiceTx();
        userServiceTx.setUserService(testUserService);
        userServiceTx.setTransactionManager(transactionManager);
        
        userDao.deleteAll();
        for (User user : users) {
            userServiceTx.add(user);
        }
        try {
            userServiceTx.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {
        }
        checkLevel(users.get(1), false);
    }
    
    static class TestUserService extends UserServiceImpl {
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
    
    static class MockUserDao implements UserDao {
        private List<User> users;
        private List<User> updated = new ArrayList<User>();
        
        public MockUserDao(List<User> users) {
            this.users = users;
        }
        
        public List<User> getUpdated() {
            return updated;
        }

        @Override
        public void add(User user) {
            throw new UnsupportedOperationException();
        }

        @Override
        public User get(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAll() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getCount() {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<User> getAll() {
            return users;
        }

        @Override
        public void update(User user) {
            updated.add(user);
        }
        
    }
    
    @Test
    public void mockUpgradeLevels() {
//        UserServiceImpl userServiceImpl = new UserServiceImpl();
//        
//        UserDao mockUserDao = mock(UserDao.class);
//        when(mockUserDao.getAll()).thenReturn(users);
//        userServiceImpl.setUserDao(mockUserDao);
//        
//        MailSender mockMailSender = mock(MailSender.class);
//        userServiceImpl.setMailSender(mockMailSender);
//        
//        userServiceImpl.upgradeLevels();
        
//        verify(mockUserDao, times(2)).update((User) any(User.class));
    }
}
