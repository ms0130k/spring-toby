package springbook.learningtest.jdk.proxy;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.lang.reflect.Proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import springbook.hello.Hello;
import springbook.hello.HelloTarget;
import springbook.hello.HelloUppercase;
import springbook.hello.UppercaseHandler;

public class DynamicProxyTest {

    @Test
    public void simpleProxy() {
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("shock"), is("Hello shock"));
        assertThat(hello.sayHi("shock"), is("Hi shock"));
        assertThat(hello.sayThankYou("shock"), is("Thank you shock"));
        
        Hello proxyHello = new HelloUppercase(hello);
        assertThat(proxyHello.sayHello("shock"), is("HELLO SHOCK"));
        assertThat(proxyHello.sayHi("shock"), is("HI SHOCK"));
        assertThat(proxyHello.sayThankYou("shock"), is("THANK YOU SHOCK"));
        
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { Hello.class }, new UppercaseHandler(hello));
        assertThat(proxiedHello.sayHello("shock"), is("HELLO SHOCK"));
        assertThat(proxiedHello.sayHi("shock"), is("HI SHOCK"));
        assertThat(proxiedHello.sayThankYou("shock"), is("THANK YOU SHOCK"));
    }
    
    @Test
    public void proxyFactoryBean() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());
        pfBean.addAdvice(new UppercaseAdvice());
        
        Hello proxiedHello = (Hello) pfBean.getObject();
        assertThat(proxiedHello.sayHello("shock"), is("HELLO SHOCK"));
        assertThat(proxiedHello.sayHi("shock"), is("HI SHOCK"));
        assertThat(proxiedHello.sayThankYou("shock"), is("THANK YOU SHOCK"));
    }
    
    static class UppercaseAdvice implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String result = (String) invocation.proceed();
            return result.toUpperCase();
        }
    }

    @Test
    public void pointcutAdvisor() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());
        
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");
        
        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
        
        Hello proxiedHello = (Hello) pfBean.getObject();
        
        assertThat(proxiedHello.sayHello("shock"), is("HELLO SHOCK"));
        assertThat(proxiedHello.sayHi("shock"), is("HI SHOCK"));
        assertThat(proxiedHello.sayThankYou("shock"), is("Thank you shock"));
    }
    
    @Test
    public void classNamePointcutAdvisor() {
        NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut() {
            public ClassFilter getClassFilter() {
                return new ClassFilter() {
                    @Override
                    public boolean matches(Class<?> clazz) {
                        return clazz.getSimpleName().startsWith("HelloT");
                    }
                };
            }
        };
        classMethodPointcut.setMappedName("sayH*");
        
        checkAdviced(new HelloTarget(), classMethodPointcut, true);
    }

    private void checkAdviced(Object target, Pointcut pointcut, boolean adviced) {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(target);
        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
        
        Hello proxiedHello = (Hello) pfBean.getObject();
        
        if (adviced) {
            assertThat(proxiedHello.sayHello("shock"), is("HELLO SHOCK"));
            assertThat(proxiedHello.sayHi("shock"), is("HI SHOCK"));
            assertThat(proxiedHello.sayThankYou("shock"), is("Thank you shock"));
        } else {
            assertThat(proxiedHello.sayHello("shock"), is("Hello shock"));
            assertThat(proxiedHello.sayHi("shock"), is("Hi shock"));
            assertThat(proxiedHello.sayThankYou("shock"), is("Thank you shock"));
        }
        
    }
    
    
    
}
