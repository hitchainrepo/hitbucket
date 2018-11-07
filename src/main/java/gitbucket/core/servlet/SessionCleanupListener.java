/*******************************************************************************
 * Copyright (c) 2018-11-02 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package gitbucket.core.servlet;

import gitbucket.core.util.Directory;
import org.apache.commons.io.FileUtils;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Removes session associated temporary files when session is destroyed.
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-02
 * auto generate by qdp.
 */
public class SessionCleanupListener implements HttpSessionListener {
    public void sessionCreated(HttpSessionEvent se) {

    }

    public void sessionDestroyed(HttpSessionEvent se) {
        try {
            FileUtils.deleteDirectory(Directory.getTemporaryDir(se.getSession().getId()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
