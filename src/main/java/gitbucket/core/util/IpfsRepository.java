/*******************************************************************************
 * Copyright (c) 2018-10-22 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform.
 ******************************************************************************/
package gitbucket.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.ipfs.multihash.Multihash;

import org.apache.commons.codec.digest.DigestUtils;
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
import org.eclipse.jgit.attributes.AttributesNodeProvider;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.errors.*;
import org.eclipse.jgit.events.ListenerList;
import org.eclipse.jgit.events.RepositoryEvent;
import org.eclipse.jgit.internal.JGitText;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.PackParser;
import org.eclipse.jgit.transport.PackedObjectInfo;
import org.eclipse.jgit.util.FS;
import org.eclipse.jgit.util.IO;
import org.eclipse.jgit.util.sha1.SHA1;

import java.io.*;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

import static org.eclipse.jgit.lib.Ref.Storage.NEW;

/**
 * IpfsRepository
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-10-22
 * auto generate by qdp.
 */
public class IpfsRepository extends Repository {

    protected String owner;
    protected String repoName;

    protected IpfsRepository(Builder options) {
        super(options);
    }

    public void create(boolean bare) throws IOException {

    }

    public IpfsObjectDatabase getObjectDatabase() {
        return new IpfsObjectDatabase();
    }

    public IpfsRefDatabase getRefDatabase() {
        return new IpfsRefDatabase(getObjectDatabase());
    }

    public IpfsStoredConfig getConfig() {
        return new IpfsStoredConfig();
    }

    public AttributesNodeProvider createAttributesNodeProvider() {
        return null;
    }

    public void scanForRepoChanges() throws IOException {

    }

    public void notifyIndexChanged(boolean internal) {

    }

    public ReflogReader getReflogReader(String refName) throws IOException {
        return null;
    }

    public ListenerList getListenerList() {
        return super.getListenerList();
    }

    public void fireEvent(RepositoryEvent<?> event) {
        super.fireEvent(event);
    }

    public void create() throws IOException {
        super.create();
    }

    public File getDirectory() {
        return super.getDirectory();
    }

    public ObjectInserter newObjectInserter() {
        return super.newObjectInserter();
    }

    public ObjectReader newObjectReader() {
        return super.newObjectReader();
    }

    public FS getFS() {
        return super.getFS();
    }

    public boolean hasObject(AnyObjectId objectId) {
        return super.hasObject(objectId);
    }

    public ObjectLoader open(AnyObjectId objectId) throws MissingObjectException, IOException {
        return super.open(objectId);
    }

    public ObjectLoader open(AnyObjectId objectId, int typeHint) throws MissingObjectException, IncorrectObjectTypeException, IOException {
        return super.open(objectId, typeHint);
    }

    public RefUpdate updateRef(String ref) throws IOException {
        return super.updateRef(ref);
    }

    public RefUpdate updateRef(String ref, boolean detach) throws IOException {
        return super.updateRef(ref, detach);
    }

    public RefRename renameRef(String fromRef, String toRef) throws IOException {
        return super.renameRef(fromRef, toRef);
    }

    public ObjectId resolve(String revstr) throws AmbiguousObjectException, IncorrectObjectTypeException, RevisionSyntaxException, IOException {
        return super.resolve(revstr);
    }

    public String simplify(String revstr) throws AmbiguousObjectException, IOException {
        return super.simplify(revstr);
    }

    public void incrementOpen() {
        super.incrementOpen();
    }

    public void close() {
        super.close();
    }

    protected void doClose() {
        super.doClose();
    }

    public String toString() {
        return super.toString();
    }

    public String getFullBranch() throws IOException {
        return super.getFullBranch();
    }

    public String getBranch() throws IOException {
        return super.getBranch();
    }

    public Set<ObjectId> getAdditionalHaves() {
        return super.getAdditionalHaves();
    }

    public Map<String, Ref> getAllRefs() {
        return super.getAllRefs();
    }

    public Map<String, Ref> getTags() {
        return super.getTags();
    }

    public Ref peel(Ref ref) {
        return super.peel(ref);
    }

    public Map<AnyObjectId, Set<Ref>> getAllRefsByPeeledObjectId() {
        return super.getAllRefsByPeeledObjectId();
    }

    public File getIndexFile() throws NoWorkTreeException {
        return super.getIndexFile();
    }

    public RevCommit parseCommit(AnyObjectId id) throws IncorrectObjectTypeException, IOException, MissingObjectException {
        return super.parseCommit(id);
    }

    public DirCache readDirCache() throws NoWorkTreeException, CorruptObjectException, IOException {
        return super.readDirCache();
    }

    public DirCache lockDirCache() throws NoWorkTreeException, CorruptObjectException, IOException {
        return super.lockDirCache();
    }

