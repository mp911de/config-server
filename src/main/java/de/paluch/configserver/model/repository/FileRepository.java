package de.paluch.configserver.model.repository;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 11.12.12 15:38
 */
public class FileRepository {

    private String id;
    private String localWorkDir;

    private Map<String, RepositoryArtifact> artifacts = new HashMap<String, RepositoryArtifact>();
    private Lock updateLock = new ReentrantLock();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocalWorkDir() {
        return localWorkDir;
    }

    public void setLocalWorkDir(String localWorkDir) {
        this.localWorkDir = localWorkDir;
    }

    public Map<String, RepositoryArtifact> getArtifacts() {
        return artifacts;
    }

    public Lock getUpdateLock() {
        return updateLock;
    }
}
