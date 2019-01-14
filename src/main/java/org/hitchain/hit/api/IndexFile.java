///*******************************************************************************
// * Copyright (c) 2018-11-28 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
// * All rights reserved.
// *
// * Contributors:
// *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
// * Auto Generate By foreveross.com Quick Deliver Platform. 
// ******************************************************************************/
//package org.hitchain.hit.api;
//
//import org.apache.commons.codec.binary.Base64;
//import org.apache.commons.lang3.StringUtils;
//import org.hitchain.hit.util.ByteUtils;
//import org.hitchain.hit.util.RSAUtil;
//
//import gitbucket.core.util.Tuple;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * <pre>
// * IndexFile:
// * SIGN::newOwnerRSAEncrypted:signatureByOwner
// * {
// * version=1
// * project=
// * ethereumUrl=
// * fileServerUrl=
// * repoPriKey=
// * repoPubKey=
// * owner=
// * ownerPubRsa=
// * ownerAddressEcc=
// * member=
// * memberPubKeyRsa=
// * memberPriKeyEcc=
// * memberRepoPriKey=
// * }
// * VERS:::1
// * PROJ::owner:projectName
// * EURL::ethereumUrl:fileServerUrl
// * RKEY:repoPriKeyEccEncripted:repoPubKeyECC:repoContractAddress
// * OKEY:owner:ownerPubKeyRSA:ownerEccAddress
// * MKEY*:member:memberPubKeyRSA:memberEccAddress
// * TKEY*::repoPriKeyEncrypt:member
// * </pre>
// * 
// * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
// * @since 2018-11-28 auto generate by qdp.
// */
//public class IndexFile {
//	private HashedFile file;
//	/**如果仓库拥有者的密钥更换了，需要使用旧的密钥把新的公钥加密，以便于其他成员进行验证，以确定是仓库拥有者的身份**/
//	private String newOwnerPubKeyRasEncryptedByOldPriKeyRsa;
//	/**数据签名的 HASH 码，以用于验证下面数据的准确性**/
//	private String signHash;
//	/**版本**/
//	private String version;
//	/**项目名称**/
//	private String projectName;
//	/**Erhereum入口 URL**/
//	private String ethereumUrl;
//	/**文件服务器入口 URL**/
//	private String fileServerUrl;
//	/**代码仓库的私钥（由拥有者的私钥加密）**/
//	private String repoPriKeyRsaEncryptedByOwnerPriKeyRsa;
//	/**代码仓库的公钥**/
//	private String repoPubKeyRsa;// Ethereum account address
//	/**代码仓库的合约地址**/
//	private String repoContractAddress;
//	/**仓库拥有者**/
//	private String owner;
//	/**仓库拥有者公钥**/
//	private String ownerPubKeyRsa;
//	/**仓库拥有者Ethereum地址**/
//	private String ownerAddressEcc;
//	private Map<String/* member */, Tuple.Two<String/* memberPublicKey */, String/* memberPublicKey */>> memberKeys = new HashMap<>();
//	private Map<String/* memberPublicKey */, String/* repositoryPrivateKeyEncryptedByMemberPrivateKey */> memberRepositoryKeys = new HashMap<>();
//
//	public static IndexFile fromFile(HashedFile file) throws IOException {
//		if (file.isDirectory()) {
//			throw new IllegalArgumentException("IndexFile HashedFile require a file type!");
//		}
//		byte[] contents = file.getContents();
//		if (contents == null || contents.length < 1) {
//			throw new IllegalArgumentException("IndexFile HashedFile is empty or InputStreamCallback is not set!");
//		}
//		IndexFile index = new IndexFile();
//		String content = new String(contents, "UTF-8");
//		String[] split = StringUtils.split(content, '\n');
//		for (String line : split) {
//			String[] values = StringUtils.split(line, ":");
//			if (values.length != 4) {
//				continue;
//			}
//			Tuple.Four<String, String, String, String> four = new Tuple.Four<>(values[0], values[1], values[2],
//					values[3]);
//			if ("SIGN".equals(values[0])) {
//				index.setNewOwnerPubKeyEncrypted(values[1]);
//				index.setSignatureHash(values[2]);
//			} else if ("VERS".equals(values[0])) {
//				index.setVersion(values[2]);
//			} else if ("PROJ".equals(values[0])) {
//				index.setOwner(values[1]);
//				index.setProjectName(values[2]);
//			} else if ("EURL".equals(values[0])) {
//				index.setEntranceUrl(values[2]);
//			} else if ("RKEY".equals(values[0])) {
//				index.setRepositoryPrivateKeyEncrypted(values[1]);
//				index.setRepositoryPublicKey(values[2]);
//			} else if ("OKEY".equals(values[0])) {
//				index.setOwner(values[1]);
//				index.setOwnerPublicKey(values[2]);
//			} else if ("MKEY".equals(values[0])) {
//				index.getMemberKeys().put(values[1], values[2]);
//			} else if ("TKEY".equals(values[0])) {
//				index.getMemberRepositoryKeys().put(values[2], values[1]);
//			}
//		}
//		return index;
//	}
//
//	public String toString() {
//		return getContent();
//	}
//
//	public String getContent() {
//		StringBuilder sb = new StringBuilder();
//		sb.append("VERS::").append(getVersion()).append("\n");
//		sb.append("PROJ:").append(getOwner()).append(":").append(getProjectName()).append('\n');
//		sb.append("EURL::").append(StringUtils.defaultString(getEntranceUrl())).append('\n');
//		sb.append("RKEY:").append(StringUtils.defaultString(getRepositoryPrivateKeyEncrypted())).append(":")
//				.append(StringUtils.defaultString(getRepositoryPublicKey())).append('\n');
//		sb.append("OKEY:").append(getOwner()).append(":").append(getOwnerPublicKey()).append('\n');
//		for (Map.Entry<String, String> entry : getMemberKeys().entrySet()) {
//			if (StringUtils.isBlank(entry.getKey()) || StringUtils.isBlank(entry.getValue())) {
//				continue;
//			}
//			sb.append("MKEY:").append(entry.getKey()).append(":").append(entry.getValue()).append('\n');
//		}
//		for (Map.Entry<String, String> entry : getMemberRepositoryKeys().entrySet()) {
//			if (StringUtils.isBlank(entry.getKey()) || StringUtils.isBlank(entry.getValue())) {
//				continue;
//			}
//			sb.append("TKEY:").append(entry.getValue()).append(":").append(entry.getKey()).append('\n');
//		}
//		return sb.toString();
//	}
//
//	public String getSignedContent(String ownerPriKey) {
//		String content = getContent();
//		byte[] sign = RSAUtil.sign(ByteUtils.utf8(content), RSAUtil.getPrivateKeyFromBase64(ownerPriKey));
//		return "SIGN::" + Base64.encodeBase64String(sign) + "\n" + content;
//	}
//
//	public boolean verify(IndexFile old) {
//		if (old != null) {
//			if (StringUtils.isNotBlank(getNewOwnerPubKeyEncrypted())) {// if owner change the rsa key.
//				String newOwnerPubKey = RSAUtil.decrypt(getNewOwnerPubKeyEncrypted(),
//						RSAUtil.getPublicKeyFromBase64(old.getOwnerPublicKey()));// use old pub key to decrypt the new
//																																						// pubKey
//				if (!StringUtils.equals(getOwnerPublicKey(), newOwnerPubKey)) {// if new pubKey not the same as current
//																				// pubKey return false
//					return false;
//				}
//			} else {// if owner not change the rsa key, compare the old and new pubKey
//				if (!StringUtils.equals(old.getOwnerPublicKey(), getOwnerPublicKey())) {
//					return false;
//				}
//			}
//		}
//		return RSAUtil.verify(ByteUtils.utf8(getContent()), Base64.decodeBase64(getSignatureHash()),
//				RSAUtil.getPublicKeyFromBase64(getOwnerPublicKey()));
//	}
//
//	public void addMemberPublic(String memberName, String memberPublicKey) {
//		if (StringUtils.isNotBlank(getRepositoryPrivateKeyEncrypted())) {
//			throw new RuntimeException("IndexFile this is private repository, use addMemberPrivate to instead!");
//		}
//		getMemberKeys().put(memberName, memberPublicKey);
//	}
//
//	public void addMemberPrivate(String ownerPrivateKey, String memberName, String memberPublicKey) {
//		if (StringUtils.isBlank(getRepositoryPrivateKeyEncrypted())) {
//			addMemberPublic(memberName, memberPublicKey);
//			return;
//		}
//		// #1. first use ownerPrivateKey to decrypt the repository priKey
//		// #2. second use memberPublicKey to encrypt the repository priKey
//		String decrypt = RSAUtil.decrypt(getRepositoryPrivateKeyEncrypted(),
//				RSAUtil.getPrivateKeyFromBase64(ownerPrivateKey));
//		String encrypt = RSAUtil.encrypt(decrypt, RSAUtil.getPublicKeyFromBase64(memberPublicKey));
//		getMemberKeys().put(memberName, memberPublicKey);
//		getMemberRepositoryKeys().put(memberPublicKey, encrypt);
//	}
//
//	public boolean isPrivate() {
//		return StringUtils.isNotBlank(getRepositoryPrivateKeyEncrypted());
//	}
//
//	public HashedFile getFile() {
//		return file;
//	}
//
//	public void setFile(HashedFile file) {
//		this.file = file;
//	}
//
//	public String getNewOwnerPubKeyEncrypted() {
//		return newOwnerPubKeyEncrypted;
//	}
//
//	public void setNewOwnerPubKeyEncrypted(String newOwnerPubKeyEncrypted) {
//		this.newOwnerPubKeyEncrypted = newOwnerPubKeyEncrypted;
//	}
//
//	public String getVersion() {
//		return version;
//	}
//
//	public void setVersion(String version) {
//		this.version = version;
//	}
//
//	public String getProjectName() {
//		return projectName;
//	}
//
//	public void setProjectName(String projectName) {
//		this.projectName = projectName;
//	}
//
//	public String getSignatureHash() {
//		return signatureHash;
//	}
//
//	public void setSignatureHash(String signatureHash) {
//		this.signatureHash = signatureHash;
//	}
//
//	public String getEntranceUrl() {
//		return entranceUrl;
//	}
//
//	public void setEntranceUrl(String entranceUrl) {
//		this.entranceUrl = entranceUrl;
//	}
//
//	public String getRepositoryPrivateKeyEncrypted() {
//		return repositoryPrivateKeyEncrypted;
//	}
//
//	public void setRepositoryPrivateKeyEncrypted(String repositoryPrivateKeyEncrypted) {
//		this.repositoryPrivateKeyEncrypted = repositoryPrivateKeyEncrypted;
//	}
//
//	public String getRepositoryPublicKey() {
//		return repositoryPublicKey;
//	}
//
//	public void setRepositoryPublicKey(String repositoryPublicKey) {
//		this.repositoryPublicKey = repositoryPublicKey;
//	}
//
//	public String getOwner() {
//		return owner;
//	}
//
//	public void setOwner(String owner) {
//		this.owner = owner;
//	}
//
//	public String getOwnerPublicKey() {
//		return ownerPublicKey;
//	}
//
//	public void setOwnerPublicKey(String ownerPublicKey) {
//		this.ownerPublicKey = ownerPublicKey;
//	}
//
//	public Map<String, String> getMemberKeys() {
//		return memberKeys;
//	}
//
//	public void setMemberKeys(Map<String, String> memberKeys) {
//		this.memberKeys = memberKeys;
//	}
//
//	public Map<String, String> getMemberRepositoryKeys() {
//		return memberRepositoryKeys;
//	}
//
//	public void setMemberRepositoryKeys(Map<String, String> memberRepositoryKeys) {
//		this.memberRepositoryKeys = memberRepositoryKeys;
//	}
//}
