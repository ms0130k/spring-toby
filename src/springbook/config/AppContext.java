package springbook.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import springbook.issuetracker.sqlservice.EnableSqlService;
import springbook.issuetracker.sqlservice.SqlMapConfig;
import springbook.user.dao.UserDao;
import springbook.user.service.DummyMailSender;
import springbook.user.service.UserService;
import springbook.user.service.UserServiceTest;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "springbook.user")
@EnableSqlService
@PropertySource("/database.properties")
public class AppContext implements SqlMapConfig {
    @Value("${db.driverClass}")
    Class<? extends java.sql.Driver> driverClass;
    @Value("${db.uri}")
    String uri;
    @Value("${db.username}")
    String username;
    @Value("${db.password}")
    String password;
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
    
    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        
        dataSource.setDriverClass(driverClass);
        dataSource.setUrl(uri);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
    
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
    
    @Configuration
    @Profile("production")
    public static class ProductionAppContext {
        @Bean
        public MailSender mailSender() {
            return new JavaMailSenderImpl();
        }
    }
    
    @Configuration
    @Profile("test")
    public static class TestAppContext {
        @Autowired
        UserDao userDao;
        
        @Bean
        public UserService testUserService() {
            UserServiceTest.TestUserService testUserService = new UserServiceTest.TestUserService();
            testUserService.setMailSender(mailSender());
            testUserService.setUserDao(userDao);
            return testUserService;
        }
        
        @Bean
        public MailSender mailSender() {
            return new DummyMailSender();
        }
    }

    @Override
    public Resource getSqlMapResource() {
        return new ClassPathResource("sqlmap.xml", UserDao.class);
    }
}
