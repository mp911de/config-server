package de.paluch.configserver.rest;

import de.paluch.configserver.RemoteUtil;
import de.paluch.configserver.model.repository.FileResource;
import de.paluch.configserver.service.RepositoryService;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.plugins.server.tjws.TJWSEmbeddedJaxrsServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.*;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ConfigResourceTest {


    private static int port;
    private static TJWSEmbeddedJaxrsServer server;

    @InjectMocks
    private final static ConfigResource sut = new ConfigResource();

    @Mock
    private RepositoryService repositoryService;

    @BeforeClass
    public static void beforeClass() throws Exception {

        port = RemoteUtil.findFreePort();

        server = new TJWSEmbeddedJaxrsServer();
        server.setPort(port);
        server.getDeployment().setResources((List) Arrays.asList(sut));
        server.start();

    }

    @AfterClass
    public static void afterClass() throws Exception {
        server.stop();
    }


    @Test
    public void testGetHostBasedRedirect() throws Exception {

        FileResource fileResource = new FileResource("artifact", "vers", "qa", "thefile");
        File file = new File("/tmp/thefile");
        fileResource.setFile(file);

        when(repositoryService.findResource(eq("myrepo"), eq("artifact"), (String) isNull(), eq("thefile"), anyString())).thenReturn(fileResource);

        ClientRequest request = new ClientRequest("http://localhost:" + port + "/dns/myrepo/artifact/thefile");
        ClientResponse<?> response = request.get();

        assertEquals(303, response.getStatus());
        assertEquals("http://localhost:" + port + "/repositories/myrepo/artifact/qa/vers/thefile", response.getLocation().getHref());
    }

    @Test
    public void testGetHostAndVersionBasedRedirect() throws Exception {
        FileResource fileResource = new FileResource("artifact", "vers", "qa", "thefile");
        File file = new File("/tmp/thefile");
        fileResource.setFile(file);

        when(repositoryService.findResource(eq("myrepo"), eq("artifact"), eq("vers"), eq("thefile"), anyString())).thenReturn(fileResource);

        ClientRequest request = new ClientRequest("http://localhost:" + port + "/dns/myrepo/artifact/vers/thefile");
        ClientResponse<?> response = request.get();

        assertEquals(303, response.getStatus());
        assertEquals("http://localhost:" + port + "/repositories/myrepo/artifact/qa/vers/thefile", response.getLocation().getHref());
    }

    @Test
    public void testGetHostnameXML() throws Exception {


        ClientRequest request = new ClientRequest("http://localhost:" + port + "/dns");
        ClientResponse<String> response = request.accept(MediaType.TEXT_XML_TYPE).get(String.class);

        assertEquals(200, response.getStatus());
        assertThat(response.getEntity(), containsString("<requestHostDetails>"));
        assertThat(response.getEntity(), containsString("<remoteAddr>"));
    }

    @Test
    public void testGetHostnameDefaultJSON() throws Exception {


        ClientRequest request = new ClientRequest("http://localhost:" + port + "/dns");
        ClientResponse<String> response = request.get(String.class);

        assertEquals(200, response.getStatus());
        assertThat(response.getEntity(), containsString("\"remoteAddr\""));
    }

    @Test
    public void testGetFileContent() throws Exception {

        ByteArrayInputStream bais = new ByteArrayInputStream("blubb".getBytes());

        when(repositoryService.getInputStream("myrepo", "artifact", "vers", "qa", "thefile")).thenReturn(bais);

        ClientRequest request = new ClientRequest("http://localhost:" + port + "/repositories/myrepo/artifact/qa/vers/thefile");
        ClientResponse<String> response = request.get(String.class);

        assertEquals(200, response.getStatus());
        assertEquals("blubb", response.getEntity());
    }

    @Test
    public void testUpdateRepository() throws Exception {
        ClientRequest request = new ClientRequest("http://localhost:" + port + "/repositories/myrepo");
        ClientResponse<?> response = request.post();

        verify(repositoryService).scheduleRepositoryUpdate("myrepo");
    }
}
