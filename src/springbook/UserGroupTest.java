package springbook;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.dao.UserDaoJdbc;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserGroupTest {
    @Autowired
    ApplicationContext con;
    @Autowired
    UserDaoJdbc dao;

    @Before
    public void setup() {
        System.out.println(con);
        System.out.println(dao);
    }
    
    @Test
    public void test() {
//        fail("Not yet implemented");
    }

}
