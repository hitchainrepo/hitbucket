/*******************************************************************************
 * Copyright (c) 2018-10-19 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform.
 ******************************************************************************/
package gitbucket.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
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
import org.hitchain.hit.api.ProjectInfoFile;
import org.hitchain.hit.util.ByteUtils;
import org.hitchain.hit.util.ECKey;
import org.hitchain.hit.util.EthereumUtils;
import org.hitchain.hit.util.RSAUtil;

import gitbucket.core.util.Tuple.Two;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;

/**
 * GitHelper
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-10-19 auto generate by qdp.
 */
public class GitHelper {

	public static final String URL_IPFS = System.getProperty("URL_IPFS", "121.40.127.45"/*"http://121.40.127.45"*/);
	public static final String URL_ETHER = System.getProperty("URL_ETHER", "https://localhost:1443");
	private static final String rootPubKeyEcc = "0x837a4bbef0f7235b8fdb03c55d0d98f27f49cda8";
	private static final String rootPriKeyEcc = "448b60044aec0065a08115d7af1038491830f697c36118e046e38cf7002ee45b";
	private static final String rootPubKeyRsa = "30819f300d06092a864886f70d010101050003818d0030818902818100df6c814a1b827317370607e207a8749f12497d4ea339cd4f4a38df3690c9d24eb279852780105ed4f7a493833b0ed27409b74eb58b1a452a66be052146ee1f5fb0fa42231221f22cd73e70026b606862b91365fdbe6b2af79838eaa38db60dddc01ecf78f6881880ad399e65747fe86f5e844f5cd4b40f6de8c3e8e60db343290203010001";
	private static final String rootPriKeyRsa = "30820275020100300d06092a864886f70d01010105000482025f3082025b02010002818100df6c814a1b827317370607e207a8749f12497d4ea339cd4f4a38df3690c9d24eb279852780105ed4f7a493833b0ed27409b74eb58b1a452a66be052146ee1f5fb0fa42231221f22cd73e70026b606862b91365fdbe6b2af79838eaa38db60dddc01ecf78f6881880ad399e65747fe86f5e844f5cd4b40f6de8c3e8e60db343290203010001028180549653eca6b5a0b52d53cf30380e02f926874435bd7e68c8982527fd149c144f4f2acacac5a56d01dc3026d90c46f44e924f2031835492d316cae24e52f85c4fbd1a340b7b4f60f758631f16955d8503a154858f129a0d66268b9a929caaa1e1ff944c861a13c28e2d0869a93f8ffc508450339856de7869b8dbcce66dbde7b1024100f63fb36a8067288e16e2d63b4dda45bf7e8ebf00e34ac4514d169cfe50aba01a1d4785fe38226893814bda6c49a7888aa4a9a108045ac2db9c65ffecdefb5eeb024100e8456b9fb09c93280b3cf1364b00b997bb3293b7f95dba8cd48bd1ef734c64cd51cdb6140948a058f9588b9f4495ce88ba5790e663e711f730f296c7f602693b02407c498e8ef49c1c960aeb16e1fbdb6d54c7d5d885e432ba7fa67f016242e93cf7b14b864fd799565b0ce9722731cdc356e6e14f0bb2d6f47ecfa393d6c47cef5d02401a5b8e5bffc1b4dd4d712bfa3a46a9c8f320492d0e6a397a33c06e215b172735397c3b96487b6a5ece64e2eb3ef03510c4fc9cdfd82467a0827874edda17e9f3024002afe2f48990647b96526c6f2ddfee427a21fafd02ad982981425372587c14f742f1c1133a0f34084436cf78b0a55484fbb547f20077b937da7e5569f63a5ad9";

