package de.paluch.configserver.repository.updater;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.storage.file.FileRepository;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 12.12.12 10:08
 */
public class GITRepositoryUpdater extends AbstractRepositoryUpdater {

    @Override
    protected void update(String url, String localWorkDir, String username, String password) throws Exception {


        File file = new File(localWorkDir);
        boolean update = false;

        if (!file.exists()) {
            file.mkdirs();
        } else {
            File gitsubdir = new File(file, ".git");
            if (gitsubdir.exists() && gitsubdir.isDirectory()) {
                update = true;
            }
        }


        UsernamePasswordCredentialsProvider user = null;
        if (username != null && password != null) {
            user = new UsernamePasswordCredentialsProvider(username, password);
        }

        Git git = null;

        if (update) {
            git = Git.open(file);
            PullCommand pull = git.pull();
            pull.setCredentialsProvider(user);
            pull.call();
        } else {

            git = new Git(new FileRepository(file));
            CloneCommand clone = git.cloneRepository();
            clone.setURI(url);
            clone.setDirectory(file);
            clone.setCredentialsProvider(user);

            clone.call();
        }

        git.getRepository().close();
    }

}
