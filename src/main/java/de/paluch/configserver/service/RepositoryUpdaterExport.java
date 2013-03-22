package de.paluch.configserver.service;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import de.paluch.configserver.model.repository.FileRepository;
import de.paluch.configserver.repository.RepositoryResolver;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 10.01.13 14:49
 */
public class RepositoryUpdaterExport {

    private Logger log = Logger.getLogger(getClass());

    @Autowired
    private RepositoryResolver repositoryResolver;

    /**
     * Perform Repository Update.
     *
     * @param repositoryId
     * @return true/false
     * @throws InterruptedException
     */
    public boolean updateRepository(String repositoryId) throws InterruptedException {

        FileRepository repo = repositoryResolver.getRepository(repositoryId);

        log.info("updateRepository|repositoryId|" + repositoryId);
        if (!repo.getUpdateLock().tryLock(60, TimeUnit.SECONDS)) {
            log.warn("updateRepository|repositoryId|" + repositoryId + "|cannot acquire lock");
            return false;
        }

        try {
            if (repositoryResolver.containsUpdater(repo.getId())) {
                repositoryResolver.getUpdater(repo.getId()).update();
                repo.getArtifacts().clear();
                log.info("updateRepository|repositoryId|" + repositoryId + "|successful");
            } else {
                log.warn("updateRepository|repositoryId|" + repositoryId + "|no updater");
            }
            return true;
        } finally {
            repo.getUpdateLock().unlock();
        }
    }

}
