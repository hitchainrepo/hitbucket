package org.hitchain.hit.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * this code copy from:
 * <p>
 * Title: RSAHelper DO NOT use the *ByDefaultKey method to encode or decode the
 * data if you need the data safety and persistent. the *ByDefaultKey method
 * using to encrypt the data temporary.
 * </p>
 * <p>
 * Description: Utility class that helps encrypt and decrypt strings using RSA
 * algorithm
 * </p>
 *
 * @author Aviran Mordo http://aviran.mordos.com
 * @version 1.0
 */
public class RSAUtil {
	protected static final String ALGORITHM = "RSA";

	/**
	 * Init java security to add BouncyCastle as an RSA provider
	 */
	static {
		try {
			Security.addProvider(new BouncyCastleProvider());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Generate key which contains a pair of privae and public key using 1024 bytes
	 *
	 * @return key pair
	 */
	public static KeyPair generateKey() {
		KeyPair key = null;
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
			keyGen.initialize(1024, new SecureRandom());
			key = keyGen.generateKeyPair();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return key;
	}

	/**
	 * Generates Public Key from bytes
	 *
	 * @param key
	 *            hex encoded string which represents the key
	 * @return The PublicKey
	 */
	public static PublicKey getPublicKeyFromBytes(byte[] key) {
		PublicKey publicKey = null;
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
			EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(key);
			publicKey = keyFactory.generatePublic(publicKeySpec);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return publicKey;
	}

	/**
	 * Generates Public Key from Hex encoded string
	 *
	 * @param key
	 *            hex encoded string which represents the key
	 * @return The PublicKey
	 */
	public static PublicKey getPublicKeyFromHex(String key) {
		PublicKey publicKey = null;
		try {
			publicKey = getPublicKeyFromBytes(Hex.decodeHex(key.toCharArray()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return publicKey;
	}

	/**
	 * Generates Public Key from BASE64 encoded string
	 *
	 * @param key
	 *            BASE64 encoded string which represents the key
	 * @return The PublicKey
	 */
	public static PublicKey getPublicKeyFromBase64(String key) {
		PublicKey publicKey = null;
		try {
			publicKey = getPublicKeyFromBytes(Base64.decodeBase64(key));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return publicKey;
	}

	/**
	 * Generates Private Key from BASE64 encoded string
	 *
	 * @param key
	 *            BASE64 encoded string which represents the key
	 * @return The PrivateKey
	 */
	public static PrivateKey getPrivateKeyFromBase64(String key) {
		PrivateKey privateKey = null;
		try {
			privateKey = getPrivateKeyFromBytes(Base64.decodeBase64(key));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return privateKey;
	}

	/**
	 * Generates Private Key from Hex encoded string
	 *
	 * @param key
	 *            Hex encoded string which represents the key
	 * @return The PrivateKey
	 */
	public static PrivateKey getPrivateKeyFromHex(String key) {
		PrivateKey privateKey = null;
		try {
			privateKey = getPrivateKeyFromBytes(Hex.decodeHex(key.toCharArray()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return privateKey;
	}

	/**
	 * Generates Private Key from bytes
	 *
	 * @param bytes
	 *            Hex encoded string which represents the key
	 * @return The PrivateKey
	 */
	public static PrivateKey getPrivateKeyFromBytes(byte[] bytes) {
		PrivateKey privateKey = null;
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
			EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bytes);
			privateKey = keyFactory.generatePrivate(privateKeySpec);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return privateKey;
	}

	/**
	 * Encrypt a text using public/private key.
	 *
	 * @param text
	 *            The original unencrypted text
	 * @param key
	 *            The public/private key
	 * @return Encrypted text
	 */
	public static byte[] encrypt(byte[] text, Key key) {
		byte[] cipherText = null;
		try {
			// get an RSA cipher object and print the provider
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", BouncyCastleProvider.PROVIDER_NAME);
			// encrypt the plaintext using the public key
			cipher.init(Cipher.ENCRYPT_MODE, key);
			cipherText = cipher.doFinal(text);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return cipherText;
	}

	/**
	 * Encrypt a text using public/private key. The result is enctypted BASE64
	 * encoded text
	 *
	 * @param text
	 *            The original unencrypted text
	 * @param key
	 *            The public/private key
	 * @return Encrypted text encoded as BASE64
	 */
	public static String encrypt(String text, Key key) {
		String encryptedText = null;
		try {
			byte[] cipherText = encrypt(text.getBytes("UTF8"), key);
			encryptedText = Base64.encodeBase64String(cipherText);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return encryptedText;
	}

	/**
	 * Decrypt text using public/private key
	 *
	 * @param text
	 *            The encrypted text
	 * @param key
	 *            The public/private key
	 * @return The unencrypted text
	 */
	public static byte[] decrypt(byte[] text, Key key) {
		byte[] dectyptedText = null;
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", BouncyCastleProvider.PROVIDER_NAME);
			cipher.init(Cipher.DECRYPT_MODE, key);
			dectyptedText = cipher.doFinal(text);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return dectyptedText;
	}

	/**
	 * Decrypt BASE64 encoded text using public/private key
	 *
	 * @param text
	 *            The encrypted text, encoded as BASE64
	 * @param key
	 *            The public/private key
	 * @return The unencrypted text encoded as UTF8
	 */
	public static String decrypt(String text, Key key) {
		String result = null;
		try {
			byte[] dectyptedText = decrypt(Base64.decodeBase64(text), key);
			result = new String(dectyptedText, "UTF8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * Convert a Key to string encoded as BASE64
	 *
	 * @param key
	 *            The key (private or public)
	 * @return A string representation of the key
	 */
	public static String getKeyAsBase64(Key key) {
		// Get the bytes of the key
		byte[] keyBytes = key.getEncoded();
		return Base64.encodeBase64String(keyBytes);
	}

	/**
	 * Convert a Key to string encoded as BASE64
	 *
	 * @param key
	 *            The key (private or public)
	 * @return A string representation of the key
	 */
	public static String getKeyAsHex(Key key) {
		// Get the bytes of the key
		byte[] keyBytes = key.getEncoded();
		return new String(Hex.encodeHex(keyBytes, true));
	}

	/**
	 * Encrypt file using 1024 RSA encryption
	 *
	 * @param content
	 * @param key
	 * @param key
	 *            The key. public/private key
	 */
	public static byte[] encryptLargeContent(byte[] content, Key key) {
		if (content == null || content.length == 0) {
			return new byte[0];
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", BouncyCastleProvider.PROVIDER_NAME);
			// RSA encryption data size limitations are slightly less than the key modulus
			// size,
			// depending on the actual padding scheme used (e.g. with 1024 bit (128 byte)
			// RSA key,
			// the size limit is 117 bytes for PKCS#1 v 1.5 padding.
			// (http://www.jensign.com/JavaScience/dotnet/RSAEncrypt/)
			ByteArrayInputStream bais = new ByteArrayInputStream(content);
			byte[] buf = new byte[100];
			int bufl;
			// init the Cipher object for Encryption...
			cipher.init(Cipher.ENCRYPT_MODE, key);
			while ((bufl = bais.read(buf)) != -1) {
				baos.write(encrypt(copyBytes(buf, bufl), key));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return baos.toByteArray();
	}

	/**
	 * Decrypt file using 1024 RSA encryption
	 *
	 * @param content
	 * @param key
	 *            The key. public/private key
	 */
	public static byte[] decryptLargeContent(byte[] content, Key key) {
		if (content == null) {
			return null;
		}
		if (content.length == 0) {
			return new byte[0];
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", BouncyCastleProvider.PROVIDER_NAME);
			// RSA encryption data size limitations are slightly less than the key modulus
			// size,
			// depending on the actual padding scheme used (e.g. with 1024 bit (128 byte)
			// RSA key,
			// the size limit is 117 bytes for PKCS#1 v 1.5 padding.
			// (http://www.jensign.com/JavaScience/dotnet/RSAEncrypt/)
			ByteArrayInputStream bais = new ByteArrayInputStream(content);
			byte[] buf = new byte[128];
			int bufl;
			// init the Cipher object for Encryption...
			cipher.init(Cipher.DECRYPT_MODE, key);
			while ((bufl = bais.read(buf)) != -1) {
				baos.write(decrypt(copyBytes(buf, bufl), key));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return baos.toByteArray();
	}

	/**
	 * <p>
	 * 用私钥对信息生成数字签名
	 * </p>
	 * 
	 * @param data
	 *            已加密数据
	 * @param privateKey
	 *            私钥(BASE64编码)
	 * @return
	 * @throws Exception
	 */
	public static byte[] sign(byte[] data, PrivateKey priKey) {
		try {
			Signature signature = Signature.getInstance("MD5withRSA");
			signature.initSign(priKey);
			signature.update(data);
			return signature.sign();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * <p>
	 * 校验数字签名
	 * </p>
	 * 
	 * @param data
	 *            已加密数据
	 * @param publicKey
	 *            公钥(BASE64编码)
	 * @param sign
	 *            数字签名
	 * @return
	 * @throws Exception
	 * 
	 */
	public static boolean verify(byte[] data, byte[] sign, PublicKey pubKey) {
		try {
			Signature signature = Signature.getInstance("MD5withRSA");
			signature.initVerify(pubKey);
			signature.update(data);
			return signature.verify(sign);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] copyBytes(byte[] arr, int length) {
		byte[] newArr = null;
		if (arr == null || arr.length == length) {
			newArr = arr;
		} else {
			newArr = new byte[length];
			for (int i = 0; i < length; i++) {
				newArr[i] = (byte) arr[i];
			}
		}
		return newArr;
	}

	public static void main(String[] args) throws Exception {
		KeyPair kp = generateKey();
		String pubKey = getKeyAsBase64(kp.getPublic());
		String priKey = getKeyAsBase64(kp.getPrivate());
		System.out.println(pubKey);
		System.out.println(priKey);
		String encrypt = encrypt("helloworld!helloworld!helloworld!helloworld!helloworld!helloworld",
				getPublicKeyFromBase64(pubKey));
		System.out.println(encrypt);
		System.out.println(decrypt(encrypt, getPrivateKeyFromBase64(priKey)));
		System.out.println("PUB-KEY Base64:" + pubKey);
		System.out.println("PUB-KEY HEX:" + getKeyAsHex(kp.getPublic()));
		System.out.println("PRI-KEY Base64:" + priKey);
		System.out.println("PRI-KEY HEX:" + getKeyAsHex(kp.getPrivate()));
		System.out.println("============");
		byte[] largeText = encryptLargeContent(
				ByteUtils.utf8(
						StringUtils.repeat("helloworld!helloworld!helloworld!helloworld!helloworld!helloworld", 100)),
				getPublicKeyFromBase64(pubKey));
		System.out.println(ByteUtils.utf8(decryptLargeContent(largeText, getPrivateKeyFromBase64(priKey))));
		System.out.println("============");
		byte[] sign = sign(largeText, getPrivateKeyFromBase64(priKey));
		System.out.println("Verify:" + verify(largeText, sign, getPublicKeyFromBase64(pubKey)));
	}
}