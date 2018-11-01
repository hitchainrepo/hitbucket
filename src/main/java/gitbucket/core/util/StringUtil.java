/*******************************************************************************
 * Copyright (c) 2018-11-01 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform.
 ******************************************************************************/
package gitbucket.core.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.mozilla.universalchardet.UniversalDetector;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * StringUtilHelper
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-01
 * auto generate by qdp.
 */
public class StringUtil {
    private static final Pattern GitBucketUrlPattern = Pattern.compile("^(https?://.+)/git/(.+?)/(.+?)\\.git$");
    private static final Pattern GitHubUrlPattern = Pattern.compile("^https://(.+@)?github\\.com/(.+?)/(.+?)\\.git$");
    private static final Pattern BitBucketUrlPattern = Pattern.compile("^https?://(.+@)?bitbucket\\.org/(.+?)/(.+?)\\.git$");
    private static final Pattern GitLabUrlPattern = Pattern.compile("^https?://(.+@)?gitlab\\.com/(.+?)/(.+?)\\.git$");
    private static String BlowfishKey = UUID.randomUUID().toString().substring(0, 16);

    public static String base64Encode(byte[] value) {
        return Base64.getEncoder().encodeToString(value);
    }

    public static byte[] base64Decode(String value) {
        return Base64.getDecoder().decode(value);
    }

    public static String pbkdf2_sha256(int iter, String salt, String value) {
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            PBEKeySpec ks = new PBEKeySpec(value.toCharArray(), base64Decode(salt), iter, 256);
            SecretKey s = keyFactory.generateSecret(ks);
            return base64Encode(s.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String pbkdf2_sha256(String value) {
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            SecureRandom secureRandom = new SecureRandom();
            byte[] salt = new byte[32];
            secureRandom.nextBytes(salt);
            int iter = 100000;
            PBEKeySpec ks = new PBEKeySpec(value.toCharArray(), salt, iter, 256);
            SecretKey s = keyFactory.generateSecret(ks);
            return "$pbkdf2-sha256$" + iter + "$" + base64Encode(salt) + "$" + base64Encode(s.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String sha1(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(value.getBytes());
            return Hex.encodeHexString(md.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String md5(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(value.getBytes());
            return Hex.encodeHexString(md.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String encodeBlowfish(String value) {
        try {
            SecretKeySpec spec = new SecretKeySpec(BlowfishKey.getBytes(), "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, spec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(value.getBytes("UTF-8")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decodeBlowfish(String value) {
        try {
            SecretKeySpec spec = new SecretKeySpec(BlowfishKey.getBytes(), "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.DECRYPT_MODE, spec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(value)), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8").replace("+", "%20");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String urlDecode(String value) {
        try {
            return URLDecoder.decode(value, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String[] splitWords(String value) {
        return value.split("[ \\t　]+");
    }

    public static boolean isInteger(String value) {
        try {
            return new Integer(value) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public static String escapeHtml(String value) {
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }

    /**
     * Make string from byte array. Character encoding is detected automatically by [[StringUtil.detectEncoding]].
     * And if given bytes contains UTF-8 BOM, it's removed from returned string.
     */
    public static String convertFromByteArray(byte[] content) {
        try {
            return IOUtils.toString(new BOMInputStream(new ByteArrayInputStream(content)), detectEncoding(content));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String detectEncoding(byte[] content) {
        UniversalDetector detector = new UniversalDetector(null);
        detector.handleData(content, 0, content.length);
        detector.dataEnd();
        String charset = detector.getDetectedCharset();
        if (charset == null) {
            return "UTF-8";
        } else {
            return charset;
        }
    }

    /**
     * Converts line separator in the given content.
     *
     * @param content       the content
     * @param lineSeparator "LF" or "CRLF"
     * @return the converted content
     */
    public static String convertLineSeparator(String content, String lineSeparator) {
        String lf = content.replace("\r\n", "\n").replace("\r", "\n");
        if ("CRLF".equals(lineSeparator)) {
            return lf.replace("\n", "\r\n");
        } else {
            return lf;
        }
    }

    /**
     * Appends LF if the given string does not end with LF.
     *
     * @param content       the content
     * @param lineSeparator "LF" or "CRLF"
     * @return the converted content
     */
    public static String appendNewLine(String content, String lineSeparator) {
        if ("CRLF".equals(lineSeparator)) {
            if (content.endsWith("\r\n")) {
                return content;
            } else {
                return content + "\r\n";
            }
        } else {
            if (content.endsWith("\n")) {
                return content;
            } else {
                return content + "\n";
            }
        }
    }

    /**
     * Extract issue id like ```#issueId``` from the given message.
     *
     * @param message the message which may contains issue id
     * @return the iterator of issue id
     */
    public static List<String> extractIssueId(String message) {
        Set<String> list = new HashSet<String>();
        Pattern pattern = Pattern.compile("(^|\\W)#(\\d+)(\\W|$)");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            list.add(matcher.group(2));
        }
        return new ArrayList<String>(list);
    }

    /**
     * Extract close issue id like ```close #issueId ``` from the given message.
     *
     * @param message the message which may contains close command
     * @return the iterator of issue id
     */
    public static List<String> extractCloseId(String message) {
        Set<String> list = new HashSet<String>();
        Pattern pattern = Pattern.compile("(?i)(?<!\\w)(?:fix(?:e[sd])?|resolve[sd]?|close[sd]?)\\s+#(\\d+)(?!\\w)");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            list.add(matcher.group(1));
        }
        return new ArrayList<String>(list);
    }

    public static String getRepositoryViewerUrl(String gitRepositoryUrl, String baseUrl) {
        {
            Matcher matcher = GitBucketUrlPattern.matcher(gitRepositoryUrl);
            if (matcher.find()) {
                String base = matcher.group(0), user = matcher.group(1), repository = matcher.group(2);
                String replaceFirst = base.replaceFirst("(https?://).+@", "$1");
                if (baseUrl != null && replaceFirst.startsWith(baseUrl)) {
                    return replaceFirst + "/" + user + "/" + replaceFirst;
                }
            }
        }
        {
            Matcher matcher = GitHubUrlPattern.matcher(gitRepositoryUrl);
            if (matcher.find()) {
                String base = matcher.group(0), user = matcher.group(1), repository = matcher.group(2);
                return "https://github.com/" + user + "/" + repository;
            }
        }
        {
            Matcher matcher = BitBucketUrlPattern.matcher(gitRepositoryUrl);
            if (matcher.find()) {
                String base = matcher.group(0), user = matcher.group(1), repository = matcher.group(2);
                return "https://bitbucket.org/" + user + "/" + repository;
            }
        }
        {
            Matcher matcher = GitLabUrlPattern.matcher(gitRepositoryUrl);
            if (matcher.find()) {
                String base = matcher.group(0), user = matcher.group(1), repository = matcher.group(2);
                return "https://gitlab.com/" + user + "/" + repository;
            }
        }
        return gitRepositoryUrl;
    }
}
