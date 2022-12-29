package springbook.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class JUnitTest {
    @Autowired
    ApplicationContext context;
    static ApplicationContext prevContext;
    static JUnitTest junitTest;
    static Set<JUnitTest> testObjects = new HashSet<>();

    @Before
    public void setUp() {
    }
    
    @Test
    public void context1() {
        assertTrue(prevContext == null || prevContext == context);
        prevContext = context;
    }
    
    @Test
    public void context2() {
        assertTrue(prevContext == null || prevContext == context);
        prevContext = context;
    }
    
    @Test
    public void context3() {
        assertTrue(prevContext == null || prevContext == context);
        prevContext = context;
    }
    
    @Test
    public void testObject1() {
        assertThat(this, is(not(sameInstance(junitTest))));
        assertFalse(testObjects.contains(this));
        junitTest = this;
        testObjects.add(this);
    }

    @Test
    public void testObject2() {
        assertThat(this, is(not(sameInstance(junitTest))));
        assertFalse(testObjects.contains(this));
        junitTest = this;
        testObjects.add(this);
    }
    
    @Test
    public void testObject3() {
        assertThat(this, is(not(sameInstance(junitTest))));
        assertFalse(testObjects.contains(this));
        junitTest = this;
        testObjects.add(this);
    }
    
}
