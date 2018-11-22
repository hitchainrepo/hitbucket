/**
 * Modify from io.ipfs.api.
 */
package org.hitchain.hit.api;

import java.io.*;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 */
public interface NamedStreamable {
    InputStream getInputStream() throws IOException;

    Optional<String> getName();

    List<NamedStreamable> getChildren();

    boolean isDirectory();

    default byte[] getContents() throws IOException {
        InputStream in = getInputStream();
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] tmp = new byte[4096];
        int r;
        while ((r = in.read(tmp)) >= 0)
            bout.write(tmp, 0, r);
        return bout.toByteArray();
    }

    class FileWrapper implements NamedStreamable {
        private final File source;

        public FileWrapper(File source) {
            if (!source.exists()) {
                throw new IllegalStateException("File does not exist: " + source);
            }
            this.source = source;
        }

        public InputStream getInputStream() throws IOException {
            return new FileInputStream(source);
        }

        public boolean isDirectory() {
            return source.isDirectory();
        }

        @Override
        public List<NamedStreamable> getChildren() {
            return isDirectory() ?
                Stream.of(source.listFiles())
                    .map(NamedStreamable.FileWrapper::new)
                    .collect(Collectors.toList()) :
                Collections.emptyList();
        }

        public Optional<String> getName() {
            try {
                return Optional.of(URLEncoder.encode(source.getName(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    class ByteArrayWrapper implements NamedStreamable {
        private final Optional<String> name;
        private final byte[] data;

        public ByteArrayWrapper(byte[] data) {
            this(Optional.empty(), data);
        }

        public ByteArrayWrapper(String name, byte[] data) {
            this(Optional.of(name), data);
        }

        public ByteArrayWrapper(Optional<String> name, byte[] data) {
            this.name = name;
            this.data = data;
        }

        public boolean isDirectory() {
            return false;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        @Override
        public List<NamedStreamable> getChildren() {
            return Collections.emptyList();
        }

        public Optional<String> getName() {
            return name;
        }
    }

    class InputStreamWrapper implements NamedStreamable {
        private final Optional<String> name;
        private final InputStream is;

        public InputStreamWrapper(InputStream is) {
            this(Optional.empty(), is);
        }

        public InputStreamWrapper(String name, InputStream is) {
            this(Optional.of(name), is);
        }

        public InputStreamWrapper(Optional<String> name, InputStream is) {
            this.name = name;
            this.is = is;
        }

        public boolean isDirectory() {
            return false;
        }

        public InputStream getInputStream() throws IOException {
            return is;
        }

        @Override
        public List<NamedStreamable> getChildren() {
            return Collections.emptyList();
        }

        public Optional<String> getName() {
            return name;
        }
    }

    class DirWrapper implements NamedStreamable {

        private final String name;
        private final List<NamedStreamable> children;

        public DirWrapper(String name, List<NamedStreamable> children) {
            this.name = name;
            this.children = children;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            throw new IllegalStateException("Cannot get an input stream for a directory!");
        }

        @Override
        public Optional<String> getName() {
            return Optional.of(name);
        }

        @Override
        public List<NamedStreamable> getChildren() {
            return children;
        }

        @Override
        public boolean isDirectory() {
            return true;
        }
    }
}
