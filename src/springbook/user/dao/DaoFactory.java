package springbook.user.dao;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

public class DaoFactory {

    @Bean
    public UserDaoJdbc userDao() {
        UserDaoJdbc userDao = new UserDaoJdbc();
        userDao.setDataSource(dataSource());
        return userDao;
    }
    
    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(org.postgresql.Driver.class);
        dataSource.setUrl("jdbc:postgresql://130.162.157.215:5432/shock");
        dataSource.setUsername("shock");
        dataSource.setPassword("Kth973897G@#$");
        return dataSource;
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new ConnectionMakerImpl();
    }
    
    @Bean
    public ConnectionMaker countingConnectionMaker() {
        return new CountingConnectionMaker(connectionMaker());
    }
}
