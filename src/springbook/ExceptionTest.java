package springbook;

import org.junit.Test;

public class ExceptionTest {

    @Test
    public void test() {
        try {
            method();
        } catch (Exception e) {
            System.out.println("처리: " + e.getMessage());
        }
        System.out.println("진행");
    }

    private void method() {
        throwRuntimeException();
    }

    private void throwRuntimeException() {
        throw new RuntimeException("runtime");
    }
}
