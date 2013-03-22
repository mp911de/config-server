package de.paluch.configserver.model.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 10.01.13 10:17
 */
public class RepositoryArtifact {

    private Properties properties = new Properties();
    private List<FileResource> cache = new ArrayList<FileResource>();

    public Properties getProperties() {
        return properties;
    }

    public List<FileResource> getCache() {
        return cache;
    }
}
