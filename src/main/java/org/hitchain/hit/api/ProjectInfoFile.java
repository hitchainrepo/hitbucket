/*******************************************************************************
 * Copyright (c) 2018-11-28 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.hitchain.hit.api;

import java.beans.Transient;
import java.io.IOException;
import java.io.Serializable;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.hitchain.hit.util.ByteUtils;
import org.hitchain.hit.util.ECCUtil;
import org.hitchain.hit.util.ECKey;
import org.hitchain.hit.util.RSAUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <pre>
 * IndexFile:
 * sign: 数据签名的 HASH 码，以用于验证下面数据的准确性
 * {
 * version: 1, 版本
 * ethereumUrl: the ethereum entrance url, Erhereum入口 URL
 * fileServerUrl: the file server url, could be ipfs entrance url, 文件服务器入口 URL
 * repoName: /userName/repoName.git, 仓库名称
 * repoPriKey: 代码仓库的私钥（由拥有者的私钥加密）
 * repoPubKey: 代码仓库的公钥
 * repoAddress: 代码仓库的合约地址
 * owner: 仓库拥有者
 * ownerPubKeyRsa: 仓库拥有者公钥
 * ownerAddressEcc: 仓库拥有者Ethereum地址
 * [{
 *   member: 团队成员
 *   memberPubKeyRsa: 成员的公钥
 *   memberAddressEcc: 成员的Ethereum地址
 *   memberRepoPriKey: 成员的仓库私钥加密
 * }*]
 * }
 * </pre>
 * 
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-28 auto generate by qdp.
 */
public class ProjectInfoFile implements Serializable {
	transient private HashedFile file;
	/**如果仓库拥有者的密钥更换了，需要使用旧的密钥把新的公钥加密，以便于其他成员进行验证，以确定是仓库拥有者的身份**/
	//private String newOwnerPubKeyRasEncryptedByOldPriKeyRsa;
	/**数据签名的 HASH 码，以用于验证下面数据的准确性**/
	transient private String sign;
	/**数据签名的内容**/
	transient private String content;
	/**版本**/
	private String version;
	/**Erhereum入口 URL**/
	private String ethereumUrl;
	/**文件服务器入口 URL**/
	private String fileServerUrl;
	/**项目名称**/
	private String repoName;
	/**代码仓库的私钥（由拥有者的私钥加密）**/
	private String repoPriKey;
	/**代码仓库的公钥**/
	private String repoPubKey;
	/**代码仓库的合约地址**/
	private String repoAddress;
	/**仓库拥有者**/
	private String owner;
	/**仓库拥有者公钥**/
	private String ownerPubKeyRsa;
	/**仓库拥有者Ethereum地址**/
	private String ownerAddressEcc;
	private List<TeamInfo> members = new ArrayList<TeamInfo>();

	public static void main(String[] args) throws Exception {
		ECKey repoKeyPair = new ECKey();
		KeyPair rootKeyRsa = RSAUtil.generateKey();
		ECKey rootKeyEcc = new ECKey();
		System.out.println("ROOT-PUB-RSA:"+Hex.toHexString(rootKeyRsa.getPublic().getEncoded()));
		System.out.println("ROOT-PRI-RSA:"+Hex.toHexString(rootKeyRsa.getPrivate().getEncoded()));
		System.out.println("ROOT-PUB-ECC:"+Hex.toHexString(rootKeyEcc.getAddress()));
		System.out.println("ROOT-PRI-ECC:"+Hex.toHexString(rootKeyEcc.getPrivKeyBytes()));
		System.out.println("REPO-PUB-ECC:"+Hex.toHexString(repoKeyPair.getPubKey()));
		System.out.println("REPO-PRI-ECC:"+Hex.toHexString(repoKeyPair.getPrivKeyBytes()));
		KeyPair helloKeyRsa = RSAUtil.generateKey();
		ECKey helloKeyEcc = new ECKey();
		ProjectInfoFile info = new ProjectInfoFile();
		info.version = "1";
		info.ethereumUrl = "test";
		info.fileServerUrl = "test";
		info.repoName = "test";
		info.repoPubKey = Hex.toHexString(repoKeyPair.getPubKey());
		info.repoPriKey = Hex.toHexString(RSAUtil.encrypt(repoKeyPair.getPrivKeyBytes(), rootKeyRsa.getPublic()));
		info.repoAddress = "address";
		info.owner = "root";
		info.ownerPubKeyRsa = Hex.toHexString(rootKeyRsa.getPublic().getEncoded());
		info.ownerAddressEcc = "0x" + Hex.toHexString(rootKeyEcc.getAddress());
		info.addMemberPrivate("hello", Hex.toHexString(helloKeyRsa.getPublic().getEncoded()),
				"0x" + Hex.toHexString(helloKeyEcc.getAddress()),
				Hex.toHexString(rootKeyRsa.getPrivate().getEncoded()));
		System.out.println(info.genContent());
		String genSignedContent = info.genSignedContent(Hex.toHexString(rootKeyRsa.getPrivate().getEncoded()));
		System.out.println(genSignedContent);

		byte[] bs = ECCUtil.publicEncrypt(genSignedContent.getBytes(),
				ECCUtil.getPublicKeyFromEthereumPublicKeyHex(info.repoPubKey));

		byte[] repoPriKeyBs = RSAUtil.decrypt(Hex.decode(info.repoPriKey), rootKeyRsa.getPrivate());
		byte[] decrypt = ECCUtil.privateDecrypt(bs,
				ECCUtil.getPrivateKeyFromEthereumHex(Hex.toHexString(repoPriKeyBs)));
		System.out.println(new String(decrypt));
	}

