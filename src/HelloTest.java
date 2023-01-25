import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.StaticApplicationContext;

public class HelloTest {

    @Test
    public void staticApplicationContext() {
        StaticApplicationContext context = new StaticApplicationContext();
        context.registerSingleton("hello1", Hello.class);
        
        Hello hello1 = context.getBean("hello1", Hello.class);
        assertNotNull(hello1);
        
        BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
        helloDef.getPropertyValues().addPropertyValue("name", "Spring");
        context.registerBeanDefinition("printer", new RootBeanDefinition(StringPrinter.class));
        helloDef.getPropertyValues().addPropertyValue("printer", new RuntimeBeanReference("printer"));
        context.registerBeanDefinition("hello2", helloDef);
        
        Hello hello2 = context.getBean("hello2", Hello.class);
        assertNotNull(hello2);
        assertEquals(hello2.sayHello(), "Hello Spring");
        assertThat(hello1, is(not(hello2)));
        assertThat(context.getBeanFactory().getBeanDefinitionCount(), is(3));
        
        
        hello2.print();
        
        assertThat(context.getBean("printer").toString(), is("Hello Spring"));
    }

}
