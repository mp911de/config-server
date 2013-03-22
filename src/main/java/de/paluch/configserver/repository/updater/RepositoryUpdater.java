package de.paluch.configserver.repository.updater;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 12.12.12 10:07
 */
public interface RepositoryUpdater {

    void update() throws Exception;
}