	public static class TeamInfo implements Serializable {
		/**团队成员**/
		private String member;
		/**成员的公钥**/
		private String memberPubKeyRsa;
		/**成员的Ethereum地址**/
		private String memberAddressEcc;
		/**成员的仓库私钥加密**/
		private String memberRepoPriKey;

		public TeamInfo() {
		}

		public TeamInfo(String member, String memberPubKeyRsa, String memberAddressEcc, String memberRepoPriKey) {
			this.member = member;
			this.memberPubKeyRsa = memberPubKeyRsa;
			this.memberAddressEcc = memberAddressEcc;
			this.memberRepoPriKey = memberRepoPriKey;
		}

		public String getMember() {
			return member;
		}

		public void setMember(String member) {
			this.member = member;
		}

		public String getMemberPubKeyRsa() {
			return memberPubKeyRsa;
		}

		public void setMemberPubKeyRsa(String memberPubKeyRsa) {
			this.memberPubKeyRsa = memberPubKeyRsa;
		}

		public String getMemberAddressEcc() {
			return memberAddressEcc;
		}

		public void setMemberAddressEcc(String memberAddressEcc) {
			this.memberAddressEcc = memberAddressEcc;
		}

		public String getMemberRepoPriKey() {
			return memberRepoPriKey;
		}

		public void setMemberRepoPriKey(String memberRepoPriKey) {
			this.memberRepoPriKey = memberRepoPriKey;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((member == null) ? 0 : member.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			TeamInfo other = (TeamInfo) obj;
			if (member == null) {
				if (other.member != null) {
					return false;
				}
			} else if (!member.equals(other.member)) {
				return false;
			}
			return true;
		}
	}

	public static ProjectInfoFile fromFile(HashedFile file) throws IOException {
		if (file.isDirectory()) {
			throw new IllegalArgumentException("ProjectInfoFile HashedFile require a file type!");
		}
		byte[] contents = file.getContents();
		if (contents == null || contents.length < 1) {
			throw new IllegalArgumentException(
					"ProjectInfoFile HashedFile is empty or InputStreamCallback is not set!");
		}
		String sign, content;
		{
			String fileContent = new String(contents, "UTF-8");
			int start = fileContent.indexOf('{');
			if (start < 0) {
				throw new IllegalArgumentException("ProjectInfoFile is broken!");
			}
			sign = fileContent.substring(0, start).trim();
			content = fileContent.substring(start, fileContent.length());
		}
		ProjectInfoFile infoFile = new ObjectMapper().readValue(content, ProjectInfoFile.class);
		infoFile.setSign(sign);
		infoFile.setFile(file);
		return infoFile;
	}

	public String toString() {
		return genContent();
	}

	public String genContent() {
		try {
			return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
		} catch (Exception e) {
			throw new RuntimeException("ProjectInfoFile can not be serialize!");
		}
	}

	public String genSignedContent(String ownerPriKey) {
		String content = genContent();
		byte[] sign = RSAUtil.sign(ByteUtils.utf8(content), RSAUtil.getPrivateKeyFromHex(ownerPriKey));
		return Hex.toHexString(sign) + "\n" + content;
	}

