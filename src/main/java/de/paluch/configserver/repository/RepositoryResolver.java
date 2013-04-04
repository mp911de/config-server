package de.paluch.configserver.repository;

import de.paluch.configserver.model.NotFoundException;
import de.paluch.configserver.model.config.ConfigServerRepository;
import de.paluch.configserver.model.repository.FileRepository;
import de.paluch.configserver.repository.updater.AbstractRepositoryUpdater;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 12.12.12 13:11
 */
public class RepositoryResolver {

    private Map<String, FileRepository> repositories = new HashMap<String, FileRepository>();
    private Map<String, AbstractRepositoryUpdater> repositoryUpdaters = new HashMap<String, AbstractRepositoryUpdater>();
    private Map<String, ConfigServerRepository> repositoryConfig = new HashMap<String, ConfigServerRepository>();

    public void addRepository(String id, FileRepository fileRepository, ConfigServerRepository config) {
        repositories.put(id, fileRepository);
        repositoryConfig.put(id, config);
    }

    public void addRepositoryUpdater(String id, AbstractRepositoryUpdater repositoryUpdater) {
        repositoryUpdaters.put(id, repositoryUpdater);
    }

    public FileRepository getRepository(String id) {

        if (repositories.containsKey(id)) {
            return repositories.get(id);
        }

        throw new NotFoundException("Cannot find Repository " + id);
    }

    public AbstractRepositoryUpdater getUpdater(String id) {

        if (containsUpdater(id)) {
            return repositoryUpdaters.get(id);
        }

        throw new NotFoundException("Cannot find Repository " + id);
    }

    public ConfigServerRepository getConfig(String id) {

        if (repositoryConfig.containsKey(id)) {
            return repositoryConfig.get(id);
        }

        throw new NotFoundException("Cannot find Repository " + id);

    }

    public boolean containsUpdater(String id) {
        return repositoryUpdaters.containsKey(id);
    }

    public Collection<AbstractRepositoryUpdater> getUpdaters() {
        return repositoryUpdaters.values();
    }

    public Collection<FileRepository> getRepositories() {
        return repositories.values();
    }


}
