package de.paluch.configserver.repository.updater;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import de.paluch.configserver.model.RepositoryUpdateException;
import de.paluch.configserver.model.repository.FileRepository;
import de.paluch.configserver.repository.RepositoryResolver;
import de.paluch.configserver.service.RepositoryUpdaterExport;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 12.12.12 13:52
 */
public class RepositoryUpdaterJob extends QuartzJobBean {

    private Logger log = Logger.getLogger(getClass());

    @Override
    protected void executeInternal(org.quartz.JobExecutionContext context) throws JobExecutionException {
        RepositoryResolver repositoryResolver = (RepositoryResolver) context.getMergedJobDataMap().get(
                "repositoryResolver");

        RepositoryUpdaterExport updaterExport = (RepositoryUpdaterExport) context.getMergedJobDataMap().get(
                "repositoryUpdaterExport");

        for (FileRepository repository : repositoryResolver.getRepositories()) {

            try {
                updaterExport.updateRepository(repository.getId());
            } catch (InterruptedException e) {
                log.warn(e.getMessage(), e);
                return;
            } catch (RepositoryUpdateException e) {
                log.warn(repository.getId() + ": " + e.getMessage());
            }
        }
    }
}
