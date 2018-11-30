/*******************************************************************************
 * Copyright (c) 2018-11-28 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.hitchain.hit.api;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * IndexFile
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-28
 * auto generate by qdp.
 */
public class IndexFile {
    private HashedFile file;
    private String projectName;
    private String projectHash;
    private String ipfsUrl;
    private String repositoryPrivateKeyEncrypted;
    private String repositoryPublicKey;//Ethereum account address
    private String owner;
    private String ownerPublicKey;
    private Map<String/*member*/, String/*memberPublicKey*/> memberKeys = new HashMap<>();
    private Map<String/*memberPublicKey*/, String/*repositoryPrivateKeyEncryptedByMemberPrivateKey*/> memberRepositoryKeys = new HashMap<>();

    public static IndexFile fromFile(HashedFile file) throws IOException {
        if (file.isDirectory()) {
            throw new IllegalArgumentException("IndexFile HashedFile require a file type!");
        }
        byte[] contents = file.getContents();
        if (contents == null || contents.length < 1) {
            throw new IllegalArgumentException("IndexFile HashedFile is empty or InputStreamCallback is not set!");
        }
        IndexFile index = new IndexFile();
        String content = new String(contents, "UTF-8");
        String[] split = StringUtils.split(content, '\n');
        for (String line : split) {
            String[] values = StringUtils.split(line, ":");
            if (values.length != 3) {
                continue;
            }
            if ("PROJ".equals(values[0])) {
                index.setProjectName(values[1]);
                index.setProjectHash(values[2]);
            } else if ("IPFS".equals(values[0])) {
                index.setIpfsUrl(values[2]);
            } else if ("RKEY".equals(values[0])) {
                index.setRepositoryPrivateKeyEncrypted(values[1]);
                index.setRepositoryPublicKey(values[2]);
            } else if ("OKEY".equals(values[0])) {
                index.setOwner(values[1]);
                index.setOwner(values[2]);
            } else if ("MKEY".equals(values[0])) {
                index.getMemberKeys().put(values[1], values[2]);
            } else if ("TKEY".equals(values[0])) {
                index.getMemberRepositoryKeys().put(values[2], values[1]);
            }
        }
        return index;
    }

    public String toString() {
        return getContent();
    }

    public String getContent() {
        StringBuilder sb = new StringBuilder();
        sb.append("PROJ:").append(getProjectName()).append(":").append(getProjectHash()).append('\n');
        if (StringUtils.isNotBlank(getIpfsUrl())) {
            sb.append("IPFS:").append("-").append(":").append(getIpfsUrl()).append('\n');
        }
        if (StringUtils.isNotBlank(getRepositoryPrivateKeyEncrypted())) {
            sb.append("RKEY:").append(getRepositoryPrivateKeyEncrypted()).append(":").append(getRepositoryPublicKey()).append('\n');
        }
        sb.append("OKEY:").append(getOwner()).append(":").append(getOwnerPublicKey()).append('\n');
        for (Map.Entry<String, String> entry : getMemberKeys().entrySet()) {
            sb.append("MKEY:").append(entry.getKey()).append(":").append(entry.getValue()).append('\n');
        }
        for (Map.Entry<String, String> entry : getMemberKeys().entrySet()) {
            sb.append("MKEY:").append(entry.getKey()).append(":").append(entry.getValue()).append('\n');
        }
        for (Map.Entry<String, String> entry : getMemberRepositoryKeys().entrySet()) {
            sb.append("TKEY:").append(entry.getValue()).append(":").append(entry.getKey()).append('\n');
        }
        return sb.toString();
    }

    public void addMemberPublic(String memberName, String memberPublicKey) {
        if (StringUtils.isNotBlank(getRepositoryPrivateKeyEncrypted())) {
            throw new RuntimeException("IndexFile this is private repository, use addMemberPrivate to instead!");
        }
        getMemberKeys().put(memberName, memberPublicKey);
    }

    public void addMemberPrivate(String ownerPrivateKey, String memberName, String memberPublicKey) {
        //TODO
        //#1. first validate ownerPrivateKey.
        //decrypt(getRepositoryPrivateKeyEncrypted(), ownerPrivateKey);
        //ECC, to publicKey == getOwnerPublicKey()
        //#2. repositoryPrivateKey + memberPublicKey = ET
        getMemberKeys().put(memberName, memberPublicKey);
        getMemberRepositoryKeys().put(memberPublicKey, "ET");
    }

    public boolean isPrivate() {
        return StringUtils.isNotBlank(getRepositoryPrivateKeyEncrypted());
    }

    public HashedFile getFile() {
        return file;
    }

    public void setFile(HashedFile file) {
        this.file = file;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectHash() {
        return projectHash;
    }

    public void setProjectHash(String projectHash) {
        this.projectHash = projectHash;
    }

    public String getIpfsUrl() {
        return ipfsUrl;
    }

    public void setIpfsUrl(String ipfsUrl) {
        this.ipfsUrl = ipfsUrl;
    }

    public String getRepositoryPrivateKeyEncrypted() {
        return repositoryPrivateKeyEncrypted;
    }

    public void setRepositoryPrivateKeyEncrypted(String repositoryPrivateKeyEncrypted) {
        this.repositoryPrivateKeyEncrypted = repositoryPrivateKeyEncrypted;
    }

    public String getRepositoryPublicKey() {
        return repositoryPublicKey;
    }

    public void setRepositoryPublicKey(String repositoryPublicKey) {
        this.repositoryPublicKey = repositoryPublicKey;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerPublicKey() {
        return ownerPublicKey;
    }

    public void setOwnerPublicKey(String ownerPublicKey) {
        this.ownerPublicKey = ownerPublicKey;
    }

    public Map<String, String> getMemberKeys() {
        return memberKeys;
    }

    public void setMemberKeys(Map<String, String> memberKeys) {
        this.memberKeys = memberKeys;
    }

    public Map<String, String> getMemberRepositoryKeys() {
        return memberRepositoryKeys;
    }

    public void setMemberRepositoryKeys(Map<String, String> memberRepositoryKeys) {
        this.memberRepositoryKeys = memberRepositoryKeys;
    }
}
