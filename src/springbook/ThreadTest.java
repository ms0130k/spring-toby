package springbook;

import org.junit.Test;

public class ThreadTest {

    @Test
    public void test() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("이건 언제?");
            e.printStackTrace();
        }
        System.out.println("gg");
    }

}
