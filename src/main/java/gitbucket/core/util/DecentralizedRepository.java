/*******************************************************************************
 * Copyright (c) 2018-10-18 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform.
 ******************************************************************************/
package gitbucket.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.eclipse.jgit.internal.storage.dfs.*;
import org.eclipse.jgit.internal.storage.pack.PackExt;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.ReceiveCommand;
import org.eclipse.jgit.util.RefList;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * DecentralizedRepository
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-10-18
 * auto generate by qdp.
 */
public class DecentralizedRepository extends DfsRepository {

    private final DfsReaderOptions readerOptions;
    private final String repository;
    private final String owner;

    public DecentralizedRepository(DfsRepositoryBuilder builder, String repository, String owner) {
        super(builder);
        this.repository = repository;
        this.owner = owner;
        this.readerOptions = builder.getReaderOptions();
        System.out.println("========>>> Create DecentralizedRepository repository=" + repository + ", owner=" + owner);
    }

    public DecentralizedObjDatabase getObjectDatabase() {
        return new DecentralizedObjDatabase(this, this.readerOptions);
    }

    public RefDatabase getRefDatabase() {
        return new DecentralizedRefDatabase(this);
    }

    public StoredConfig getConfig() {
        return super.getConfig();
    }

    public static class IPFSUtil {

        public static void main(String[] args) throws Exception {
            String fileName = "/Users/zhaochen/dev/workspace/idea/gitbucket/src/main/java/gitbucket/core/util/IpfsFile.java";
            byte[] bs = FileUtils.readFileToByteArray(new File(fileName));
            write(fileName, "java", bs);
            byte[] asByte = getAsByte(fileName, "java");
            System.out.println(new String(asByte, "UTF-8"));
        }

