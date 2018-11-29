/*******************************************************************************
 * Copyright (c) 2018-11-29 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.hitchain.hit.api;

import io.ipfs.api.NamedStreamable;
import org.hitchain.hit.util.ECCUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * MyFileWrapper
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-29
 * auto generate by qdp.
 */
public class EncryptableFileWrapper implements NamedStreamable {
    private final HashedFile source;
    private final IndexFile indexFile;

    public EncryptableFileWrapper(HashedFile source, IndexFile indexFile) {
        if (source == null) {
            throw new IllegalStateException("EncryptableFileWrapper HashedFile does not exist: " + source);
        } else {
            this.source = source;
        }
        if (indexFile == null) {
            throw new IllegalStateException("EncryptableFileWrapper IndexFile does not exist: " + indexFile);
        } else {
            this.indexFile = indexFile;
        }
    }

    public InputStream getInputStream() throws IOException {
        if (indexFile.isPrivate()) {
            PublicKey publicKey = ECCUtil.getPublicKeyFromEthereumPublicKeyHex(indexFile.getRepositoryPublicKey());
            InputStream is = source.getInputStream();
            byte[] bytes = new byte[0];
            try {
                bytes = ECCUtil.publicEncrypt(is, publicKey);
                return new ByteArrayInputStream(bytes);
            } catch (Exception e) {
                throw new IOException(e);
            }
        }
        return source.getInputStream();
    }

    public boolean isDirectory() {
        return this.source.isDirectory();
    }

    public List<NamedStreamable> getChildren() {
        if (source.isDirectory()) {
            List<HashedFile> children = source.getChildren();
            List<NamedStreamable> list = new ArrayList<>();
            for (HashedFile hf : children) {
                list.add(new EncryptableFileWrapper(hf, indexFile));
            }
            return list;
        }
        return Collections.emptyList();
    }

    public Optional<String> getName() {
        try {
            return Optional.of(URLEncoder.encode(this.source.getName(), "UTF-8"));
        } catch (UnsupportedEncodingException var2) {
            throw new RuntimeException(var2);
        }
    }
}
