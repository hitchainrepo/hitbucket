/*******************************************************************************
 * Copyright (c) 2018-11-22 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.hitchain.hit.provider;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.ipfs.api.IPFS;
import io.ipfs.multihash.Multihash;
import org.apache.commons.lang3.StringUtils;
import org.hitchain.hit.api.HashedFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * IPFSStorage
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-22
 * auto generate by qdp.
 */
public class IPFSStorage {
    private String entranceUrl;

    public static void main(String[] args) {
        IPFSStorage storage = IPFSStorage.create("http://121.40.127.45");
        try {
            HashedFile tree = storage.listFileTree("QmWvL3jE7gMzH88SRxTKMcLmn8rFFZoNsXx7z4mqxb3WXQ");
            System.out.println(new ObjectMapper().writeValueAsString(tree));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static IPFSStorage create(String entranceUrl) {
        IPFSStorage ipfs = new IPFSStorage();
        ipfs.entranceUrl = entranceUrl;
        return ipfs;
    }

    //{"Arguments":{"QmWvL3jE7gMzH88SRxTKMcLmn8rFFZoNsXx7z4mqxb3WXQ":"QmWvL3jE7gMzH88SRxTKMcLmn8rFFZoNsXx7z4mqxb3WXQ"},
    //"Objects":{"QmWvL3jE7gMzH88SRxTKMcLmn8rFFZoNsXx7z4mqxb3WXQ":
    //{"Hash":"QmWvL3jE7gMzH88SRxTKMcLmn8rFFZoNsXx7z4mqxb3WXQ","Size":0,"Type":"Directory","Links":[
    //{"Name":"HEAD","Hash":"QmWeKwYTKwBwVd7AKioXTmtpLFxTk3MBBk2ef2JtwCccAi","Size":23,"Type":"File"},
    //{"Name":"branches","Hash":"QmUNLLsPACCz1vLxQVkXqqLX5R1X345qqfHbsf67hvA3Nn","Size":4,"Type":"Directory"},
    //{"Name":"config","Hash":"Qmd3mEdGupyT5Ych61u4dJgkKAS9V6rqfsGAJjYhsqSKic","Size":119,"Type":"File"},
    //{"Name":"hooks","Hash":"QmUNLLsPACCz1vLxQVkXqqLX5R1X345qqfHbsf67hvA3Nn","Size":4,"Type":"Directory"},
    //{"Name":"info","Hash":"QmfRus53gcWSVFbwMWmRQfBdpPvAB7RhPyQRAk9GgeG4mj","Size":117,"Type":"Directory"},
    //{"Name":"logs","Hash":"QmX9YUf6sTqr9jUGupTDJVi7uxb8bkmyAvAyC8xtn4bnkW","Size":105,"Type":"Directory"},
    //{"Name":"objects","Hash":"QmWobzjSgd8C89TXJQ27HwWsC6jPX8LvihEjPmbvFvSKUW","Size":4185,"Type":"Directory"},
    //{"Name":"packed-refs","Hash":"QmbFMke1KXqnYyBBWxB74N4c5SBnJMVAiMNRcGu6x1AwQH","Size":0,"Type":"File"},
    //{"Name":"refs","Hash":"Qma9pYESGfgpkKtU4w4P7LywoKKYB1yJWdjw59UKQ5dhjn","Size":202,"Type":"Directory"}]}}}
    public HashedFile listFileTree(String fileHash) throws IOException {
        IPFS ipfs = new IPFS("/ip4/" + StringUtils.substringAfterLast(entranceUrl, "//") + "/tcp/5001");
        HashedFile.InputStreamCallback callback = new HashedFile.InputStreamCallback() {
            public InputStream call(HashedFile hashedFile) throws IOException {
                byte[] bytes = ipfs.cat(Multihash.fromBase58(hashedFile.getHash()));
                return new ByteArrayInputStream(bytes);
            }
        };
        HashedFile.DirWrapper root = new HashedFile.DirWrapper("");
        List<Object> folder = new LinkedList<Object>();
        {
            IpfsFileLs ls = getIpfsFileLs(fileHash, ipfs);
            root.setHash(fileHash);
            ls.setCurrent(root);
            folder.add(ls);
        }
        Object temp = null;
        while (folder.size() > 0 && (temp = folder.remove(0)) != null) {
            if (temp instanceof IpfsLinks) {//==IpfsLinks, this add by program
                IpfsFileLs ls = getIpfsFileLs(((IpfsLinks) temp).getHash(), ipfs);
                ls.setCurrent(((IpfsLinks) temp).getCurrent());
                folder.add(ls);
            }
            if (!(temp instanceof IpfsFileLs)) {
                continue;
            }
            //==IpfsFileLs
            IpfsFileLs ls = (IpfsFileLs) temp;
            HashedFile parent = ls.getCurrent();
            for (Map.Entry<String, IpfsObject> entry : ls.objects.entrySet()) {
                String hash = entry.getKey();
                IpfsObject object = entry.getValue();
                if (!object.isDirectory()) {//===object is file
                    if (object.getCurrent() != null) {
                        continue;
                    }
                    HashedFile.FileWrapper file = new HashedFile.FileWrapper(parent.getName() + "/" + object.getName(), callback);
                    file.setHash(object.getHash());
                    parent.getChildren().add(file);
                    object.setCurrent(file);
                    continue;
                }
                //===object is directory
                if (object.getCurrent() == null) {
                    if (StringUtils.equals(hash, parent.getHash())) {
                        object.setCurrent(parent);//is set from IpfsFileLs
                    } else {
                        HashedFile.DirWrapper dir = new HashedFile.DirWrapper(parent.getName() + "/" + StringUtils.defaultString(object.getName()));
                        dir.setHash(object.getHash());
                        parent.getChildren().add(dir);
                        object.setCurrent(dir);
                    }
                }
                List<IpfsLinks> links = object.getLinks();
                for (IpfsLinks link : links) {
                    if (link.getParent() == null) {
                        link.setParent(object.getCurrent());
                    }
                    if (!link.isDirectory()) {//===link is file
                        if (link.getCurrent() != null) {
                            continue;
                        }
                        HashedFile.FileWrapper file = new HashedFile.FileWrapper(link.getParent().getName() + "/" + link.getName(), callback);
                        file.setHash(link.getHash());
                        link.getParent().getChildren().add(file);
                        link.setCurrent(file);
                        continue;
                    }
                    //===link is directory
                    if (link.getCurrent() == null) {
                        HashedFile.DirWrapper dir = new HashedFile.DirWrapper(link.getParent().getName() + "/" + link.getName());
                        dir.setHash(link.getHash());
                        link.getParent().getChildren().add(dir);
                        link.setCurrent(dir);
                    }
                    folder.add(link);// <<<===== add by program
                }
            }
        }
        return root;
    }

    private IpfsFileLs getIpfsFileLs(String fileHash, IPFS ipfs) throws IOException {
        IpfsFileLs ls = null;
        {
            Map map = ipfs.file.ls(Multihash.fromBase58(fileHash));
            ObjectMapper om = new ObjectMapper();
            ls = om.readValue(om.writeValueAsString(map), IpfsFileLs.class);
        }
        return ls;
    }

    public static class IpfsFileLs implements Serializable {
        private HashedFile current;
        @JsonProperty(value = "Arguments")
        private Map<String, String> arguments;
        @JsonProperty(value = "Objects")
        private Map<String, IpfsObject> objects;

        public HashedFile getCurrent() {
            return current;
        }

        public void setCurrent(HashedFile current) {
            this.current = current;
        }

        public Map<String, String> getArguments() {
            return arguments;
        }

        public void setArguments(Map<String, String> arguments) {
            this.arguments = arguments;
        }

        public Map<String, IpfsObject> getObjects() {
            return objects;
        }

        public void setObjects(Map<String, IpfsObject> objects) {
            this.objects = objects;
        }
    }

    public static class IpfsObject implements Serializable {
        private HashedFile current;
        @JsonProperty(value = "Name")
        private String name;
        @JsonProperty(value = "Hash")
        private String hash;
        @JsonProperty(value = "Size")
        private long size;
        @JsonProperty(value = "Type")
        private String type;
        @JsonProperty(value = "Links")
        private List<IpfsLinks> links;

        public boolean isDirectory() {
            return "Directory".equalsIgnoreCase(type);
        }

        public HashedFile getCurrent() {
            return current;
        }

        public void setCurrent(HashedFile current) {
            this.current = current;
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

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<IpfsLinks> getLinks() {
            return links;
        }

        public void setLinks(List<IpfsLinks> links) {
            this.links = links;
        }
    }

    public static class IpfsLinks implements Serializable {
        private HashedFile parent;
        private HashedFile current;
        @JsonProperty(value = "Name")
        private String name;
        @JsonProperty(value = "Hash")
        private String hash;
        @JsonProperty(value = "Size")
        private long size;
        @JsonProperty(value = "Type")
        private String type;

        public boolean isDirectory() {
            return "Directory".equalsIgnoreCase(type);
        }

        public HashedFile getParent() {
            return parent;
        }

        public void setParent(HashedFile parent) {
            this.parent = parent;
        }

        public HashedFile getCurrent() {
            return current;
        }

        public void setCurrent(HashedFile current) {
            this.current = current;
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

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
