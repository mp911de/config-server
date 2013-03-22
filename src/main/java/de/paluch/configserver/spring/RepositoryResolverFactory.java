package de.paluch.configserver.spring;

import de.paluch.configserver.model.config.ConfigRoot;
import de.paluch.configserver.model.config.ConfigServerRepository;
import de.paluch.configserver.model.config.RepositoryType;
import de.paluch.configserver.model.repository.FileRepository;
import de.paluch.configserver.repository.RepositoryResolver;
import de.paluch.configserver.repository.updater.SVNRepositoryUpdater;
import de.paluch.configserver.repository.updater.AbstractRepositoryUpdater;
import de.paluch.configserver.repository.updater.GITRepositoryUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import java.io.File;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 12.12.12 13:12
 */
public class RepositoryResolverFactory extends AbstractFactoryBean<RepositoryResolver> {

    @Autowired
    private ConfigRoot configuration;

    @Override
    public Class<?> getObjectType() {
        return RepositoryResolver.class;
    }

    @Override
    protected RepositoryResolver createInstance() throws Exception {

        RepositoryResolver resolver = new RepositoryResolver();

        for (ConfigServerRepository repositoryConfig : configuration.getConfigServerRepositories().getRepositories()) {

            FileRepository fileRepository = new FileRepository();
            fileRepository.setId(repositoryConfig.getId());
            fileRepository.setLocalWorkDir(repositoryConfig.getLocalWorkDirectory());


            File repoRoot = new File(repositoryConfig.getLocalWorkDirectory());

            if (!repoRoot.exists()) {
                repoRoot.mkdirs();
            }

            resolver.addRepository(repositoryConfig.getId(), fileRepository, repositoryConfig);

            AbstractRepositoryUpdater updater = createUpdater(repositoryConfig);
            if (updater != null) {
                resolver.addRepositoryUpdater(repositoryConfig.getId(), updater);
            }
        }

        return resolver;
    }

    private AbstractRepositoryUpdater createUpdater(ConfigServerRepository repositoryConfig) {
        AbstractRepositoryUpdater result = null;

        if (repositoryConfig.getType() == RepositoryType.GIT) {
            result = new GITRepositoryUpdater();
        }

        if (repositoryConfig.getType() == RepositoryType.SVN) {
            result = new SVNRepositoryUpdater();
        }

        if (result != null) {
            result.setLocalWorkDir(repositoryConfig.getLocalWorkDirectory());
            result.setPassword(repositoryConfig.getPassword());
            result.setUrl(repositoryConfig.getRemoteUrl());
            result.setUsername(repositoryConfig.getUsername());
        }

        return result;
    }
}
