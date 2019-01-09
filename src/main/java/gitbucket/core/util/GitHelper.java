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
import org.apache.commons.codec.digest.DigestUtils;
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
import org.hitchain.hit.api.HashedFile.ByteArrayInputStreamCallback;
import org.hitchain.hit.api.IndexFile;
import org.hitchain.hit.util.ByteUtils;
import org.hitchain.hit.util.ECCUtil;
import org.hitchain.hit.util.RSAUtil;

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
	private static final String rootPubKeyRsa = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCOg6USlEHh5LRb/hCerCZWk5JxzWqNQWMGMNMhqihII9YfFwcjaECc+BJQ0b+49g8TaXk6zuIM1AQpjQ4mvUZY7u/rPsUc37mCSyJCUhgJrFJp4HyIkxmJOhy1Ow+ttBT0/zIWXXq70WIstRtOjn5xGPJ8XjbPGHHZ1hyDu6ti2wIDAQAB";
	private static final String rootPriKeyRsa = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAI6DpRKUQeHktFv+EJ6sJlaTknHNao1BYwYw0yGqKEgj1h8XByNoQJz4ElDRv7j2DxNpeTrO4gzUBCmNDia9Rlju7+s+xRzfuYJLIkJSGAmsUmngfIiTGYk6HLU7D620FPT/MhZdervRYiy1G06OfnEY8nxeNs8YcdnWHIO7q2LbAgMBAAECgYAjOPMj7+OGlpVjBRyLcuW40RlJKilTBx2XyppsABebmMvTfEgF3r7VbNRuCCEX8CySsidFuxsQa5gpwtSEC5SmiNa7Rvg+Pu0TYoG90yQX5f9/oRWnZ4eHQCEbHVu0jbcC/Vr+a2Jc77CgIK2mZO6S8R2bYXzq9QZ2Ykt4+l3qrQJBAMSZIEofd4CX1lFsHnU/Fi71tDC2UheffprcwssCWMhGmVuMCDbigY9RkM+Zkyh5s1YiShwlMt62qUBgVJf8am0CQQC5kyBOj5Q0bfMCYOQdjdZyXRI7HyWvzMieR7IKij1DLreeNkdTsoefTAvUePuc0IwrizEwk3DHWjRTnBBy5DVnAkBHoRr4prpdqfS2OdRnF5M3jOIYFXWXkc8JEYIPgU1juwVJK54akTBvTWKboPLS/nRu35Ns6ci9CIRmJjLsJVWJAkBlzfnOP2Qtsxe6eU8Li5FWogprVrYFEJIKiwh4Ucg0AAAJkntkxi8yy7Q9trVKHPqYtL6iiHA5XRoRuC8p6FoHAkBqTdHMZKQi/VbCHJsMhpz2hXGVe8aEUo72aTQFgjx0GX4CLKjDav+LghMfVuVsbV4DZWh8kRlZQtPLU0gNQwNg";
	private static final String repoPubKey = "04e027cf70f2e8b083c04ef6a55ddcb8af9af19af36c9f0cb63a4c052ee4cae260c0c455c75ce33bf8401dad5092d6b6a97382cd1f807b536b273b46abe99d6e04";
	private static final String repoPriKey = "68f5f0a7c29ebd7c6b23ac0c45b90cf0dc0e357eba2b746bf153790ca253ec13";
	private static final ReentrantReadWriteLock indexFileLock = new ReentrantReadWriteLock();

	public static void main(String[] args) throws Exception {
		// System.out.println(listGitFiles(new
		// File("/Users/zhaochen/.gitbucket/repositories/root/test.git")));
		System.out.println(new Multihash(Multihash.Type.sha1, DigestUtils.sha1("hello")).toBase58());
		System.out.println(Hex.encodeHexString(DigestUtils.sha1("hello")));
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

	// public static String updateProject2(File projectDir) {
	// try {
	// updateServerInfo(projectDir);
	// String urlIpfs = URL_IPFS;
	// IPFS ipfs = new IPFS("/ip4/" + StringUtils.substringAfterLast(urlIpfs, "//")
	// + "/tcp/5001");
	// // NamedStreamable.FileWrapper dir = new
	// // NamedStreamable.FileWrapper(projectDir);
	// IndexFile indexFile = readIndexFileFromIpfs(getProjectName(projectDir));
	// EncryptableFileWrapper dir = new EncryptableFileWrapper(
	// new HashedFile.FileSystemWrapper(projectDir.getAbsolutePath()), indexFile);
	// List<MerkleNode> add = ipfs.add(dir);
	// String hash = add.get(add.size() - 1).hash.toBase58();
	// System.out.println("Project name: " + projectDir.getPath() + ", hash: " +
	// urlIpfs + ":8080/ipfs/" + hash);
	// updateProjectHash(projectDir, hash);
	// return hash;
	// } catch (Exception e) {
	// e.printStackTrace();
	// throw new RuntimeException(e);
	// }
	// }

	public static String updateProject(File projectDir, String indexHash) {
		try {
			System.out.println("==indexHash from db==" + indexHash);
			// updateServerInfo(projectDir);
			String urlIpfs = URL_IPFS;
			IPFS ipfs = getIpfs();
			IndexFile oldIndexFile = readIndexFileFromIpfs(projectDir);
			Map<String, Two<String/* ipfs hash */, String/* sha1 */>> oldGitFileIndex = readGitFileIndexFromIpfs(
					projectDir);
			for (Entry<String, Two<String, String>> entry : oldGitFileIndex.entrySet()) {
				System.out.println("OLD:" + entry.getKey());
			}
			// #1. list all current file
			Map<String, File> current = listGitFiles(projectDir);
			for (Entry<String, File> entry : current.entrySet()) {
				System.out.println("CURR:" + entry.getKey());
			}
			// #2. IndexFile changed by owner: add team member or change the key
			// TODO
			// #3. get the change files
			Two<Map<String, File>, Map<String, Two<String/* ipfs hash */, String/* sha1 */>>> tuple = diffGitFiles(
					current, oldGitFileIndex);
			// #4. write changed file to ipfs
			Map<String, Two<String/* ipfs hash */, String/* sha1 */>> newGitFileIndexToIpfs = writeNewFileToIpfs(
					tuple.getFirst(), oldIndexFile, ipfs);
			for (Entry<String, Two<String, String>> entry : newGitFileIndexToIpfs.entrySet()) {
				System.out.println("ADD:" + entry.getKey());
			}
			// #5. generate the new git file index
			Map<String, Two<String/* ipfs hash */, String/* sha1 */>> newGitFileIndex = generateNewGitFileIndex(current,
					oldGitFileIndex, newGitFileIndexToIpfs);
			for (Entry<String, Two<String, String>> entry : newGitFileIndex.entrySet()) {
				System.out.println("NEW:" + entry.getKey() + ", ipfsHash:" + entry.getValue().getFirst() + ", sha1:"
						+ entry.getValue().getSecond());
			}
			// #6. write the new git file index to disk and ipfs
			String gitFileIndexHash = writeGitFileIndexToIpfs(projectDir, newGitFileIndex);
			System.out.println("Project name0: " + projectDir.getPath() + ", gitFileHash: " + urlIpfs + ":8080/ipfs/"
					+ gitFileIndexHash);
			String newProjectHash = updateProjectHash(projectDir, gitFileIndexHash);
			// #7. update gitFileIndexHash to newGitFileIndex
			{
				Two<String, String> two = newGitFileIndex.get("objects/pack/gitfile.idx");
				two = two == null ? new Two<>() : two;
				two.setFirst(gitFileIndexHash);
				two.setSecond(sha1(new File(projectDir, "objects/pack/gitfile.idx")));
				newGitFileIndex.put("objects/pack/gitfile.idx", two);
			}
			{
				Two<String, String> two = newGitFileIndex.get("objects/info/projecthash");
				two = two == null ? new Two<>() : two;
				two.setFirst(newProjectHash);
				two.setSecond(sha1(new File(projectDir, "objects/info/projecthash")));
				newGitFileIndex.put("objects/info/projecthash", two);
			}
			gitFileIndexHash = writeGitFileIndexToIpfs(projectDir, newGitFileIndex);
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

	public static void syncProjectByGitFileIndex(File projectDir) {
		if (!projectDir.getAbsolutePath().startsWith(Directory.getGitBucketHome())) {
			return;
		}
		if (projectDir.exists()) {
			return;
		}
		IPFS ipfs = getIpfs();
		Map<String/* filename */, Two<String/* ipfs hash */, String/* sha1 */>> map = readGitFileIndexFromIpfs(
				projectDir);
		if (map == null || map.isEmpty()) {
			return;
		}
		IndexFile indexFile = readIndexFileFromIpfs(projectDir);
		for (Entry<String, Two<String, String>> entry : map.entrySet()) {
			String fileName = entry.getKey(), ipfsHash = entry.getValue().getFirst(),
					sha1 = entry.getValue().getSecond();
			boolean isHashed = fileName
					.startsWith("objects/")/* && fileName.length() > len && fileName.charAt(len) == '/' */;
			File f = new File(projectDir, fileName);
			if (isHashed && f.exists()) {
				continue;// file already exist.
			}
			if (f.exists() && sha1(f).equals(sha1)) {
				continue;// file already exist.
			}
			f.getParentFile().mkdirs();
			try {
				byte[] cat = ipfs.cat(Multihash.fromBase58(ipfsHash));
				DecryptableFileWrapper decryptableFile = new DecryptableFileWrapper(
						new HashedFile.FileWrapper(f.getAbsolutePath(),
								new HashedFile.ByteArrayInputStreamCallback(cat)),
						indexFile, "root", rootPriKey);
				FileUtils.writeByteArrayToFile(f, IOUtils.toByteArray(decryptableFile.getInputStream()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static IPFS getIpfs() {
		String urlIpfs = URL_IPFS;
		IPFS ipfs = new IPFS("/ip4/" + StringUtils.substringAfterLast(urlIpfs, "//") + "/tcp/5001");
		return ipfs;
	}

	public static Map<String/* filename */, Two<String/* ipfs hash */, String/* sha1 */>> readGitFileIndexFromIpfs(
			File projectDir) {
		try {
			File gitFileIndex = new File(projectDir, "objects/pack/gitfile.idx");
			if(!gitFileIndex.exists()) {
				return parseGitFilesIndex(null);
			}
			byte[] contentWithCompress = FileUtils.readFileToByteArray(gitFileIndex);
			return parseGitFilesIndex(contentWithCompress);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String writeGitFileIndexToIpfs(File projectDir,
			Map<String/* filename */, Two<String/* ipfs hash */, String/* sha1 */>> gitFileHash) {
		String urlIpfs = URL_IPFS;
		IPFS ipfs = new IPFS("/ip4/" + StringUtils.substringAfterLast(urlIpfs, "//") + "/tcp/5001");
		try {
			byte[] gitFileIndexWithCompress = toGitFileIndexWithCompress(gitFileHash);
			File gitFileIndex = new File(projectDir, "objects/pack/gitfile.idx");
			writeUpdateFile(gitFileIndex, gitFileIndexWithCompress);
			NamedStreamable.ByteArrayWrapper file = new NamedStreamable.ByteArrayWrapper("gitfile.idx",
					gitFileIndexWithCompress);
			List<MerkleNode> add = ipfs.add(file);
			return add.get(add.size() - 1).hash.toBase58();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Map<String/* filename */, Two<String/* ipfs hash */, String/* sha1 */>> generateNewGitFileIndex(
			Map<String/* relativePath */, File> current,
			Map<String/* filename */, Two<String/* ipfs hash */, String/* sha1 */>> oldGitFileIndex,
			Map<String/* filename */, Two<String/* ipfs hash */, String/* sha1 */>> newGitFileIndex) {
		Map<String, Two<String, String>> map = new LinkedHashMap<String, Two<String, String>>();
		for (Entry<String, File> entry : current.entrySet()) {
			String key = entry.getKey();
			Two<String, String> twoNew = newGitFileIndex.get(key);
			Two<String, String> twoOld = oldGitFileIndex.get(key);
			String ipfsHash = StringUtils.defaultString(twoNew == null ? null : twoNew.getFirst(),
					twoOld == null ? null : twoOld.getFirst());
			String sha1 = StringUtils.defaultString(twoNew == null ? null : twoNew.getSecond(),
					twoOld == null ? null : twoOld.getSecond());
			map.put(key, new Two<String, String>(ipfsHash, sha1));
		}
		return map;
	}

	public static Map<String/* filename */, Two<String/* ipfs hash */, String/* sha1 */>> writeNewFileToIpfs(
			Map<String/* relativePath */, File> newGitFile, IndexFile indexFile, IPFS ipfs) {
		Map<String, Two<String, String>> map = new HashMap<String, Two<String, String>>();
		Map<String, Two<String, String>> hashMap = new HashMap<String, Two<String, String>>();
		for (Entry<String, File> entry : newGitFile.entrySet()) {
			try {
				ByteArrayInputStream bais = new ByteArrayInputStream(FileUtils.readFileToByteArray(entry.getValue()));
				EncryptableFileWrapper file = new EncryptableFileWrapper(
						new HashedFile.FileWrapper(entry.getKey(), new HashedFile.ByteArrayInputStreamCallback(bais)),
						indexFile);
				List<MerkleNode> add = ipfs.add(file);
				hashMap.put(entry.getKey(),
						new Two<String, String>(add.get(add.size() - 1).hash.toBase58(), sha1(entry.getValue())));
				System.out.println(entry.getKey() + "==" + add.get(add.size() - 1).hash.toBase58());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		for (Entry<String, File> entry : newGitFile.entrySet()) {
			map.put(entry.getKey(), hashMap.get(entry.getKey()));
		}
		return map;
	}

	public static Tuple.Two<Map<String, File>, Map<String, Two<String/* ipfs hash */, String/* sha1 */>>> diffGitFiles(
			Map<String/* relativePath */, File> current,
			Map<String/* filename */, Two<String/* ipfs hash */, String/* sha1 */>> gitFileIndex) {
		Map<String, File> fileAdd = new HashMap<String, File>();
		Map<String, Two<String, String>> fileRemove = new HashMap<String, Two<String, String>>();
		for (Entry<String, File> entry : current.entrySet()) {
			String key = entry.getKey();
			File file = entry.getValue();
			if (!key.startsWith("objects/")) {// is not the hash object, so hash the file and compare the hash.
				String sha1 = sha1(file);
				Two<String, String> two = gitFileIndex.get(key);
				if (two == null || !sha1.equals(two.getSecond())) {
					fileAdd.put(key, file);
				}
				continue;
			}
			if (!gitFileIndex.containsKey(key)) {
				fileAdd.put(key, file);
			}
		}
		for (Entry<String, Two<String, String>> entry : gitFileIndex.entrySet()) {
			if (!current.containsKey(entry.getKey())) {
				fileRemove.put(entry.getKey(), entry.getValue());
			}
		}
		return new Tuple.Two<Map<String, File>, Map<String, Two<String, String>>>(fileAdd, fileRemove);
	}

	public static String sha1(File file) {
		FileInputStream is = null;
		try {
			is = new FileInputStream(file);
			return Hex.encodeHexString(DigestUtils.sha1(is));
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				is.close();
			} catch (Exception e) {
			}
		}
	}

	public static byte[] toGitFileIndexWithCompress(
			Map<String/* filename */, Two<String/* ipfs hash */, String/* sha1 */>> gitFileIndex) {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, Two<String, String>> entry : gitFileIndex.entrySet()) {
			sb.append(entry.getValue().getFirst()).append(',').append(entry.getValue().getSecond()).append(',')
					.append(entry.getKey()).append('\n');
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

	public static Map<String/* filename */, Two<String/* ipfs hash */, String/* sha1 */>> parseGitFilesIndex(
			byte[] contentWithCompress) {
		Map<String, Two<String, String>> map = new LinkedHashMap<String, Two<String, String>>();
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
				String[] nameIpfsSha = StringUtils.split(line, ',');
				if (nameIpfsSha.length != 3) {
					continue;
				}
				map.put(nameIpfsSha[2], new Two<String, String>(nameIpfsSha[0], nameIpfsSha[1]));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return map;
	}

	public static String updateProjectHash(File projectDir, String projectHash) {
		String urlIpfs = URL_IPFS;
		IPFS ipfs = new IPFS("/ip4/" + StringUtils.substringAfterLast(urlIpfs, "//") + "/tcp/5001");
		try {
			// update project hash to objects/info/projecthash file
			File projectHashFile = new File(projectDir, "objects/info/projecthash");
			writeUpdateFile(projectHashFile, ByteUtils.utf8(projectHash));
			NamedStreamable.ByteArrayWrapper file = new NamedStreamable.ByteArrayWrapper("projecthash",
					ByteUtils.utf8(projectHash));
			List<MerkleNode> add = ipfs.add(file);
			return add.get(add.size() - 1).hash.toBase58();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String readProjectHash(File projectDir) {
		File projectHashFile = new File(projectDir, "objects/info/projecthash");
		if (!projectHashFile.exists()) {
			return null;
		}
		try {
			return FileUtils.readFileToString(projectHashFile, "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String getProjectName(File dir) {
		String path = dir.getAbsolutePath();
		if (path.endsWith(".git")) {
			return dir.getParentFile().getName() + "/" + dir.getName();
		}
		throw new RuntimeException("GitHelper can not get project name !");
	}

	public static Git open(File dir) {
		try {
			// syncProject(dir);
			syncProjectByGitFileIndex(dir);
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
		return readProjectHash(new File(fileName));
	}

	public static IndexFile readIndexFileFromIpfs(File projectDir) {
		try {
			File file = new File(projectDir, "objects/info/projectinfo");
			if (!file.exists()) {
				// is new project
				IndexFile indexFile = new IndexFile();
				indexFile.setVersion("1");
				indexFile.setProjectName(getProjectName(projectDir));
				indexFile.setOwner("root");
				indexFile.setOwnerPublicKey(rootPubKeyRsa);
				indexFile.setRepositoryPublicKey(repoPubKey);
				indexFile.setRepositoryPrivateKeyEncrypted(Hex.encodeHexString(RSAUtil.encrypt(repoPriKey.getBytes(), RSAUtil.getPrivateKeyFromBase64(rootPriKeyRsa))));
				writeUpdateFile(file, ByteUtils.utf8(indexFile.getSignedContent(rootPriKeyRsa)));
			}
			byte[] content = FileUtils.readFileToByteArray(file);
			return IndexFile.fromFile(
					new HashedFile.FileWrapper("objects/info/projectinfo", new HashedFile.InputStreamCallback() {
						public InputStream call(HashedFile hashedFile) throws IOException {
							return new ByteArrayInputStream(content);
						}
					}));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void writeIndexFileToIpfs(File projectDir, IndexFile indexFile) {
		writeUpdateFile(new File(projectDir, "objects/info/projectinfo"),
				ByteUtils.utf8(indexFile.getSignedContent(rootPriKeyRsa)));
	}

	// public static void writeIndexFileHash(String repositoryName, String hash) {
	// indexFileLock.writeLock().lock();
	// try {
	// String file = System.getProperty("HASH_FILE",
	// "/Users/zhaochen/Desktop/IndexFileHash.txt");
	// File hashFile = new File(file);
	// if (!hashFile.exists()) {
	// hashFile.createNewFile();
	// }
	// byte[] datas = (repositoryName + ":" + hash + ":").getBytes("UTF-8");
	// byte[] line = new byte[100];
	// RandomAccessFile raf = new RandomAccessFile(hashFile, "rw");
	// long length = raf.length(), pos = 0;
	// boolean found = false;
	// while (length > pos) {
	// raf.seek(pos);
	// raf.read(line);// file pointer is change
	// raf.seek(pos);
	// pos += line.length;
	// String str = new String(line, "UTF-8");
	// if (!(str.startsWith(repositoryName) && str.charAt(repositoryName.length())
	// == ':')) {
	// continue;
	// }
	// if (datas.length > 100) {
	// throw new RuntimeException("GitHelper repository name and hash is too long
	// (over 200)!");
	// }
	// Arrays.fill(line, (byte) ' ');
	// line[line.length - 1] = '\n';
	// System.arraycopy(datas, 0, line, 0, datas.length);
	// raf.write(line);
	// found = true;
	// break;
	// }
	// if (!found) {
	// Arrays.fill(line, (byte) ' ');
	// line[line.length - 1] = '\n';
	// System.arraycopy(datas, 0, line, 0, datas.length);
	// raf.seek(raf.length());
	// raf.write(line);
	// }
	// raf.close();
	// } catch (Exception e) {
	// throw new RuntimeException(e);
	// } finally {
	// indexFileLock.writeLock().unlock();
	// }
	// }

	// public static String readIndexFileHash(String repositoryName) {
	// indexFileLock.readLock().lock();
	// try {
	// String file = System.getProperty("HASH_FILE",
	// "/Users/zhaochen/Desktop/IndexFileHash.txt");
	// File hashFile = new File(file);
	// if (!hashFile.exists()) {
	// hashFile.createNewFile();
	// }
	// byte[] line = new byte[100];
	// RandomAccessFile raf = new RandomAccessFile(hashFile, "r");
	// long length = raf.length(), pos = 0;
	// String hash = null;
	// while (length > pos) {
	// raf.seek(pos);
	// raf.read(line);
	// pos += line.length;
	// String str = new String(line, "UTF-8");
	// if (!(str.startsWith(repositoryName) && str.charAt(repositoryName.length())
	// == ':')) {
	// continue;
	// }
	// String[] split = StringUtils.split(str, ':');
	// if (split.length > 2) {
	// hash = split[1];
	// break;
	// }
	// }
	// raf.close();
	// return hash;
	// } catch (Exception e) {
	// throw new RuntimeException(e);
	// } finally {
	// indexFileLock.readLock().unlock();
	// }
	// }
}
