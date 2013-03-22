package de.paluch.configserver.repository.updater;

import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;

import java.io.File;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 11.12.12 15:43
 */
public class SVNRepositoryUpdater extends AbstractRepositoryUpdater {


    @Override
    protected void update(String url, String localWorkDir, String username, String password) throws Exception {
        DAVRepositoryFactory.setup();
        SVNClientManager clientManager = SVNClientManager.newInstance(null, username, password);


        SVNURL svnurl = SVNURL.parseURIEncoded(url);

        File file = new File(localWorkDir);
        boolean update = false;

        if (!file.exists()) {
            file.mkdirs();
        } else {
            File svnsubdir = new File(file, ".svn");
            if (svnsubdir.exists() && svnsubdir.isDirectory()) {
                update = true;
            }
        }


        if (update) {
            clientManager.getWCClient().doCleanup(file);
            clientManager.getUpdateClient().doUpdate(file, SVNRevision.HEAD, true);
        } else {
            clientManager.getUpdateClient().doCheckout(svnurl, file, SVNRevision.HEAD, SVNRevision.HEAD,
                                                       true);
        }

    }

}
