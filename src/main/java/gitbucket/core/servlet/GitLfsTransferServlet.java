/*******************************************************************************
 * Copyright (c) 2018-11-05 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package gitbucket.core.servlet;

import gitbucket.core.util.FileUtil;
import gitbucket.core.util.StringUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.json4s.Formats;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.MessageFormat;

import static org.json4s.jackson.Serialization.write;

/**
 * Provides GitLFS Transfer API
 * https://github.com/git-lfs/git-lfs/blob/master/docs/api/basic-transfers.md
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-05
 * auto generate by qdp.
 */
public class GitLfsTransferServlet extends HttpServlet {
    private static long LongObjectIdLength = 32;
    private static long LongObjectIdStringLength = LongObjectIdLength * 2;
    private Formats jsonFormats = gitbucket.core.api.JsonFormat.jsonFormats();

    protected void doGet(HttpServletRequest req, HttpServletResponse res) {
        String[] pathInfo = getPathInfo(req, res);
        if (pathInfo.length == 3 && checkToken(req, pathInfo[2])) {
            try {
                File file = new File(FileUtil.getLfsFilePath(pathInfo[0], pathInfo[1], pathInfo[2]));
                if (file.exists()) {
                    res.setStatus(HttpStatus.SC_OK);
                    res.setContentType("application/octet-stream");
                    res.setContentLength(Long.valueOf(file.length()).intValue());
                    FileInputStream in = new FileInputStream(file);
                    ServletOutputStream out = res.getOutputStream();
                    IOUtils.copy(in, out);
                    out.flush();
                } else {
                    sendError(res, HttpStatus.SC_NOT_FOUND, MessageFormat.format("Object ''{0}'' not found", pathInfo[2]));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected void doPut(HttpServletRequest req, HttpServletResponse res) {
        String[] pathInfo = getPathInfo(req, res);
        if (pathInfo.length == 3 && checkToken(req, pathInfo[2])) {
            try {
                File file = new File(FileUtil.getLfsFilePath(pathInfo[0], pathInfo[1], pathInfo[2]));
                FileUtils.forceMkdir(file.getParentFile());
                ServletInputStream in = req.getInputStream();
                FileOutputStream out = new FileOutputStream(file);
                IOUtils.copy(in, out);
                res.setStatus(HttpStatus.SC_OK);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean checkToken(HttpServletRequest req, String oid) {
        String token = req.getHeader("Authorization");
        if (token != null) {
            String[] split = StringUtil.decodeBlowfish(token).split(" ");
            String expireAt = split[0], targetOid = split[1];
            return StringUtils.equals(oid, targetOid) && Long.valueOf(expireAt) > System.currentTimeMillis();
        } else {
            return false;
        }
    }

    private String[] getPathInfo(HttpServletRequest req, HttpServletResponse res) {
        String[] split = req.getRequestURI().substring(1).split("/");
        ArrayUtils.reverse(split);
        if (split.length >= 3) {//owner, repository, oid
            return new String[]{split[0], split[1], split[2]};
        }
        return new String[0];
    }

    private void sendError(HttpServletResponse res, int status, String message) {
        try {
            res.setStatus(status);
            PrintWriter out = res.getWriter();
            out.write(write(new GitLfs.Error(message), jsonFormats));
            out.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
