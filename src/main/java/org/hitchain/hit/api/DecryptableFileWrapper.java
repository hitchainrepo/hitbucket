/*******************************************************************************
 * Copyright (c) 2018-11-29 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.hitchain.hit.api;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.hitchain.hit.util.ECCUtil;
import org.hitchain.hit.util.RSAUtil;

import io.ipfs.api.NamedStreamable;

/**
 * MyFileWrapper
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-29 auto generate by qdp.
 */
public class DecryptableFileWrapper implements NamedStreamable {
	private final HashedFile source;
	private final IndexFile indexFile;
	private final String account;
	private final String privateKey;

	public DecryptableFileWrapper(HashedFile source, IndexFile indexFile, String account, String privateKey) {
		if (source == null) {
			throw new IllegalStateException("DecryptableFileWrapper HashedFile does not exist: " + source);
		} else {
			this.source = source;
		}
		if (indexFile == null) {
			throw new IllegalStateException("DecryptableFileWrapper IndexFile does not exist: " + indexFile);
		} else {
			this.indexFile = indexFile;
		}
		if (indexFile.isPrivate() && StringUtils.isBlank(account)) {
			throw new IllegalStateException("DecryptableFileWrapper account does not exist: " + account);
		} else {
			this.account = account;
		}
		if (indexFile.isPrivate() && StringUtils.isBlank(privateKey)) {
			throw new IllegalStateException("DecryptableFileWrapper privateKey does not exist: " + privateKey);
		} else {
			this.privateKey = privateKey;
		}
	}

	public InputStream getInputStream() throws IOException {
		if (indexFile.isPrivate()) {
			String encrypt = null;
			if (account.equals(indexFile.getOwner()) || account.equals(indexFile.getOwnerPublicKey())) {// #if is owner, get the repository encrypted private key.
				encrypt = indexFile.getRepositoryPrivateKeyEncrypted();
			} else if (indexFile.getMemberKeys().containsKey(account)) {// #if is member account, get the member repository encrypted private key.
				encrypt = indexFile.getMemberRepositoryKeys().get(indexFile.getMemberKeys().get(account));
			} else if (indexFile.getMemberRepositoryKeys().containsKey(account)) {// #if account is public key, get the member repository encrypted private key.
				encrypt = indexFile.getMemberRepositoryKeys().get(account);
			}
			try {
				if (encrypt == null) {
					return source.getInputStream();
				}
				byte[] repositoryPrivateKey = RSAUtil.decrypt(encrypt.getBytes(), RSAUtil.getPrivateKeyFromBase64(privateKey));
				InputStream is = source.getInputStream();
				byte[] bytes = ECCUtil.privateDecrypt(is,
						ECCUtil.getPrivateKeyFromEthereumHex(new String(repositoryPrivateKey)));
				is.close();
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
				list.add(new DecryptableFileWrapper(hf, indexFile, account, privateKey));
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
