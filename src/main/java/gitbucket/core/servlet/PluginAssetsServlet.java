/*******************************************************************************
 * Copyright (c) 2018-11-05 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package gitbucket.core.servlet;

import gitbucket.core.plugin.PluginRegistry;
import gitbucket.core.util.FileUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import scala.Tuple3;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static scala.collection.JavaConverters.seqAsJavaList;

/**
 * Supply assets which are provided by plugins.
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-05
 * auto generate by qdp.
 */
public class PluginAssetsServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Tuple3<String, String, ClassLoader>> assetsMappings = seqAsJavaList(new PluginRegistry().getAssetsMappings());
        String path = req.getRequestURI().substring(req.getContextPath().length());
        for (Tuple3<String, String, ClassLoader> tuple3 : assetsMappings) {
            String prefix = tuple3._1();
            String resourcePath = tuple3._2();
            ClassLoader classLoader = tuple3._3();
            if (path.startsWith("/plugin-assets" + prefix)) {
                String resourceName = path.substring(("/plugin-assets" + prefix).length());
                InputStream in = classLoader.getResourceAsStream(StringUtils.removeStart(resourcePath, "/") + resourceName);
                try {
                    byte[] bytes = IOUtils.toByteArray(in);
                    resp.setContentLength(bytes.length);
                    resp.setContentType(FileUtil.getMimeType(path, bytes));
                    resp.setHeader("Cache-Control", "max-age=3600");
                    resp.getOutputStream().write(bytes);
                } finally {
                    in.close();
                }
            }
        }
        if (assetsMappings.isEmpty()) {
            resp.setStatus(404);
        }
    }
}
