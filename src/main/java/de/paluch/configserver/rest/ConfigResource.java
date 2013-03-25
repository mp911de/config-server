package de.paluch.configserver.rest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.springframework.beans.factory.annotation.Autowired;

import de.paluch.configserver.model.repository.FileResource;
import de.paluch.configserver.model.rest.RequestHostDetails;
import de.paluch.configserver.service.RepositoryService;
import de.paluch.configserver.service.ResourceFinder;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 11.12.12 15:33
 */
@Path("/")
public class ConfigResource {

    @Autowired
    private RepositoryService repositoryService;

    @GET
    @Path("dns/{repositoryId}/{artifactId}/{filename}")
    public Response getHostBasedRedirect(@PathParam("repositoryId") String repositoryId,
                                         @PathParam("artifactId") String artifactId,
                                         @PathParam("filename") String filename,
                                         @Context HttpServletRequest request, @Context UriInfo uriInfo)
            throws IOException {

        return getHostAndVersionBasedRedirect(repositoryId, artifactId, null, filename, request, uriInfo);
    }

    @GET
    @Path("dns/{repositoryId}/{artifactId}/{version}/{filename}")
    public Response getHostAndVersionBasedRedirect(@PathParam("repositoryId") String repositoryId,
                                                   @PathParam("artifactId") String artifactId,
                                                   @PathParam("version") String version,
                                                   @PathParam("filename") String filename,
                                                   @Context HttpServletRequest request, @Context UriInfo uriInfo)
            throws IOException {

        FileResource resource = repositoryService.findResource(repositoryId, artifactId, version, filename,
                                                               request.getRemoteAddr());

       UriBuilder builder = uriInfo.getBaseUriBuilder().path(getClass(), "getFileContent");

        File file = resource.getFile();

        URI location =  builder.build(repositoryId, resource.getArtifactId(), resource.getVersion(),  resource.getEnvironment(),
                file.getName());

        return Response.seeOther(location).build();
    }

    @GET
    @Path("dns")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public RequestHostDetails getHostname(@Context HttpServletRequest request) {

        ResourceFinder finder = new ResourceFinder();
        String fqdn = finder.getFqdn(request.getRemoteAddr());
        String hostname = finder.getHostname(fqdn);

        RequestHostDetails result = new RequestHostDetails();

        result.setFqdn(fqdn);
        result.setHostname(hostname);
        result.setRemoteAddr(request.getRemoteAddr());

        return result;
    }

    @GET
    @Path("repositories/{repositoryId}/{artifactId}/{version}/{environment}/{filename}")
    public Response getFileContent(@PathParam("repositoryId") String repositoryId,
                                   @PathParam("artifactId") String artifactId,
                                   @PathParam("environment") String environment,
                                   @PathParam("version") String version, @PathParam("filename") String filename)
            throws IOException {

        InputStream theStream = repositoryService.getInputStream(repositoryId, artifactId, version, environment,
                                                                 filename);

        return Response.ok(theStream, MediaType.APPLICATION_OCTET_STREAM).build();
    }

    @POST
    @Path("repositories/{repositoryId}")
    public Response updateRepository(@PathParam("repositoryId") String repositoryId) throws Exception {

        repositoryService.scheduleRepositoryUpdate(repositoryId);
        return Response.ok("OK - SCHEDULED", MediaType.TEXT_PLAIN_TYPE).build();
    }
}
