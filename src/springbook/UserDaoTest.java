package springbook;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.dao.UserDao;
import springbootk.user.domain.Level;
import springbootk.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserDaoTest {
    @Autowired
    ApplicationContext context;
    @Autowired
    UserDao dao;
    @Autowired
    DataSource dataSource;
    User user1;
    User user2;
    User user3;
    
    @Before
    public void setup() {
        System.out.println(this.context);
        System.out.println(this);
        
        user1 = User.builder()
                .id("id1")
                .name("name1")
                .password("password1")
                .level(Level.BASIC)
                .login(55)
                .recommend(10)
                .build();
        user2 = User.builder()
                .id("id2")
                .name("name2")
                .password("password2")
                .level(Level.GOLD)
                .login(51)
                .recommend(109)
                .build();
        user3 = User.builder()
                .id("id3")
                .name("name3")
                .password("password3")
                .level(Level.SILVER)
                .login(0)
                .recommend(0)
                .build();
    }
    
    @After
    public void deleteAll() throws SQLException {
        dao.deleteAll();
    }
    
    @Test
    public void addAdnGet() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));
        
        dao.add(user1);
        assertThat(dao.getCount(), is(1));
        checkSameUser(user1, dao.get(user1.getId()));
        
        dao.add(user2);
        assertThat(dao.getCount(), is(2));
        checkSameUser(user2, dao.get(user2.getId()));
        
        dao.add(user3);
        assertThat(dao.getCount(), is(3));
        checkSameUser(user3, dao.get(user3.getId()));
        
    }
    
    @Test(expected = EmptyResultDataAccessException.class)
    public void get_결과_없을때() {
        dao.get("id");
    }
    
    @Test
    public void getAll() throws SQLException {
        dao.add(user1);
        assertThat(dao.getAll().size(), is(1));
        dao.add(user2);
        assertThat(dao.getAll().size(), is(2));
        dao.add(user3);
        assertThat(dao.getAll().size(), is(3));
        
        dao.deleteAll();
        assertThat(dao.getAll().size(), is(0));
    }
    
    @Test(expected = DataAccessException.class)
    public void duplicateKey() {
        dao.add(user1);
        dao.add(user1);
    }
    
    @Test
    public void sqlExceptionTranslate() {
        try {
            dao.add(user1);
            dao.add(user1);
        } catch (DuplicateKeyException e) {
            SQLException sqlEx = (SQLException) e.getRootCause();
            SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(dataSource);
            assertThat(set.translate(null, null, sqlEx), is(DuplicateKeyException.class));
        }
    }
    
    @Test
    public void update() {
        dao.add(user1);
        dao.add(user2);
        
        user1.setName("이름");
        user1.setLevel(Level.SILVER);
        user1.setRecommend(20);
        user1.setLogin(19);
        dao.update(user1);
        
        dao.update(user1);
        User updated = dao.get(user1.getId());
        checkSameUser(user1, updated);
        
        
        assertThat(dao.get(user1.getId()).getName(), not(dao.get(user2.getId()).getName()));
    }
    
    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId(), is(user2.getId()));
        assertThat(user1.getName(), is(user2.getName()));
        assertThat(user1.getPassword(), is(user2.getPassword()));
        assertThat(user1.getLevel(), is(user2.getLevel()));
        assertThat(user1.getRecommend(), is(user2.getRecommend()));
        assertThat(user1.getLogin(), is(user2.getLogin()));
    }
}
