package com.zby.openapi;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
public class AES {
	private IvParameterSpec ivSpec;
	private SecretKeySpec keySpec;
	
	public AES(String key,byte[] iv) {
		try {
			byte[] keyBytes = key.getBytes();
			this.keySpec = new SecretKeySpec(keyBytes, "AES");
			this.ivSpec = new IvParameterSpec(iv);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public byte[] encrypt(byte[] origData) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, this.keySpec, this.ivSpec);
			return cipher.doFinal(origData);
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public byte[] decrypt(byte[] crypted) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, this.keySpec, this.ivSpec);
			return cipher.doFinal(crypted);
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
