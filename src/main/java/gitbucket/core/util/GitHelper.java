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

import gitbucket.core.util.Tuple.Two;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.internal.storage.file.LockFile;
import org.eclipse.jgit.internal.storage.file.PackFile;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.RefWriter;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.hitchain.hit.api.DecryptableFileWrapper;
import org.hitchain.hit.api.EncryptableFileWrapper;
import org.hitchain.hit.api.HashedFile;
import org.hitchain.hit.api.IndexFile;
import org.hitchain.hit.util.ECCUtil;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GitHelper
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-10-19 auto generate by qdp.
 */
public class GitHelper {

	public static final String URL_IPFS = System.getProperty("URL_IPFS", "http://121.40.127.45");
	private static final String rootPubKey = "04e86b10fae9a5df60b31f853f200cf6ed4a2eecdbbbf1f35a6e2053f1a0a23894eaa746565b689715c4f11b4db2e441c6746276014e1266f26f4e073c16762c7e";
	private static final String rootPriKey = "ce837abb3be97434f9bcb3e0e0be6c2f8c122ff40d5ced32d715ac7cce15383a";
	private static final String repoPubKey = "04e027cf70f2e8b083c04ef6a55ddcb8af9af19af36c9f0cb63a4c052ee4cae260c0c455c75ce33bf8401dad5092d6b6a97382cd1f807b536b273b46abe99d6e04";
	private static final String repoPriKey = "68f5f0a7c29ebd7c6b23ac0c45b90cf0dc0e357eba2b746bf153790ca253ec13";
	private static final ReentrantReadWriteLock indexFileLock = new ReentrantReadWriteLock();

	public static void main(String[] args) throws Exception {
		System.out.println(listGitFiles(new File("/Users/zhaochen/.gitbucket/repositories/root/test.git")));
		// addEncryptRepository();
		// syncProject(new
		// File("/Users/zhaochen/.gitbucket/repositories/root/test.git"));
		// IPFS ipfs = new IPFS("/ip4/121.40.127.45/tcp/5001");
		// NamedStreamable.FileWrapper dir = new NamedStreamable.FileWrapper(new
		// File("/Users/zhaochen/dev/workspace/testgit"));
		// List<MerkleNode> add = ipfs.add(dir);
		// for (MerkleNode mn : add) {
		// System.out.println("MerkleNode = " + mn);
		// System.out.println(mn.name + ", " + mn.hash);
		// }
	}

