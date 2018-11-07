/*******************************************************************************
 * Copyright (c) 2018-11-02 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package gitbucket.core.servlet;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * MockFilterChain
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-02
 * auto generate by qdp.
 */
public class MockFilterChain implements FilterChain {
    protected boolean isContinue = false;

    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        isContinue = true;
    }
}
