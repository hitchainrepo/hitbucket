/*******************************************************************************
 * Copyright (c) 2018-10-31 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform.
 ******************************************************************************/
package gitbucket.core.util;

import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

/**
 * Provides HTTP (Basic) Authentication related functions.
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-10-31
 * auto generate by qdp.
 */
public class AuthUtil {

    public static void requireAuth(HttpServletResponse response) {
        try {
            response.setHeader("WWW-Authenticate", "BASIC realm=\"GitBucket\"");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decodeAuthHeader(String header) {
        try {
            return new String(Base64.getDecoder().decode(header.substring(6)));
        } catch (Throwable e) {
            return "";
        }
    }
}
