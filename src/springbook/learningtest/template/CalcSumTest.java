package springbook.learningtest.template;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

public class CalcSumTest {

    @Test
    public void 합계() {
        Calculator calculator = new Calculator();
        int sum = calculator.calcSum(getClass().getResource("sample_sum.txt").getPath());
        assertThat(sum, is(15));
    }
}
