package com.zby.openapi;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

//import com.qingqing.common.exception.QingQingRuntimeException;

public class HmacSha256 {
	
	private static final String algo = "HmacSHA256";

	public static String hashHmac(String msg, String keyString) {
		String digest = null;
		try {
			SecretKeySpec key = new SecretKeySpec(
					(keyString).getBytes("UTF-8"), algo);
			Mac mac = Mac.getInstance(algo);
			mac.init(key);
			byte[] bytes = mac.doFinal(msg.getBytes("ASCII"));
			StringBuffer hash = new StringBuffer();
			for (int i = 0; i < bytes.length; i++) {
				String hex = Integer.toHexString(0xFF & bytes[i]);
				if (hex.length() == 1) {
					hash.append('0');
				}
				hash.append(hex);
			}
			digest = hash.toString();
		} catch (Exception e) {
		      //	throw new Exception("hash hmac error.", e);
		}
		return digest;
	}
}
