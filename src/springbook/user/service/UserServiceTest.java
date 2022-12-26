package springbook.user.service;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static springbootk.user.domain.Level.BASIC;
import static springbootk.user.domain.Level.GOLD;
import static springbootk.user.domain.Level.SILVER;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;

import springbook.proxy.TransactionHandler;
import springbook.user.dao.UserDao;
import springbootk.user.domain.Level;
import springbootk.user.domain.User;

public class UserServiceTest {
    UserServiceImpl userServiceImpl = new UserServiceImpl();
    MockMailSender mailSender = new MockMailSender();
    List<User> users = Arrays.asList(
            new User("aa", "123", "44", BASIC, UserServiceImpl.MIN_LOGIN_COUNT_FOR_SILVER - 1, 11, "ms0130k@naver.com"),
            new User("bb", "77", "123", BASIC, UserServiceImpl.MIN_LOGIN_COUNT_FOR_SILVER, 10, "ms0130k@naver.com"),
            new User("cc", "66", "22", SILVER, 99, UserServiceImpl.MIN_RECOMMEND_FOR_GOLD - 1, "ms0130k@naver.com"),
            new User("dd", "55", "33", SILVER, 100, UserServiceImpl.MIN_RECOMMEND_FOR_GOLD, "ms0130k@naver.com"),
            new User("ee", "44", "55", GOLD, 20, Integer.MAX_VALUE, "ms0130k@naver.com"));;
    MockUserDao mockUserDao = new MockUserDao(users);
    DefaultUserLevelUpgradePolicy defaultUserLevelUpgradePolicy = new DefaultUserLevelUpgradePolicy();
    PlatformTransactionManager platformTransactionManager = new DataSourceTransactionManager();

    @Before
    public void setup() {
        userServiceImpl.setUserDao(mockUserDao);
        userServiceImpl.setMailSender(mailSender);
    }

    @After
    public void after() {
//        dao.deleteAll();
    }

    @Test
    public void upgradeLevels() throws Exception {
        userServiceImpl.upgradeLevels();

        List<User> updated = mockUserDao.getUpdated();
        assertThat(updated.size(), is(2));
        checkUserAndLevel(updated.get(0), "bb", Level.SILVER);
        checkUserAndLevel(updated.get(1), "dd", Level.GOLD);

        List<String> requests = mailSender.getRequests();
        assertThat(requests.size(), is(2));
        assertThat(requests.get(0), is(users.get(1).getEmail()));
        assertThat(requests.get(1), is(users.get(3).getEmail()));
    }

    private void checkUserAndLevel(User updated, String id, Level level) {
        assertThat(updated.getId(), is(id));
        assertThat(updated.getLevel(), is(level));
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User updatedUser = mockUserDao.get(user.getId());
        if (upgraded)
            assertThat(updatedUser.getLevel(), is(user.getLevel().nextLevel()));
        else
            assertThat(updatedUser.getLevel(), is(user.getLevel()));
    }

    static class TestUserService extends UserServiceImpl {
        private String id;

        private TestUserService(String id) {
            this.id = id;
        }

        @Override
        protected void upgradeLevel(User user) {
            super.setMailSender(new DummyMailSender());
            if (user.getId().equals(this.id))
                throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException {
        private static final long serialVersionUID = -6951044591013051063L;
    }

    @Test
    public void upgradejoinPointAllOrNothing() throws Exception {
        TestUserService testService = new TestUserService(users.get(3).getId());
        testService.setUserDao(mockUserDao);
        testService.setUserLevelUpgradePolicy(defaultUserLevelUpgradePolicy);

        TransactionHandler transactionHandler = new TransactionHandler(testService, platformTransactionManager,
                "upgradeLevels");
        UserService userServiceTx = (UserService) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { UserService.class }, transactionHandler);

        try {
            userServiceTx.upgradeLevels();
            fail("TestUserServiceException is expected");
        } catch (TestUserServiceException e) {
        }

        for (User user : users) {
            checkLevelUpgraded(user, false);
        }
    }

    static class MockMailSender implements MailSender {
        private List<String> requests = new ArrayList<String>();

        public List<String> getRequests() {
            return requests;
        }

        @Override
        public void send(SimpleMailMessage mailMessage) throws MailException {
            requests.add(mailMessage.getTo()[0]);
        }

        @Override
        public void send(SimpleMailMessage[] arg0) throws MailException {
        }

    }

    static class MockUserDao implements UserDao {
        private List<User> users;
        private List<User> updated = new ArrayList<>();

        private MockUserDao(List<User> users) {
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
        public int update(User user) {
            updated.add(user);
            return 1;
        }

    }

    @Test
    public void mockUpgradeLevels() throws Exception {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        UserDao mockUserDao = mock(UserDao.class);
        when(mockUserDao.getAll()).thenReturn(this.users);
        userServiceImpl.setUserDao(mockUserDao);

        MailSender mockMailSender = mock(MailSender.class);
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();

        verify(mockUserDao, times(2)).update((User) any(User.class));
    }
}
