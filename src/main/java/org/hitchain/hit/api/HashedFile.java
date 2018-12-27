/*******************************************************************************
 * Copyright (c) 2018-11-23 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.hitchain.hit.api;

import org.apache.commons.io.IOUtils;

import java.beans.Transient;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * HashedIndexTree
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-23 auto generate by qdp.
 */
public interface HashedFile {

	String getName();

	String getHash();

	boolean isDirectory();

	void setInputStreamCallback(InputStreamCallback callback);

	InputStream getInputStream() throws IOException;

	byte[] getContents() throws IOException;

	List<HashedFile> getChildren();

	interface InputStreamCallback {
		InputStream call(HashedFile hashedFile) throws IOException;
	}

	class ByteArrayInputStreamCallback implements InputStreamCallback {
		protected ByteArrayInputStream bais;

		public ByteArrayInputStreamCallback(ByteArrayInputStream bais) {
			this.bais = bais;
		}

		public ByteArrayInputStreamCallback(byte[] bytes) {
			this.bais = new ByteArrayInputStream(bytes);
		}

		public InputStream call(HashedFile hashedFile) throws IOException {
			return bais;
		}
	}

	class FileInputStreamCallback implements InputStreamCallback {
		protected File file;

		public FileInputStreamCallback(File file) {
			this.file = file;
		}

		public InputStream call(HashedFile hashedFile) throws IOException {
			return new FileInputStream(file);
		}
	}

	class FileWrapper implements HashedFile {

		private String name;
		private String hash;
		private transient InputStreamCallback inputStreamCallback;
		private transient byte[] contents;

		public FileWrapper() {
		}

		public FileWrapper(String name) {
			this.name = name;
		}

		public FileWrapper(String name, InputStreamCallback inputStreamCallback) {
			this.name = name;
			this.inputStreamCallback = inputStreamCallback;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getHash() {
			return hash;
		}

		public void setHash(String hash) {
			this.hash = hash;
		}

		public boolean isDirectory() {
			return false;
		}

		@Transient
		public InputStreamCallback getInputStreamCallback() {
			return inputStreamCallback;
		}

		@Transient
		public void setInputStreamCallback(InputStreamCallback inputStreamCallback) {
			this.inputStreamCallback = inputStreamCallback;
		}

		@Transient
		public InputStream getInputStream() throws IOException {
			return inputStreamCallback == null ? null : inputStreamCallback.call(this);
		}

		@Transient
		public byte[] getContents() throws IOException {
			if (contents == null) {
				InputStream is = getInputStream();
				contents = IOUtils.toByteArray(is);
				is.close();
			}
			return contents;
		}

		@Transient
		public List<HashedFile> getChildren() {
			return null;
		}

		@Override
		public String toString() {
			return "FileWrapper{" + "name='" + name + '\'' + ", hash='" + hash + '\'' + '}';
		}
	}

	class DirWrapper implements HashedFile {

		private String name;
		private String hash;
		private List<HashedFile> children = new ArrayList<>();

		public DirWrapper() {
		}

		public DirWrapper(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getHash() {
			return hash;
		}

		public void setHash(String hash) {
			this.hash = hash;
		}

		public boolean isDirectory() {
			return true;
		}

		@Transient
		public InputStreamCallback getInputStreamCallback() {
			return null;
		}

		@Transient
		public void setInputStreamCallback(InputStreamCallback callback) {

		}

		@Transient
		public InputStream getInputStream() throws IOException {
			return null;
		}

		@Transient
		public byte[] getContents() throws IOException {
			return null;
		}

		public List<HashedFile> getChildren() {
			return children;
		}

		public void setChildren(List<HashedFile> children) {
			this.children = children;
		}

		@Override
		public String toString() {
			return "DirWrapper{" + "name='" + name + '\'' + ", hash='" + hash + '\'' + ", children=" + children + '}';
		}
	}

	class FileSystemWrapper implements HashedFile {
		private String name;
		private String hash;
		private List<HashedFile> children = new ArrayList<>();
		private transient byte[] contents;
		private Boolean isDir = null;

		public FileSystemWrapper() {
		}

		public FileSystemWrapper(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getHash() {
			return hash;
		}

		public void setHash(String hash) {
			this.hash = hash;
		}

		public boolean isDirectory() {
			return isDir == null ? (isDir = new File(name).isDirectory()) : isDir;
		}

		@Transient
		public InputStreamCallback getInputStreamCallback() {
			return null;
		}

		@Transient
		public void setInputStreamCallback(InputStreamCallback callback) {

		}

		@Transient
		public InputStream getInputStream() throws IOException {
			return new FileInputStream(name);
		}

		@Transient
		public byte[] getContents() throws IOException {
			if (contents == null) {
				InputStream is = getInputStream();
				contents = IOUtils.toByteArray(is);
				is.close();
			}
			return contents;
		}

		public List<HashedFile> getChildren() {
			if (isDirectory()) {
				File[] files = new File(name).listFiles();
				for (File file : files) {
					children.add(new FileSystemWrapper(file.getAbsolutePath()));
				}
				return children;
			}
			return Collections.emptyList();
		}

		public void setChildren(List<HashedFile> children) {
			this.children = children;
		}

		@Override
		public String toString() {
			return "DirWrapper{" + "name='" + name + '\'' + ", hash='" + hash + '\'' + ", children=" + children + '}';
		}
	}
}
