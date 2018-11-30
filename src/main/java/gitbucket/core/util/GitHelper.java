/*******************************************************************************
 * Copyright (c) 2018-10-19 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform.
 ******************************************************************************/
package gitbucket.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.internal.storage.file.LockFile;
import org.eclipse.jgit.internal.storage.file.PackFile;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.RefWriter;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.hitchain.hit.api.HashedFile;
import org.hitchain.hit.api.IndexFile;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * GitHelper
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-10-19 auto generate by qdp.
 */
public class GitHelper {

    public static final String URL_IPFS = System.getProperty("URL_IPFS", "http://121.40.127.45");
    private static final ReentrantReadWriteLock indexFileLock = new ReentrantReadWriteLock();

    public static void main(String[] args) throws Exception {
        syncProject(new File("/Users/zhaochen/.gitbucket/repositories/root/test.git"));
        // IPFS ipfs = new IPFS("/ip4/121.40.127.45/tcp/5001");
        // NamedStreamable.FileWrapper dir = new NamedStreamable.FileWrapper(new
        // File("/Users/zhaochen/dev/workspace/testgit"));
        // List<MerkleNode> add = ipfs.add(dir);
        // for (MerkleNode mn : add) {
        // System.out.println("MerkleNode = " + mn);
        // System.out.println(mn.name + ", " + mn.hash);
        // }
    }

    public static void updateServerInfo(File projectDir) throws Exception {
        MyGit git = MyGit.open(projectDir);
        Repository db = git.getRepository();
        if (db instanceof FileRepository) {
            final FileRepository fr = (FileRepository) db;
            RefWriter rw = new RefWriter(fr.getRefDatabase().getRefs()) {
                protected void writeFile(String name, byte[] bin) throws IOException {
                    File path = new File(fr.getDirectory(), name);
                    writeUpdateFile(path, bin);
                }
            };
            rw.writePackedRefs();
            rw.writeInfoRefs();

            final StringBuilder w = new StringBuilder();
            for (PackFile p : fr.getObjectDatabase().getPacks()) {
                w.append("P ");
                w.append(p.getPackFile().getName());
                w.append('\n');
            }
            writeUpdateFile(new File(new File(fr.getObjectDatabase().getDirectory(), "info"), "packs"),
                Constants.encodeASCII(w.toString()));
        }
    }

    public static void writeUpdateFile(File p, byte[] bin) {
        final LockFile lck = new LockFile(p);
        try {
            if (!lck.lock()) {
                throw new RuntimeException("Can't write " + p);
            }
            lck.write(bin);
        } catch (Exception ioe) {
            throw new RuntimeException("Can't write " + p);
        }
        if (!lck.commit()) {
            throw new RuntimeException("Can't write " + p);
        }
    }