    public RepositoryState getRepositoryState() {
        return super.getRepositoryState();
    }

    public boolean isBare() {
        return super.isBare();
    }

    public File getWorkTree() throws NoWorkTreeException {
        return super.getWorkTree();
    }

    public String shortenRemoteBranchName(String refName) {
        return super.shortenRemoteBranchName(refName);
    }

    public String getRemoteName(String refName) {
        return super.getRemoteName(refName);
    }

    public String getGitwebDescription() throws IOException {
        return super.getGitwebDescription();
    }

    public void setGitwebDescription(String description) throws IOException {
        super.setGitwebDescription(description);
    }

    public String readMergeCommitMsg() throws IOException, NoWorkTreeException {
        return super.readMergeCommitMsg();
    }

    public void writeMergeCommitMsg(String msg) throws IOException {
        super.writeMergeCommitMsg(msg);
    }

    public String readCommitEditMsg() throws IOException, NoWorkTreeException {
        return super.readCommitEditMsg();
    }

    public void writeCommitEditMsg(String msg) throws IOException {
        super.writeCommitEditMsg(msg);
    }

    public List<ObjectId> readMergeHeads() throws IOException, NoWorkTreeException {
        return super.readMergeHeads();
    }

    public void writeMergeHeads(List<? extends ObjectId> heads) throws IOException {
        super.writeMergeHeads(heads);
    }

    public ObjectId readCherryPickHead() throws IOException, NoWorkTreeException {
        return super.readCherryPickHead();
    }

    public ObjectId readRevertHead() throws IOException, NoWorkTreeException {
        return super.readRevertHead();
    }

    public void writeCherryPickHead(ObjectId head) throws IOException {
        super.writeCherryPickHead(head);
    }

    public void writeRevertHead(ObjectId head) throws IOException {
        super.writeRevertHead(head);
    }

    public void writeOrigHead(ObjectId head) throws IOException {
        super.writeOrigHead(head);
    }

    public ObjectId readOrigHead() throws IOException, NoWorkTreeException {
        return super.readOrigHead();
    }

    public String readSquashCommitMsg() throws IOException {
        return super.readSquashCommitMsg();
    }

    public void writeSquashCommitMsg(String msg) throws IOException {
        super.writeSquashCommitMsg(msg);
    }

    public List<RebaseTodoLine> readRebaseTodo(String path, boolean includeComments) throws IOException {
        return super.readRebaseTodo(path, includeComments);
    }

    public void writeRebaseTodoFile(String path, List<RebaseTodoLine> steps, boolean append) throws IOException {
        super.writeRebaseTodoFile(path, steps, append);
    }

    public Set<String> getRemoteNames() {
        return super.getRemoteNames();
    }

    public void autoGC(ProgressMonitor monitor) {
        super.autoGC(monitor);
    }

    public static class IpfsStoredConfig extends StoredConfig {
        public void load() throws IOException, ConfigInvalidException {
        }

        public void save() throws IOException {
        }
    }

    public static class IpfsRefDatabase extends RefDatabase {

        protected IpfsObjectDatabase odb;

        protected ConcurrentMap<String, Ref> refs = new ConcurrentHashMap<>();

        public IpfsRefDatabase(IpfsObjectDatabase odb) {
            this.odb = odb;
            try {
                ObjectMapper om = new ObjectMapper();
                byte[] bs = DecentralizedRepository.IPFSUtil.getAsByte(Ref.class.getName(), "REF");
                Map<String, Ref> map = om.readValue(new String(bs), om.getTypeFactory().constructMapType(ConcurrentHashMap.class, String.class, Ref.class));
                refs.putAll(map);
            } catch (Exception e) {
                throw new RuntimeException("Error getting references", e);
            }
        }

        public void create() throws IOException {
        }

        public void close() {
        }

        public boolean isNameConflicting(String name) throws IOException {
            return refs.containsKey(name);
        }

        public RefUpdate newUpdate(String name, boolean detach) throws IOException {
            Ref ref = null;
            if (refs.containsKey(name)) {
                ref = refs.get(name);
            } else {
                ref = new ObjectIdRef.Unpeeled(NEW, name, null);
            }
            return new IpfsRefUpdate(this, ref);
        }

        public RefRename newRename(String fromName, String toName) throws IOException {
            return null;
        }

        public Ref getRef(String name) throws IOException {
            return null;
        }

        public Map<String, Ref> getRefs(String prefix) throws IOException {
            return null;
        }

        public List<Ref> getAdditionalRefs() throws IOException {
            return null;
        }

        public Ref peel(Ref ref) throws IOException {
            return null;
        }

        public IpfsObjectDatabase getObjectDatabase() {
            return odb;
        }
    }

