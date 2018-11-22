/*******************************************************************************
 * Copyright (c) 2018-11-09 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package gitbucket.core.plugin;

import gitbucket.core.view.helpers;
import play.twirl.api.Html;

/**
 * DefaultRenderer
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-09
 * auto generate by qdp.
 */
public class DefaultRenderer implements Renderer {
    public static final DefaultRenderer INSTANCE = new DefaultRenderer();

    public Html render(RenderRequest request) {
        return new Html("<tt><pre class=\"plain\">" + helpers.urlLink(request.fileContent()) + "</pre></tt>");
    }
}
