package springbook.proxy;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.junit.Test;

import springbook.hello.Hello;
import springbook.hello.HelloTarget;

public class HelloTest {

    @Test
    public void simpleProxy() {
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("Shock"), is("Hello Shock"));
        assertThat(hello.sayHi("Shock"), is("Hi Shock"));
        assertThat(hello.sayThankYou("Shock"), is("Thank you Shock"));
    }
    
    @Test
    public void uppercase() {
        Hello hello = new HelloUppercase(new HelloTarget());
        assertThat(hello.sayHello("Shock"), is("HELLO SHOCK"));
        assertThat(hello.sayHi("Shock"), is("HI SHOCK"));
        assertThat(hello.sayThankYou("Shock"), is("THANK YOU SHOCK"));
    }

    @Test
    public void proxyHandlerMy() throws Throwable {
        UppercaseHandler uppercaseHandler = new UppercaseHandler(new HelloTarget());
        Method method = HelloTarget.class.getMethod("sayHello", String.class);
        assertThat((String) uppercaseHandler.invoke(new Object(), method , new Object[] { "Shock" }), is("HELLO SHOCK"));
    }
    
    @Test
    public void userProxyHandler() {
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { Hello.class }, new UppercaseHandler(new HelloTarget()));
        assertThat(proxiedHello.sayHello("Shock"), is("HELLO SHOCK"));
        assertThat(proxiedHello.sayHi("Shock"), is("HI SHOCK"));
        assertThat(proxiedHello.sayThankYou("Shock"), is("THANK YOU SHOCK"));
    }
}
