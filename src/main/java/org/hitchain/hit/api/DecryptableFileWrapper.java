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
import org.bouncycastle.util.encoders.Hex;
import org.hitchain.hit.api.ProjectInfoFile.TeamInfo;
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
	private final ProjectInfoFile projectInfoFile;
	private final String member;
	private final String priKeyRsa;

	public DecryptableFileWrapper(HashedFile source, ProjectInfoFile projectInfoFile, String member, String priKeyRsa) {
		if (source == null) {
			throw new IllegalStateException("DecryptableFileWrapper HashedFile does not exist: " + source);
		} else {
			this.source = source;
		}
		if (projectInfoFile == null) {
			throw new IllegalStateException(
					"DecryptableFileWrapper ProjectInfoFile does not exist: " + projectInfoFile);
		} else {
			this.projectInfoFile = projectInfoFile;
		}
		if (projectInfoFile.isPrivate() && StringUtils.isBlank(member)) {
			throw new IllegalStateException("DecryptableFileWrapper member does not exist: " + member);
		} else {
			this.member = member;
		}
		if (projectInfoFile.isPrivate() && StringUtils.isBlank(priKeyRsa)) {
			throw new IllegalStateException("DecryptableFileWrapper privateKey does not exist: " + priKeyRsa);
		} else {
			this.priKeyRsa = priKeyRsa;
		}
	}

	public InputStream getInputStream() throws IOException {
		if (projectInfoFile.isPrivate()) {
			String encrypt = null;
			if (StringUtils.equalsAny(member, projectInfoFile.getOwner(), projectInfoFile.getOwnerPubKeyRsa(),
					projectInfoFile.getOwnerAddressEcc())) {// #if is owner, get the repository encrypted private key.
				encrypt = projectInfoFile.getRepoPriKey();
			} else {
				for (TeamInfo ti : projectInfoFile.getMembers()) {
					if (StringUtils.equalsAny(member, ti.getMember(), ti.getMemberPubKeyRsa(),
							ti.getMemberAddressEcc())) {
						encrypt = ti.getMemberRepoPriKey();
					}
				}
			}
			try {
				if (encrypt == null) {
					return source.getInputStream();
				}
				byte[] repositoryPrivateKey = RSAUtil.decrypt(encrypt.getBytes(),
						RSAUtil.getPrivateKeyFromHex(priKeyRsa));
				InputStream is = source.getInputStream();
				byte[] bytes = ECCUtil.privateDecrypt(is,
						ECCUtil.getPrivateKeyFromEthereumHex(Hex.toHexString(repositoryPrivateKey)));
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
				list.add(new DecryptableFileWrapper(hf, projectInfoFile, member, priKeyRsa));
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
