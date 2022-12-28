package springbook.learningtest.template;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class CalcSumTest {
    Calculator calculator;
    
    @Before
    public void setUp() {
        calculator = new Calculator(getClass().getResource("sample.txt").getPath());
    }
    
    @Test
    public void sumOfNumbers() {
        assertThat(calculator.calcSum(), is(15));
    }
    
    @Test
    public void multipleOfNumbers() {
        assertEquals(calculator.calcMultiply(), 120);
    }
    
    @Test
    public void linkString() {
        assertEquals(calculator.calcLinkString(), "12345");
    }
    
    static class Calculator {
        String filePath;
        
        public Calculator(String filePath) {
            this.filePath = filePath;
        }

        public int calcMultiply() {
            Behavior<Integer> behavior = new Behavior<Integer>() {
                @Override
                public Integer doSomething(Integer result, String line) {
                    return result * Integer.parseInt(line);
                }
            };
            return workWithContext(1, behavior);
        }

        public Integer calcSum() {
            Behavior<Integer> behavior = new Behavior<Integer>() {
                @Override
                public Integer doSomething(Integer result, String line) {
                    return result + Integer.parseInt(line);
                }
            };
            return workWithContext(0, behavior);
        }
        
        public String calcLinkString() {
            Behavior<String> behavior = new Behavior<String>() {
                @Override
                public String doSomething(String result, String line) {
                    return result + line;
                }
            };
            return workWithContext("", behavior);
        }
        
        public <T> T workWithContext(T initValue, Behavior<T> behavior) {
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                T result = initValue;
                String line = null;
                while ((line = br.readLine()) != null) {
                    result = behavior.doSomething(result, line);
                }
                return result;
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        
        interface Behavior<T> {
            T doSomething(T result, String line);
        }
    }
}
