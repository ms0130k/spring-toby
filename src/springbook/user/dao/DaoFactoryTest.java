package springbook.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class DaoFactoryTest {
    ApplicationContext context;
    
    @Before
    public void setup() {
        context = new AnnotationConfigApplicationContext(DaoFactory.class);
    }

    @Test
    public void context_bean_class() {
        assertThat(context.getBean("userDao", UserDaoJdbc.class), is(not(nullValue())));
    }
    
    @Test
    public void identity() {
        DaoFactory daoFactory = new DaoFactory();
        UserDaoJdbc userDao1 = daoFactory.userDao();
        UserDaoJdbc userDao2 = daoFactory.userDao();
        assertNotSame(userDao1, userDao2);
        
        userDao1 = context.getBean("userDao", UserDaoJdbc.class);
        userDao2 = context.getBean("userDao", UserDaoJdbc.class);
        assertEquals(userDao1, userDao2);
    }
}
