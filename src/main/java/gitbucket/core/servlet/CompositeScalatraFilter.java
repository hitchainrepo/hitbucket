/*******************************************************************************
 * Copyright (c) 2018-11-05 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package gitbucket.core.servlet;

import org.scalatra.ScalatraFilter;

import gitbucket.core.account.html.newrepo;
import gitbucket.core.util.Tuple;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * CompositeScalatraFilter
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-05 auto generate by qdp.
 */
public class CompositeScalatraFilter implements Filter {

	// private ListBuffer<Tuple2<ScalatraFilter, String>> filters = new
	// ListBuffer<>();
	private List<Tuple.Two<String, ScalatraFilter>> filters = new ArrayList<>();

	public void init(FilterConfig filterConfig) throws ServletException {
		for (Tuple.Two<String, ScalatraFilter> two : filters) {
			two.getSecond().init(filterConfig);
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String contextPath = request.getServletContext().getContextPath();
		String requestPath = ((HttpServletRequest) request).getRequestURI().substring(contextPath.length());
		String checkPath = requestPath;
		if (!requestPath.endsWith("/")) {
			checkPath = requestPath + "/";
		}
		if (!checkPath.startsWith("/upload/") && !checkPath.startsWith("/git/") && !checkPath.startsWith("/git-lfs/")
				&& !checkPath.startsWith("/plugin-assets/")) {
			for (Tuple.Two<String, ScalatraFilter> two : filters) {
				String path = two.getFirst();
				ScalatraFilter filter = two.getSecond();
				String start = path.replaceFirst("/\\*$", "/");
				if (checkPath.startsWith(start)) {
					MockFilterChain mockChain = new MockFilterChain();
					filter.doFilter(request, response, mockChain);
					if (mockChain.isContinue == false) {
						return;
					}
				}
			}
		}
		chain.doFilter(request, response);
	}

	public void destroy() {

	}

	public void mount(ScalatraFilter filter, String path) {
		filters.add(new Tuple.Two<String, ScalatraFilter>(path, filter));
	}
}
