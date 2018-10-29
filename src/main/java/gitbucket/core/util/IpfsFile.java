/*******************************************************************************
 * Copyright (c) 2018-10-18 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform.
 ******************************************************************************/
package gitbucket.core.util;

import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;

/**
 * MyFile
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-10-18
 * auto generate by qdp.
 */
public class IpfsFile extends java.io.File {

    public IpfsFile(String pathname) {
        super(pathname);
    }


    public IpfsFile(String parent, String child) {
        super(parent, child);
    }


    public IpfsFile(IpfsFile parent, String child) {
        super(parent, child);
    }


    public IpfsFile(URI uri) {
        super(uri);
    }

    public IpfsFile(java.io.File parent, String child) {
        super(parent, child);
    }

    public String getName() {
        return super.getName();
    }

    public String getParent() {
        return super.getParent();
    }

    public IpfsFile getParentFile() {
        //return super.getParentFile();
        return null;
    }

    public String getPath() {
        return super.getPath();
    }

    public boolean isAbsolute() {
        return super.isAbsolute();
    }

    public String getAbsolutePath() {
        return super.getAbsolutePath();
    }

    public IpfsFile getAbsoluteFile() {
        //return super.getAbsoluteFile();
        return null;
    }

    public String getCanonicalPath() throws IOException {
        return super.getCanonicalPath();
    }

    public IpfsFile getCanonicalFile() throws IOException {
        //return super.getCanonicalFile();
        return null;
    }

    public URL toURL() throws MalformedURLException {
        return super.toURL();
    }

    public URI toURI() {
        return super.toURI();
    }

    public boolean canRead() {
        return super.canRead();
    }

    public boolean canWrite() {
        return super.canWrite();
    }

    public boolean exists() {
        return super.exists();
    }

    public boolean isDirectory() {
        return super.isDirectory();
    }

    public boolean isFile() {
        return super.isFile();
    }

    public boolean isHidden() {
        return super.isHidden();
    }

    public long lastModified() {
        return super.lastModified();
    }

    public long length() {
        return super.length();
    }

    public boolean createNewFile() throws IOException {
        return super.createNewFile();
    }

    public boolean delete() {
        return super.delete();
    }

    public void deleteOnExit() {
        super.deleteOnExit();
    }

    public String[] list() {
        return super.list();
    }

    public String[] list(FilenameFilter filter) {
        return super.list(filter);
    }

    public IpfsFile[] listFiles() {
        //return super.listFiles();
        return null;
    }

    public IpfsFile[] listFiles(FilenameFilter filter) {
        //return super.listFiles(filter);
        return null;
    }

    public IpfsFile[] listFiles(FileFilter filter) {
        //return super.listFiles(filter);
        return null;
    }

    public boolean mkdir() {
        return super.mkdir();
    }

    public boolean mkdirs() {
        return super.mkdirs();
    }

    public boolean renameTo(java.io.File dest) {
        return super.renameTo(dest);
    }

    public boolean setLastModified(long time) {
        return super.setLastModified(time);
    }

    public boolean setReadOnly() {
        return super.setReadOnly();
    }

    public boolean setWritable(boolean writable, boolean ownerOnly) {
        return super.setWritable(writable, ownerOnly);
    }

    public boolean setWritable(boolean writable) {
        return super.setWritable(writable);
    }

    public boolean setReadable(boolean readable, boolean ownerOnly) {
        return super.setReadable(readable, ownerOnly);
    }

    public boolean setReadable(boolean readable) {
        return super.setReadable(readable);
    }

    public boolean setExecutable(boolean executable, boolean ownerOnly) {
        return super.setExecutable(executable, ownerOnly);
    }

    public boolean setExecutable(boolean executable) {
        return super.setExecutable(executable);
    }

    public boolean canExecute() {
        return super.canExecute();
    }

    public long getTotalSpace() {
        return super.getTotalSpace();
    }

    public long getFreeSpace() {
        return super.getFreeSpace();
    }

    public long getUsableSpace() {
        return super.getUsableSpace();
    }

    public int compareTo(java.io.File pathname) {
        return super.compareTo(pathname);
    }

    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        return super.toString();
    }

    public Path toPath() {
        return super.toPath();
    }
}
