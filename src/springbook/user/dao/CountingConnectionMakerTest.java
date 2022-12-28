package springbook.user.dao;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class CountingConnectionMakerTest {
    @Test
    public void test() throws SQLException {
        CountingConnectionMaker connectionMaker = new AnnotationConfigApplicationContext(DaoFactory.class).getBean("countingConnectionMaker", CountingConnectionMaker.class);
        int count = 5;
        for (int i = 0; i < count; i++) {
            connectionMaker.getConnection();
        }
        assertEquals(count, connectionMaker.getCount());
    }

}
