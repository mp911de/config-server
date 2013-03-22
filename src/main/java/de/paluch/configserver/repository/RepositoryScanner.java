package de.paluch.configserver.repository;

import de.paluch.configserver.model.repository.FileResource;
import de.paluch.configserver.model.repository.RepositoryArtifact;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 12.12.12 11:14
 */
public class RepositoryScanner {

    public static final int LEVEL_ARTIFACT = 0;
    public static final int LEVEL_VERSION = 1;
    public static final int LEVEL_ENVIRONMENT = 2;
    public static final int LEVEL_FILE = 4;

    private Logger log = Logger.getLogger(getClass());


    private void scanArtifact(File artifactBaseDirectory, List<FileResource> result) throws IOException {

        File[] files = artifactBaseDirectory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (!file.isDirectory()) {
                    continue;
                }

                scanVersion(artifactBaseDirectory, file, result);
            }
        }

    }


    private void scanVersion(File artifactBaseDirectory, File versionBaseDirectory,
                             List<FileResource> result) throws IOException {

        File[] files = versionBaseDirectory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (!file.isDirectory()) {
                    continue;
                }

                scanFiles(artifactBaseDirectory, versionBaseDirectory, file, file, result);
            }
        }

    }


    private void scanFiles(File artifactBaseDirectory, File versionBaseDirectory, File environmentBaseDirectory,
                           File parent,
                           List<FileResource> result) throws IOException {

        File[] files = parent.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    scanFiles(artifactBaseDirectory, environmentBaseDirectory, versionBaseDirectory, file, result);
                } else {

                    String relativePath = getRelativePath(environmentBaseDirectory, file);
                    FileResource resource = new FileResource();
                    resource.setArtifactId(artifactBaseDirectory.getName());
                    resource.setEnvironment(environmentBaseDirectory.getName());
                    resource.setVersion(versionBaseDirectory.getName());
                    resource.setFile(file);
                    resource.setRelativeName(relativePath);

                    result.add(resource);
                }
            }
        }
    }

    private String getRelativePath(File baseDirectory, File file) throws IOException {

        String full = file.getCanonicalPath();
        String base = baseDirectory.getCanonicalPath();

        String result = full;

        if (full.startsWith(base)) {
            result = full.substring(base.length());
        }

        if (result.startsWith("/")) {
            result = result.substring(1);
        }

        return result;
    }

    /**
     *
     * @param directory
     * @param artifactId
     * @return RepositoryArtifact
     * @throws IOException
     */
    public RepositoryArtifact scanArtifact(String directory, String artifactId) throws IOException {

        File artifactDirectory = new File(directory, artifactId);

        if (!artifactDirectory.exists()) {
            return null;
        }

        RepositoryArtifact artifact = new RepositoryArtifact();
        List<FileResource> resources = new ArrayList<FileResource>();

        scanArtifact(artifactDirectory, resources);
        artifact.getCache().addAll(resources);

        readProperties(artifact.getProperties(), artifactDirectory);

        return artifact;
    }

    private void readProperties(Properties properties, File baseDirectory) {

        File thePropertyFile = new File(baseDirectory, "hosts.properties");
        if (thePropertyFile.exists()) {

            InputStream is = null;
            try {
                is = new FileInputStream(thePropertyFile);
                properties.load(is);
            } catch (IOException e) {
                log.warn(e);
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    log.debug(e.getMessage(), e);
                }
            }
        }
    }
}
