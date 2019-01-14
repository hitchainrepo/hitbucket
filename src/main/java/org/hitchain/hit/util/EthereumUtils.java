/*******************************************************************************
 * Copyright (c) 2018-12-17 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.hitchain.hit.util;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

/**
 * Utils
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-12-17
 * auto generate by qdp.
 */
public class EthereumUtils {

	/**
	 * 绕过验证
	 *     
	 * @return
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 */
	public static SSLContext createIgnoreVerifySSL() throws Exception {
		SSLContext sc = SSLContext.getInstance("SSLv3");
		// 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
		X509TrustManager trustManager = new X509TrustManager() {
			public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
					String paramString) throws CertificateException {
			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
					String paramString) throws CertificateException {
			}

			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		sc.init(null, new TrustManager[] { trustManager }, null);
		return sc;
	}

	public static String post(String url, String content) {
		try {
			RequestConfig config = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(3000).build();
			//采用绕过验证的方式处理https请求
			SSLContext sslcontext = createIgnoreVerifySSL();
			// 设置协议http和https对应的处理socket链接工厂的对象
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.INSTANCE)
					.register("https", new SSLConnectionSocketFactory(sslcontext)).build();
			PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
					socketFactoryRegistry);
			HttpClients.custom().setConnectionManager(connManager);
			//创建自定义的httpclient对象
			CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(config)
					.setConnectionManager(connManager).build();
			//
			HttpPost post = new HttpPost(url);
			StringEntity entity = new StringEntity(content, "UTF-8");
			entity.setContentType("text/plain");
			post.setEntity(entity);
			// 构造消息头
			post.setHeader("Content-type", "text/plain");
			post.setHeader("Connection", "Close");
			CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(post);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				post.abort();
				return "ERROR:HttpClient,error status code :" + statusCode;
			}
			HttpEntity responseEntity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(responseEntity);
				EntityUtils.consume(entity);
				response.close();
			}
			return StringUtils.defaultString(result, "ERROR:null");
		} catch (Exception e) {
			return "ERROR:" + e.getMessage();
		}
	}

	public static String createContractForProject(String urlBase, String ownerAddressEcc, String projectName) {
		///====create
		String url = urlBase + "/api/web3j/deployRepositoryNameContract";
		String content = "PrivateKey=-\nGasLimit=5000000\nGwei=0\n";
		String address = post(url, content);
		if (StringUtils.startsWith(address, "ERROR:")) {
			for (int i = 0; i < 10; i++) {
				address = post(url, content);
				if (!address.startsWith("ERROR:")) {
					break;
				}
			}
		}
		if (StringUtils.startsWith(address, "ERROR:")) {
			return null;
		}
		//====init
		url = urlBase + "/api/web3j/writeRepositoryNameContract";
		content = "PrivateKey=-\n" + "ContractAddress=" + address + "\n" + "FunctionName=init(addr,repoName)\n"
				+ "Arg1=" + ownerAddressEcc + "\n" + "Arg2=" + projectName + "\n" + "GasLimit=5000000\n" + "Gwei=0\n";
		String init = post(url, content);
		if (StringUtils.startsWith(init, "ERROR:")) {
			for (int i = 0; i < 10; i++) {
				address = post(url, content);
				if (!address.startsWith("ERROR:")) {
					return address;
				}
			}
		}
		return null;
	}

	public static void updateProjectAddress(String urlBase, String contractAddressddress, String ownerPriKeyEcc,
			String newProjectHash) {
		String oldProjectAddress = StringUtils.defaultString(getProjectAddress(urlBase, contractAddressddress), "-");
		//
		String url = urlBase + "/api/web3j/writeRepositoryNameContract";
		String content = "PrivateKey=" + ownerPriKeyEcc + "\n" + "ContractAddress=" + contractAddressddress + "\n"
				+ "updateRepositoryAddress(oldAddr,newAddr)\n" + "Arg1=" + oldProjectAddress + "\n" + "Arg2="
				+ newProjectHash + "\n" + "GasLimit=5000000\n" + "Gwei=0\n";
		String write = post(url, content);
		if (StringUtils.startsWith(write, "ERROR:")) {
			throw new RuntimeException(write);
		}
	}

	public static String getProjectAddress(String urlBase, String contractAddress) {
		String url = urlBase + "/api/web3j/readRepositoryNameContract";
		String content = "FromAddress=-\nContractAddress=" + contractAddress
				+ "\nFunctionName=repositoryAddress\nArg=-\n";
		return StringUtils.defaultString(post(url, content), "");
	}

	public static String encryptPriKeyEcc(String urlBase, String priKeyEcc) {
		String url = urlBase + "/api/web3j/encryptPrivateKey";
		String content = "PrivateKey=" + priKeyEcc + "\n";
		String encrypt = post(url, content);
		return StringUtils.startsWith(encrypt, "ERROR:") ? null : encrypt;
	}
}
