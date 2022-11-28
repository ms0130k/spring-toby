package springbook;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.hasItem;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/jUnitTestContext.xml")
public class JUnitTest {
    static Set<JUnitTest> set = new HashSet<JUnitTest>();
    static ApplicationContext contextObject = null;
    
    @Autowired
    ApplicationContext context;

    @Test
    public void test1() {
        assertThat(set, not(hasItem(this)));
        set.add(this);

        assertThat(contextObject == null || contextObject == this.context, is(true));
        contextObject = this.context;
    }

    @Test
    public void test2() {
        assertThat(set, not(hasItem(this)));
        set.add(this);
        
        assertTrue(contextObject == null || contextObject == this.context);
        contextObject = this.context;
    }
    
    @Test
    public void test3() {
        assertThat(set, not(hasItem(this)));
        set.add(this);
        
        assertTrue(contextObject == null || contextObject == this.context);
        contextObject = this.context;
    }
}
