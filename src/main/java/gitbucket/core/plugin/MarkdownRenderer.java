/*******************************************************************************
 * Copyright (c) 2018-11-09 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package gitbucket.core.plugin;

import gitbucket.core.view.Markdown;
import play.twirl.api.Html;

/**
 * MarkdownRenderer
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-09
 * auto generate by qdp.
 */
public class MarkdownRenderer implements Renderer {
    public static final MarkdownRenderer INSTANCE = new MarkdownRenderer();

    public Html render(RenderRequest request) {
        return new Html(
            Markdown.toHtml(
                request.fileContent(),
                request.repository(),
                request.enableWikiLink(),
                request.enableRefsLink(),
                request.enableAnchor(),
                false,
                false,
                false,
                null,
                request.context()
            )
        );
    }
}