    public static String updateProject(File projectDir) {
        try {
            updateServerInfo(projectDir);
            String urlIpfs = URL_IPFS;
            IPFS ipfs = new IPFS("/ip4/" + StringUtils.substringAfterLast(urlIpfs, "//") + "/tcp/5001");
            NamedStreamable.FileWrapper dir = new NamedStreamable.FileWrapper(projectDir);
            List<MerkleNode> add = ipfs.add(dir);
            String hash = add.get(add.size() - 1).hash.toBase58();
            System.out.println("Project name: " + projectDir.getPath() + ", hash: " + urlIpfs + ":8080/ipfs/" + hash);
            updateProjectHash(projectDir, hash);
            return hash;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void syncProject(File dir) {
        try {
            if (dir.exists()) {
                return;
            }
            dir.mkdirs();
            String indexFileHash = readIndexFileHash(getProjectName(dir));
            if (StringUtils.isBlank(indexFileHash)) {
                return;
            }
            IndexFile indexFile = readIndexFileFromIpfs(getProjectName(dir));
            String hash = indexFile.getProjectHash();
            String urlIpfs = URL_IPFS;
            IPFS ipfs = new IPFS("/ip4/" + StringUtils.substringAfterLast(urlIpfs, "//") + "/tcp/5001");
            List<MerkleNode> ls = ipfs.ls(Multihash.fromBase58(hash));
            Map map = ipfs.file.ls(Multihash.fromBase58(hash));
            System.out.println(new ObjectMapper().writeValueAsString(map));
            List<Map> folder = new LinkedList<Map>();
            List<Map> files = new LinkedList<Map>();
            {
                folder.add(map);
            }
            Map temp = map;
            while (folder.size() > 0 && (temp = folder.remove(0)) != null) {
                String path = (String) temp.get("Path");
                path = path == null ? "" : path;
                if (temp.containsKey("Objects")) {// {Objects:{hash:{Type,Links:[{Type,Hash}]}}}
                    Map objs = (Map) temp.get("Objects");
                    for (Map.Entry entry : (Set<Map.Entry>) objs.entrySet()) {
                        Map folderMap = (Map) entry.getValue();
                        if ("Directory".equals(folderMap.get("Type"))) {
                            List<Map> links = (List<Map>) folderMap.get("Links");
                            for (Map m : links) {
                                m.put("Path", path + "/" + m.get("Name"));
                                if ("Directory".equals(m.get("Type"))) {
                                    folder.add(m);
                                    new File(dir, (String) m.get("Path")).mkdirs();
                                } else if ("File".equals(m.get("Type"))) {
                                    files.add(m);
                                }
                            }
                        } else if ("File".equals(folderMap.get("Type"))) {
                            files.add(folderMap);
                        }
                    }
                } else {// {Name,Hash,Size,Type}
                    Map hash1 = ipfs.file.ls(Multihash.fromBase58((String) temp.get("Hash")));
                    hash1.put("Path", path);
                    {
                        File f = new File(dir, (String) hash1.get("Path"));
                        f.getParentFile().mkdirs();
                    }
                    folder.add(hash1);
                }
            }
            for (Map file : files) {
                String fileName = (String) file.get("Path");
                File f = new File(dir, fileName);
                f.getParentFile().mkdirs();
                byte[] hashes = ipfs.cat(Multihash.fromBase58((String) file.get("Hash")));
                FileUtils.writeByteArrayToFile(f, hashes);
            }
            System.out.println(new ObjectMapper().writeValueAsString(files));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void updateProjectHash(File projectDir, String projectHash) {
        // update project hash to index file
        String indexFileHash = readIndexFileHash(getProjectName(projectDir));
        if (StringUtils.isBlank(indexFileHash)) {
            throw new RuntimeException("GitHelper IndexFile hash is blank!");
        }
        IndexFile indexFile = readIndexFileFromIpfs(getProjectName(projectDir));
        indexFile.setProjectHash(projectHash);
        indexFileHash = writeIndexFileToIpfs(indexFile);
        writeIndexFileHash(getProjectName(projectDir), indexFileHash);
    }

    public static String getProjectName(File dir) {
        if (dir.exists() && dir.isDirectory()) {
            String path = dir.getAbsolutePath();
            if (path.endsWith(".git")) {
                return dir.getParentFile().getName() + "/" + dir.getName();
            }
        }
        throw new RuntimeException("GitHelper can not get project name!");
    }

    public static Git open(File dir) {
        try {
            syncProject(dir);
            return MyGit.open(dir);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void initRepository(File dir) {
        try {//
            DecentralizedRepository.Builder builder = new DecentralizedRepository.Builder();
            DecentralizedRepository repository = builder.withPath(dir).build();
            repository.create(true);
            StoredConfig config = repository.getConfig();
            config.setBoolean("http", null, "receivepack", true);
            config.save();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getHashFromFile(String fileName) {
        String projectName = getProjectName(new File(fileName));
        IndexFile indexFile = readIndexFileFromIpfs(projectName);
        return indexFile.getProjectHash();
    }

    public static IndexFile readIndexFileFromIpfs(String repositoryName) {
        String urlIpfs = URL_IPFS;
        IPFS ipfs = new IPFS("/ip4/" + StringUtils.substringAfterLast(urlIpfs, "//") + "/tcp/5001");
        try {
            byte[] datas = ipfs.cat(Multihash.fromBase58(readIndexFileHash(repositoryName)));
            return IndexFile.fromFile(new HashedFile.FileWrapper("IndexFile.txt", new HashedFile.InputStreamCallback() {
                public InputStream call(HashedFile hashedFile) throws IOException {
                    return new ByteArrayInputStream(datas);
                }
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String writeIndexFileToIpfs(IndexFile indexFile) {
        String urlIpfs = URL_IPFS;
        IPFS ipfs = new IPFS("/ip4/" + StringUtils.substringAfterLast(urlIpfs, "//") + "/tcp/5001");
        try {
            NamedStreamable.ByteArrayWrapper file = new NamedStreamable.ByteArrayWrapper("IndexFile.txt",
                indexFile.getContent().getBytes("UTF-8"));
            List<MerkleNode> add = ipfs.add(file);
            return add.get(add.size() - 1).hash.toBase58();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeIndexFileHash(String repositoryName, String hash) {
        indexFileLock.writeLock().lock();
        try {
            String file = System.getProperty("HASH_FILE", "/Users/zhaochen/Desktop/IndexFileHash.txt");
            File hashFile = new File(file);
            if (!hashFile.exists()) {
                hashFile.createNewFile();
            }
            byte[] datas = (repositoryName + ":" + hash + ":").getBytes("UTF-8");
            byte[] line = new byte[100];
            RandomAccessFile raf = new RandomAccessFile(hashFile, "rw");
            long length = raf.length(), pos = 0;
            boolean found = false;
            while (length > pos) {
                raf.seek(pos);
                raf.read(line);// file pointer is change
                raf.seek(pos);
                pos += line.length;
                String str = new String(line, "UTF-8");
                if (!(str.startsWith(repositoryName) && str.charAt(repositoryName.length()) == ':')) {
                    continue;
                }
                if (datas.length > 100) {
                    throw new RuntimeException("GitHelper repository name and hash is too long (over 200)!");
                }
                Arrays.fill(line, (byte) ' ');
                line[line.length - 1] = '\n';
                System.arraycopy(datas, 0, line, 0, datas.length);
                raf.write(line);
                found = true;
                break;
            }
            if (!found) {
                Arrays.fill(line, (byte) ' ');
                line[line.length - 1] = '\n';
                System.arraycopy(datas, 0, line, 0, datas.length);
                raf.seek(raf.length());
                raf.write(line);
            }
            raf.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            indexFileLock.writeLock().unlock();
        }
    }

    public static String readIndexFileHash(String repositoryName) {
        indexFileLock.readLock().lock();
        try {
            String file = System.getProperty("HASH_FILE", "/Users/zhaochen/Desktop/IndexFileHash.txt");
            File hashFile = new File(file);
            if (!hashFile.exists()) {
                hashFile.createNewFile();
            }
            byte[] line = new byte[100];
            RandomAccessFile raf = new RandomAccessFile(hashFile, "r");
            long length = raf.length(), pos = 0;
            String hash = null;
            while (length > pos) {
                raf.seek(pos);
                raf.read(line);
                pos += line.length;
                String str = new String(line, "UTF-8");
                if (!(str.startsWith(repositoryName) && str.charAt(repositoryName.length()) == ':')) {
                    continue;
                }
                String[] split = StringUtils.split(str, ':');
                if (split.length > 2) {
                    hash = split[1];
                    break;
                }
            }
            raf.close();
            return hash;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            indexFileLock.readLock().unlock();
        }
    }
}
