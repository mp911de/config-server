package de.paluch.configserver.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import de.paluch.configserver.model.repository.FileResource;
import org.apache.log4j.Logger;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.xbill.DNS.Address;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 12.12.12 12:40
 */
public class ResourceFinder {

    private Logger log = Logger.getLogger(getClass());

    private LoadingCache<String, String> addressToFqdnCache;

    public ResourceFinder() {
        addressToFqdnCache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).
                maximumSize(4096).build(new CacheLoader<String, String>() {

            @Override
            public String load(String key) throws Exception {
                try {
                    String fqdn = Address.getHostName(Address.getByName(key));
                    if (fqdn.endsWith(".")) {
                        fqdn = fqdn.substring(0, fqdn.length() - 1);
                    }
                    return fqdn;
                } catch (Exception e) {
                    log.warn("Remote: " + key + ", Exception: " + e.getMessage());
                    return null;
                }
            }
        });
    }


    public FileResource findResource(List<FileResource> resources, String environment, String version,
                                     String filename) {

        List<FileResource> matching = new ArrayList<FileResource>();

        for (FileResource resource : resources) {
            if (resource.getEnvironment().equals(environment) && resource.getRelativeName().equals(filename)) {
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
        try {
            return addressToFqdnCache.get(remoteAddr);
        } catch (ExecutionException e) {
            log.warn("Remote: " + remoteAddr + ", Exception: " + e.getMessage());
        }
        return null;
    }
}