    public static class IpfsRefUpdate extends RefUpdate {
        protected IpfsRefDatabase rdb;

        protected IpfsRefUpdate(IpfsRefDatabase rdb, Ref ref) {
            super(ref);
            this.rdb = rdb;
        }

        protected IpfsRefDatabase getRefDatabase() {
            return rdb;
        }

        protected IpfsRepository getRepository() {
            return rdb.getObjectDatabase().getRepository();
        }

        protected boolean tryLock(boolean deref) throws IOException {
            return true;
        }

        protected void unlock() {
        }

        protected Result doUpdate(Result desiredResult) throws IOException {
//            ObjectIdRef newRef;
//            RevObject obj = new RevWalk(getRepository()).parseAny(getNewObjectId());
//            if (obj instanceof RevTag) {
//                newRef = new ObjectIdRef.PeeledTag(
//                    Ref.Storage.PACKED,
//                    getRef().getName(),
//                    getNewObjectId(),
//                    rw.peel(obj).copy());
//            } else {
//                newRef = new ObjectIdRef.PeeledNonTag(
//                    Ref.Storage.PACKED,
//                    dstRef.getName(),
//                    getNewObjectId());
//            }
            return null;
        }

        protected Result doDelete(Result desiredResult) throws IOException {
            return null;
        }

        protected Result doLink(String target) throws IOException {
            return null;
        }
    }

    public static class IpfsObjectDatabase extends ObjectDatabase {

        protected IpfsRepository repository;

        public ObjectInserter newInserter() {
            return new IpfsObjectInserter(this);
        }

        public ObjectReader newReader() {
            return new IpfsObjectReader();
        }

        public void close() {

        }

        public IpfsRepository getRepository() {
            return repository;
        }
    }

    public static class IpfsObjectReader extends ObjectReader {
        public ObjectReader newReader() {
            return null;
        }

        public Collection<ObjectId> resolve(AbbreviatedObjectId id) throws IOException {
            return null;
        }

        public ObjectLoader open(AnyObjectId objectId, int typeHint) throws MissingObjectException, IncorrectObjectTypeException, IOException {
            return null;
        }

        public Set<ObjectId> getShallowCommits() throws IOException {
            return null;
        }

        public void close() {

        }

    }

    public static class IpfsObjectInserter extends ObjectInserter {
        protected IpfsObjectDatabase odb;

        public IpfsObjectInserter(IpfsObjectDatabase odb) {
            this.odb = odb;
        }

        public ObjectId insert(int type/*org.eclipse.jgit.lib.Constants*/, long len, InputStream is) throws IOException {
            if (len <= buffer().length) {
                byte[] buf = buffer();
                int actLen = IO.readFully(is, buf, 0);
                {
                    ObjectId id = idFor(type, buf, 0, actLen);
                    {
                        SHA1 md = SHA1.newInstance();
                        md.update(Constants.encodedTypeString(type));
                        md.update((byte) ' ');
                        md.update(Constants.encodeASCII(len));
                        md.update((byte) 0);
                        md.update(buf, 0, actLen);
                    }
                    ByteArrayOutputStream bout = new ByteArrayOutputStream();
                    try {
                        DeflaterOutputStream cOut = new DeflaterOutputStream(bout, new Deflater(Deflater.DEFAULT_COMPRESSION), 8192);
                        {
                            cOut.write(Constants.encodedTypeString(type));
                            cOut.write((byte) ' ');
                            cOut.write(Constants.encodeASCII(len));
                            cOut.write((byte) 0);
                        }
                        {
                            cOut.write(buf);
                            cOut.finish();
                        }
                        {
                            IPFSUtil.write(id.name(), Constants.typeString(type), bout.toByteArray());
                        }
                    } finally {
                        bout.close();
                    }
                    return id;
                }
            } else {
                SHA1 md = digest();
                ObjectId id = null;
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                try {
                    DeflaterOutputStream cOut = new DeflaterOutputStream(bout, new Deflater(Deflater.DEFAULT_COMPRESSION), 8192);
                    SHA1OutputStream dOut = new SHA1OutputStream(cOut, md);
                    {
                        dOut.write(Constants.encodedTypeString(type));
                        dOut.write((byte) ' ');
                        dOut.write(Constants.encodeASCII(len));
                        dOut.write((byte) 0);
                    }
                    {
                        final byte[] buf = buffer();
                        while (len > 0) {
                            int n = is.read(buf, 0, (int) Math.min(len, buf.length));
                            if (n <= 0) {
                                throw new EOFException(MessageFormat.format(JGitText.get().inputDidntMatchLength, Long.valueOf(len)));
                            }
                            dOut.write(buf, 0, n);
                            len -= n;
                        }
                        dOut.flush();
                        cOut.finish();
                    }
                    md.toObjectId();
                    {
                        IPFSUtil.write(id.name(), Constants.typeString(type), bout.toByteArray());
                    }
                } finally {
                    bout.close();
                }
                return id;
            }
        }

