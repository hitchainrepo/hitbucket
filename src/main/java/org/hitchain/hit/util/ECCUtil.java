package org.hitchain.hit.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

public class ECCUtil {
	/**
	 * The parameters of the secp256k1 curve that Ethereum uses.
	 */
	public static final ECDomainParameters CURVE;
	/**
	 * Equal to CURVE.getN().shiftRight(1), used for canonicalising the S value of a
	 * signature. If you aren't sure what this is about, you can ignore it.
	 */
	public static final BigInteger HALF_CURVE_ORDER;
	private static final SecureRandom secureRandom;

	static {
		// All clients must agree on the curve to use by agreement. Ethereum uses
		// secp256k1.
		X9ECParameters params = SECNamedCurves.getByName("secp256k1");
		CURVE = new ECDomainParameters(params.getCurve(), params.getG(), params.getN(), params.getH());
		HALF_CURVE_ORDER = params.getN().shiftRight(1);
		secureRandom = new SecureRandom();
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}

	// 生成秘钥对
	public static KeyPair getKeyPair() throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", "BC");
		keyPairGenerator.initialize(256, new SecureRandom());
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		return keyPair;
	}

	// 获取公钥(Base64编码)
	public static String getPublicKey(KeyPair keyPair) {
		ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
		byte[] bytes = publicKey.getEncoded();
		return Base64.getEncoder().encodeToString(bytes);
	}

	// 获取私钥(Base64编码)
	public static String getPrivateKey(KeyPair keyPair) {
		ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();
		byte[] bytes = privateKey.getEncoded();
		return Base64.getEncoder().encodeToString(bytes);
	}

	// 将Base64编码后的公钥转换成PublicKey对象
	public static ECPublicKey string2PublicKey(String pubStr) throws Exception {
		byte[] keyBytes = Base64.getDecoder().decode(pubStr);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
		ECPublicKey publicKey = (ECPublicKey) keyFactory.generatePublic(keySpec);
		return publicKey;
	}

	// 将Base64编码后的私钥转换成PrivateKey对象
	public static ECPrivateKey string2PrivateKey(String priStr) throws Exception {
		byte[] keyBytes = Base64.getDecoder().decode(priStr);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
		ECPrivateKey privateKey = (ECPrivateKey) keyFactory.generatePrivate(keySpec);
		return privateKey;
	}

	// 公钥加密
	public static byte[] publicEncrypt(byte[] content, PublicKey publicKey) throws Exception {
		Cipher cipher = Cipher.getInstance("ECIES", "BC");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] bytes = cipher.doFinal(content);
		return bytes;
	}

	// 公钥加密
	public static byte[] publicEncrypt(InputStream is, PublicKey publicKey) throws Exception {
		Cipher cipher = Cipher.getInstance("ECIES", "BC");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		CipherOutputStream cos = new CipherOutputStream(baos, cipher);
		IOUtils.copy(is, cos);
		cos.close();
		return baos.toByteArray();
	}

	// 私钥解密
	public static byte[] privateDecrypt(byte[] content, PrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance("ECIES", "BC");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] bytes = cipher.doFinal(content);
		return bytes;
	}

	// 私钥解密
	public static byte[] privateDecrypt(InputStream is, PrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance("ECIES", "BC");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		CipherInputStream cis = new CipherInputStream(is, cipher);
		byte[] bytes = IOUtils.toByteArray(cis);
		cis.close();
		return bytes;
	}

	public static PrivateKey getPrivateKeyFromECBigIntAndCurve(BigInteger s) {
		ECParameterSpec ecParameterSpec = new ECNamedCurveSpec("secp256k1", CURVE.getCurve(), CURVE.getG(),
				CURVE.getN(), CURVE.getH(), CURVE.getSeed());
		ECPrivateKeySpec privateKeySpec = new ECPrivateKeySpec(s, ecParameterSpec);
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("EC");
			ECKey key;
			return keyFactory.generatePrivate(privateKeySpec);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static PrivateKey getPrivateKeyFromEthereumHex(String hex) {
		return getPrivateKeyFromECBigIntAndCurve(new BigInteger(hex, 16));
	}

	public static PublicKey getPublicKeyFromECBigInt(ECPoint s) {
		ECParameterSpec ecParameterSpec = new ECNamedCurveSpec("secp256k1", CURVE.getCurve(), CURVE.getG(),
				CURVE.getN(), CURVE.getH(), CURVE.getSeed());
		ECPublicKeySpec publicKeySpec = new ECPublicKeySpec(s, ecParameterSpec);
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("EC");
			return keyFactory.generatePublic(publicKeySpec);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static PublicKey getPublicKeyFromECBigInt(BigInteger xCoord, BigInteger yCoord) {
		return getPublicKeyFromECBigInt(new ECPoint(xCoord, yCoord));
	}

	public static PublicKey getPublicKeyFromEthereumPrivateKeyHex(String privateKeyHex) {
		ECKey key = ECKey.fromPrivate(Hex.decode(privateKeyHex));
		org.bouncycastle.math.ec.ECPoint point = key.getPubKeyPoint();
		return getPublicKeyFromECBigInt(point.getXCoord().toBigInteger(), point.getYCoord().toBigInteger());
	}

	public static PublicKey getPublicKeyFromEthereumPublicKeyHex(String publicKeyHex) {
		ECKey key = ECKey.fromPublicOnly(Hex.decode(publicKeyHex));
		org.bouncycastle.math.ec.ECPoint point = key.getPubKeyPoint();
		return getPublicKeyFromECBigInt(point.getXCoord().toBigInteger(), point.getYCoord().toBigInteger());
	}

	public static byte[] sha256(byte[] data) {
		return DigestUtils.sha256(data);
	}

	public static byte[] sign(byte[] data, byte[] privateKey) {
		try {
			ECKey key = ECKey.fromPrivate(privateKey);
			ECKey.ECDSASignature sign = key.sign(sha256(data));
			return sign.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean verify(byte[] data, byte[] signedHash, byte[] publicKey) {
		try {
			return ECKey.verify(sha256(data), signedHash, publicKey);
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean verifyByNodeId(byte[] data, byte[] signedHash, byte[] nodeId) {
		try {
			ECKey key = ECKey.signatureToKey(sha256(data), signedHash);
			byte[] pubKeyToNodeId = pubKeyToNodeId(key.getPubKey());
			return Arrays.equals(pubKeyToNodeId, nodeId);
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean verifyByAddress(byte[] data, byte[] signedHash, byte[] address) {
		try {
			ECKey key = ECKey.signatureToKey(sha256(data), signedHash);
			return Arrays.equals(key.getAddress(), address);
		} catch (Exception e) {
			return false;
		}
	}

	public static byte[] pubKeyToNodeId(byte[] pubKey) {
		return sha256(pubKey);
	}

	public static KeyPair toKeyPair(String privateKeyHex) {
		return toKeyPair(Hex.decode(privateKeyHex));
	}

	public static KeyPair toKeyPair(byte[] privateKey) {
		return toKeyPair(ECKey.fromPrivate(privateKey));
	}

	public static KeyPair toKeyPair(ECKey key) {
		org.bouncycastle.math.ec.ECPoint point = key.getPubKeyPoint();
		return new KeyPair(getPublicKeyFromECBigInt(point.getXCoord().toBigInteger(), point.getYCoord().toBigInteger()),
				getPrivateKeyFromECBigIntAndCurve(key.getPrivKey()));
	}

	public static String encryptWithPasswordHex(String content, String password) {
		try {
			String pwd = password; // 口令
			PBEKeySpec keySpec = new PBEKeySpec(pwd.toCharArray()); // 密钥格式化
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWithMD5AndDES"); // 密钥工厂
			Key key = factory.generateSecret(keySpec);
			byte[] salt = new byte[8]; // 盐
			byte[] pwdBytes = pwd.getBytes();
			System.arraycopy(pwdBytes, 0, salt, 0, Math.min(salt.length, pwdBytes.length));
			PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, 100); // PBE参数格式化
			Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES"); // 确定算法
			cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec); // 确定口令 和 盐
			return Hex.toHexString(cipher.doFinal(ByteUtils.utf8(content))); // 加密
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String decryptWithPassword(String encryptedHex, String password) {
		try {
			String pwd = password; // 口令
			PBEKeySpec keySpec = new PBEKeySpec(pwd.toCharArray()); // 密钥格式化
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWithMD5AndDES"); // 密钥工厂
			Key key = factory.generateSecret(keySpec);
			byte[] salt = new byte[8]; // 盐
			byte[] pwdBytes = pwd.getBytes();
			System.arraycopy(pwdBytes, 0, salt, 0, Math.min(salt.length, pwdBytes.length));
			PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, 100); // PBE参数格式化
			Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES"); // 确定算法
			cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec); // 进入解密模式
			return ByteUtils.utf8(cipher.doFinal(Hex.decode(encryptedHex))); // 解密
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) throws Exception {
		String pwd = "test"; // 口令
		String hexString = Hex.toHexString(new ECKey().getPrivKeyBytes());
		String encrypted = encryptWithPasswordHex(hexString, pwd);
		System.out.println(encrypted);
		System.out.println(decryptWithPassword(encrypted, pwd));
		System.out.println(hexString);

	}

	public static void main6(String[] args) throws Exception {
		ECKey key = ECKey.fromPrivate(Hex.decode("f9553a312f682c94fc7647aab8308668cf801b862d5340ba0c0a9bcd90766da3"));
		byte[] nodeId = pubKeyToNodeId(key.getPubKey());
		byte[] bytes = "HELLO".getBytes();
		byte[] sign = ECCUtil.sign("HELLO".getBytes(), key.getPrivKeyBytes());
		System.out.println("SHA0=" + Hex.toHexString(sha256(key.getPubKey())));
		System.out.println("SHA1=" + Hex.toHexString(sha256(key.getPubKey())));
		System.out.println("PubKey0=" + Hex.toHexString(key.getPubKey()));
		byte[] pubKey = ECKey.signatureToKey(sha256(bytes), sign).getPubKey();
		System.out.println("PubKey1=" + Hex.toHexString(pubKey));
		System.out.println("NodeId0=" + Hex.toHexString(nodeId));
		System.out.println("NodeId1=" + Hex.toHexString(sha256(pubKey)));
		System.out.println("Verify: " + verifyByNodeId(bytes, sign, nodeId));

		System.out.println(key.getPubKey().length);
		System.out.println(DigestUtils.sha512("HELLO").length);
		System.out.println(DigestUtils.sha384("HELLO").length);
		System.out.println(DigestUtils.sha256("HELLO").length);
	}

	public static void main5(String[] args) throws Exception {
		ECKey key = ECKey.fromPrivate(Hex.decode("f9553a312f682c94fc7647aab8308668cf801b862d5340ba0c0a9bcd90766da3"));
		System.out.println("PRI=" + key.getPrivKeyBytes().length);
		System.out.println("PUB=" + key.getAddress().length);
		System.out.println("PUB=" + key.getPubKey().length);
		System.out.println("PUB=" + Hex.toHexString(key.getAddress()));
		for (int i = 0; i < 10; i++) {
			byte[] data = ByteUtils.utf8(StringUtils.repeat("hello", new Random().nextInt(100)));
			byte[] sign = sign(data, key.getPrivKeyBytes());
			System.out.println("PUB=" + Hex.toHexString(ECKey.signatureToKey(sha256(data), sign).getAddress()));
			System.out.println("length=" + sign.length);
			System.out.println(verify(data, sign, key.getPubKey()));
		}
		main4(args);
	}

	public static void main4(String[] args) throws Exception {
		ECKey key = new ECKey();
		System.out.println("Pub-len=" + key.getPubKey().length);
		System.out.println("Pri-len=" + key.getPrivKeyBytes().length);
		System.out.println("Pub=" + Hex.toHexString(key.getPubKey()));
		System.out.println("Pri=" + key.getPrivKey().toString(16));
		org.bouncycastle.math.ec.ECPoint point = key.getPubKeyPoint();
		PublicKey pubKey = getPublicKeyFromECBigInt(point.getXCoord().toBigInteger(), point.getYCoord().toBigInteger());
		System.out.println(Hex.toHexString(pubKey.getEncoded()));
		PrivateKey priKey = getPrivateKeyFromECBigIntAndCurve(key.getPrivKey());
		System.out.println(Hex.toHexString(priKey.getEncoded()));
		byte[] publicEncrypt = publicEncrypt(StringUtils.repeat("hello world", 10000).getBytes(), pubKey);
		byte[] privateDecrypt = privateDecrypt(publicEncrypt, priKey);
		System.out.println(new String(privateDecrypt));
	}

	public static void main1(String[] args) throws Exception {
		ECKey key = new ECKey();
		System.out.println("Pri:" + key.getPrivKey().toString(16));
		System.out.println("Pub:" + Hex.toHexString(key.getPubKey()));
		ECGenParameterSpec ecGenSpec = new ECGenParameterSpec("secp256k1");
		KeyPairGenerator g = KeyPairGenerator.getInstance("ECDSA", "BC");
		g.initialize(ecGenSpec, new SecureRandom());
		KeyPair pair = g.generateKeyPair();
		System.out.println("---");
		System.out.println(Hex.toHexString(pair.getPublic().getEncoded()));
		System.out.println(Hex.toHexString(pair.getPrivate().getEncoded()));
		System.out.println("---");
		//
		System.out.println(Hex.toHexString(ECKey.fromPrivate(pair.getPrivate().getEncoded()).getPubKey()));
		System.out.println(ECKey.fromPrivate(pair.getPrivate().getEncoded()).getPrivKey().toString(16));
		System.out.println("---");
		org.bouncycastle.math.ec.ECPoint point = key.getPubKeyPoint();
		PublicKey pk = getPublicKeyFromECBigInt(
				new ECPoint(point.getXCoord().toBigInteger(), point.getYCoord().toBigInteger()));
		System.out.println(Hex.toHexString(pk.getEncoded()));
		System.out.println(Hex.toHexString(getPrivateKeyFromECBigIntAndCurve(key.getPrivKey()).getEncoded()));
		System.out.println("====");
		Cipher cipher = Cipher.getInstance("ECIES", "BC");
		System.out.println(cipher);
	}

	public static void main2(String[] args) throws Exception {
		KeyPair keyPair = ECCUtil.getKeyPair();
		String publicKeyStr = ECCUtil.getPublicKey(keyPair);
		String privateKeyStr = ECCUtil.getPrivateKey(keyPair);
		System.out.println("ECC公钥Base64编码:" + publicKeyStr);
		System.out.println("ECC私钥Base64编码:" + privateKeyStr);

		ECPublicKey publicKey = string2PublicKey(publicKeyStr);
		ECPrivateKey privateKey = string2PrivateKey(privateKeyStr);

		byte[] publicEncrypt = publicEncrypt("hello world".getBytes(), publicKey);
		byte[] privateDecrypt = privateDecrypt(publicEncrypt, privateKey);
		System.out.println(new String(privateDecrypt));
	}
}
