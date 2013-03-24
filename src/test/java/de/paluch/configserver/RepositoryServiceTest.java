package de.paluch.configserver;

import com.sun.tools.javac.resources.version;
import de.paluch.configserver.model.config.ConfigRoot;
import de.paluch.configserver.model.repository.FileResource;
import de.paluch.configserver.repository.RepositoryResolver;
import de.paluch.configserver.service.RepositoryService;
import de.paluch.configserver.service.ResourceFinder;
import de.paluch.configserver.spring.ConfigFactory;
import de.paluch.configserver.spring.RepositoryResolverFactory;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import static org.junit.Assert.assertSame;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.eq;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.expression.spel.support.ReflectionHelper;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StreamUtils;

import java.io.InputStream;
import java.util.Properties;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 22.03.13 22:00
 */
@RunWith(MockitoJUnitRunner.class)
public class RepositoryServiceTest {

    @InjectMocks
    private RepositoryService repositoryService = new RepositoryService();

    @Mock
    private ResourceFinder resourceFinder;


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
        InputStream result = repositoryService.getInputStream("myrepo", "theArtifact", "theVersion", "qa", "config.properties");
        assertNotNull(result);
        byte[] bytes = StreamUtils.copyToByteArray(result);
        result.close();

        assertThat(bytes.length, greaterThan(10));

    }

    @Test
    public void findResource() throws Exception {

        when(resourceFinder.findEnvironmentFromHost(any(Properties.class), eq("localhost"))).thenReturn("blubb");

        FileResource fileResource = new FileResource("theArtifact", "theVersion", "blubb", "config.properties");

        when(resourceFinder.findResource(anyList(), eq("blubb"), eq("theVersion"), eq("config.properties"))).thenReturn(fileResource);

        FileResource result = repositoryService.findResource("myrepo", "theArtifact", "theVersion", "config.properties", "localhost");
        assertSame(fileResource, result);
    }
}
