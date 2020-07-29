package project1;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;


public class OneTimePad {

	public static void main(String[] args) throws IOException {
		OneTimePad op = new OneTimePad();
		System.out.println("Please enter the plaintext string to encode.");
		Scanner sc = new Scanner(System.in);
		String stringToEncrypt = sc.nextLine();
		
		System.out.println("Here is the generated key: ");
		byte[] currentKey = op.generateKey(stringToEncrypt);
		System.out.println(Base64.getEncoder().encodeToString(currentKey));
		
		System.out.println("Here is the encrypted message: ");
		byte[] encryptedMessage = op.encryptMessage(stringToEncrypt, currentKey);
		System.out.println(Base64.getEncoder().encodeToString(encryptedMessage));
		
		
		System.out.println("Here is the decrypted message, derived using the displayed key:");
		System.out.println(op.decryptMessage(encryptedMessage, currentKey));
		
		sc.close();
		
	}
	
	public byte[] generateKey(String s) throws IOException {
		byte[] getBytes = s.getBytes();
		int byteLength = getBytes.length;
		byte[] key = new byte[byteLength];
		SecureRandom sr = new SecureRandom();
		sr.nextBytes(key);
		return key;
	}
	
	public byte[] encryptMessage(String s, byte[] key) {
		byte[] getBytes = s.getBytes();
		byte[] encryptedMessage = new byte[getBytes.length]; 
		
		for (int i = 0; i < getBytes.length; i++) {
			encryptedMessage[i] = (byte) (getBytes[i] ^ key[i]);
		}
		
		return encryptedMessage;
	}
	
	public String decryptMessage(byte[] cipherText, byte[] key) {
		byte[] decodedMessage = new byte[cipherText.length];
		for (int i = 0; i < decodedMessage.length; i++) {
			decodedMessage[i] = (byte) (cipherText[i] ^ key[i]);
		}
		
		String plainText = new String(decodedMessage);
		
		return plainText;
	}
}