	private static void addEncryptRepository() {
		try {
			String projectName = "root/test.git";
			IndexFile indexFile = readIndexFileFromIpfs(projectName);
			indexFile.setOwnerPublicKey(rootPubKey);
			indexFile.setRepositoryPublicKey(repoPubKey);
			indexFile.setRepositoryPrivateKeyEncrypted(Hex.encodeHexString(ECCUtil.publicEncrypt(repoPriKey.getBytes(),
					ECCUtil.getPublicKeyFromEthereumPublicKeyHex(rootPubKey))));
			writeIndexFileHash(projectName, writeIndexFileToIpfs(indexFile));
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public static String updateProject2(File projectDir) {
		try {
			updateServerInfo(projectDir);
			String urlIpfs = URL_IPFS;
			IPFS ipfs = new IPFS("/ip4/" + StringUtils.substringAfterLast(urlIpfs, "//") + "/tcp/5001");
			// NamedStreamable.FileWrapper dir = new
			// NamedStreamable.FileWrapper(projectDir);
			IndexFile indexFile = readIndexFileFromIpfs(getProjectName(projectDir));
			EncryptableFileWrapper dir = new EncryptableFileWrapper(
					new HashedFile.FileSystemWrapper(projectDir.getAbsolutePath()), indexFile);
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
	
	public static String updateProject(File projectDir) {
		try {
			//updateServerInfo(projectDir);
			String urlIpfs = URL_IPFS;
			IndexFile indexFile = readIndexFileFromIpfs(getProjectName(projectDir));
			Map<String, File> current = listGitFiles(projectDir);
			Map<String, String> oldGitFileIndex = readGitFileIndexFromIpfs(getProjectName(projectDir));
			Two<Map<String, File>, Map<String, String>> tuple = diffGitFiles(current, oldGitFileIndex);
			Map<String, String> newGitFileIndexToIpfs = writeNewFileToIpfs(tuple.getFirst(), indexFile);
			Map<String, String> newGitFileIndex = generateNewGitFileIndex(current, oldGitFileIndex, newGitFileIndexToIpfs);
			String gitFileIndexHash = writeGitFileIndexToIpfs(newGitFileIndex);
			System.out.println("Project name: " + projectDir.getPath() + ", hash: " + urlIpfs + ":8080/ipfs/" + gitFileIndexHash);
			updateProjectHash(projectDir, gitFileIndexHash);
			return gitFileIndexHash;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * <pre>
	 * Git File Index:
	 * filehash,path/path2/filename
	 * </pre>
	 * 
	 * @param projectDir
	 * @return
	 */
	public static Map<String/* relativePath */, File> listGitFiles(File projectDir) {
		String basePath = projectDir.getAbsolutePath();
		Collection<File> files = FileUtils.listFiles(projectDir, null, true);
		Map<String, File> map = new HashMap<String, File>();
		for (File file : files) {
			if (file.isFile()) {
				map.put(file.getAbsolutePath().substring(basePath.length() + 1), file);
			}
		}
		return map;
	}

	public static void syncProjectByGitFileIndex(File dir) {
		Map<String, String> map = readGitFileIndexFromIpfs(getProjectName(dir));
		if (map == null || map.isEmpty()) {
			return;
		}
		IndexFile indexFile = readIndexFileFromIpfs(getProjectName(dir));
		String urlIpfs = URL_IPFS;
		IPFS ipfs = new IPFS("/ip4/" + StringUtils.substringAfterLast(urlIpfs, "//") + "/tcp/5001");
		for (Entry<String, String> entry : map.entrySet()) {
			String fileName = entry.getKey(), hash = entry.getValue();
			int len = "objects/xx".length();
			boolean isHashed = fileName.startsWith("objects") && fileName.length() > len && fileName.charAt(len) == '/';
			File f = new File(dir, fileName);
			if (isHashed && f.isFile()) {
				continue;// file already exist.
			}
			f.getParentFile().mkdirs();
			try {
				byte[] cat = ipfs.cat(Multihash.fromBase58(hash));
				DecryptableFileWrapper decryptableFile = new DecryptableFileWrapper(
						new HashedFile.FileWrapper(f.getAbsolutePath(), new HashedFile.InputStreamCallback() {
							public InputStream call(HashedFile hashedFile) throws IOException {
								return new ByteArrayInputStream(cat);
							}
						}), indexFile, "root", rootPriKey);
				FileUtils.writeByteArrayToFile(f, IOUtils.toByteArray(decryptableFile.getInputStream()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static Map<String/* filename */, String/* hash */> readGitFileIndexFromIpfs(String repositoryName) {
		String urlIpfs = URL_IPFS;
		IPFS ipfs = new IPFS("/ip4/" + StringUtils.substringAfterLast(urlIpfs, "//") + "/tcp/5001");
		try {
			IndexFile indexFile = readIndexFileFromIpfs(repositoryName);
			if ("init".equals(indexFile.getProjectHash())) {
				return new LinkedHashMap<String, String>();
			}
			byte[] contentWithCompress = ipfs.cat(Multihash.fromBase58(indexFile.getProjectHash()));
			return parseGitFilesIndex(contentWithCompress);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String writeGitFileIndexToIpfs(Map<String/* filename */, String/* hash */> gitFileHash) {
		String urlIpfs = URL_IPFS;
		IPFS ipfs = new IPFS("/ip4/" + StringUtils.substringAfterLast(urlIpfs, "//") + "/tcp/5001");
		try {
			NamedStreamable.ByteArrayWrapper file = new NamedStreamable.ByteArrayWrapper("GitFileIndex.gz",
					toGitFileIndexWithCompress(gitFileHash));
			List<MerkleNode> add = ipfs.add(file);
			return add.get(add.size() - 1).hash.toBase58();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Map<String/* filename */, String/* hash */> generateNewGitFileIndex(
			Map<String/* relativePath */, File> current, Map<String/* filename */, String/* hash */> oldGitFileIndex,
			Map<String/* filename */, String/* hash */> newGitFileIndex) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (Entry<String, File> entry : current.entrySet()) {
			String key = entry.getKey();
			map.put(key, StringUtils.defaultString(newGitFileIndex.get(key), oldGitFileIndex.get(key)));
		}
		return map;
	}

	public static Map<String/* filename */, String/* hash */> writeNewFileToIpfs(
			Map<String/* relativePath */, File> newGitFile, IndexFile indexFile) {
		Map<String, String> map = new HashMap<String, String>();
		String urlIpfs = URL_IPFS;
		IPFS ipfs = new IPFS("/ip4/" + StringUtils.substringAfterLast(urlIpfs, "//") + "/tcp/5001");
		try {
			List<NamedStreamable> files = new ArrayList<NamedStreamable>();
			for (Entry<String, File> entry : newGitFile.entrySet()) {
				ByteArrayInputStream bais = new ByteArrayInputStream(FileUtils.readFileToByteArray(entry.getValue()));
				EncryptableFileWrapper dir = new EncryptableFileWrapper(
						new HashedFile.FileWrapper(entry.getKey(), new HashedFile.InputStreamCallback() {
							public InputStream call(HashedFile hashedFile) throws IOException {
								return bais;
							}
						}), indexFile);
				files.add(dir);
			}
			List<MerkleNode> add = ipfs.add(files, false, false);
			Map<String, String> hashMap = new HashMap<String, String>();
			for (MerkleNode mn : add) {
				hashMap.put(mn.name.get(), mn.hash.toBase58());
			}
			for (Entry<String, File> entry : newGitFile.entrySet()) {
				map.put(entry.getKey(), hashMap.get(entry.getKey()));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return map;
	}

	public static Tuple.Two<Map<String, File>, Map<String, String>> diffGitFiles(
			Map<String/* relativePath */, File> current, Map<String/* filename */, String/* hash */> gitFileIndex) {
		Map<String, File> fileAdd = new HashMap<String, File>();
		Map<String, String> fileRemove = new HashMap<String, String>();
		for (Entry<String, File> entry : current.entrySet()) {
			if (!gitFileIndex.containsKey(entry.getKey())) {
				fileAdd.put(entry.getKey(), entry.getValue());
			}
		}
		for (Entry<String, String> entry : gitFileIndex.entrySet()) {
			if (!current.containsKey(entry.getKey())) {
				fileRemove.put(entry.getKey(), entry.getValue());
			}
		}
		return new Tuple.Two<Map<String, File>, Map<String, String>>(fileAdd, fileRemove);
	}

	public static byte[] toGitFileIndexWithCompress(Map<String/* filename */, String/* hash */> gitFileIndex) {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, String> entry : gitFileIndex.entrySet()) {
			sb.append(entry.getValue()).append(',').append(entry.getKey()).append('\n');
		}
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			GZIPOutputStream gzip = new GZIPOutputStream(out);
			gzip.write(sb.toString().getBytes("UTF-8"));
			gzip.close();
			return out.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Map<String/* filename */, String/* hash */> parseGitFilesIndex(byte[] contentWithCompress) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		if (contentWithCompress == null || contentWithCompress.length == 0) {
			return map;
		}
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ByteArrayInputStream in = new ByteArrayInputStream(contentWithCompress);
			GZIPInputStream ungzip = new GZIPInputStream(in);
			IOUtils.copy(ungzip, out);
			ungzip.close();
			String index = new String(out.toByteArray(), "UTF-8");
			String[] lines = StringUtils.split(index, '\n');
			for (String line : lines) {
				int indexOf = line.indexOf(',');
				if (indexOf < 0) {
					continue;
				}
				String fileHash = line.substring(0, indexOf), fileName = line.substring(indexOf + 1);
				map.put(fileName, fileHash);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return map;
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
				DecryptableFileWrapper decryptableFile = new DecryptableFileWrapper(
						new HashedFile.FileWrapper(f.getAbsolutePath(), new HashedFile.InputStreamCallback() {
							public InputStream call(HashedFile hashedFile) throws IOException {
								return new ByteArrayInputStream(
										ipfs.cat(Multihash.fromBase58((String) file.get("Hash"))));
							}
						}), indexFile, "root", rootPriKey);
				FileUtils.writeByteArrayToFile(f, IOUtils.toByteArray(decryptableFile.getInputStream()));
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
			String hash = readIndexFileHash(repositoryName);
			byte[] datas = StringUtils.isNotBlank(hash) ? ipfs.cat(Multihash.fromBase58(hash)) : null;
			if (datas != null) {
				return IndexFile
						.fromFile(new HashedFile.FileWrapper("IndexFile.txt", new HashedFile.InputStreamCallback() {
							public InputStream call(HashedFile hashedFile) throws IOException {
								return new ByteArrayInputStream(datas);
							}
						}));
			}
			// is new project
			IndexFile indexFile = new IndexFile();
			indexFile.setProjectName(repositoryName);
			indexFile.setProjectHash("init");
			indexFile.setOwner("root");
			indexFile.setOwnerPublicKey(rootPubKey);
			indexFile.setRepositoryPublicKey(repoPubKey);
			indexFile.setRepositoryPrivateKeyEncrypted(Hex.encodeHexString(ECCUtil.publicEncrypt(repoPriKey.getBytes(),
					ECCUtil.getPublicKeyFromEthereumPublicKeyHex(rootPubKey))));
			hash = writeIndexFileToIpfs(indexFile);
			writeIndexFileHash(repositoryName, hash);
			byte[] newDatas = ipfs.cat(Multihash.fromBase58(hash));
			return IndexFile.fromFile(new HashedFile.FileWrapper("IndexFile.txt", new HashedFile.InputStreamCallback() {
				public InputStream call(HashedFile hashedFile) throws IOException {
					return new ByteArrayInputStream(newDatas);
				}
			}));
		} catch (Exception e) {
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
