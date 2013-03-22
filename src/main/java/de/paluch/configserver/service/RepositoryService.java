package de.paluch.configserver.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.jgroups.blocks.MethodCall;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.RpcDispatcher;

import de.paluch.configserver.model.NotFoundException;
import de.paluch.configserver.model.UnconfiguredHostException;
import de.paluch.configserver.model.repository.FileRepository;
import de.paluch.configserver.model.repository.FileResource;
import de.paluch.configserver.model.repository.RepositoryArtifact;
import de.paluch.configserver.repository.RepositoryResolver;
import de.paluch.configserver.repository.RepositoryScanner;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 08.01.13 20:47
 */
public class RepositoryService {

    private Logger log = Logger.getLogger(getClass());

    @Autowired
    private RepositoryResolver repositoryResolver;

    @Autowired
    private RpcDispatcher rpcDispatcher;

    /**
     * Schedule Update Repository using JGroups.
     * @param repositoryId
     * @throws Exception
     */
    public void scheduleRepositoryUpdate(String repositoryId) throws Exception {

        repositoryResolver.getRepository(repositoryId);

        MethodCall call = new MethodCall("updateRepository", new Object[] { repositoryId },
                                         new Class[] { String.class });

        rpcDispatcher.callRemoteMethodsWithFuture(rpcDispatcher.getChannel().getView().getMembers(), call,
                                                  new RequestOptions());
    }


    public InputStream getInputStream(String repositoryId, String artifactId, String version, String environment,
                                      String filename) throws IOException {

        FileRepository repo = repositoryResolver.getRepository(repositoryId);


        File repoFile = new File(repo.getLocalWorkDir());
        File artifactFile = new File(repoFile, artifactId);
        File versionFile = new File(artifactFile, version);
        File envFile = new File(versionFile, environment);
        File theFile = new File(envFile, filename);

        if (theFile.exists()) {

            return InputStreamBuilder.newInstance().artifact(artifactFile).
                    environment(envFile).version(versionFile).file(theFile).guessType().build();
        }

        throw new NotFoundException(filename + " (" + theFile + ")");
    }

    public FileResource findResource(String repositoryId, String artifactId, String version, String filename,
                                     String remoteAddr) throws IOException {

        FileRepository repo = repositoryResolver.getRepository(repositoryId);
        ResourceFinder finder = new ResourceFinder();

        RepositoryArtifact artifact = getRepositoryArtifact(artifactId, repo);
        String environment = getEnvironment(remoteAddr, finder, artifact);

        FileResource result = finder.findResource(artifact.getCache(), environment, version, filename);
        if (result == null) {
            throw new NotFoundException("Cannot find file " + filename);
        }

        return null;
    }

    private String getEnvironment(String remoteAddr, ResourceFinder finder, RepositoryArtifact artifact) {

        String environment = finder.findEnvironmentFromHost(artifact.getProperties(), remoteAddr);
        if (environment == null) {
            throw new UnconfiguredHostException(finder.getFqdn(remoteAddr));
        }
        return environment;
    }

    private RepositoryArtifact getRepositoryArtifact(String artifactId, FileRepository repo) throws IOException {

        RepositoryArtifact artifact = repo.getArtifacts().get(artifactId);

        if (artifact == null) {
            RepositoryScanner scanner = new RepositoryScanner();
            artifact = scanner.scanArtifact(repo.getLocalWorkDir(), artifactId);
            repo.getArtifacts().put(artifactId, artifact);
        }

        if (artifact == null) {
            throw new NotFoundException("Cannot find artifact " + artifactId);
        }
        return artifact;
    }

}
