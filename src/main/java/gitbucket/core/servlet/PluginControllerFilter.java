/*******************************************************************************
 * Copyright (c) 2018-11-02 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package gitbucket.core.servlet;

import gitbucket.core.controller.ControllerBase;
import gitbucket.core.plugin.PluginRegistry;
import scala.Tuple2;
import scala.collection.JavaConverters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * PluginControllerFilter
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-02
 * auto generate by qdp.
 */
public class PluginControllerFilter implements Filter {

    private FilterConfig filterConfig = null;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String contextPath = request.getServletContext().getContextPath();
        String requestUri = ((HttpServletRequest) request).getRequestURI().substring(contextPath.length());
        ConcurrentLinkedQueue<Tuple2<ControllerBase, String>> controllers = PluginRegistry.apply().getControllers();
        for (Tuple2<ControllerBase, String> controller : controllers) {
            ControllerBase ctrl = controller._1;
            String path = controller._2;
            String start = path.replaceFirst("/\\*$", "/");
            if ((requestUri + "/").startsWith(start)) {
                if (ctrl.config() == null) {
                    ctrl.init(filterConfig);
                }
                MockFilterChain mockChain = new MockFilterChain();
                ctrl.doFilter(request, response, mockChain);
                if (mockChain.isContinue == false) {
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }

    public void destroy() {
        ConcurrentLinkedQueue<Tuple2<ControllerBase, String>> controllers = PluginRegistry.apply().getControllers();
        for (Tuple2<ControllerBase, String> controller : controllers) {
            controller._1.destroy();
        }
    }
}