        public PackParser newPackParser(InputStream in) throws IOException {
            return new IpfsPackParser(odb, in);
        }

        public ObjectReader newReader() {
            return null;
        }

        public void flush() throws IOException {

        }

        public void close() {

        }
    }

    public static class IpfsPackParser extends PackParser {

        protected IpfsObjectDatabase odb;

        protected IpfsPackParser(IpfsObjectDatabase odb, InputStream src) {
            super(odb, src);
            this.odb = odb;
        }

        protected void onStoreStream(byte[] raw, int pos, int len) throws IOException {

        }

        protected void onObjectHeader(Source src, byte[] raw, int pos, int len) throws IOException {

        }

        protected void onObjectData(Source src, byte[] raw, int pos, int len) throws IOException {

        }

        protected void onInflatedObjectData(PackedObjectInfo obj, int typeCode, byte[] data) throws IOException {

        }

        protected void onPackHeader(long objCnt) throws IOException {

        }

        protected void onPackFooter(byte[] hash) throws IOException {

        }

        protected boolean onAppendBase(int typeCode, byte[] data, PackedObjectInfo info) throws IOException {
            return false;
        }

        protected void onEndThinPack() throws IOException {

        }

        protected ObjectTypeAndSize seekDatabase(PackedObjectInfo obj, ObjectTypeAndSize info) throws IOException {
            return null;
        }

        protected ObjectTypeAndSize seekDatabase(UnresolvedDelta delta, ObjectTypeAndSize info) throws IOException {
            return null;
        }

        protected int readDatabase(byte[] dst, int pos, int cnt) throws IOException {
            return 0;
        }

        protected boolean checkCRC(int oldCRC) {
            return false;
        }

        protected void onBeginWholeObject(long streamPosition, int type, long inflatedSize) throws IOException {

        }

        protected void onEndWholeObject(PackedObjectInfo info) throws IOException {

        }

        protected void onBeginOfsDelta(long deltaStreamPosition, long baseStreamPosition, long inflatedSize) throws IOException {

        }

        protected void onBeginRefDelta(long deltaStreamPosition, AnyObjectId baseId, long inflatedSize) throws IOException {

        }
    }

    public static class SHA1OutputStream extends FilterOutputStream {
        private final SHA1 md;

        SHA1OutputStream(OutputStream out, SHA1 md) {
            super(out);
            this.md = md;
        }

        public void write(int b) throws IOException {
            md.update((byte) b);
            out.write(b);
        }

        public void write(byte[] in, int p, int n) throws IOException {
            md.update(in, p, n);
            out.write(in, p, n);
        }
    }

    public static class Builder extends BaseRepositoryBuilder<IpfsRepository.Builder, IpfsRepository> {
        private File dir;
        private String owner = "root";
        private String repository = "temp";

        public IpfsRepository build() throws IOException {
            IpfsRepository ipfs = new IpfsRepository(this);
            ipfs.owner = owner;
            ipfs.repoName = repository;
            return ipfs;
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

        public Builder withOwner(String owner) {
            this.owner = owner;
            return this;
        }

        public Builder withRepository(String name) {
            this.repository = name;
            return this;
        }
    }

    public static class IPFSUtil {

        public static void main(String[] args) throws Exception {
            //System.out.println(sha256AndBase58("ref: refs/heads/master"));
            String fileName = "/Users/zhaochen/dev/workspace/idea/gitbucket/src/test/HEAD";
            System.out.println(sha256AndBase58(fileName));
            byte[] bs = FileUtils.readFileToByteArray(new File(fileName));
            write(fileName, "java", bs);
            byte[] asByte = getAsByte(fileName, "java");
            System.out.println(new String(asByte, "UTF-8"));
        }

        public static String sha256AndBase58(String str) {
            //所有哈希都以“Qm”开头。 这是因为哈希实际上是一个多哈希（multihash），哈希的前两个字节中指定了哈希函数和哈希长度。 在上面的例子中，前两个字节的十六进制是1220，其中12表示哈希函数为SHA256，20表示以字节为单位的哈希长度 - 32字节。
            //return new Multihash(Multihash.Type.sha2_256, DigestUtils.sha256(str)).toBase58();
        	return new Multihash(Multihash.Type.sha2_256, DigestUtils.sha256(str)).toBase58();
        }

        public static String genHash(String content) {
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
                builder.addBinaryBody(content, content.getBytes());
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
                        return (String) map.get("Hash");
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
}