        public static byte[] getAsByte(String fileName, String fileType) {
            CloseableHttpClient httpCilent2 = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)   //设置连接超时时间
                .setConnectionRequestTimeout(5000) // 设置请求超时时间
                .setSocketTimeout(5000)
                .setRedirectsEnabled(true)//默认允许自动重定向
                .build();
            String uri = "http://121.40.127.45:8080/ipfs/" + getHashFromFile(fileName, fileType);
            System.out.println("========>>> Read File=" + uri);
            HttpGet httpGet2 = new HttpGet(uri);
            httpGet2.setConfig(requestConfig);
            String srtResult = "";
            try {
                HttpResponse httpResponse = httpCilent2.execute(httpGet2);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    byte[] bytes = EntityUtils.toByteArray(httpResponse.getEntity());//获得返回的结果
                    return bytes;
                } else if (httpResponse.getStatusLine().getStatusCode() == 400) {
                    return null;
                } else if (httpResponse.getStatusLine().getStatusCode() == 500) {
                    return null;
                }
                return null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    httpCilent2.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public static Map<String, Object> write(String fileName, String fileType, byte[] content) {
            //获取可关闭的 httpCilent
            CloseableHttpClient httpClient = HttpClients.createDefault();
            //配置超时时间
            RequestConfig requestConfig = RequestConfig.custom().
                setConnectTimeout(1000).setConnectionRequestTimeout(1000)
                .setSocketTimeout(1000).setRedirectsEnabled(true).build();

            HttpPost httpPost = new HttpPost("http://121.40.127.45:5001/api/v0/add");
            try {
                //设置超时时间
                httpPost.setConfig(requestConfig);
                //装配post请求参数
                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                builder.addBinaryBody(fileName, content);
                HttpEntity entity = builder.build();
                //设置post求情参数
                httpPost.setEntity(entity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                String strResult = "";
                if (httpResponse != null) {
                    System.out.println(httpResponse.getStatusLine().getStatusCode());
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        strResult = EntityUtils.toString(httpResponse.getEntity());
                        ObjectMapper mapper = new ObjectMapper();
                        HashMap map = mapper.readValue(strResult, HashMap.class);
                        writeHashToFile(fileName, fileType, (String) map.get("Hash"));
                        System.out.println("========>>> Write File=" + fileName + ", Type=" + fileType + ", Hash=" + map.get("Hash"));
                    } else if (httpResponse.getStatusLine().getStatusCode() == 400) {
                        strResult = "Error Response: " + httpResponse.getStatusLine().toString();
                    } else if (httpResponse.getStatusLine().getStatusCode() == 500) {
                        strResult = "Error Response: " + httpResponse.getStatusLine().toString();
                    } else {
                        strResult = "Error Response: " + httpResponse.getStatusLine().toString();
                    }
                } else {
                }
                System.out.println(strResult);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (httpClient != null) {
                        httpClient.close(); //释放资源
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        public static void writeHashToFile(String fileName, String fileType, String hash) {
            String file = "/Users/zhaochen/Desktop/hashFile.txt";
            try {
                FileUtils.write(new File(file), fileType + "," + fileName + "," + hash + "\n", Charset.forName("UTF-8"), true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static String getHashFromFile(String fileName, String fileType) {
            String file = "/Users/zhaochen/Desktop/hashFile.txt";
            try {
                String content = FileUtils.readFileToString(new File(file), "UTF-8");
                String[] lines = StringUtils.split(content, '\n');
                ArrayUtils.reverse(lines);
                for (String line : lines) {
                    String[] split = StringUtils.split(line, ",");
                    if (split.length != 3) {
                        continue;
                    }
                    if (split[0].equals(fileType) && split[1].equals(fileName)) {
                        return split[2];
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return null;
        }
    }

    public static class Builder extends DfsRepositoryBuilder<Builder, DecentralizedRepository> {
        private File dir;
        private String owner = "root";
        private String repository = "temp";

        public Builder() {
            setReaderOptions(new DfsReaderOptions());
            setRepositoryDescription(new DfsRepositoryDescription());
        }

        public Builder withPath(File dir) {
            this.dir = dir;
            String path = dir.getPath();
            String[] split = StringUtils.split(path, '/');
            if (split.length > 0) {
                repository = split[split.length - 1];
            }
            if (split.length > 1) {
                owner = split[split.length - 2];
            }
            return this;
        }

        public DecentralizedRepository build() throws IOException {
            return new DecentralizedRepository(this, repository, owner);
        }
    }

    public class DecentralizedRefDatabase extends DfsRefDatabase {

        private final ReadWriteLock lock = new ReentrantReadWriteLock(true /* fair */);
        private final ConcurrentMap<String, Ref> refs = new ConcurrentHashMap<>();

        public DecentralizedRefDatabase(DfsRepository repository) {
            super(repository);
        }

        public void create() {
            super.create();
            try {
                //TODO read references json from storage
                if (refs.isEmpty()) {
                    ObjectMapper om = new ObjectMapper();
                    byte[] bs = IPFSUtil.getAsByte(Ref.class.getName(), "REF");
                    Map<String, Ref> map = om.readValue(new String(bs), om.getTypeFactory().constructMapType(ConcurrentHashMap.class, String.class, Ref.class));
                    refs.putAll(map);
                }
            } catch (Exception e) {
                throw new RuntimeException("Error getting references", e);
            }
        }

        public void close() {
            super.close();
            try {
                //TODO write references json to storage
                ObjectMapper om = new ObjectMapper();
                String json = om.writeValueAsString(refs);
                IPFSUtil.write(Ref.class.getName(), "REF", json.getBytes());
            } catch (Exception e) {
                throw new RuntimeException("Error setting references", e);
            }
        }

        public boolean performsAtomicTransactions() {
            return true;
        }

        public BatchRefUpdate newBatchUpdate() {
            return new BatchRefUpdate(this) {
                public void execute(RevWalk walk, ProgressMonitor monitor) throws IOException {
                    try {
                        lock.writeLock().lock();
                        batch(getCommands());
                    } finally {
                        lock.writeLock().unlock();
                    }
                }

                protected void batch(List<ReceiveCommand> cmds) {
                    // Validate that the target exists in a new RevWalk, as the RevWalk
                    // from the RefUpdate might be reading back unflushed objects.
                    Map<ObjectId, ObjectId> peeled = new HashMap<>();
                    try (RevWalk rw = new RevWalk(getRepository())) {
                        for (ReceiveCommand c : cmds) {
                            if (c.getResult() != ReceiveCommand.Result.NOT_ATTEMPTED) {
                                ReceiveCommand.abort(cmds);
                                return;
                            }

                            if (!ObjectId.zeroId().equals(c.getNewId())) {
                                try {
                                    RevObject o = rw.parseAny(c.getNewId());
                                    if (o instanceof RevTag) {
                                        peeled.put(o, rw.peel(o).copy());
                                    }
                                } catch (IOException e) {
                                    c.setResult(ReceiveCommand.Result.REJECTED_MISSING_OBJECT);
                                    ReceiveCommand.abort(cmds);
                                    return;
                                }
                            }
                        }
                    }

                    // Check all references conform to expected old value.
                    for (ReceiveCommand c : cmds) {
                        Ref r = refs.get(c.getRefName());
                        if (r == null) {
                            if (c.getType() != ReceiveCommand.Type.CREATE) {
                                c.setResult(ReceiveCommand.Result.LOCK_FAILURE);
                                ReceiveCommand.abort(cmds);
                                return;
                            }
                        } else {
                            ObjectId objectId = r.getObjectId();
                            if (r.isSymbolic() || objectId == null || !objectId.equals(c.getOldId())) {
                                c.setResult(ReceiveCommand.Result.LOCK_FAILURE);
                                ReceiveCommand.abort(cmds);
                                return;
                            }
                        }
                    }

                    // Write references.
                    for (ReceiveCommand c : cmds) {
                        if (c.getType() == ReceiveCommand.Type.DELETE) {
                            refs.remove(c.getRefName());
                            c.setResult(ReceiveCommand.Result.OK);
                            continue;
                        }

                        ObjectId p = peeled.get(c.getNewId());
                        Ref r;
                        if (p != null) {
                            r = new ObjectIdRef.PeeledTag(Ref.Storage.PACKED, c.getRefName(), c.getNewId(), p);
                        } else {
                            r = new ObjectIdRef.PeeledNonTag(Ref.Storage.PACKED, c.getRefName(), c.getNewId());
                        }
                        refs.put(r.getName(), r);
                        c.setResult(ReceiveCommand.Result.OK);
                    }
                }
            };
        }

        public RefCache scanAllRefs() throws IOException {
            RefList.Builder<Ref> ids = new RefList.Builder<>();
            RefList.Builder<Ref> sym = new RefList.Builder<>();
            try {
                lock.readLock().lock();
                for (Ref ref : refs.values()) {
                    if (ref.isSymbolic()) {
                        sym.add(ref);
                    }
                    ids.add(ref);
                }
            } finally {
                lock.readLock().unlock();
            }
            ids.sort();
            sym.sort();
            getRepository().getObjectDatabase().getCurrentPackList().markDirty();
            return new RefCache(ids.toRefList(), sym.toRefList());
        }

        public boolean compareAndPut(Ref oldRef, Ref newRef) throws IOException {
            try {
                lock.writeLock().lock();
                ObjectId id = newRef.getObjectId();
                if (id != null) {
                    try (RevWalk rw = new RevWalk(getRepository())) {
                        // Validate that the target exists in a new RevWalk, as the RevWalk
                        // from the RefUpdate might be reading back unflushed objects.
                        rw.parseAny(id);
                    }
                }
                String name = newRef.getName();
                if (oldRef == null) {
                    return refs.putIfAbsent(name, newRef) == null;
                }

                Ref cur = refs.get(name);
                if (cur != null) {
                    if (eq(cur, oldRef)) {
                        return refs.replace(name, cur, newRef);
                    }
                }

                if (oldRef.getStorage() == Ref.Storage.NEW) {
                    return refs.putIfAbsent(name, newRef) == null;
                }

                return false;
            } finally {
                lock.writeLock().unlock();
            }
        }

        public boolean compareAndRemove(Ref oldRef) throws IOException {
            try {
                lock.writeLock().lock();
                String name = oldRef.getName();
                Ref cur = refs.get(name);
                if (cur != null && eq(cur, oldRef)) {
                    return refs.remove(name, cur);
                } else {
                    return false;
                }
            } finally {
                lock.writeLock().unlock();
            }
        }

        protected boolean eq(Ref a, Ref b) {
            if (!Objects.equals(a.getName(), b.getName())) {
                return false;
            }
            if (a.isSymbolic() != b.isSymbolic()) {
                return false;
            }
            if (a.isSymbolic()) {
                return Objects.equals(a.getTarget().getName(), b.getTarget().getName());
            } else {
                return Objects.equals(a.getObjectId(), b.getObjectId());
            }
        }
    }

    public class DecentralizedObjDatabase extends DfsObjDatabase {

        public DecentralizedObjDatabase(DfsRepository repository, DfsReaderOptions options) {
            super(repository, options);
        }

        public DfsPackDescription newPack(PackSource source) throws IOException {
            String packId = "pack-" + StringUtils.removeAll(UUID.randomUUID().toString(), "-") + "-" + source.name();
            DfsPackDescription desc = new DfsPackDescription(getRepository().getDescription(), packId, source);
            return desc;
        }

        public void commitPackImpl(Collection<DfsPackDescription> desc, Collection<DfsPackDescription> replaces) throws IOException {
            List<DfsPackDescription> packs = listPacks();
            List<DfsPackDescription> allPacks = new ArrayList<>(desc.size() + packs.size());
            allPacks.addAll(desc);
            allPacks.addAll(packs);
            if (replaces != null) {
                allPacks.removeAll(replaces);
            }
            //TODO write allPacks desc to storage, save in one json.
            ObjectMapper om = new ObjectMapper();
            String json = om.writeValueAsString(allPacks);
            IPFSUtil.write(DfsPackDescription.class.getName(), "PACKDESC", json.getBytes());
        }

        public void rollbackPack(Collection<DfsPackDescription> desc) {
            // Do nothing. Pack is not recorded until commitPack.
        }

        public List<DfsPackDescription> listPacks() throws IOException {
            //TODO read allPacks desc from storage.
            byte[] bs = IPFSUtil.getAsByte(DfsPackDescription.class.getName(), "PACKDESC");
            String json = new String(bs);
            ObjectMapper om = new ObjectMapper();
            return om.readValue(json, om.getTypeFactory().constructCollectionType(List.class, DfsPackDescription.class));
        }

        public ReadableChannel openFile(DfsPackDescription desc, PackExt ext) throws FileNotFoundException, IOException {
            String fileName = desc.getFileName(ext);
            //TODO read file from storage.
            byte[] bs = IPFSUtil.getAsByte(fileName, "PACK");
            return new ByteArrayReadableChannel(bs, 1024);
        }

        public DfsOutputStream writeFile(DfsPackDescription desc, PackExt ext) throws IOException {
            final String fileName = desc.getFileName(ext);
            return new Out() {
                public void flush() throws IOException {
                    //TODO write file to storage.
                    IPFSUtil.write(fileName, "PACK", getData());
                }
            };
        }
    }

    public class ByteArrayReadableChannel implements ReadableChannel {
        private final byte[] data;
        private final int blockSize;
        private int position;
        private boolean open = true;

        public ByteArrayReadableChannel(byte[] buf, int blockSize) {
            data = buf;
            this.blockSize = blockSize;
        }

        public int read(ByteBuffer dst) {
            int n = Math.min(dst.remaining(), data.length - position);
            if (n == 0) {
                return -1;
            }
            dst.put(data, position, n);
            position += n;
            return n;
        }

        public void close() {
            open = false;
        }

        public boolean isOpen() {
            return open;
        }

        public long position() {
            return position;
        }

        public void position(long newPosition) {
            position = (int) newPosition;
        }

        public long size() {
            return data.length;
        }

        public int blockSize() {
            return blockSize;
        }

        public void setReadAheadBytes(int b) {
            // Unnecessary on a byte array.
        }
    }

    public abstract class Out extends DfsOutputStream {
        private final ByteArrayOutputStream dst = new ByteArrayOutputStream();

        private byte[] data;

        public void write(byte[] buf, int off, int len) {
            data = null;
            dst.write(buf, off, len);
        }

        public int read(long position, ByteBuffer buf) {
            byte[] d = getData();
            int n = Math.min(buf.remaining(), d.length - (int) position);
            if (n == 0) {
                return -1;
            }
            buf.put(d, (int) position, n);
            return n;
        }

        public byte[] getData() {
            if (data == null) {
                data = dst.toByteArray();
            }
            return data;
        }

        public abstract void flush() throws IOException;

        public void close() throws IOException {
            flush();
        }
    }
}
