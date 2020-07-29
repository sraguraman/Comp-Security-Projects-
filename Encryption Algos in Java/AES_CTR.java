package project1;
import java.io.IOException;
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
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES_CTR {
	
	
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
		KeyGenerator currentKey = KeyGenerator.getInstance("AES");
		currentKey.init(128); 
		SecretKey generatedKey = currentKey.generateKey();
		
		byte [] initVector = new byte[16];
		SecureRandom rand = new SecureRandom();
		rand.nextBytes(initVector);
		
		System.out.println("Enter the plaintext to encrypt: ");
		Scanner sc = new Scanner(System.in);
		String plain = sc.nextLine();
		byte [] plainBytes = plain.getBytes();
		
		Cipher c = Cipher.getInstance("AES/CTR/PKCS5Padding");
		IvParameterSpec iSpec = new IvParameterSpec(initVector);
		SecretKeySpec kSpec = new SecretKeySpec(generatedKey.getEncoded(), "AES");
		
		c.init(Cipher.ENCRYPT_MODE, kSpec, iSpec);
		
		System.out.println("Ciphertext: ");
		byte [] cipherText = c.doFinal(plainBytes);
		System.out.println(Base64.getEncoder().encodeToString(cipherText));
		
		System.out.println("Now let us decrypt the generated ciphertext.");
		
		Cipher c2 = Cipher.getInstance("AES/CTR/PKCS5Padding");
		IvParameterSpec iSpec2 = new IvParameterSpec(initVector);
		SecretKeySpec kSpec2 = new SecretKeySpec(generatedKey.getEncoded(), "AES");
		c2.init(Cipher.DECRYPT_MODE, kSpec2, iSpec2);
		
		String decryptString = new String(c2.doFinal(cipherText));
		System.out.println("Plaintext: " + decryptString);
		
		sc.close();
	}
}