	public static void main(String[] args) throws Exception {
		// System.out.println(listGitFiles(new
		// File("/Users/zhaochen/.gitbucket/repositories/root/test.git")));
		System.out.println(new Multihash(Multihash.Type.sha1, DigestUtils.sha1("hello")).toBase58());
		System.out.println(Hex.toHexString(DigestUtils.sha1("hello")));
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

	public static String updateProject(File projectDir, String indexHash) {
		try {
			System.out.println("==indexHash from db==" + indexHash);
			// updateServerInfo(projectDir);
			String urlIpfs = URL_IPFS;
			IPFS ipfs = getIpfs();
			ProjectInfoFile projectInfoFile = readProjectInfoFile(projectDir);
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
			// #2. ProjectInfoFile changed by owner: add team member or change the key
			// TODO
			// #3. get the change files
			Two<Map<String, File>, Map<String, Two<String/* ipfs hash */, String/* sha1 */>>> tuple = diffGitFiles(
					current, oldGitFileIndex);
			// #4. write changed file to ipfs
			Map<String, Two<String/* ipfs hash */, String/* sha1 */>> newGitFileIndexToIpfs = writeNewFileToIpfs(
					tuple.getFirst(), projectInfoFile, ipfs);
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
			updateProjectAddress(projectInfoFile, gitFileIndexHash);
			// #7. update gitFileIndexHash to newGitFileIndex
			{
				Two<String, String> two = newGitFileIndex.get("objects/pack/gitfile.idx");
				two = two == null ? new Two<>() : two;
				two.setFirst(gitFileIndexHash);
				two.setSecond(sha1(new File(projectDir, "objects/pack/gitfile.idx")));
				newGitFileIndex.put("objects/pack/gitfile.idx", two);
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
		ProjectInfoFile indexFile = readProjectInfoFile(projectDir);
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
						indexFile, "root", rootPriKeyEcc);
				FileUtils.writeByteArrayToFile(f, IOUtils.toByteArray(decryptableFile.getInputStream()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static IPFS getIpfs() {
		String urlIpfs = URL_IPFS;
		IPFS ipfs = new IPFS(urlIpfs, 5001, "/api/v0/", false) ;
		return ipfs;
	}

	public static Map<String/* filename */, Two<String/* ipfs hash */, String/* sha1 */>> readGitFileIndexFromIpfs(
			File projectDir) {
		try {
			File gitFileIndex = new File(projectDir, "objects/pack/gitfile.idx");
			if (!gitFileIndex.exists()) {
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
		IPFS ipfs = getIpfs();
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
			Map<String/* relativePath */, File> newGitFile, ProjectInfoFile projectInfoFile, IPFS ipfs) {
		Map<String, Two<String, String>> map = new HashMap<String, Two<String, String>>();
		Map<String, Two<String, String>> hashMap = new HashMap<String, Two<String, String>>();
		for (Entry<String, File> entry : newGitFile.entrySet()) {
			try {
				ByteArrayInputStream bais = new ByteArrayInputStream(FileUtils.readFileToByteArray(entry.getValue()));
				EncryptableFileWrapper file = new EncryptableFileWrapper(
						new HashedFile.FileWrapper(entry.getKey(), new HashedFile.ByteArrayInputStreamCallback(bais)),
						projectInfoFile);
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
			return Hex.toHexString(DigestUtils.sha1(is));
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

	public static void updateProjectAddress(ProjectInfoFile projectInfoFile, String newProjectAddress) {
		EthereumUtils.updateProjectAddress(URL_ETHER, projectInfoFile.getRepoAddress(),
				EthereumUtils.encryptPriKeyEcc(URL_ETHER, rootPriKeyEcc), newProjectAddress);
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

	public static String getProjectOwner(File dir) {
		String path = dir.getAbsolutePath();
		if (path.endsWith(".git")) {
			return dir.getParentFile().getName();
		}
		throw new RuntimeException("GitHelper can not get project owner !");
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
		return EthereumUtils.getProjectAddress(URL_ETHER, readProjectInfoFile(new File(fileName)).getRepoAddress());
		//return readProjectHash(new File(fileName));
	}

	public static ProjectInfoFile readProjectInfoFile(File projectDir) {
		try {
			File file = new File(projectDir, "objects/info/projectinfo");
			if (!file.exists()) {
				// is new project
				ProjectInfoFile info = new ProjectInfoFile();
				{
					info.setVersion("1");
					info.setEthereumUrl("https://localhost:1443");
					info.setFileServerUrl(URL_IPFS);
					info.setRepoName(getProjectName(projectDir));
					ECKey repoKeyPair = new ECKey();
					info.setRepoPubKey(Hex.toHexString(repoKeyPair.getPubKey()));
					info.setRepoPriKey(Hex.toHexString(RSAUtil.encrypt(repoKeyPair.getPrivKeyBytes(),
							RSAUtil.getPublicKeyFromHex(rootPubKeyRsa))));
					String address = EthereumUtils.createContractForProject(URL_ETHER, rootPubKeyEcc,
							info.getRepoName());
					if (address == null) {
						throw new RuntimeException("Can't not create contract for project!");
					}
					info.setRepoAddress(address);
					info.setOwner(getProjectOwner(projectDir));
					info.setOwnerPubKeyRsa(rootPubKeyRsa);
					info.setOwnerAddressEcc(rootPubKeyEcc);
				}
				writeUpdateFile(file, ByteUtils.utf8(info.genSignedContent(rootPriKeyRsa)));
			}
			byte[] content = FileUtils.readFileToByteArray(file);
			return ProjectInfoFile.fromFile(
					new HashedFile.FileWrapper("objects/info/projectinfo", new HashedFile.InputStreamCallback() {
						public InputStream call(HashedFile hashedFile) throws IOException {
							return new ByteArrayInputStream(content);
						}
					}));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void writeIndexFileToIpfs(File projectDir, ProjectInfoFile projectInfoFile) {
		writeUpdateFile(new File(projectDir, "objects/info/projectinfo"),
				ByteUtils.utf8(projectInfoFile.genSignedContent(rootPriKeyRsa)));
	}
}
