package project1;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

//This program was done in a single flow: allowed by Professor when asked during Q&A period. 

public class CBC_MAC {

	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		SecureRandom sr = new SecureRandom();
		byte[] currentKey = new byte[16];
		sr.nextBytes(currentKey);
		
		byte[] iv1 = new byte[16];
		sr.nextBytes(iv1);
		
		CBC_MAC cm = new CBC_MAC();
		
		System.out.println("First, let's generate the tag and ciphertext.");
		byte[] returnMessage = cm.generateTag(currentKey, iv1);
		
		System.out.println("Second, let's authorize a correct tag to decrypt the above generated ciphertext.");
		cm.verifyTag(currentKey, returnMessage);
	}
		
	private byte[] generateTag(byte[] key, byte[] iv) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		final Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
		Scanner s = new Scanner(System.in);
		System.out.println("Enter plaintext.");
		String plainText = s.nextLine();
		
		byte[] cipherText = c.doFinal(plainText.getBytes());
		
		SecretKey macK = new SecretKeySpec(key, "AES");
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(macK);
		mac.update(iv);
		mac.update(cipherText);
		
		byte[] tag1 = mac.doFinal();
		
		ByteBuffer bb = ByteBuffer.allocate(1 + iv.length + 1 + tag1.length + cipherText.length);
		bb.put((byte) iv.length);
		bb.put(iv);
		bb.put((byte) tag1.length);
		bb.put(tag1);
		bb.put(cipherText);
		
		System.out.println("tag: " + Base64.getEncoder().encodeToString(tag1));
		System.out.println("ciphertext:" + Base64.getEncoder().encodeToString(cipherText));
		
		byte[] returnMessage = bb.array();
		return returnMessage;
	}
	
	private void verifyTag(byte[] key, byte[] buff) throws InvalidKeyException, NoSuchAlgorithmException {
		ByteBuffer bb2 = ByteBuffer.wrap(buff);
		
		int ivlen = (bb2.get());
		byte[] iv2 = new byte[ivlen];
		bb2.get(iv2);
		
		int maclen = (bb2.get());
		byte[] hMac = new byte[maclen];
		bb2.get(hMac);
		
		byte[] encryptedMessage = new byte[bb2.remaining()];
		bb2.get(encryptedMessage);
		
		System.out.println("Enter a tag.");
		Scanner checkTag = new Scanner(System.in);
		String enteredTag = checkTag.nextLine();
		byte [] tagBytes = Base64.getDecoder().decode(enteredTag.getBytes());
		
		SecretKey macK2 = new SecretKeySpec(key, "AES");
		Mac mac2 = Mac.getInstance("HmacSHA256");
		mac2.init(macK2);
		mac2.update(iv2);
		mac2.update(encryptedMessage);
		
		byte[] verifyMac = mac2.doFinal();
		
		if (!MessageDigest.isEqual(tagBytes, verifyMac)) {
			System.out.println("Invalid tag.");
		}
		else {
			System.out.println("Valid tag.");
		}
		
	}
	
	
}
