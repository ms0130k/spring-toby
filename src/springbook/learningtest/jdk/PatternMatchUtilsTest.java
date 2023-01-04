package springbook.learningtest.jdk;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.util.PatternMatchUtils;

public class PatternMatchUtilsTest {

    @Test
    public void test() {
        assertTrue(PatternMatchUtils.simpleMatch("*test", "abcdtest"));
        assertTrue(PatternMatchUtils.simpleMatch("*test", "test"));
        assertFalse(PatternMatchUtils.simpleMatch("*test", "testa"));
        
        assertTrue(PatternMatchUtils.simpleMatch("test*", "testabce"));
        assertTrue(PatternMatchUtils.simpleMatch("test*", "test"));
        assertFalse(PatternMatchUtils.simpleMatch("test*", "atest"));
        
        assertTrue(PatternMatchUtils.simpleMatch("*test*", "aaatestddd"));
        assertTrue(PatternMatchUtils.simpleMatch("*test*", "test"));
        assertFalse(PatternMatchUtils.simpleMatch("*test*", "tes"));
    }

}
