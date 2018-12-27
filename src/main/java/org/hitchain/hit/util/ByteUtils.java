/*******************************************************************************
 * Copyright (c) 2018-12-17 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.hitchain.hit.util;

import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;
import java.net.InetAddress;

/**
 * Utils
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-12-17
 * auto generate by qdp.
 */
public class ByteUtils {

    public static byte[] utf8(String data) {
        try {
            return data == null || data.length() == 0 ? new byte[0] : data.getBytes("UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String utf8(byte[] data) {
        try {
            return data == null || data.length == 0 ? "" : new String(data, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toBinaryString(byte[] bs) {
        BigInteger bi = new BigInteger(1, bs);
        return String.format("%s", bi.toString(2));
    }

    public static String toBinaryString(byte[] bs, int length) {
        BigInteger bi = new BigInteger(1, bs);
        return String.format("%" + (length > 0 ? Integer.valueOf(length).toString() : "") + "s", bi.toString(2)).replace(' ', '0');
    }


    public static byte[] toBytesInt(int value) {
        return new byte[]{(byte) (value >> 24 & 0xFF), (byte) (value >> 16 & 0xFF), (byte) (value >> 8 & 0xFF), (byte) (value & 0xFF)};
    }

    public static int toInt(byte[] value) {
        if (value == null) {
            return 0;
        }
        if (value.length > 3) {
            return ((value[0] & 0xFF) << 24) | ((value[1] & 0xFF) << 16) | ((value[2] & 0xFF) << 8) | (value[3] & 0xFF);
        }
        if (value.length > 2) {
            return ((value[0] & 0xFF) << 16) | ((value[1] & 0xFF) << 8) | (value[2] & 0xFF);
        }
        if (value.length > 1) {
            return ((value[0] & 0xFF) << 8) | (value[1] & 0xFF);
        }
        return value[0] & 0xFF;
    }

    /**
     * @param arrays - arrays to merge
     * @return - merged array
     */
    public static byte[] merge(byte[]... arrays) {
        int count = 0;
        for (byte[] array : arrays) {
            count += array.length;
        }
        // Create new array and copy all array contents
        byte[] mergedArray = new byte[count];
        int start = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, mergedArray, start, array.length);
            start += array.length;
        }
        return mergedArray;
    }

    /**
     * Converts 4 bytes IPv4 IP to String representation
     */
    public static String bytesToIp(byte[] bytesIp) {
        StringBuilder sb = new StringBuilder();
        sb.append(bytesIp[0] & 0xFF);
        sb.append(".");
        sb.append(bytesIp[1] & 0xFF);
        sb.append(".");
        sb.append(bytesIp[2] & 0xFF);
        sb.append(".");
        sb.append(bytesIp[3] & 0xFF);
        String ip = sb.toString();
        return ip;
    }

    /**
     * Converts ip to bytes.
     */
    public static byte[] ipToBytes(String ip) {
        try {
            return InetAddress.getByName(ip).getAddress();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Converts port to 2 bytes.
     */
    public static byte[] portToBytes(int port) {
        return ArrayUtils.subarray(toBytesInt(port), 2, 4);
    }

    /**
     * Converts  2 bytes to port.
     */
    public static int bytesToPort(byte[] portBytes) {
        return toInt(portBytes);
    }

    /**
     * Converts version to 2 bytes.
     */
    public static byte[] versionToBytes(int version) {
        return ArrayUtils.subarray(toBytesInt(version), 2, 4);
    }

    /**
     * Converts  2 bytes to version.
     */
    public static int bytesToVersion(byte[] versionBytes) {
        return toInt(versionBytes);
    }

    /**
     * 使用UTF-8编码表进行截取字符串，一个汉字对应三个负数，一个英文字符对应一个正数
     *
     * @param data
     * @param length
     * @return
     */
    public static byte[] setUtf8Length(String data, int length) {
        if (data == null || length < 0 || data.length() < 1 || length < 1) {
            return new byte[0];
        }
        byte[] bs = utf8(data);
        if (length >= bs.length) {
            return bs;
        }
        int count = 0;
        //从要截取的位置往回找，如果当前字节是一个 UTF8 字节则往回寻找，如果是英文字母则值为正数，就停止寻找。
        //当 count = 0 || count % 3 = 0 时，说明 count 计算的字节是一个完整 UTF8 文字，截取的位置刚好是文字末尾。
        //当 count = 1 || count % 3 = 1 时，说明 count 计算的字节为 1/3 个 UTF8 文字，截取的位置要前移一个字节。
        //当 count = 2 || count % 3 = 2 时，说明 count 计算的字节为 2/3 个 UTF8 文字，截取的位置要前移两个字节。
        for (int i = length - 1; i >= 0; i--) {
            if (bs[i] < 0) {
                count += 1;
            } else {
                break;
            }
        }
        return ArrayUtils.subarray(bs, 0, length - (count % 3));
    }

    public static void main(String[] args) {
        int port = 0xFFFF;
        System.out.println(bytesToPort(new byte[]{(byte) 0xFF, (byte) 0xFF}));
        System.out.println(toBinaryString(portToBytes(port)));
    }

    public static void main1(String[] args) throws Exception {
        String s = "从要截取的位置往回找，如果当前字节是一个 UTF8 字节则往回寻找，如果是英文字母则值为正数，就停止寻找。";
        byte[] bs = utf8(s);
        for (int i = 0; i < bs.length + 10; i++) {
            System.out.println(utf8(setUtf8Length(s, i)));
        }
    }
}
