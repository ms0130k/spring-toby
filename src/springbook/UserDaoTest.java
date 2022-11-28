package springbook;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.dao.UserDao;
import springbootk.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserDaoTest {
    @Autowired
    ApplicationContext context;
    @Autowired
    UserDao userDao;
    
    @Before
    public void setContext() {
        System.out.println(this.context);
        System.out.println(this);
    }
    
    @Test
    public void addAdnGet() throws SQLException {
        String id = UUID.randomUUID().toString();
        User user1 = User.builder()
                .id(id)
                .name("이름")
                .password("비번")
                .build();
        User user2 = User.builder()
                .id(UUID.randomUUID().toString())
                .name("이름")
                .password("비번")
                .build();
        User user3 = User.builder()
                .id(UUID.randomUUID().toString())
                .name("이름")
                .password("비번")
                .build();
        
        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));
        
        userDao.add(user1);
        assertThat(userDao.getCount(), is(1));
        
        userDao.add(user2);
        assertThat(userDao.getCount(), is(2));
        
        userDao.add(user3);
        assertThat(userDao.getCount(), is(3));
        
        userDao.get(id);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void get_결과_없을때() throws SQLException {
        userDao.get("id");
    }
}
