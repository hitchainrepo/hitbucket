package org.hitchain.hit.util;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.jce.spec.IESParameterSpec;
import org.bouncycastle.math.ec.custom.sec.SecP256K1Curve;
import org.bouncycastle.math.ec.custom.sec.SecP256K1FieldElement;
import org.bouncycastle.math.ec.custom.sec.SecP256K1Point;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.ECFieldFp;
import java.security.spec.ECPoint;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.EllipticCurve;


@Deprecated
public class EncryptWithPubKey {

    public static void main(String[] args) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, InvalidKeyException, IOException {

        // ECDSA secp256k1 algorithm constants
        BigInteger pointGPre = new BigInteger("79be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798", 16);
        BigInteger pointGPost = new BigInteger("483ada7726a3c4655da4fbfc0e1108a8fd17b448a68554199c47d08ffb10d4b8", 16);
        BigInteger factorN = new BigInteger("fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141", 16);
        BigInteger fieldP = new BigInteger("fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2f", 16);

        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("ECIES", "BC");
        IESParameterSpec iesParams = new IESParameterSpec(null, null, 64);

        //----------------------------
        // Encrypt with public key
        //----------------------------

        // public key for test
        String publicKeyValue = "30d67dc730d0d253df841d82baac12357430de8b8f5ce8a35e254e1982c304554fdea88c9cebdda72bf6be9b14aa684288eb3ba6f9cb7b7872b6e41d2b9706fc";
        String prePublicKeyStr = publicKeyValue.substring(0, 64);
        String postPublicKeyStr = publicKeyValue.substring(64);

        EllipticCurve ellipticCurve = new EllipticCurve(new ECFieldFp(fieldP), new BigInteger("0"), new BigInteger("7"));
        ECPoint pointG = new ECPoint(pointGPre, pointGPost);
        ECNamedCurveSpec namedCurveSpec = new ECNamedCurveSpec("secp256k1", ellipticCurve, pointG, factorN);

        // public key
        SecP256K1Curve secP256K1Curve = new SecP256K1Curve();
        SecP256K1Point secP256K1Point = new SecP256K1Point(secP256K1Curve, new SecP256K1FieldElement(new BigInteger(prePublicKeyStr, 16)), new SecP256K1FieldElement(new BigInteger(postPublicKeyStr, 16)));
        SecP256K1Point secP256K1PointG = new SecP256K1Point(secP256K1Curve, new SecP256K1FieldElement(pointGPre), new SecP256K1FieldElement(pointGPost));
        ECDomainParameters domainParameters = new ECDomainParameters(secP256K1Curve, secP256K1PointG, factorN);
        ECPublicKeyParameters publicKeyParameters = new ECPublicKeyParameters(secP256K1Point, domainParameters);
        BCECPublicKey publicKeySelf = new BCECPublicKey("ECDSA", publicKeyParameters, namedCurveSpec, BouncyCastleProvider.CONFIGURATION);

        // begin encrypt

        cipher.init(Cipher.ENCRYPT_MODE, publicKeySelf, iesParams);
        String cleartextFile = "test/source.txt";
        String ciphertextFile = "test/cipher.txt";
        byte[] block = new byte[64];
//        FileInputStream fis = new FileInputStream(cleartextFile);
//        FileOutputStream fos = new FileOutputStream(ciphertextFile);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CipherOutputStream cos = new CipherOutputStream(baos, cipher);

//        int i;
//        while ((i = fis.read(block)) != -1) {
//            cos.write(block, 0, i);
//        }
//        cos.close();
        cos.write("hello world".getBytes("UTF-8"));
        cos.close();

        //----------------------------
        // Decrypt with private key
        //----------------------------

        // private key for test, match with public key above
        BigInteger privateKeyValue = new BigInteger("eb06bde0e1e9427b3e23ab010a124e8cea0d9242b5406eff295f0a501b49db3", 16);

        ECPrivateKeySpec privateKeySpec = new ECPrivateKeySpec(privateKeyValue, namedCurveSpec);
        BCECPrivateKey privateKeySelf = new BCECPrivateKey("ECDSA", privateKeySpec, BouncyCastleProvider.CONFIGURATION);

        // begin decrypt
        String cleartextAgainFile = "test/decrypt.txt";
        cipher.init(Cipher.DECRYPT_MODE, privateKeySelf, iesParams);
//        fis = new FileInputStream(ciphertextFile);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        CipherInputStream cis = new CipherInputStream(bais, cipher);
//        fos = new FileOutputStream(cleartextAgainFile);
//        while ((i = cis.read(block)) != -1) {
//            fos.write(block, 0, i);
//        }
//        fos.close();
        System.out.println("===decrypt===" + IOUtils.toString(cis, "UTF-8"));
        cis.close();
    }
}
