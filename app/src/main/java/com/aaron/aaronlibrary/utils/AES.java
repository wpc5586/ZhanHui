/*****************************************************************************
 * 东方国信手机经分项目[wm-spider]
 *----------------------------------------------------------------------------
 * cn.com.bonc.jf.common.util.AES.java
 *
 * @author andy
 * @date 2016年11月20日
 * @version 0.0.1
 * @since 0.0.1
 *----------------------------------------------------------------------------
 * (C) 北京东方国信科技股份有限公司
 *     Business-intelligence Of Oriental Nations Corporation Ltd. 2016
 *****************************************************************************/
package com.aaron.aaronlibrary.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密工具类
 * cn.com.bonc.jf.common.util.AES.java
 * 
 * @author andy
 * @date 2016年11月20日
 *
 * @since 0.0.1
 */
public class AES {
	
	public static final String DEFAULT_ENCODING = "utf-8";
	
	public static final String PASS = "bonc_mobile_jf00";
	
	public static final String IV_STRING = "16-Bytes--bonc00";
	
	/**
	 * 加密（接口数据）
	 * 
	 * @param data 需要加密的内容
	 * @return String
	 */
	public static String encrypt(String data) {
		if (data == null)
			return "";
		try {
			return encryptAES(data, PASS);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String encryptWithH5(String data) {
		return encrypt(data, PASS).replace("+", "%2B");
	}

	/**
	 * 解密（接口数据）
	 * 
	 * @param data 待解密内容
	 * @return String
	 */
	public static String decrypt(String data) {
		if (data == null)
			data = "";
		try {
			return decryptAES(data, PASS);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 加密
	 * 
	 * @param data 需要加密的内容
	 * @param secretKey 加密秘钥
	 * @return String
	 */
	public static String encrypt(String data, String secretKey) {
		if (data == null)
			data = "";
		try {
			byte[] input = data.getBytes(DEFAULT_ENCODING);
	
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(secretKey.getBytes(DEFAULT_ENCODING));
			SecretKeySpec skc = new SecretKeySpec(thedigest, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skc);
	
			byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
			int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
			ctLength += cipher.doFinal(cipherText, ctLength);
			return parseByte2HexStr(cipherText);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param data 待解密内容
	 * @param secretKey 解密秘钥
	 * @return String
	 */
	public static String decrypt(String data, String secretKey) {
		if (data == null)
			data = "";
		try {
			byte[] keyb = secretKey.getBytes(DEFAULT_ENCODING);
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(keyb);
			SecretKeySpec skey = new SecretKeySpec(thedigest, "AES");
			Cipher dcipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			dcipher.init(Cipher.DECRYPT_MODE, skey);
	
			byte[] clearbyte = dcipher.doFinal(parseHexStr2Byte(data));
			return new String(clearbyte);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return String
	 */
	private static String parseByte2HexStr(byte[] buf) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex);
		}
		return sb.toString();
	}
	
	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return byte[]
	 */
	private static byte[] parseHexStr2Byte(String hexStr) {
		int len = hexStr.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++) {
			result[i] = Integer.valueOf(hexStr.substring(2 * i, 2 * i + 2), 16).byteValue();
		}
		return result;
	}

	public static String encryptAES(String content, String key)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		byte[] byteContent = content.getBytes("UTF-8");
		// 注意，为了能与 iOS 统一
		// 这里的 key 不可以使用 KeyGenerator、SecureRandom、SecretKey 生成
		byte[] enCodeFormat = key.getBytes();
		SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
		byte[] initParam = IV_STRING.getBytes();
		IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
		// 指定加密的算法、工作模式和填充方式
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
		byte[] encryptedBytes = cipher.doFinal(byteContent);
		// 同样对加密后数据进行 base64 编码
		com.aaron.aaronlibrary.utils.Base64.Encoder encoder = com.aaron.aaronlibrary.utils.Base64.getEncoder();
		return encoder.encodeToString(encryptedBytes);
	}

	public static String decryptAES(String content, String key) throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException,
            UnsupportedEncodingException {
		// base64 解码
		com.aaron.aaronlibrary.utils.Base64.Decoder decoder = com.aaron.aaronlibrary.utils.Base64.getDecoder();
		byte[] encryptedBytes = decoder.decode(content);
		byte[] enCodeFormat = key.getBytes();
		SecretKeySpec secretKey = new SecretKeySpec(enCodeFormat, "AES");
		byte[] initParam = IV_STRING.getBytes();
		IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
		byte[] result = cipher.doFinal(encryptedBytes);
		return new String(result, "UTF-8");
	}
	
	public static void main(String[] args) {
		try {
			System.out.println("~!~ result = " + Auth.createToken("999", "xiaow"));
			System.out.println("~!~ result = " + encrypt("testdishi"));
			System.out.println("~!~ result = " + decrypt("7CaIwo8j7VmDCdc9C1AyG7CIwJr2YXrOIqQp1PiteoXMgiIq5ETAuOOQMHXmysXV"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