	public boolean verify(ProjectInfoFile old) {
		if (old != null) {
			// compare the old and new pubKey
			if (!StringUtils.equals(old.getOwnerPubKeyRsa(), getOwnerPubKeyRsa())) {
				return false;
			}
		}
		return RSAUtil.verify(ByteUtils.utf8(getContent()), Hex.decode(getSign()),
				RSAUtil.getPublicKeyFromHex(getOwnerPubKeyRsa()));
	}

	/**
	 * 添加团队成员
	 * @param member 团队成员
	 * @param memberPubKeyRsa 成员的公钥
	 * @param memberAddressEcc 成员的Ethereum地址
	 */
	public void addMemberPublic(String member, String memberPubKeyRsa, String memberAddressEcc) {
		if (isPrivate()) {
			throw new RuntimeException("ProjectInfoFile this is private repository, use addMemberPrivate to instead!");
		}
		TeamInfo info = new TeamInfo(member, memberPubKeyRsa, memberAddressEcc, "");
		if (members.contains(info)) {
			members.set(members.indexOf(info), info);
		} else {
			members.add(info);
		}
	}

	public void addMemberPrivate(String member, String memberPubKeyRsa, String memberAddressEcc,
			String ownerPriKeyRsa) {
		if (!isPrivate()) {
			addMemberPublic(member, memberPubKeyRsa, memberAddressEcc);
			return;
		}
		// #1. first use ownerPrivateKey to decrypt the repository priKey
		// #2. second use memberPublicKey to encrypt the repository priKey
		byte[] decrypt = RSAUtil.decrypt(Hex.decode(getRepoPriKey()), RSAUtil.getPrivateKeyFromHex(ownerPriKeyRsa));
		byte[] encrypt = RSAUtil.encrypt(decrypt, RSAUtil.getPublicKeyFromHex(memberPubKeyRsa));
		TeamInfo info = new TeamInfo(member, memberPubKeyRsa, memberAddressEcc, Hex.toHexString(encrypt));
		if (members.contains(info)) {
			members.set(members.indexOf(info), info);
		} else {
			members.add(info);
		}
	}

	@Transient
	public boolean isPrivate() {
		return StringUtils.isNotBlank(getRepoPriKey());
	}

	@Transient
	public HashedFile getFile() {
		return file;
	}

	@Transient
	public void setFile(HashedFile file) {
		this.file = file;
	}

	@Transient
	public String getSign() {
		return sign;
	}

	@Transient
	public void setSign(String sign) {
		this.sign = sign;
	}

	@Transient
	public String getContent() {
		return content;
	}

	@Transient
	public void setContent(String content) {
		this.content = content;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getEthereumUrl() {
		return ethereumUrl;
	}

	public void setEthereumUrl(String ethereumUrl) {
		this.ethereumUrl = ethereumUrl;
	}

	public String getFileServerUrl() {
		return fileServerUrl;
	}

	public void setFileServerUrl(String fileServerUrl) {
		this.fileServerUrl = fileServerUrl;
	}

	public String getRepoName() {
		return repoName;
	}

	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}

	public String getRepoPriKey() {
		return repoPriKey;
	}

	public void setRepoPriKey(String repoPriKey) {
		this.repoPriKey = repoPriKey;
	}

	public String getRepoPubKey() {
		return repoPubKey;
	}

	public void setRepoPubKey(String repoPubKey) {
		this.repoPubKey = repoPubKey;
	}

	public String getRepoAddress() {
		return repoAddress;
	}

	public void setRepoAddress(String repoAddress) {
		this.repoAddress = repoAddress;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwnerPubKeyRsa() {
		return ownerPubKeyRsa;
	}

	public void setOwnerPubKeyRsa(String ownerPubKeyRsa) {
		this.ownerPubKeyRsa = ownerPubKeyRsa;
	}

	public String getOwnerAddressEcc() {
		return ownerAddressEcc;
	}

	public void setOwnerAddressEcc(String ownerAddressEcc) {
		this.ownerAddressEcc = ownerAddressEcc;
	}

	public List<TeamInfo> getMembers() {
		return members;
	}

	public void setMembers(List<TeamInfo> members) {
		this.members = members;
	}
}
