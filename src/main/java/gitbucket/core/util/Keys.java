/*******************************************************************************
 * Copyright (c) 2018-11-01 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform.
 ******************************************************************************/
package gitbucket.core.util;

/**
 * Define key strings for request attributes, session attributes or flash attributes.
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-01
 * auto generate by qdp.
 */
public class Keys {
    /**
     * Define session keys.
     */
    public static class Session {

        /**
         * Session key for the logged in account information.
         */
        public static final String LoginAccount = "loginAccount";

        /**
         * Session key for the issue search condition in dashboard.
         */
        public static final String DashboardIssues = "dashboard/issues";

        /**
         * Session key for the pull request search condition in dashboard.
         */
        public static final String DashboardPulls = "dashboard/pulls";

        /**
         * Session key for the OpenID Connect authentication.
         */
        public static final String OidcContext = "oidcContext";

        /**
         * Generate session key for the issue search condition.
         */
        public static String Issues(String owner, String name) {
            return owner + "/" + name + "/issues";
        }

        /**
         * Generate session key for the pull request search condition.
         */
        public static String Pulls(String owner, String name) {
            return owner + "/" + name + "/pulls";
        }

        /**
         * Generate session key for the upload filename.
         */
        public static String Upload(String fileId) {
            return "upload_" + fileId;
        }

    }

    public static class Flash {

        /**
         * Flash key for the redirect URL.
         */
        public static final String Redirect = "redirect";

        /**
         * Flash key for the information message.
         */
        public static final String Info = "info";

    }

    /**
     * Define request keys.
     */
    public static class Request {

        /**
         * Request key for the Slick Session.
         */
        public static final String DBSession = "DB_SESSION";

        /**
         * Request key for the Ajax request flag.
         */
        public static final String Ajax = "AJAX";

        /**
         * Request key for the /api/v3 request flag.
         */
        public static final String APIv3 = "APIv3";

        /**
         * Request key for the username which is used during Git repository access.
         */
        public static final String UserName = "USER_NAME";

        /**
         * Generate request key for the request cache.
         */
        public static String Cache(String key) {
            return "cache." + key;
        }
    }
}
