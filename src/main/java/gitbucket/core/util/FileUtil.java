/*******************************************************************************
 * Copyright (c) 2018-10-31 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform.
 ******************************************************************************/
package gitbucket.core.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.tika.Tika;
import scala.util.Random;

import java.io.File;

/**
 * FileUtilHelper
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-10-31
 * auto generate by qdp.
 */
public class FileUtil {

    public static long MaxFileSize = getMaxFileSize();
    public static String generateFileId = System.currentTimeMillis() + new Random().alphanumeric().take(10).mkString();

    public static long getMaxFileSize() {
        if (System.getProperty("gitbucket.maxFileSize") != null) {
            return new Long(System.getProperty("gitbucket.maxFileSize"));
        } else {
            return 3 * 1024 * 1024;
        }
    }

    public static String getMimeType(String name) {
        Tika tika = new Tika();
        String detect = tika.detect(name);
        if (detect == null) {
            return "application/octet-stream";
        } else {
            return detect;
        }
    }

    public static String getMimeType(String name, byte[] bytes) {
        String mimeType = getMimeType(name);
        if ("application/octet-stream".equals(mimeType) && isText(bytes)) {
            return "text/plain";
        } else {
            return mimeType;
        }
    }

    public static String getSafeMimeType(String name) {
        return getMimeType(name).replace("text/html", "text/plain");
    }

    public static boolean isImage(String name) {
        return getMimeType(name).startsWith("image/");
    }

    public static boolean isLarge(Long size) {
        return size > 1024 * 1000;
    }

    public static boolean isText(byte[] content) {
        return !ArrayUtils.contains(content, (byte) 0);
    }

    public static String getExtension(String name) {
        int i = name.lastIndexOf('.');
        if (i >= 0) {
            return name.substring(i + 1);
        } else {
            return "";
        }
    }

    //TODO gitbucket.core.util.FileUtil#withTmpDir(java.io.File, scala.Function1)
    public static <T> T withTmpDir(File dir, Object action) {
        if (dir.exists()) {
            try {
                FileUtils.deleteDirectory(dir);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        try {
            return (T) ((scala.Function1) action).apply(dir);
        } finally {
            try {
                FileUtils.deleteDirectory(dir);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String getLfsFilePath(String owner, String repository, String oid) {
        return Directory.getLfsDir(owner, repository) + "/" + checkFilename(oid);
    }

    public static String readableSize(Long size) {
        return FileUtils.byteCountToDisplaySize(size);
    }

    /**
     * Delete the given directory if it's empty.
     * Do nothing if the given File is not a directory or not empty.
     */
    public static void deleteDirectoryIfEmpty(File dir) {
        if (dir.isDirectory() && dir.list().length < 1) {
            try {
                FileUtils.deleteDirectory(dir);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Delete file or directory forcibly.
     */
    public static File deleteIfExists(File file) {
        if (file.exists()) {
            try {
                FileUtils.forceDelete(file);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return file;
    }

    /**
     * Create an instance of java.io.File safely.
     */
    public static String checkFilename(String name) {
        if (name.contains("..")) {
            throw new IllegalArgumentException("Invalid file name: " + name);
        }
        return name;
    }
}
