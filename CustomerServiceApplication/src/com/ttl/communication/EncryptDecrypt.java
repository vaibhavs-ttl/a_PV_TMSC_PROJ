package com.ttl.communication;



import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

/*
 * This class implements encryption and decryption of string
 * For Encryption expexted input is String to be encoded, Private key (length = 32) and IV Parameters (length = 16) and returns Base 64 encoded string
 * For Decryption expexted input is base 64 encoded String to be decoded, Private key (length = 32) and IV Parameters (length = 16) and returns UTF-8 string
 * */
public class EncryptDecrypt {

	public String encrypt(String text,String password,byte[] aesIVBytes) {
		try {
			// generate key
			byte[] key = password.getBytes("UTF-8");
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16); // use only first 128 bit
			SecretKeySpec secret = new SecretKeySpec(key, "AES");

			IvParameterSpec ips = new IvParameterSpec(aesIVBytes);

			/* Encrypt the message. */
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secret, ips);

			byte[] ciphertext = cipher.doFinal(text.getBytes("UTF-8"));

			String encryptedString = Base64.encodeToString(ciphertext,
					Base64.DEFAULT);
			return encryptedString;

		} catch (Exception e) {
			return e.toString();
		}
	}

	public String decrypt(String ciphertext,String password,byte[] aesIVBytes) {
		try {
			byte[] clearBytes = Base64.decode(ciphertext.getBytes("UTF-8"),
					Base64.DEFAULT);
			;

			// generate key
			byte[] key = password.getBytes("UTF-8");
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16); // use only first 128 bit
			SecretKeySpec secret = new SecretKeySpec(key, "AES");

			IvParameterSpec ips = new IvParameterSpec(aesIVBytes);

			/* Decrypt the message, given derived key and initialization vector. */
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secret, ips);
			String plaintext = new String(cipher.doFinal(clearBytes), "UTF-8");

			return plaintext;
		} catch (Exception e) {
			return e.toString();
		}
	}

}
