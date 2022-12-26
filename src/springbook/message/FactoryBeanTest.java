package springbook.message;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class FactoryBeanTest {
    @Autowired
    ApplicationContext context;
    @Autowired
    Message message;

    @Test
    public void autowired() {
        assertThat(message, is(not(nullValue())));
    }
    
    @Test
    public void getMessageFromFactoryBean() {
        Message message = context.getBean("message", Message.class);
        assertThat(message.getText(), is("Factory Bean"));
    }

}
