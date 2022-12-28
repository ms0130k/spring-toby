package springbook.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static springbook.user.dao.Level.*;

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
        user1 = User.builder().id("id1").name("name1").password("password1").level(SILVER).login(1).recommend(10).build();
        user2 = User.builder().id("id2").name("name2").password("password2").level(GOLD).login(1).recommend(10).build();
        user3 = User.builder().id("id3").name("name3").password("password3").level(BASIC).login(1).recommend(10).build();
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
    }
}
