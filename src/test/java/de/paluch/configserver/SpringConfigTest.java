package de.paluch.configserver;

import org.hamcrest.BaseMatcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 22.03.13 21:39
 */
public class SpringConfigTest {

    @Test
    public void testContext() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/springconfig/spring-application" +
                                                                                            ".xml");
        assertThat(context.getBeanDefinitionCount(), greaterThan(5));
        context.close();
    }

}
