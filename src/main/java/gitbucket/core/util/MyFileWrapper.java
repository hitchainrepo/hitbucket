/*******************************************************************************
 * Copyright (c) 2018-11-29 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package gitbucket.core.util;

import io.ipfs.api.NamedStreamable;

import java.io.*;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * MyFileWrapper
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-29
 * auto generate by qdp.
 */
public class MyFileWrapper implements NamedStreamable {
    private final File source;

    public MyFileWrapper(File source) {
        if (!source.exists()) {
            throw new IllegalStateException("File does not exist: " + source);
        } else {
            this.source = source;
        }
    }

    public InputStream getInputStream() throws IOException {
        org.bouncycastle.math.ec.ECCurve ecc;
        return new FileInputStream(this.source);
    }

    public boolean isDirectory() {
        return this.source.isDirectory();
    }

    public List<NamedStreamable> getChildren() {
        return this.isDirectory() ? (List) Stream.of(this.source.listFiles()).map(NamedStreamable.FileWrapper::new).collect(Collectors.toList()) : Collections.emptyList();
    }

    public Optional<String> getName() {
        try {
            return Optional.of(URLEncoder.encode(this.source.getName(), "UTF-8"));
        } catch (UnsupportedEncodingException var2) {
            throw new RuntimeException(var2);
        }
    }
}
