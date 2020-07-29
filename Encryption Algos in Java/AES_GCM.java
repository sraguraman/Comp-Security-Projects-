package project1;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

//I did not print out or verify the tags because the GCMParameterSpec does not allow for isolation of the tag. This was confirmed by this 
//piazza post: https://piazza.com/class/jza2a0jr7stf0?cid=52 

public class AES_GCM {

	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		KeyGenerator kg = KeyGenerator.getInstance("AES");
		kg.init(128);
		SecretKey k = kg.generateKey();
		SecureRandom sr = new SecureRandom();
		byte[] initVector = new byte[12];
		sr.nextBytes(initVector);
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the plaintext to encrypt.");
		Scanner encrypt = new Scanner(System.in);
		String encryptionString = encrypt.nextLine();
		byte [] byteText = encryptionString.getBytes();
			
		Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
		byte [] encodedKey = k.getEncoded();
		SecretKeySpec ks = new SecretKeySpec(encodedKey, "AES");
		GCMParameterSpec gcmSpec = new GCMParameterSpec(128, initVector);
			
		c.init(Cipher.ENCRYPT_MODE, ks, gcmSpec);
		byte[] encrypted = c.doFinal(byteText);
			
		System.out.println("Ciphertext: ");
		String encryptedString = new String(Base64.getEncoder().encode(encrypted));
		System.out.println(encryptedString);
		
		System.out.println("Would you like to decrypt the generated ciphertext? Enter y for yes and n for no.");
		String decision = sc.nextLine();
		if (decision.equals("y")) {
			GCMParameterSpec gcmSpec2 = new GCMParameterSpec(128, initVector);
			SecretKeySpec ks2 = new SecretKeySpec(encodedKey, "AES");
			Cipher c2 = Cipher.getInstance("AES/GCM/NoPadding");
			c2.init(Cipher.DECRYPT_MODE, ks2, gcmSpec2);
			byte[] decrypted = c2.doFinal(encrypted);
				
			String decryptedString = new String(decrypted);
				
			System.out.println("Plaintext: ");
			System.out.println(decryptedString);
			encrypt.close();
		}
		else {
			sc.close();
			encrypt.close();
			return;
		}
		sc.close();
	}

}
