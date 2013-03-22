package de.paluch.configserver.service;

import de.paluch.configserver.model.repository.FileResource;
import org.apache.log4j.Logger;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.xbill.DNS.Address;

import java.util.*;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 12.12.12 12:40
 */
public class ResourceFinder {

    private Logger log = Logger.getLogger(getClass());

    public FileResource findResource(List<FileResource> resources, String environment,
                                     String version,
                                     String filename) {

        List<FileResource> matching = new ArrayList<FileResource>();

        for (FileResource resource : resources) {
            if (resource.getEnvironment().equals(environment) &&
                    resource.getRelativeName().equals(filename)) {
                matching.add(resource);
            }
        }

        Collections.sort(matching, new Comparator<FileResource>() {
            @Override
            public int compare(FileResource fileResource, FileResource fileResource2) {
                return compareVersions(fileResource.getVersion(), fileResource2.getVersion());
            }
        });

        FileResource matchingVersion = null;

        for (FileResource fileResource : matching) {


            if (compareVersions(version, fileResource.getVersion()) < 0) {
                if (matchingVersion == null) {
                    matchingVersion = fileResource;
                }
                break;
            }

            matchingVersion = fileResource;

            if (compareVersions(version, matchingVersion.getVersion()) == 0) {
                break;
            }
        }

        return matchingVersion;
    }


    public int compareVersions(String left, String right) {

        DefaultArtifactVersion o1 = new DefaultArtifactVersion(left);
        DefaultArtifactVersion o2 = new DefaultArtifactVersion(right);
        return o1.compareTo(o2);
    }


    public String findEnvironmentFromHost(Properties properties, String remoteAddr) {


        String fqdn = getFqdn(remoteAddr);
        String hostname = getHostname(fqdn);


        if (properties.containsKey(remoteAddr)) {
            return properties.getProperty(remoteAddr);
        }

        if (hostname != null && properties.containsKey(hostname)) {
            return properties.getProperty(hostname);
        }

        if (fqdn == null) {
            throw new IllegalArgumentException("Cannot resolve " + remoteAddr + " to a FQDN.");
        }

        return properties.getProperty(fqdn);
    }


    public String getHostname(String fqdn) {
        String hostname = null;
        if (fqdn != null) {
            int index = fqdn.indexOf('.');
            if (index != -1) {
                hostname = fqdn.substring(0, index);
            } else {
                hostname = fqdn;
            }
        }

        return hostname;
    }

    public String getFqdn(String remoteAddr) {
        String fqdn = null;
        try {
            fqdn = Address.getHostName(Address.getByName(remoteAddr));
            if (fqdn.endsWith(".")) {
                fqdn = fqdn.substring(0, fqdn.length() - 1);
            }
        } catch (Exception e) {
            log.warn("Remote: " + remoteAddr + ", Exception: " + e.getMessage());
        }
        return fqdn;
    }
}
