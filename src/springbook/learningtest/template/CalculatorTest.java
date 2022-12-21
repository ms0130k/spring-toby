package springbook.learningtest.template;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class CalculatorTest {
    Calculator calculator;
    String path;
    
    @Before
    public void setup() {
        calculator = new Calculator();
        path = getClass().getResource("sample_calc.txt").getPath();
    }

    @Test
    public void 합() throws IOException {
        assertThat(calculator.calcSum(path), is(10));
    }
    
    @Test
    public void 곱() throws IOException {
        assertThat(calculator.calcMultiply(path), is(24));
    }
    
    @Test
    public void 문자열_연결() throws IOException {
        assertThat(calculator.calcConcatenate(path), is("1234"));
    }
    
}
