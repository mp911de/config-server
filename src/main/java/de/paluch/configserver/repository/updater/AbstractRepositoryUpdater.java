package de.paluch.configserver.repository.updater;

import de.paluch.configserver.model.RepositoryUpdateException;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 12.12.12 13:18
 */
public abstract class AbstractRepositoryUpdater implements RepositoryUpdater {

    private String url;
    private String localWorkDir;
    private String username;
    private String password;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setLocalWorkDir(String localWorkDir) {
        this.localWorkDir = localWorkDir;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public void update() {
        try {
            update(url, localWorkDir, username, password);
        } catch (Exception e) {
            throw new RepositoryUpdateException(e);
        }
    }

    /**
     * @param url
     * @param localWorkDir
     * @param username
     * @param password
     * @throws Exception
     */
    protected abstract void update(String url, String localWorkDir, String username, String password) throws Exception;
}
