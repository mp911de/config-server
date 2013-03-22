package de.paluch.configserver;

import de.paluch.configserver.model.config.ConfigRoot;
import de.paluch.configserver.model.repository.FileResource;
import de.paluch.configserver.repository.RepositoryResolver;
import de.paluch.configserver.service.RepositoryService;
import de.paluch.configserver.spring.ConfigFactory;
import de.paluch.configserver.spring.RepositoryResolverFactory;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.expression.spel.support.ReflectionHelper;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StreamUtils;

import java.io.InputStream;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 22.03.13 22:00
 */
public class RepositoryServiceTest {

    private RepositoryService repositoryService = new RepositoryService();


    @Before
    public void before() throws Exception {
        ConfigFactory factory = new ConfigFactory();
        factory.setSingleton(false);
        factory.setConfigLocation(getClass().getResource("/config.xml"));

        RepositoryResolverFactory rrFactory = new RepositoryResolverFactory();
        rrFactory.setSingleton(false);

        ReflectionTestUtils.setField(rrFactory, "configuration", factory.getObject());
        ReflectionTestUtils.setField(repositoryService, "repositoryResolver", rrFactory.getObject());
    }

    @Test
    public void getInputStream() throws Exception {
        InputStream result = repositoryService.getInputStream("myrepo", "theArtifact",
                                                              "theVersion", "qa", "config.properties");
        assertNotNull(result);
        byte[] bytes = StreamUtils.copyToByteArray(result);
        result.close();

        assertThat(bytes.length, greaterThan(10));


    }
}
