package com.kubeiwu.commontool.volley.cache.disk.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Log;

/**
 * Names image file as MD5 hash of image URI
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.4.0
 */
public class Md5 {

	private static final String HASH_ALGORITHM = "MD5";
	private static final int RADIX = 10 + 26; // 10 digits + 26 letters

	public static String generate(String imageUri) {
		byte[] md5 = getMD5(imageUri.getBytes());
		BigInteger bi = new BigInteger(md5).abs();
		return bi.toString(RADIX);
	}
	
	private static byte[] getMD5(byte[] data) {
		byte[] hash = null;
		try {
			MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
			digest.update(data);
			hash = digest.digest();
		} catch (NoSuchAlgorithmException e) {
			Log.e("Md5FileNameGenerator", e.toString());
		}
		return hash;
	}
}
