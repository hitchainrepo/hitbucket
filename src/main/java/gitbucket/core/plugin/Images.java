/*******************************************************************************
 * Copyright (c) 2018-11-08 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package gitbucket.core.plugin;

/**
 * Provides a helper method to generate data URI of images registered by plug-in.
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-08
 * auto generate by qdp.
 */
public class Images {
    public static String dataURI(String id) {
        return "data:image/png;base64," + PluginRegistry.apply().getImage(id);
    }
}
