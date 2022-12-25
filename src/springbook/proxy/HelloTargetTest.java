package springbook.proxy;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import springbook.hello.Hello;
import springbook.hello.HelloTarget;

public class HelloTargetTest {

    @Test
    public void simpleProxy() {
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("Shock"), is("Hello Shock"));
        assertThat(hello.sayHi("Shock"), is("Hi Shock"));
        assertThat(hello.sayThankYou("Shock"), is("Thank you Shock"));
    }

}
