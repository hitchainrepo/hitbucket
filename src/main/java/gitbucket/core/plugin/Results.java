/*******************************************************************************
 * Copyright (c) 2018-11-09 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package gitbucket.core.plugin;


import play.twirl.api.Html;

/**
 * Defines result case classes returned by plugin controller.
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-09
 * auto generate by qdp.
 */
public class Results {

    public static class Redirect {
        String path;

        public Redirect(String path) {
            this.path = path;
        }

        public String path() {
            return path;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    public static class Fragment {
        Html html;

        public Fragment(Html html) {
            this.html = html;
        }

        public Html html() {
            return html;
        }

        public Html getHtml() {
            return html;
        }

        public void setHtml(Html html) {
            this.html = html;
        }
    }
}
