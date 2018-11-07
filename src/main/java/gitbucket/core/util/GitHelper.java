/*******************************************************************************
 * Copyright (c) 2018-10-19 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform.
 ******************************************************************************/
package gitbucket.core.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Hex;
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
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.internal.storage.file.LockFile;
import org.eclipse.jgit.internal.storage.file.PackFile;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.RefWriter;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;

import com.fasterxml.jackson.databind.ObjectMapper;

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
			String urlIpfs = System.getProperty("URL_IPFS", "http://121.40.127.45");
			IPFS ipfs = new IPFS("/ip4/" + StringUtils.substringAfterLast(urlIpfs, "//") + "/tcp/5001");
			NamedStreamable.FileWrapper dir = new NamedStreamable.FileWrapper(projectDir);
			List<MerkleNode> add = ipfs.add(dir);
			String hash = add.get(add.size() - 1).hash.toBase58();
			System.out.println("Project name: " + projectDir.getPath() + ", hash: " + urlIpfs + ":8080/ipfs/" + hash);
			writeHashToFile(projectDir.getAbsolutePath(), hash);
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
			String hash = getHashFromFile(dir.getAbsolutePath());
			String urlIpfs = System.getProperty("URL_IPFS", "http://121.40.127.45");
			IPFS ipfs = new IPFS("/ip4/" + StringUtils.substringAfterLast(urlIpfs, "//") + "/tcp/5001");
			List<MerkleNode> ls = ipfs.ls(Multihash.fromBase58(hash));
			for (MerkleNode mn : ls) {
				System.out.println(mn.name + "=" + mn.hash);
			}
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

	public static Git open(File dir) {
		try {
			syncProject(dir);
			return MyGit.open(dir);
			// final Git open = Git.open(dir);
			// Enhancer enhancer = new Enhancer();
			// enhancer.setUseCache(false);
			// enhancer.setSuperclass(open.getClass());
			// enhancer.setCallbackType(MethodInterceptor.class);
			// Class<?> proxyClass = enhancer.createClass();
			// Enhancer.registerCallbacks(proxyClass, new Callback[]{new MethodInterceptor()
			// {
			// public Object intercept(Object obj, Method method, Object[] args, MethodProxy
			// proxy) throws Throwable {
			// System.out.println("===GIT===" + method.getName());
			// if ("close".equals(method.getName())) {
			// }
			// //return proxy.invokeSuper(obj, args);
			// return method.invoke(open, args);
			// }
			// }});
			// // 创建代理对象
			// return (Git) ObjenesisHelper.newInstance(proxyClass);
			// return Git.wrap(new DecentralizedRepository.Builder().withPath(dir).build());
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

	public static byte[] getAsByte(String fileName, String fileType) {
		CloseableHttpClient httpCilent2 = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000) // 设置连接超时时间
				.setConnectionRequestTimeout(5000) // 设置请求超时时间
				.setSocketTimeout(5000).setRedirectsEnabled(true)// 默认允许自动重定向
				.build();
		String urlIpfs = System.getProperty("URL_IPFS", "http://121.40.127.45");
		String uri = urlIpfs + ":8080/ipfs/" + getHashFromFile(fileName);
		System.out.println("========>>> Read File=" + uri);
		HttpGet httpGet2 = new HttpGet(uri);
		httpGet2.setConfig(requestConfig);
		String srtResult = "";
		try {
			HttpResponse httpResponse = httpCilent2.execute(httpGet2);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				byte[] bytes = EntityUtils.toByteArray(httpResponse.getEntity());// 获得返回的结果
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

	public static Map<String, Object> write(String fileName, byte[] content) {
		// 获取可关闭的 httpCilent
		CloseableHttpClient httpClient = HttpClients.createDefault();
		// 配置超时时间
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(1000).setConnectionRequestTimeout(1000)
				.setSocketTimeout(1000).setRedirectsEnabled(true).build();
		String urlIpfs = System.getProperty("URL_IPFS", "http://121.40.127.45");
		HttpPost httpPost = new HttpPost(urlIpfs + ":5001/api/v0/add");
		try {
			// 设置超时时间
			httpPost.setConfig(requestConfig);
			// 装配post请求参数
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.addBinaryBody(fileName, content);
			HttpEntity entity = builder.build();
			// 设置post求情参数
			httpPost.setEntity(entity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			String strResult = "";
			if (httpResponse != null) {
				System.out.println(httpResponse.getStatusLine().getStatusCode());
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					strResult = EntityUtils.toString(httpResponse.getEntity());
					ObjectMapper mapper = new ObjectMapper();
					HashMap map = mapper.readValue(strResult, HashMap.class);
					writeHashToFile(fileName, (String) map.get("Hash"));
					System.out.println("========>>> Write File=" + fileName + ", Hash=" + map.get("Hash"));
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
					httpClient.close(); // 释放资源
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void writeHashToFile(String fileName, String hash) {
		String file = System.getProperty("HASH_FILE", "/Users/zhaochen/Desktop/hashFile");
		try {
			File hashFileDir = new File(file);
			if (!hashFileDir.exists()) {
				hashFileDir.mkdirs();
			}
			String[] pathSplit = StringUtils.split(fileName, StringUtils.contains(fileName, '/') ? '/' : '\\');
			String hexHash = Hex.encodeHexString(
					(pathSplit[pathSplit.length - 2] + "@" + pathSplit[pathSplit.length - 1]).getBytes());
			File indexFile = new File(hashFileDir, hexHash);
			if (!indexFile.exists()) {
				indexFile.createNewFile();
				FileUtils.write(indexFile, "PROJ:"
						+ (pathSplit[pathSplit.length - 2] + "@" + pathSplit[pathSplit.length - 1]) + ":" + hash + "\n",
						Charset.forName("UTF-8"), true);
				return;
			}
			String content = FileUtils.readFileToString(indexFile, "UTF-8");
			String[] lines = StringUtils.split(content, '\n');
			StringBuilder sb = new StringBuilder();
			for (String line : lines) {
				String[] split = StringUtils.split(line, ':');
				if (split.length != 3) {
					continue;
				}
				String type = split[0], value1 = split[1], value2 = split[2];
				// PROJ:projectName:hash
				// IPFS:-:url
				// RKEY:encrypt_repoPrivateKey_by_owner_pubKey:pubKey
				// OKEY:owner:pubKey
				// MKEY*:member:pubKey
				// TKEY*:encrypt_repoPrivateKey_by_member_pubKey:pubKey
				if ("PROJ".equals(type)) {
					sb.append(type).append(':').append(value1).append(':').append(hash).append('\n');
				} else if ("IPFS".equals(type)) {
					sb.append(type).append(':').append(value1).append(':').append(value2).append('\n');
				} else if ("RKEY".equals(type)) {
					sb.append(type).append(':').append(value1).append(':').append(value2).append('\n');
				} else if ("OKEY".equals(type)) {
					sb.append(type).append(':').append(value1).append(':').append(value2).append('\n');
				} else if ("MKEY".equals(type)) {
					sb.append(type).append(':').append(value1).append(':').append(value2).append('\n');
				} else if ("TKEY".equals(type)) {
					sb.append(type).append(':').append(value1).append(':').append(value2).append('\n');
				}
			}
			FileUtils.write(indexFile, sb.toString(), Charset.forName("UTF-8"), true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String getHashFromFile(String fileName) {
		try {
			String file = System.getProperty("HASH_FILE", "/Users/zhaochen/Desktop/hashFile");
			File hashFileDir = new File(file);
			if (!hashFileDir.exists()) {
				hashFileDir.mkdirs();
			}
			String[] pathSplit = StringUtils.split(fileName, StringUtils.contains(fileName, '/') ? '/' : '\\');
			String hexHash = Hex.encodeHexString(
					(pathSplit[pathSplit.length - 2] + "@" + pathSplit[pathSplit.length - 1]).getBytes());
			File indexFile = new File(hashFileDir, hexHash);
			if(!indexFile.exists()) {
				return "";
			}
			String content = FileUtils.readFileToString(indexFile, "UTF-8");
			String[] lines = StringUtils.split(content, '\n');
			for (String line : lines) {
				String[] split = StringUtils.split(line, ':');
				if (split.length != 3) {
					continue;
				}
				String type = split[0], value1 = split[1], value2 = split[2];
				// PROJ:projectName:hash
				// IPFS:-:url
				// RKEY:encrypt_repoPrivateKey_by_owner_pubKey:pubKey
				// OKEY:owner:pubKey
				// MKEY*:member:pubKey
				// TKEY*:encrypt_repoPrivateKey_by_member_pubKey:pubKey
				if ("PROJ".equals(type)) {
					return value2;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

}
