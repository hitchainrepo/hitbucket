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
 * A render engine to render content to HTML.
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-09
 * auto generate by qdp.
 */
public interface Renderer {
    /**
     * Render the given request to HTML.
     */
    Html render(RenderRequest request);
}
