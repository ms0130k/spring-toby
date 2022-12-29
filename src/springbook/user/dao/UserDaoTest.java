package springbook.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static springbook.user.dao.Level.BASIC;
import static springbook.user.dao.Level.GOLD;
import static springbook.user.dao.Level.SILVER;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserDaoTest {
    @Autowired
    UserDao userDao;
    
    User user1;
    User user2;
    User user3;

    @Before
    public void setUp() {
        userDao.deleteAll();
        user1 = new User("id1", "name1", "password1", SILVER, 1, 10);
        user2 = new User("id2", "name2", "password2", GOLD, 1, 10);
        user3 = new User("id3", "name3", "password3", BASIC, 1, 10);
    }
    
    @Test
    public void getAll() {
        List<User> users0 = userDao.getAll();
        assertEquals(0, users0.size());
        
        userDao.add(user1);
        List<User> users1 = userDao.getAll();
        assertEquals(1, users1.size());
        checkSameUser(user1, users1.get(0));
        
        userDao.add(user2);
        List<User> users2 = userDao.getAll();
        assertEquals(2, users2.size());
        checkSameUser(user1, users2.get(0));
        checkSameUser(user2, users2.get(1));
        
        userDao.add(user3);
        List<User> users3 = userDao.getAll();
        assertEquals(3, users3.size());
        checkSameUser(user1, users3.get(0));
        checkSameUser(user2, users3.get(1));
        checkSameUser(user3, users3.get(2));
    }
    
    private void checkSameUser(User user1, User user2) {
        assertEquals(user1.getId(), user2.getId());
        assertEquals(user1.getName(), user2.getName());
        assertEquals(user1.getPassword(), user2.getPassword());
        assertEquals(user1.getLevel(), user2.getLevel());
        assertEquals(user1.getLogin(), user2.getLogin());
        assertEquals(user1.getRecommend(), user2.getRecommend());
    }

    @Test
    public void addAndGet() {
        userDao.add(user1);
        userDao.add(user2);
        
        User addedUser1 = userDao.get(user1.getId());
        checkSameUser(user1, addedUser1);
        User addedUser2 = userDao.get(user2.getId());
        checkSameUser(user2, addedUser2);
    }
    
    @Test(expected = EmptyResultDataAccessException.class)
    public void failGettingUser() {
        userDao.get("null");
    }
    
    @Test
    public void count() {
        assertThat(userDao.getCount(), is(0));
        userDao.add(user1);
        assertThat(userDao.getCount(), is(1));
    }
    
    @Test(expected = DuplicateKeyException.class)
    public void addSameId() {
        userDao.add(user1);
        userDao.add(user1);
    }
    
    @Test
    public void update() {
        userDao.add(user1);
        userDao.add(user2);
        
        user1.setName("이름변경");
        user1.setPassword("비번변경");
        user1.setLevel(GOLD);
        user1.setLogin(500);
        user1.setRecommend(999);
        userDao.update(user1);
        
        User updatedUser = userDao.get(user1.getId());
        checkSameUser(user1, updatedUser);
        
        User sameUser = userDao.get(user2.getId());
        checkSameUser(user2, sameUser);
    }
}
